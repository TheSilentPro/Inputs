package com.github.thesilentpro.inputs.paper;

import com.github.thesilentpro.inputs.api.Input;
import com.github.thesilentpro.inputs.api.InputParserRegistry;
import com.github.thesilentpro.inputs.api.InputRegistry;
import com.github.thesilentpro.inputs.paper.parser.PaperInputParserRegistry;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Deque;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class PaperInputRegistry implements InputRegistry<Component,AsyncChatEvent> {

    public static final PaperInputRegistry INSTANCE = new PaperInputRegistry(PaperInputParserRegistry.INSTANCE, new ConcurrentHashMap<>());

    private final InputParserRegistry<Component> parserRegistry;
    private final Map<UUID, Deque<Input<?,AsyncChatEvent,Component>>> inputs;

    public PaperInputRegistry(InputParserRegistry<Component> parserRegistry, Map<UUID, Deque<Input<?,AsyncChatEvent,Component>>> map) {
        this.parserRegistry = parserRegistry;
        this.inputs = map;
    }

    @Override
    public <T> void register(UUID id, Input<T,AsyncChatEvent,Component> input) {
        inputs.computeIfAbsent(id, k -> new ConcurrentLinkedDeque<>()).add(input);
    }

    @Override
    public void process(@NotNull UUID id, @NotNull Component input, @Nullable AsyncChatEvent event) {
        Deque<Input<?,AsyncChatEvent,Component>> registeredInputs = inputs.get(id);
        if (registeredInputs == null) {
            return;
        }

        if (!registeredInputs.isEmpty()) {
            // Poll the first input in the queue
            Input<?,AsyncChatEvent,Component> registeredInput = registeredInputs.pollFirst();
            if (registeredInput != null) {
                //noinspection CodeBlock2Expr
                parserRegistry.find(registeredInput.getRequiredInputType())
                        .ifPresentOrElse(parser -> {
                            parser.parse(input).ifPresentOrElse(parsedInput -> {
                                if (registeredInput.hasExpired() && !registeredInput.shouldIgnoreExpired()) {
                                    if (registeredInput.getExpiredHandler() != null) {
                                        registeredInput.getExpiredHandler().accept(input, event);
                                    }
                                    return;
                                }

                                if (registeredInput.getInputHandler() != null) {
                                    //noinspection unchecked
                                    BiConsumer<Object,AsyncChatEvent> handler = (BiConsumer<Object,AsyncChatEvent>) registeredInput.getInputHandler();
                                    handler.accept(parsedInput, event);
                                }
                            }, () -> {
                                if (registeredInput.getMismatchHandler() != null) {
                                    registeredInput.getMismatchHandler().accept(input, event);
                                }
                            });
                        }, () -> onInvalidParser(registeredInput.getRequiredInputType()));
            }
        }

        // Clean up the registry if no inputs remain for the given ID
        if (registeredInputs.isEmpty()) {
            inputs.remove(id);
        }
    }

}
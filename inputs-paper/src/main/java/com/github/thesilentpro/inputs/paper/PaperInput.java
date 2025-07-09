package com.github.thesilentpro.inputs.paper;

import com.github.thesilentpro.inputs.api.BaseInput;
import com.github.thesilentpro.inputs.api.Input;
import com.github.thesilentpro.inputs.api.InputRegistry;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Paper-specific implementation of {@link Input}, gathering chat messages as Components.
 * @param <T> expected input type
 */
public class PaperInput<T> extends BaseInput<T, AsyncChatEvent, Component> {

    public PaperInput(Class<T> requiredInputType) {
        super(requiredInputType);
    }

    public static <T> PaperInput<T> await(Class<T> requiredInputType) {
        return new PaperInput<>(requiredInputType);
    }

    public static PaperInput<Component> awaitComponent() {
        return await(Component.class);
    }

    public static PaperInput<String> awaitString() {
        return await(String.class);
    }

    public static PaperInput<Number> awaitNumber() {
        return await(Number.class);
    }

    public static PaperInput<Integer> awaitInteger() {
        return await(Integer.class);
    }

    public static PaperInput<Long> awaitLong() {
        return await(Long.class);
    }

    public static PaperInput<Double> awaitDouble() {
        return await(Double.class);
    }

    public static PaperInput<Float> awaitFloat() {
        return await(Float.class);
    }

    public static PaperInput<Byte> awaitByte() {
        return await(Byte.class);
    }

    public static PaperInput<Boolean> awaitBoolean() {
        return await(Boolean.class);
    }

    @Override
    public <U> PaperInput<U> wait(Class<U> requiredInputType) {
        return await(requiredInputType);
    }

    @Override
    public PaperInput<T> await() {
        return await(getRequiredInputType());
    }

    @Override
    public PaperInput<T> until(Duration duration) {
        super.until(duration);
        return this;
    }

    // then with Consumer<T>
    @Override
    public PaperInput<T> then(Consumer<T> handler) {
        super.then(handler);
        return this;
    }

    // then with BiConsumer<T, AsyncChatEvent>
    @Override
    public PaperInput<T> then(BiConsumer<T, AsyncChatEvent> handler) {
        super.then(handler);
        return this;
    }

    // mismatch with Consumer<Component>
    @Override
    public PaperInput<T> mismatch(Consumer<Component> handler) {
        super.mismatch(handler);
        return this;
    }

    // mismatch with BiConsumer<Component, AsyncChatEvent>
    @Override
    public PaperInput<T> mismatch(BiConsumer<Component, AsyncChatEvent> handler) {
        super.mismatch(handler);
        return this;
    }

    // expired with Consumer<Component>
    @Override
    public PaperInput<T> expired(Consumer<Component> handler) {
        super.expired(handler);
        return this;
    }

    // expired with BiConsumer<Component, AsyncChatEvent>
    @Override
    public PaperInput<T> expired(BiConsumer<Component, AsyncChatEvent> handler) {
        super.expired(handler);
        return this;
    }

    @Override
    public PaperInput<T> timestamp(Instant timestamp) {
        super.timestamp(timestamp);
        return this;
    }

    @Override
    public PaperInput<T> ignoreExpired() {
        super.ignoreExpired();
        return this;
    }

    @Override
    public PaperInput<T> register(UUID id, InputRegistry<Component, AsyncChatEvent> registry) {
        super.register(id, registry);
        return this;
    }

    /**
     * Registers this input in the Paper registry under the given id.
     */
    public PaperInput<T> register(UUID id) {
        PaperInputRegistry.INSTANCE.register(id, this);
        return this;
    }

    /**
     * Convenience method: await, set expiration, attach handler, and register in one chain.
     */
    public static <T> PaperInput<T> of(UUID id, Class<T> type, Duration expireAfter, Consumer<T> handler) {
        return await(type).until(expireAfter).then(handler).register(id);
    }

}
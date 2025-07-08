package com.github.thesilentpro.inputs.paper.parser;

import com.github.thesilentpro.inputs.api.InputParser;
import com.github.thesilentpro.inputs.api.InputParserRegistry;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class PaperInputParserRegistry implements InputParserRegistry<Component> {

    public static final PaperInputParserRegistry INSTANCE = new PaperInputParserRegistry(new ConcurrentHashMap<>()).registerDefaults();

    private final Map<Class<?>, List<InputParser<Component,?>>> parsers;

    public PaperInputParserRegistry(Map<Class<?>, List<InputParser<Component,?>>> map) {
        this.parsers = map;
    }

    @NotNull
    public <T> Optional<InputParser<Component,T>> find(@NotNull Class<T> type) {
        List<InputParser<Component,?>> parsers = this.parsers.get(type);
        if (parsers == null || parsers.isEmpty()) {
            return Optional.empty();
        }

        //noinspection unchecked,SequencedCollectionMethodCanBeUsed
        return Optional.of((InputParser<Component,T>) parsers.get(0));
    }

    @NotNull
    public <T> List<InputParser<Component,T>> findAll(@NotNull Class<T> type) {
        //noinspection unchecked
        List<InputParser<Component,T>> parsers = (List<InputParser<Component,T>>) (List<?>) this.parsers.get(type);

        if (parsers == null || parsers.isEmpty()) {
            return List.of();
        }

        return Collections.unmodifiableList(parsers);
    }

    /**
     * Register a new {@link InputParser} with the class type.
     *
     * @param type The class type for the parser
     * @param parser The parser
     * @param <T> The type
     */
    public <T> void register(@NotNull Class<T> type, @NotNull InputParser<Component,T> parser) {
        List<InputParser<Component,?>> list = this.parsers.computeIfAbsent(type, t -> new ArrayList<>());
        if (!list.contains(parser)) {
            list.add(parser);
        }
    }

    public <T> void update(@NotNull Class<T> type, @NotNull InputParser<Component,T> parser) {
        List<InputParser<Component,?>> list = this.parsers.computeIfAbsent(type, t -> new ArrayList<>());
        if (list.contains(parser)) {
            list.set(list.indexOf(parser), parser);
        }
    }

    /**
     * Registers a set of default parsers for common types such as String, Integer, Boolean, Duration, etc.
     * This method is automatically invoked to populate the registry with commonly used parsers.
     *
     * @return The {@link InputParserRegistry} instance with the default parsers registered.
     */
    public PaperInputParserRegistry registerDefaults() {
        register(Component.class, Optional::of);
        register(String.class, s -> Optional.of(PlainTextComponentSerializer.plainText().serialize(s).trim()));
        register(Number.class, NumberParser::parse);
        register(Integer.class, NumberParser::parseInteger);
        register(Long.class, NumberParser::parseLong);
        register(Double.class, NumberParser::parseDouble);
        register(Float.class, NumberParser::parseFloat);
        register(Byte.class, NumberParser::parseByte);
        register(Boolean.class, s -> {
            String text = PlainTextComponentSerializer.plainText().serialize(s);
            String trimmed = text.trim();
            if (trimmed.equalsIgnoreCase("true") || trimmed.equalsIgnoreCase("yes") || trimmed.equalsIgnoreCase("on")) {
                return Optional.of(true);
            } else if (trimmed.equalsIgnoreCase("false") || trimmed.equalsIgnoreCase("No") || trimmed.equalsIgnoreCase("off")) {
                return Optional.of(false);
            } else {
                return Optional.empty();
            }
        });
        register(Duration.class, DurationParser::parseSafely);
        register(UUID.class, s -> {
            try {
                return Optional.of(UUID.fromString(PlainTextComponentSerializer.plainText().serialize(s).trim()));
            } catch (IllegalArgumentException e) {
                return Optional.empty();
            }
        });
        return this;
    }

}

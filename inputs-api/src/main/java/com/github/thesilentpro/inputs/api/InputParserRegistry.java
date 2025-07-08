package com.github.thesilentpro.inputs.api;


import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * A registry that manages type-based {@link InputParser}s.
 * It allows finding, registering, and updating parsers for various types of inputs.
 *
 * @author TheSilentPro (Silent)
 */
public interface InputParserRegistry<I> {

    /**
     * Finds a parser for the specified type.
     *
     * @param type The class type of the input that needs to be parsed.
     * @param <T> The type of the parsed value.
     * @return An {@link Optional} containing the parser, if present.
     */
    @NotNull <T> Optional<InputParser<I,T>> find(@NotNull Class<T> type);

    /**
     * Finds all parsers for the specified type.
     *
     * @param type The class type of the input that needs to be parsed.
     * @param <T> The type of the parsed value.
     * @return A list of parsers that handle the specified type, which may be empty if none are found.
     */
    @NotNull <T> List<InputParser<I,T>> findAll(@NotNull Class<T> type);

    /**
     * Registers a new {@link InputParser} for the specified class type.
     *
     * @param type The class type for the parser.
     * @param parser The parser to register.
     * @param <T> The type of the parsed value.
     */
    <T> void register(@NotNull Class<T> type, @NotNull InputParser<I,T> parser);

    /**
     * Updates an existing {@link InputParser} for the specified class type.
     * This operation is marked as experimental and may change in future releases.
     *
     * @param type The class type for the parser.
     * @param parser The parser to update.
     * @param <T> The type of the parsed value.
     * @since 1.0
     */
    @ApiStatus.Experimental
    <T> void update(@NotNull Class<T> type, @NotNull InputParser<I,T> parser);

}
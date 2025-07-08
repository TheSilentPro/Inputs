package com.github.thesilentpro.inputs.api;

import java.util.Optional;

/**
 * A parser that processes a given input and converts it into a specific type.
 *
 * @param <T> The type that the input will be parsed into.
 * @author TheSilentPro (Silent)
 */
public interface InputParser<I,T> {

    /**
     * Parses the given input string and attempts to convert it into the expected type.
     *
     * @param input The input string to parse.
     * @return An {@link Optional} containing the parsed value if successful, or an empty {@link Optional} if parsing fails.
     */
    Optional<T> parse(I input);

}
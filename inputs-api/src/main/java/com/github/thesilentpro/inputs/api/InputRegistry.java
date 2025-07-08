package com.github.thesilentpro.inputs.api;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

/**
 * Manages the registration and processing of user inputs.
 * This interface is responsible for registering inputs, processing them,
 * and handling the invalid parser scenarios.
 *
 * @author TheSilentPro (Silent)
 */
public interface InputRegistry<I,E> {

    /**
     * Registers an input handler that is awaiting a user response.
     *
     * @param input The input handler to register.
     * @param <T> The type of input expected (e.g., String, Integer).
     */
    <T> void register(UUID id, Input<T,E,I> input);

    /**
     * Processes an input string associated with a specific input handler.
     * This method is called when an input event (e.g., a chat event) triggers the processing.
     *
     * @param id The unique identifier for the input instance.
     * @param input The input string to process.
     * @param event The event source that triggered the input processing (nullable).
     */
    void process(@NotNull UUID id, @NotNull I input, @Nullable E event);

    /**
     * Processes an input string associated with a specific input handler, without an event context.
     * This method will be called when no event source is provided.
     *
     * @param id The unique identifier for the input instance.
     * @param input The input string to process.
     */
    default void process(@NotNull UUID id, @NotNull I input) {
        process(id, input, null);
    }

    /**
     * Handler for cases where an invalid {@link InputParser} is passed to an {@link Input#wait(Class)}.
     * This method is invoked when the parser registry cannot find a parser for the specified input type.
     *
     * @param type The class type of the input for which no parser was found.
     * @param <T> The type of input.
     */
    default <T> void onInvalidParser(@NotNull Class<T> type) {
        throw new IllegalArgumentException("No parser found for input type: " + type.getName());
    }

}
package com.github.thesilentpro.inputs.api;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Represents a generic input handler that manages awaiting and processing user input.
 * The input can be associated with specific handlers for successful input, mismatches,
 * expiration events, and more. The input operation has an optional expiration time and
 * can be registered in an input registry.
 *
 * @param <T> The type of input expected (e.g., String, Integer, etc.)
 * @param <E> The event listening for inputs
 * @param <I> The input type
 * @author TheSilentPro (Silent)
 */
public interface Input<T,E,I> {

    /**
     * Creates a new input instance that awaits user input of the specified type.
     *
     * @param requiredInputType The class type of the expected input.
     * @param <T> The type of the expected input.
     * @return A new {@link Input} instance that is awaiting user input.
     */
    static <T,E,I> Input<T,E,I> of(Class<T> requiredInputType) {
        return new BaseInput<>(requiredInputType);
    }

    default Input<String,E,I> waitString() {
        return wait(String.class);
    }

    default Input<Number,E,I> waitNumber() {
        return wait(Number.class);
    }

    default Input<Integer,E,I> waitInteger() {
        return wait(Integer.class);
    }

    default Input<Long,E,I> waitLong() {
        return wait(Long.class);
    }

    default Input<Double,E,I> waitDouble() {
        return wait(Double.class);
    }

    default Input<Float,E,I> waitFloat() {
        return wait(Float.class);
    }

    default Input<Byte,E,I> waitByte() {
        return wait(Byte.class);
    }

    default Input<Boolean,E,I> waitBoolean() {
        return wait(Boolean.class);
    }

    /**
     * Creates a new input instance that awaits user input of the specified type.
     *
     * @param requiredInputType The class type of the expected input.
     * @param <U> The type of the expected input.
     * @return A new {@link Input} instance that is awaiting user input.
     */
    default <U> Input<U,E,I> wait(Class<U> requiredInputType) {
        return of(requiredInputType);
    }

    /**
     * Creates a new input instance that awaits user input.
     *
     * @param input The input to wait for.
     * @param <U> The type of the expected input.
     * @return A new {@link Input} instance that is awaiting user input.
     */
    default <U> Input<U,E,I> await(Input<U,E,I> input) {
        return input;
    }

    /**
     * Creates a new input instance that awaits user input of the same type.
     *
     * @return A new {@link Input} instance that is awaiting user input.
     */
    default Input<T,E,I> await() {
        return wait(getRequiredInputType());
    }

    /**
     * Sets a maximum duration that the input will remain active, after which it expires.
     *
     * @param duration The duration to wait before the input expires.
     * @return The updated input instance with the expiration time set.
     */
    Input<T,E,I> until(Duration duration);

    /**
     * Sets a handler to be executed when the expected input is successfully provided.
     *
     * @param handler The handler that processes the input.
     * @return The updated input instance with the handler applied.
     */
    Input<T,E,I> then(Consumer<T> handler);

    /**
     * Sets a handler that is executed when the expected input is provided, with
     * additional context from an {@link E}.
     *
     * @param handler The handler that processes the input and the event.
     * @return The updated input instance with the handler applied.
     */
    Input<T,E,I> then(BiConsumer<T, E> handler);

    /**
     * Sets a handler to be executed when the input does not match the expected format.
     *
     * @param handler The handler that processes the mismatch event.
     * @return The updated input instance with the mismatch handler applied.
     */
    Input<T,E,I> mismatch(Consumer<I> handler);

    /**
     * Sets a handler to be executed when the input does not match the expected format.
     *
     * @param handler The handler that processes the mismatch event.
     * @return The updated input instance with the mismatch handler applied.
     */
    Input<T,E,I> mismatch(BiConsumer<I,E> handler);

    /**
     * Sets a handler to be executed when the input has expired.
     *
     * @param handler The handler that processes the expiration event.
     * @return The updated input instance with the expiration handler applied.
     */
    Input<T,E,I> expired(Consumer<I> handler);

    /**
     * Sets a handler to be executed when the input has expired.
     *
     * @param handler The handler that processes the expiration event.
     * @return The updated input instance with the expiration handler applied.
     */
    Input<T,E,I> expired(BiConsumer<I,E> handler);

    /**
     * Sets the timestamp at which the input was created.
     *
     * @param timestamp The timestamp.
     * @return The updated input instance with the timestamp.
     * @see #hasExpired()
     */
    Input<T,E,I> timestamp(Instant timestamp);

    /**
     * Sets the timestamp at which the input was created.
     *
     * @return The updated input instance with the timestamp.
     * @see #hasExpired()
     */
    default Input<T,E,I> timestamp() {
        return timestamp(Instant.now());
    }

    /**
     * Indicates that expired inputs should be ignored, preventing the expiration handler
     * from being triggered.
     *
     * @return The updated input instance with expired inputs ignored.
     */
    Input<T,E,I> ignoreExpired();

    Input<T,E,I> register(UUID id, InputRegistry<I,E> registry);

    // Getters

    /**
     * Gets the duration for which the input is valid, or {@code null} if no expiration
     * duration has been set.
     *
     * @return The duration the input will remain active.
     */
    Duration getDuration();

    /**
     * Gets the timestamp when the input was created.
     *
     * @return The instant when the input was created.
     */
    Instant getTimestamp();

    /**
     * Gets the handler that processes the successful input.
     *
     * @return The consumer that handles the expected input.
     */
    BiConsumer<T,E> getInputHandler();

    /**
     * Gets the handler that processes a mismatch in the input.
     *
     * @return The consumer that handles the mismatch event.
     */
    BiConsumer<I,E> getMismatchHandler();

    /**
     * Gets the handler that processes an expired input.
     *
     * @return The consumer that handles the expiration event.
     */
    BiConsumer<I,E> getExpiredHandler();

    /**
     * Gets the class type of the expected input.
     *
     * @return The class type of the input.
     */
    Class<T> getRequiredInputType();

    /**
     * Checks if the input has expired.
     *
     * @return {@code true} if the input has expired, {@code false} otherwise.
     */
    boolean hasExpired();

    /**
     * Checks if expired inputs should be ignored.
     *
     * @return {@code true} if expired inputs are ignored, {@code false} otherwise.
     */
    boolean shouldIgnoreExpired();

}
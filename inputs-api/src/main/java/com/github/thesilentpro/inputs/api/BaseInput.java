package com.github.thesilentpro.inputs.api;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * @author TheSilentPro (Silent)
 */
public class BaseInput<T,E,I> implements Input<T,E,I> {

    private final Class<T> requiredInputType;
    private Instant createdAt;
    private Duration duration;
    private Consumer<T> handler;
    private BiConsumer<T, E> biHandler;
    private Consumer<I> mismatchHandler;
    private Consumer<I> expiredHandler;
    private boolean ignoreExpired;

    public BaseInput(Class<T> requiredInputType) {
        this.requiredInputType = requiredInputType;
        this.createdAt = Instant.now();
        this.ignoreExpired = false;
    }

    @Override
    public Input<T,E,I> until(Duration duration) {
        this.duration = duration;
        return this;
    }

    @Override
    public Input<T,E,I> then(Consumer<T> handler) {
        this.handler = handler;
        return this;
    }

    @Override
    public Input<T,E,I> then(BiConsumer<T,E> handler) {
        this.biHandler = handler;
        return this;
    }

    @Override
    public Input<T,E,I> mismatch(Consumer<I> handler) {
        this.mismatchHandler = handler;
        return this;
    }

    @Override
    public Input<T,E,I> expired(Consumer<I> handler) {
        this.expiredHandler = handler;
        return this;
    }

    @Override
    public Input<T,E,I> timestamp(Instant timestamp) {
        this.createdAt = timestamp;
        return this;
    }

    @Override
    public Input<T,E,I> ignoreExpired() {
        this.ignoreExpired = true;
        return this;
    }

    @Override
    public Input<T, E, I> register(UUID id, InputRegistry<I, E> registry) {
        registry.register(id, this);
        return this;
    }

    // Getters

    @Override
    public Duration getDuration() {
        return duration;
    }

    @Override
    public Instant getTimestamp() {
        return createdAt;
    }

    @Override
    public Consumer<T> getInputHandler() {
        return handler;
    }

    @Override
    public BiConsumer<T,E> getBiInputHandler() {
        return biHandler;
    }

    @Override
    public Consumer<I> getMismatchHandler() {
        return mismatchHandler;
    }

    @Override
    public Consumer<I> getExpiredHandler() {
        return expiredHandler;
    }

    @Override
    public Class<T> getRequiredInputType() {
        return requiredInputType;
    }

    @Override
    public boolean hasExpired() {
        if (duration != null) {
            return Instant.now().isAfter(createdAt.plus(duration));
        } else {
            return false;
        }
    }

    @Override
    public boolean shouldIgnoreExpired() {
        return ignoreExpired;
    }

}
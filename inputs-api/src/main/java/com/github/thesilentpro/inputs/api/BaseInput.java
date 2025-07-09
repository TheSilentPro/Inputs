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

    private BiConsumer<T, E> handler;
    private BiConsumer<I,E> mismatchHandler;
    private BiConsumer<I,E> expiredHandler;

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
        this.handler = (t, e) -> handler.accept(t);
        return this;
    }

    @Override
    public Input<T,E,I> then(BiConsumer<T,E> handler) {
        this.handler = handler;
        return this;
    }

    @Override
    public Input<T,E,I> mismatch(Consumer<I> handler) {
        this.mismatchHandler = (i, e) -> handler.accept(i);
        return this;
    }

    @Override
    public Input<T, E, I> mismatch(BiConsumer<I, E> handler) {
        this.mismatchHandler = handler;
        return this;
    }

    @Override
    public Input<T,E,I> expired(Consumer<I> handler) {
        this.expiredHandler = (i, e) -> handler.accept(i);
        return this;
    }

    @Override
    public Input<T, E, I> expired(BiConsumer<I, E> handler) {
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
    public BiConsumer<T,E> getInputHandler() {
        return handler;
    }

    @Override
    public BiConsumer<I,E> getMismatchHandler() {
        return mismatchHandler;
    }

    @Override
    public BiConsumer<I,E> getExpiredHandler() {
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
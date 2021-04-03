package dev.benergy10.minecrafttools.configs;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

class SimpleConfigOption<T> implements ConfigOption<T> {

    protected String path;
    protected T defaultValue;
    protected String[] comments;
    protected ConfigOptionHandler<T, ?> handler;
    protected Consumer<T> setConsumer;

    SimpleConfigOption() { }

    @Override
    public @NotNull String getPath() {
        return path;
    }

    @Override
    public @Nullable T getDefaultValue() {
        return defaultValue;
    }

    @Override
    public String @NotNull [] getComments() {
        return comments;
    }

    @Override
    public @NotNull ConfigOptionHandler<T, ?> getHandler() {
        return handler;
    }

    @Override
    public @NotNull Consumer<T> getSetConsumer() {
        return setConsumer;
    }
}

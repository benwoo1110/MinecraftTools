package dev.benergy10.minecrafttools.configs;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

class SimpleConfigOption<T> implements ConfigOption<T> {

    protected String path;
    protected T defaultValue;
    protected String[] comments;
    protected ConfigOptionHandler<T, ?> handler;

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
        return this.handler;
    }
}

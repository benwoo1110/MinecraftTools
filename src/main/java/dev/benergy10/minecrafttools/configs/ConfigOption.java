package dev.benergy10.minecrafttools.configs;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public interface ConfigOption<T> {

    @NotNull String getPath();

    @Nullable T getDefaultValue();

    @NotNull String[] getComments();

    @NotNull ConfigOptionHandler<T, ?> getHandler();

    @NotNull Consumer<T> getSetConsumer();

    class Builder<T> {

        private final SimpleConfigOption<T> option;
        private final List<String> comments;

        public Builder() {
            this.option = new SimpleConfigOption<>();
            this.comments = new ArrayList<>();
        }

        public Builder<T> path(String path) {
            this.option.path = path;
            return this;
        }

        public Builder<T> defaultValue(T defaultValue) {
            this.option.defaultValue = defaultValue;
            return this;
        }

        public Builder<T> comment(String comment, Object...replacements) {
            this.comments.add(String.format(comment, replacements));
            return this;
        }

        public Builder<T> handler(ConfigOptionHandler<T, ?> handler) {
            this.option.handler = handler;
            return this;
        }

        public Builder<T> setConsumer(Consumer<T> setConsumer) {
            this.option.setConsumer = setConsumer;
            return this;
        }

        public SimpleConfigOption<T> build() {
            Objects.requireNonNull(this.option.path);
            this.option.comments = this.comments.toArray(new String[0]);
            if (this.option.handler == null) {
                this.option.handler = ConfigOptionHandler.getDefault();
            }
            if (this.option.setConsumer == null) {
                this.option.setConsumer = (value) -> { };
            }
            return this.option;
        }

        public SimpleConfigOption<T> register(Consumer<ConfigOption<?>> optionRegister) {
            SimpleConfigOption<T> option = build();
            optionRegister.accept(option);
            return option;
        }
    }
}

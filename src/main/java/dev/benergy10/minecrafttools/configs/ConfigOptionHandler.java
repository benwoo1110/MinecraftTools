package dev.benergy10.minecrafttools.configs;

public interface ConfigOptionHandler<T> {

    Object serialize(T t);

    T deserialize(Object obj);

    static <T> ConfigOptionHandler<T> getDefault() {
        return new SimpleConfigOptionHandler<>();
    }
}

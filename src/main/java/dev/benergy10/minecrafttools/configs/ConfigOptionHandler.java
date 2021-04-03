package dev.benergy10.minecrafttools.configs;

import org.bukkit.configuration.file.YamlConfiguration;

public interface ConfigOptionHandler<T, D> {

    default D getData(YamlConfiguration config, String path) {
        return (D) config.get(path);
    }

    Object serialize(T t);

    T deserialize(D data);

    static <T> ConfigOptionHandler<T, Object> getDefault() {
        return new SimpleConfigOptionHandler<>();
    }
}

package dev.benergy10.minecrafttools.configs;

import java.util.Collection;
import java.util.Collections;

public interface YamlFile {
    boolean reload();

    boolean save();

    <T> void setValue(ConfigOption<T> option, T value);

    <T> T getValue(ConfigOption<T> option);

    Collection<ConfigOption<?>> getSupportedOptions();
}

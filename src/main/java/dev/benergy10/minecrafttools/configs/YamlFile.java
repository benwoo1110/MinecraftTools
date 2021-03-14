package dev.benergy10.minecrafttools.configs;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.Collection;

public interface YamlFile {
    boolean reload();

    boolean save();

    <T> void setValue(ConfigOption<T> option, T value);

    <T> T getValue(ConfigOption<T> option);

    Collection<ConfigOption<?>> getSupportedOptions();

    File getFile();

    YamlConfiguration getConfig();

    String[] getHeader();
}

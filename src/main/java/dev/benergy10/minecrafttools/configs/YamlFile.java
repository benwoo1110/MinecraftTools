package dev.benergy10.minecrafttools.configs;

import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.Collection;
import java.util.Optional;

public interface YamlFile {
    boolean reload();

    boolean save();

    <T> boolean setValue(ConfigOption<T> option, T value);

    boolean setValue(String optionPath, Object value);

    <T> T getValue(ConfigOption<T> option);

    @Nullable Object getValue(String optionPath);

    @NotNull Optional<ConfigOption> getOptionFromPath(String optionPath);

    Collection<ConfigOption> getSupportedOptions();

    File getFile();

    YamlConfiguration getConfig();

    String[] getHeader();
}

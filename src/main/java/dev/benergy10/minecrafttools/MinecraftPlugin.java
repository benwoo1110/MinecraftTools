package dev.benergy10.minecrafttools;

import co.aikar.commands.PaperCommandManager;
import co.aikar.locales.MessageKey;
import dev.benergy10.minecrafttools.utils.Logging;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public abstract class MinecraftPlugin extends JavaPlugin {

    protected CommandManager commandManager;

    @Override
    public final void onLoad() {
        Logging.setup(this);
        this.load();
    }

    public void load() { }

    @Override
    public final void onEnable() {
        this.commandManager = new CommandManager(this);
        this.enable();
    }

    public void enable() { }

    @NotNull
    public File getConfigFile() {
        this.getDataFolder().mkdirs();
        return new File(this.getDataFolder(), "config.yml");
    }

    @NotNull
    public CommandManager getCommandManager() {
        return commandManager;
    }
}

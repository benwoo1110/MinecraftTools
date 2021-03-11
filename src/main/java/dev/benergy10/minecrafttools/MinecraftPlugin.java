package dev.benergy10.minecrafttools;

import dev.benergy10.minecrafttools.utils.Logging;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.List;

public abstract class MinecraftPlugin extends JavaPlugin {

    @Override
    public final void onLoad() {
        Logging.setup(this);
        this.load();
    }

    public void load() { }

    @Override
    public final void onEnable() {
        this.enable();
    }

    public void enable() { }

    @Override
    public boolean onCommand(@NotNull CommandSender sender,
                             @NotNull Command command,
                             @NotNull String label,
                             @NotNull String[] args) {

        return super.onCommand(sender, command, label, args);
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender,
                                                @NotNull Command command,
                                                @NotNull String alias,
                                                @NotNull String[] args) {

        return super.onTabComplete(sender, command, alias, args);
    }

    public File getConfigFile() {
        this.getDataFolder().mkdirs();
        return new File(this.getDataFolder(), "config.yml");
    }
}

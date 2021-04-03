package dev.benergy10.minecrafttools.commands;

import co.aikar.commands.PaperCommandManager;
import dev.benergy10.minecrafttools.commands.flags.FlagGroupManager;
import org.bukkit.plugin.Plugin;

public class CommandManager extends PaperCommandManager {

    private final FlagGroupManager flagGroupManager;

    public CommandManager(Plugin plugin) {
        super(plugin);
        this.flagGroupManager = new FlagGroupManager(this);
    }

    public FlagGroupManager getFlagGroupManager() {
        return this.flagGroupManager;
    }
}

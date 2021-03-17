package dev.benergy10.minecrafttools.commands;

import co.aikar.commands.PaperCommandManager;
import dev.benergy10.minecrafttools.commands.flags.FlagGroupManager;
import dev.benergy10.minecrafttools.utils.FileResClassLoader;
import org.bukkit.plugin.Plugin;

import java.util.Locale;

public class CommandManager extends PaperCommandManager {

    private final FlagGroupManager flagGroupManager;

    public CommandManager(Plugin plugin) {
        super(plugin);
        this.flagGroupManager = new FlagGroupManager(this);

        // Locales
        this.getLocales().addBundleClassLoader(new FileResClassLoader(this.getClass().getClassLoader(), plugin));
        this.addSupportedLanguage(Locale.ENGLISH);
        this.getLocales().loadLanguages();
    }

    public FlagGroupManager getFlagGroupManager() {
        return this.flagGroupManager;
    }
}

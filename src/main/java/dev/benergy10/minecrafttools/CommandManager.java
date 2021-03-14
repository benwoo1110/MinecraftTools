package dev.benergy10.minecrafttools;

import co.aikar.commands.PaperCommandManager;
import dev.benergy10.minecrafttools.utils.FileResClassLoader;
import org.bukkit.plugin.Plugin;

import java.util.Locale;

public class CommandManager extends PaperCommandManager {

    public CommandManager(Plugin plugin) {
        super(plugin);
        this.getLocales().addBundleClassLoader(new FileResClassLoader(this.getClass().getClassLoader(), plugin));
        this.addSupportedLanguage(Locale.ENGLISH);
        this.getLocales().loadLanguages();
    }

}

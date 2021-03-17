package dev.benergy10.minecrafttools.commands.flags;

import co.aikar.commands.BukkitCommandCompletionContext;
import dev.benergy10.minecrafttools.commands.CommandManager;
import dev.benergy10.minecrafttools.utils.Logging;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class FlagGroupManager {

    private final CommandManager manager;
    private final Map<String, FlagGroup> flagGroupMap;

    public FlagGroupManager(CommandManager manager) {
        this.manager = manager;
        this.flagGroupMap = new HashMap<>();
        this.manager.getCommandCompletions().registerAsyncCompletion("flags", this::suggestFlags);
    }

    private @NotNull Collection<String> suggestFlags(BukkitCommandCompletionContext context) {
        String groupName = context.getConfig();
        FlagGroup flagGroup = this.getGroup(groupName);
        if (flagGroup == null) {
            Logging.warning("No flag group with name found: " + groupName);
            return Collections.emptyList();
        }
        String[] args = context.getContextValue(String[].class);
        return flagGroup.suggestNextArgument(args);
    }

    public void addNewGroup(String name, FlagGroup group) {
        String trimmedName = name.toLowerCase();
        if (this.flagGroupMap.containsKey(trimmedName)) {
            throw new IllegalArgumentException("Duplicate flag group name: " + trimmedName);
        }
        this.flagGroupMap.put(trimmedName, group);
    }

    public FlagGroup getGroup(String name) {
        return this.flagGroupMap.get(name.toLowerCase());
    }

    public Collection<FlagGroup> getGroups() {
        return this.flagGroupMap.values();
    }

    public Collection<String> getGroupNames() {
        return this.flagGroupMap.keySet();
    }

    public CommandManager getManager() {
        return manager;
    }
}

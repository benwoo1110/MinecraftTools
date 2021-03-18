package dev.benergy10.minecrafttools.commands.flags;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A group of {@link Flag}, with indexed keys for efficient lookup.
 */
public class FlagGroup {

    public static final FlagGroup EMPTY = new FlagGroup(Collections.emptyList());

    public static FlagGroup empty() {
        return EMPTY;
    }

    /**
     * Create a new Flag Group with multiple {@link Flag}.
     *
     * @param flags Multiple flags.
     * @return A new {@link FlagGroup} generated from the flags.
     */
    public static FlagGroup of(Flag<?>...flags) {
        return new FlagGroup(Arrays.asList(flags));
    }

    private final List<String> identifiers;
    private final Set<Flag<?>> flags;
    private final Map<String, Flag<?>> keyMap;

    /**
     * Create a new Flag Group with multiple {@link Flag}.
     *
     * @param flags Array of flags
     */
    public FlagGroup(Collection<Flag<?>> flags) {
        this.identifiers = new ArrayList<>(flags.size());
        this.flags = new HashSet<>(flags);
        this.keyMap = new HashMap<>();
        this.flags.forEach(this::addFlag);
    }

    /**
     * Add and indexes a flag.
     *
     * @param flag  The flag to add.
     */
    private void addFlag(Flag<?> flag) {
        String id = flag.getIdentifier().toLowerCase();
        this.identifiers.add(id);
        this.keyMap.put(id, flag);
        for (String flagAlias : flag.getAliases()) {
            this.keyMap.put(flagAlias, flag);
        }
    }

    /**
     * Suggest possible identifiers available for this Flag Group.
     *
     * @param args  Current state of the arguments.
     * @return A collection of suggested text for the next flag argument.
     */
    @NotNull
    public Collection<String> suggestNextArgument(String[] args) {
        Flag<?> flag = (args.length <= 1) ? null : this.getByKey(args[args.length - 2]);
        if (flag == null || flag instanceof NoValueFlag) {
            // suggest new flags.
            return getRemainingFlagIdentifiers(args);
        }
        Collection<String> suggestions = flag.suggestValue();
        if (flag instanceof OptionalValueFlag) {
            // suggest new flags and values.
            suggestions.addAll(getRemainingFlagIdentifiers(args));
            return suggestions;
        }
        // suggest new values.
        return suggestions == null ? Collections.emptySet() : suggestions;
    }

    private Collection<String> getRemainingFlagIdentifiers(String[] args) {
        Set<String> identifiersRemaining = new HashSet<>(this.identifiers);
        identifiersRemaining.removeAll(Arrays.asList(args));
        return identifiersRemaining;
    }

    /**
     * Parse the arguments to get it's flag values.
     *
     * @param args  The arguments to parse.
     * @return A {@link FlagValues} containing value results.
     */
    @NotNull
    public FlagValues parse(String[] args) throws FlagParseFailedException {
        return FlagValues.parse(args,this);
    }

    /**
     * Gets flag from pre-indexed key mapping.
     *
     * @param key   The target key.
     * @return A {@link Flag} if found, else null.
     */
    @Nullable
    public Flag<?> getByKey(@Nullable String key) {
        if (key == null) {
            return null;
        }
        return this.keyMap.get(key.toLowerCase());
    }

    public boolean containsFlag(Flag<?> flag) {
        return this.flags.contains(flag);
    }

    public Set<Flag<?>> getFlags() {
        return flags;
    }

    public Collection<String> getIdentifiers() {
        return identifiers;
    }

    @Override
    public String toString() {
        return "FlagGroup{" +
                "identifiers=" + identifiers +
                ", flags=" + flags +
                ", keyMap=" + keyMap +
                '}';
    }
}

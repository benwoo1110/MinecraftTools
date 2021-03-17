package dev.benergy10.minecrafttools.commands.flags;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;

/**
 * This flag will always not require a user input to parse value.
 *
 * @param <T>   The flag Type.
 */
public abstract class NoValueFlag<T> extends Flag<T> {

    /**
     * {@inheritDoc}
     */
    public NoValueFlag(String name, String identifier, Class<T> type) {
        super(name, identifier, type);
    }

    /**
     * {@link NoValueFlag} will always not require a user input to parse value.
     * Thus, no value suggestion needed.
     */
    @Override
    public final Collection<String> suggestValue() {
        return Collections.emptyList();
    }

    /**
     * {@link NoValueFlag} will always not require a user input to parse value.
     * Thus, this operation is not allowed.
     */
    @Override
    public final T getValue(@NotNull String input) {
        throw new FlagParseFailedException("%s flag '%s' does not require a value.", this.name, this.identifier);
    }
}

package dev.benergy10.minecrafttools.commands.flags;

/**
 * Command Flag that optionally allows a user input value.
 *
 * @param <T>   The flag Type.
 */
public abstract class OptionalValueFlag<T> extends Flag<T> {

    /**
     * {@inheritDoc}
     */
    public OptionalValueFlag(String name, String identifier, Class<T> type) {
        super(name, identifier, type);
    }
}

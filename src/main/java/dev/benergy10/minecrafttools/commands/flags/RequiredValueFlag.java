package dev.benergy10.minecrafttools.commands.flags;

/**
 * This flag will always require a user input to parse value.
 *
 * @param <T>   The flag Type.
 */
public abstract class RequiredValueFlag<T> extends Flag<T> {

    /**
     * {@inheritDoc}
     */
    public RequiredValueFlag(String name, String identifier, Class<T> type) {
        super(name, identifier, type);
    }

    /**
     * {@link RequiredValueFlag} will always require a user input to parse value.
     * Thus, this operation is not allowed.
     */
    @Override
    public final T getValue() {
        throw new FlagParseFailedException("%s flag '%s' requires a value input.", this.name, this.identifier);
    }
}

package dev.benergy10.minecrafttools.commands.flags;

import co.aikar.commands.InvalidCommandArgument;
import co.aikar.locales.MessageKey;
import co.aikar.locales.MessageKeyProvider;

/**
 * Thrown when there is an issue with parsing flags from string arguments.
 */
public class FlagParseFailedException extends InvalidCommandArgument {

    public FlagParseFailedException() {
    }

    public FlagParseFailedException(boolean showSyntax) {
        super(showSyntax);
    }

    public FlagParseFailedException(MessageKeyProvider key, String... replacements) {
        super(key, replacements);
    }

    public FlagParseFailedException(MessageKey key, String... replacements) {
        super(key, replacements);
    }

    public FlagParseFailedException(MessageKeyProvider key, boolean showSyntax, String... replacements) {
        super(key, showSyntax, replacements);
    }

    public FlagParseFailedException(MessageKey key, boolean showSyntax, String... replacements) {
        super(key, showSyntax, replacements);
    }

    public FlagParseFailedException(String message, Object...replacements) {
        super(String.format(message, replacements));
    }

    public FlagParseFailedException(String message, boolean showSyntax) {
        super(message, showSyntax);
    }
}

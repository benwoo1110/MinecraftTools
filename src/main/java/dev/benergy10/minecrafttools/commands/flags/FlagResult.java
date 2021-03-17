package dev.benergy10.minecrafttools.commands.flags;

import com.google.common.base.Strings;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents the value result parsed from command arguments.
 */
public class FlagResult {

    public static FlagResult newEmpty(@NotNull FlagGroup flagGroup) {
        return new FlagResult(flagGroup);
    }

    /**
     * Parse arguments into its flag key and values.
     *
     * @param args      The arguments to parse.
     * @param flagGroup The flags available to parse into.
     * @return The {@link FlagResult} from the parse.
     */
    public static FlagResult parse(@Nullable String[] args,
                                   @NotNull FlagGroup flagGroup) throws FlagParseFailedException {

        Creator flagResult = new FlagResult.Creator(flagGroup);

        // No args to parse.
        if (args == null || args.length <= 0) {
            return flagResult.finalise();
        }

        // First arg must be a flag.
        Flag<?> currentFlag = flagGroup.getByKey(args[0]);
        boolean completed = false;

        // Parse the arguments.
        for (int i = 1, argsLength = args.length; i <= argsLength; i++) {
            if (currentFlag == null) {
                throw new FlagParseFailedException("%s is not a valid flag.", args[i-1]);
            }
            // This ensures that flag is not null during final parse.
            if (i >= argsLength) {
                break;
            }

            String arg = args[i];
            if (arg == null) {
                throw new FlagParseFailedException("Arguments cannot be null!");
            }

            Flag<?> nextFlag = flagGroup.getByKey(arg);

            // Arg must be a flag key.
            if (currentFlag instanceof NoValueFlag) {
                flagResult.addFromInput(currentFlag, null);
                currentFlag = nextFlag;
                continue;
            }

            // Arg can be a flag key or value.
            if (currentFlag instanceof OptionalValueFlag) {
                if (nextFlag != null) {
                    // It's a key.
                    flagResult.addFromInput(currentFlag, null);
                    currentFlag = nextFlag;
                    continue;
                }
                // It's a value.
                flagResult.addFromInput(currentFlag, arg);
                if (i == argsLength - 1) {
                    completed = true;
                    break;
                }
                currentFlag = flagGroup.getByKey(args[++i]);
                continue;
            }

            // Arg must be a flag value.
            if (currentFlag instanceof RequiredValueFlag) {
                if (nextFlag != null) {
                    // It's a key, error!
                    throw new FlagParseFailedException("%s flag '%s' requires a value input.",
                            currentFlag.getName(), currentFlag.getIdentifier());
                }
                // It's a value.
                flagResult.addFromInput(currentFlag, arg);
                if (i == argsLength - 1) {
                    completed = true;
                    break;
                }
                currentFlag = flagGroup.getByKey(args[++i]);
            }
        }

        // Parse last flag.
        if (!completed) {
            if (currentFlag instanceof RequiredValueFlag) {
                throw new FlagParseFailedException("%s flag '%s' requires a value input.",
                        currentFlag.getName(), currentFlag.getIdentifier());
            }
            flagResult.addFromInput(currentFlag, null);
        }

        return flagResult.finalise();
    }

    private final FlagGroup flagGroup;
    private final Map<Flag<?>, SingleFlagResult<?>> resultMap;

    private FlagResult(FlagGroup flagGroup) {
        this.flagGroup = flagGroup;
        resultMap = new HashMap<>();
    }

    /**
     * Gets value of a flag.
     *
     * @param flag  The flag to get value from.
     * @param <T>   The type of value.
     * @return The value which is associated with the flag.
     */
    public <T> T getValue(Flag<T> flag) {
        if (!this.flagGroup.containsFlag(flag)) {
            throw new IllegalArgumentException("Flag is not in group: " + flag.getName());
        }
        SingleFlagResult<?> result = resultMap.get(flag);
        if (result == null) {
            return flag.getDefaultValue();
        }
        return (T) result.value;
    }

    /**
     * Gets if the flag value is by a user input.
     *
     * @param flag  The flag to check against.
     * @return True if value is by user input, else false.
     */
    public boolean isByUserInput(Flag<?> flag) {
        SingleFlagResult<?> result = resultMap.get(flag);
        if (result == null) {
            return false;
        }
        return result.isFromUserInput;
    }

    /**
     * Gets if the flag is a default value, and key was not present in user's command arguments.
     * i.e. Not in the {@link #resultMap}.
     *
     * @param flag  The flag to check against.
     * @return True if flag was not present in user's command arguments, else false.
     */
    public boolean isDefaulted(Flag<?> flag) {
        return resultMap.get(flag) != null;
    }

    public boolean isForGroup(FlagGroup group) {
        return this.flagGroup.equals(group);
    }

    public FlagGroup getFlagGroup() {
        return this.flagGroup;
    }

    @Override
    public String toString() {
        return "FlagResult{" +
                "resultMap=" + resultMap +
                '}';
    }

    /**
     * Represents a single value result parsed.
     * Stores value and addition data such as if value is by user input.
     *
     * @param <T>   The type of value.
     */
    private static class SingleFlagResult<T> {
        private final T value;
        private final boolean isFromUserInput;

        /**
         * @param value     The resultant value from argument parsing.
         * @param fromInput Indicates if value is parsed by user input.
         */
        private SingleFlagResult(T value, boolean fromInput) {
            this.value = value;
            this.isFromUserInput = fromInput;
        }

        @Override
        public String toString() {
            return "SingleFlagResult{" +
                    "value=" + value +
                    ", isFromUserInput=" + isFromUserInput +
                    '}';
        }
    }

    public static class Creator {

        private final FlagResult result;

        public Creator(FlagGroup group) {
            this.result = new FlagResult(group);
        }

        /**
         * Add a new value result from parsing arguments.
         *
         * @param flag          The flag that the value represents.
         * @param inputValue    The raw arg input of the flag.
         */
        public Creator addFromInput(Flag<?> flag, @Nullable String inputValue) {
            if (!this.result.flagGroup.containsFlag(flag)) {
                throw new IllegalArgumentException("Flag is not in group: " + flag.getName());
            }
            if (Strings.isNullOrEmpty(inputValue)) {
                this.result.resultMap.put(flag, new SingleFlagResult<>(flag.getValue(), false));
                return this;
            }
            this.result.resultMap.put(flag, new SingleFlagResult<>(flag.getValue(inputValue), true));
            return this;
        }

        public <T> Creator add(Flag<T> flag, T value) {
            this.result.resultMap.put(flag, new SingleFlagResult<>(value, false));
            return this;
        }

        public FlagResult finalise() {
            return this.result;
        }
    }
}

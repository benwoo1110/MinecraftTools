package dev.benergy10.minecrafttools.configs;

import com.google.common.base.Strings;
import dev.benergy10.minecrafttools.utils.Logging;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

public class YamlFile {

    private static final int DEPTH_SPACES = 2;
    private static final Pattern CONFIG_KEY = Pattern.compile("[a-zA-Z0-9_-]+");

    private final File file;
    private final Set<ConfigOption<?>> configOptions;
    private final Map<String, String[]> comments;
    private final Map<ConfigOption<?>, Object> cacheOptionValues;

    private YamlConfiguration config;

    public YamlFile(File file, Collection<ConfigOption<?>> configOptions) {
        this.file = file;
        this.configOptions = new HashSet<>(configOptions);
        this.comments = new HashMap<>();
        this.cacheOptionValues = new HashMap<>();
        this.setup();
    }

    private void setup() {
        for (ConfigOption<?> option : configOptions) {
            this.comments.put(option.getPath(), option.getComments());
        }
        this.reload();
    }

    public boolean reload() {
        this.load();
        return this.save();
    }

    private void load() {
        this.cacheOptionValues.clear();
        if (!createYamlFile()) {
            return;
        }
        this.config = YamlConfiguration.loadConfiguration(this.file);
        loadDefaultValues();
    }

    private boolean createYamlFile() {
        try {
            this.file.getParentFile().mkdirs();
            if (this.file.createNewFile()) {
                Logging.info("Create new %s file.", this.file.getName());
            }
        } catch (IOException e) {
            Logging.severe("An error occurred while trying to create %s file.", this.file.getName());
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private void loadDefaultValues() {
        for (ConfigOption<?> option : this.configOptions) {
            if (this.config.get(option.getPath()) == null) {
                this.config.set(option.getPath(), option.getDefaultValue());
            }
        }
    }

    public boolean save() {
        try {
            this.config.options().header(null);
            this.config.save(this.file);
            return this.writeToFile(this.addCommentsToFile());
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private String addCommentsToFile() throws IOException {
        BufferedReader fileData = new BufferedReader(new FileReader(this.file));
        ConfigPath path = new ConfigPath();
        ConfigContents configContents = new ConfigContents();
        int currentDepth = 0;
        String line;

        while ((line = fileData.readLine()) != null) {
            String key = getSectionKey(line);
            if (key == null) {
                configContents.addLine(line);
                continue;
            }

            int lineDepth = getLineDepth(line);
            int depthDifference = lineDepth - currentDepth;
            adjustDepth(path, key, depthDifference);
            currentDepth = lineDepth;

            insertComments(configContents, currentDepth, this.comments.get(path.getPathString()));
            configContents.addLine(line);
        }

        fileData.close();
        return configContents.build();
    }

    private String getSectionKey(String line) {
        String[] split = line.trim().split(":", 2);
        if (split.length != 2) {
            return null;
        }
        String key = split[0];
        if (!CONFIG_KEY.matcher(key).matches()) {
            return null;
        }
        return key;
    }

    private int getLineDepth(String line) {
        int spaces = 0;
        for (char c : line.toCharArray()) {
            if (c == ' ') {
                spaces++;
            } else {
                break;
            }
        }
        return spaces / DEPTH_SPACES;
    }

    private void adjustDepth(ConfigPath path, String key, int depthDifference) {
        if (depthDifference == 0) {
            path.changeCurrent(key);
        } else if (depthDifference < 0) {
            path.backToParent(key, -depthDifference);
        } else if (depthDifference == 1) {
            path.newChild(key);
        } else {
            throw new IllegalArgumentException("Invalid depth change: " + depthDifference);
        }
    }

    private void insertComments(ConfigContents configContents, int currentDepth, String[] comments) {
        if (comments == null) {
            return;
        }
        for (String comment : comments) {
            String formattedComment = this.formatComment(comment, currentDepth);
            configContents.addLine(formattedComment);
        }
    }

    private String formatComment(String comment, int depth) {
        if (Strings.isNullOrEmpty(comment)) {
            return "";
        }
        return Strings.repeat(" ", depth * DEPTH_SPACES) + "# " + comment;
    }

    private boolean writeToFile(String data) {
        try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8)) {
            writer.write(data);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public <T> void setValue(ConfigOption<T> option, T value) {
        this.config.set(option.getPath(), option.getHandler().serialize(value));
        this.cacheOptionValues.put(option, value);
    }

    public <T> T getValue(ConfigOption<T> option) {
        return (T) this.cacheOptionValues.computeIfAbsent(option, (ConfigOption<?> opt) -> computeValue(opt));
    }

    private Object computeValue(ConfigOption<?> option) {
        return option.getHandler().deserialize(this.config.get(option.getPath()));
    }
}

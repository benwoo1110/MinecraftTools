package dev.benergy10.minecrafttools.configs;

public class ConfigContents {

    private final StringBuilder builder;

    public ConfigContents() {
        this.builder = new StringBuilder();
    }

    public ConfigContents addLine(String line) {
        this.builder.append(line).append(System.lineSeparator());
        return this;
    }

    public String build() {
        return this.builder.toString();
    }
}

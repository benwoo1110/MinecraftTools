package dev.benergy10.minecrafttools.configs;

import java.util.LinkedList;

public class ConfigPath {

    private final LinkedList<String> path;

    public ConfigPath() {
        this.path = new LinkedList<>();
    }

    public void newChild(String key) {
        this.path.add(key);
    }

    public void backToParent(String key, int by) {
        for (int i = 0; i < by; i++) {
            back();
        }
        if (!this.getCurrentKey().equals(key)) {
            this.changeCurrent(key);
        }
    }

    public void changeCurrent(String key) {
        this.back();
        this.newChild(key);
    }

    private void back() {
        if (!this.path.isEmpty()) {
            this.path.removeLast();
        }
    }

    public String getCurrentKey() {
        return (this.path.isEmpty()) ? "" : this.path.getLast();
    }

    public String getPathString() {
        if (this.path.isEmpty()) {
            return "";
        }
        StringBuilder pathString = new StringBuilder();
        for (String key : this.path) {
            pathString.append(key);
            if (!key.equals(this.path.getLast())) {
                pathString.append('.');
            }
        }
        return pathString.toString();
    }
}

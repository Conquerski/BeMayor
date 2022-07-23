package me.bemayor.api.commands.subcommands;

import io.github.bakedlibs.dough.config.Config;

public class ConfigInputEvent {
    private final Config config;
    private final String path;
    private final String text;

    public ConfigInputEvent(Config config, String path, String text) {
        this.config = config;
        this.path = path;
        this.text = text;
    }

    public Config getConfig() {
        return config;
    }

    public String getPath() {
        return path;
    }

    public String getText() {
        return text;
    }
}

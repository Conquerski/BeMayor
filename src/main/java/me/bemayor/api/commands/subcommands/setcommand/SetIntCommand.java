package me.bemayor.api.commands.subcommands.setcommand;

import io.github.bakedlibs.dough.config.Config;
import me.bemayor.api.commands.subcommands.SetCommand;
import me.bemayor.api.common.CommonPatterns;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class SetIntCommand extends SetCommand {
    public SetIntCommand(String vaultName, String description, Config config, String path, Supplier<List<String>> listForTab, Consumer<Integer> newAction) {
        super(vaultName, description, config, path, listForTab,
                s -> {
                    return CommonPatterns.ZEROINTEGER.matcher(s).matches() || CommonPatterns.POSITIVEINTEGER.matcher(s).matches() || CommonPatterns.NEGATIVEINTEGER.matcher(s).matches();
                },
                e -> {
                    newAction.accept(Integer.parseInt(e.getTexts().get(0)));
                },
                e -> {
                    e.getConfig().setValue(e.getPath(), Integer.parseInt(e.getText()));
                }
        );
    }

    public SetIntCommand(String vaultName, String description, Supplier<List<String>> listForTab, Consumer<Integer> newAction) {
        this(vaultName, description, null, "", listForTab, newAction);
    }
}

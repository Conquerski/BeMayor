package me.bemayor.api.commands.subcommands.setcommand;

import io.github.bakedlibs.dough.config.Config;
import me.bemayor.api.commands.subcommands.SetCommand;
import me.bemayor.api.common.CommonPatterns;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class SetDoubleCommand extends SetCommand {
    public SetDoubleCommand(String vaultName, String description, Config config, String path, Supplier<List<String>> listForTab, Consumer<Double> newAction){
        super(vaultName, description, config, path, listForTab,
                s -> {
                    return CommonPatterns.DECIMAL.matcher(s).matches();
                },
                e ->{
                    newAction.accept(Double.valueOf(e.getTexts().get(0)));
                },
                e -> {
                    e.getConfig().setValue(e.getPath(), Double.valueOf(e.getText()));
                }
        );
    }
    public SetDoubleCommand(String vaultName, String description, Supplier<List<String>> listForTab, Consumer<Double> newAction){
        this(vaultName,description,null,"",listForTab,newAction);
    }
}

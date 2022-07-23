package me.bemayor.components.theoriginalmodify.commands;

import io.github.bakedlibs.dough.config.Config;
import me.bemayor.api.commands.SubCommand;
import me.bemayor.api.commands.subcommands.OnOffCommand;
import me.bemayor.api.commands.subcommands.setcommand.SetIntCommand;
import me.bemayor.components.theoriginalmodify.listener.BlockBreakEventListener;
import me.bemayor.components.theoriginalmodify.listener.MultipleDurabilityListener;
import me.bemayor.components.theoriginalmodify.listener.NoMendingListener;

import java.util.ArrayList;
import java.util.Arrays;

public class TheOriginalModifyCommands {

    private final Config config;
    private SubCommand command;

    public TheOriginalModifyCommands(Config config,NoMendingListener noMendingListener,
                                     MultipleDurabilityListener multipleDurabilityListener,
                                     BlockBreakEventListener blockBreakEventListener){
        this.config=config;

        command=new SubCommand("theOriginalModify","原版修改组件的指令集",false);

        command.addSubCommand(new SubCommand("noMendingListener","禁用经验修补的设置",false,null,
                new ArrayList<>(Arrays.asList(
                        new OnOffCommand("禁用经验修补",config,"noMending.onOff",
                                sender -> { noMendingListener.on_off=true; },
                                sender -> { noMendingListener.on_off=false; })
                ))
        ));

        command.addSubCommand(new SubCommand("blockBreakEventListener","幸运挖方块的设置",false,null,
                new ArrayList<>(Arrays.asList(
                        new OnOffCommand("幸运挖方块", config,"blockBreakEvent.onOff",
                                sender -> { blockBreakEventListener.on_off=true; },
                                sender -> { blockBreakEventListener.on_off=false; }),
                        new SubCommand("stone","挖石头的设置",false,null,
                                new ArrayList<>(Arrays.asList(
                                        new SubCommand("enchantedGoldenApple","挖出附魔金苹果的设置",false,null,
                                                new ArrayList<>(Arrays.asList(
                                                        new SetIntCommand("Probability","挖出附魔金苹果的可能性", config,"blockBreakEvent.stone.enchantedGoldenApple.probability",
                                                                ()->{return new ArrayList<>(Arrays.asList("0",String.valueOf(blockBreakEventListener.stoneAppleProbability),"1000"));},
                                                                i -> { blockBreakEventListener.stoneAppleProbability=i; }),
                                                        new SetIntCommand("Amount","挖出附魔金苹果的数量", config,"blockBreakEvent.stone.enchantedGoldenApple.amount",
                                                                ()->{return new ArrayList<>(Arrays.asList("1",String.valueOf(blockBreakEventListener.stoneAppleAmount),"64"));},
                                                                i -> { blockBreakEventListener.stoneAppleAmount=i; })
                                                ))
                                        ),
                                        new SubCommand("exp","挖出经验球的设置",false,null,
                                                new ArrayList<>(Arrays.asList(
                                                        new SetIntCommand("Probability","挖出经验球的可能性", config,"blockBreakEvent.stone.exp.probability",
                                                                ()->{return new ArrayList<>(Arrays.asList("0",String.valueOf(blockBreakEventListener.stoneExpProbability),"1000"));},
                                                                i -> { blockBreakEventListener.stoneExpProbability=i; }),
                                                        new SetIntCommand("Amount","挖出经验球的数量", config,"blockBreakEvent.stone.exp.amount",
                                                                ()->{return new ArrayList<>(Arrays.asList("1",String.valueOf(blockBreakEventListener.stoneExpAmount),"10000"));},
                                                                i -> { blockBreakEventListener.stoneExpAmount=i; })
                                                ))
                                        )
                                ))
                        ),
                        new SubCommand("deepslate","挖深板岩的设置",false,null,
                                new ArrayList<>(Arrays.asList(
                                        new SubCommand("enchantedGoldenApple","挖出附魔金苹果的设置",false,null,
                                                new ArrayList<>(Arrays.asList(
                                                        new SetIntCommand("Probability","挖出附魔金苹果的可能性", config,"blockBreakEvent.deepslate.enchantedGoldenApple.probability",
                                                                ()->{return new ArrayList<>(Arrays.asList("0",String.valueOf(blockBreakEventListener.deepslateAppleProbability),"1000"));},
                                                                i -> { blockBreakEventListener.deepslateAppleProbability=i; }),
                                                        new SetIntCommand("Amount","挖出附魔金苹果的数量", config,"blockBreakEvent.deepslate.enchantedGoldenApple.amount",
                                                                ()->{return new ArrayList<>(Arrays.asList("1",String.valueOf(blockBreakEventListener.deepslateAppleAmount),"64"));},
                                                                i -> { blockBreakEventListener.deepslateAppleAmount=i; })
                                                ))
                                        )
                                ))
                        )
                ))
        ));

        command.addSubCommand(new SubCommand("multipleDurabilityListener","工具更废耐久的设置",false,null,
                new ArrayList<>(Arrays.asList(
                        new OnOffCommand("工具更废耐久",config,"multipleDurability.onOff",
                                sender -> { multipleDurabilityListener.on_off=true; },
                                sender -> { multipleDurabilityListener.on_off=false; }),
                        new SubCommand("dr","随机6份的设置",false,null,
                                new ArrayList<>(Arrays.asList(
                                        new SubCommand("dr1","随机6份中第1份的设置",false,null,
                                                new ArrayList<>(Arrays.asList(
                                                        new SetIntCommand("Probability","第1份的可能性", config,"multipleDurability.dr1.probability",
                                                                ()->{return new ArrayList<>(Arrays.asList("0",String.valueOf(multipleDurabilityListener.dr1Probability),"1000"));},
                                                                i -> { multipleDurabilityListener.dr1Probability=i; }),
                                                        new SetIntCommand("Amount","第1份的数量",config,"multipleDurability.dr1.amount",
                                                                ()->{return new ArrayList<>(Arrays.asList("-100",String.valueOf(multipleDurabilityListener.dr1Amount),"100"));},
                                                                i -> { multipleDurabilityListener.dr1Amount=i; })
                                                ))
                                        ),
                                        new SubCommand("dr2","随机6份中第2份的设置",false,null,
                                                new ArrayList<>(Arrays.asList(
                                                        new SetIntCommand("Probability","第2份的可能性", config,"multipleDurability.dr2.probability",
                                                                ()->{return new ArrayList<>(Arrays.asList("0",String.valueOf(multipleDurabilityListener.dr2Probability),"1000"));},
                                                                i -> { multipleDurabilityListener.dr2Probability=i; }),
                                                        new SetIntCommand("Amount","第2份的数量",config,"multipleDurability.dr2.amount",
                                                                ()->{return new ArrayList<>(Arrays.asList("-100",String.valueOf(multipleDurabilityListener.dr2Amount),"100"));},
                                                                i -> { multipleDurabilityListener.dr2Amount=i; })
                                                ))
                                        ),
                                        new SubCommand("dr3","随机6份中第3份的设置",false,null,
                                                new ArrayList<>(Arrays.asList(
                                                        new SetIntCommand("Probability","第3份的可能性", config,"multipleDurability.dr3.probability",
                                                                ()->{return new ArrayList<>(Arrays.asList("0",String.valueOf(multipleDurabilityListener.dr3Probability),"1000"));},
                                                                i -> { multipleDurabilityListener.dr3Probability=i; }),
                                                        new SetIntCommand("Amount","第3份的数量",config,"multipleDurability.dr3.amount",
                                                                ()->{return new ArrayList<>(Arrays.asList("-100",String.valueOf(multipleDurabilityListener.dr3Amount),"100"));},
                                                                i -> { multipleDurabilityListener.dr3Amount=i; })
                                                ))
                                        ),
                                        new SubCommand("dr4","随机6份中第4份的设置",false,null,
                                                new ArrayList<>(Arrays.asList(
                                                        new SetIntCommand("Probability","第4份的可能性", config,"multipleDurability.dr4.probability",
                                                                ()->{return new ArrayList<>(Arrays.asList("0",String.valueOf(multipleDurabilityListener.dr4Probability),"1000"));},
                                                                i -> { multipleDurabilityListener.dr4Probability=i; }),
                                                        new SetIntCommand("Amount","第4份的数量",config,"multipleDurability.dr4.amount",
                                                                ()->{return new ArrayList<>(Arrays.asList("-100",String.valueOf(multipleDurabilityListener.dr4Amount),"100"));},
                                                                i -> { multipleDurabilityListener.dr4Amount=i; })
                                                ))
                                        ),
                                        new SubCommand("dr5","随机6份中第5份的设置",false,null,
                                                new ArrayList<>(Arrays.asList(
                                                        new SetIntCommand("Probability","第5份的可能性", config,"multipleDurability.dr5.probability",
                                                                ()->{return new ArrayList<>(Arrays.asList("0",String.valueOf(multipleDurabilityListener.dr5Probability),"1000"));},
                                                                i -> { multipleDurabilityListener.dr5Probability=i; }),
                                                        new SetIntCommand("Amount","第5份的数量",config,"multipleDurability.dr5.amount",
                                                                ()->{return new ArrayList<>(Arrays.asList("-100",String.valueOf(multipleDurabilityListener.dr5Amount),"100"));},
                                                                i -> { multipleDurabilityListener.dr5Amount=i; })
                                                ))
                                        ),
                                        new SubCommand("dr6","随机6份中第6份的设置",false,null,
                                                new ArrayList<>(Arrays.asList(
                                                        new SetIntCommand("Probability","第6份的可能性", config,"multipleDurability.dr6.probability",
                                                                ()->{return new ArrayList<>(Arrays.asList("0",String.valueOf(multipleDurabilityListener.dr6Probability),"1000"));},
                                                                i -> { multipleDurabilityListener.dr6Probability=i; }),
                                                        new SetIntCommand("Amount","第6份的数量",config,"multipleDurability.dr6.amount",
                                                                ()->{return new ArrayList<>(Arrays.asList("-100",String.valueOf(multipleDurabilityListener.dr6Amount),"100"));},
                                                                i -> { multipleDurabilityListener.dr6Amount=i; })
                                                ))
                                        )
                                ))
                        ),
                        new SubCommand("rate","加速损耗的倍率的设置",false,null,
                                new ArrayList<>(Arrays.asList(
                                        new SetIntCommand("nephrite","下界合金质的倍率", config,"multipleDurability.rate.nephrite",
                                                ()->{return new ArrayList<>(Arrays.asList("0",String.valueOf(multipleDurabilityListener.netheriteRate),"10"));},
                                                i -> { multipleDurabilityListener.netheriteRate =i; }),
                                        new SetIntCommand("diamond","钻石质的倍率", config,"multipleDurability.rate.diamond",
                                                ()->{return new ArrayList<>(Arrays.asList("0",String.valueOf(multipleDurabilityListener.diamondRate),"10"));},
                                                i -> { multipleDurabilityListener.diamondRate=i; }),
                                        new SetIntCommand("iron","铁质的倍率", config,"multipleDurability.rate.iron",
                                                ()->{return new ArrayList<>(Arrays.asList("0",String.valueOf(multipleDurabilityListener.ironRate),"10"));},
                                                i -> { multipleDurabilityListener.ironRate=i; }),
                                        new SetIntCommand("golden","金质的倍率", config,"multipleDurability.rate.golden",
                                                ()->{return new ArrayList<>(Arrays.asList("0",String.valueOf(multipleDurabilityListener.goldenRate),"10"));},
                                                i -> { multipleDurabilityListener.goldenRate=i; }),
                                        new SetIntCommand("stone","石质的倍率", config,"multipleDurability.rate.stone",
                                                ()->{return new ArrayList<>(Arrays.asList("0",String.valueOf(multipleDurabilityListener.stoneRate),"10"));},
                                                i -> { multipleDurabilityListener.stoneRate=i; }),
                                        new SetIntCommand("wooden","木质的倍率", config,"multipleDurability.rate.wooden",
                                                ()->{return new ArrayList<>(Arrays.asList("0",String.valueOf(multipleDurabilityListener.woodenRate),"10"));},
                                                i -> { multipleDurabilityListener.woodenRate=i; }),
                                        new SetIntCommand("elytra","鞘翅的倍率", config,"multipleDurability.rate.elytra",
                                                ()->{return new ArrayList<>(Arrays.asList("0",String.valueOf(multipleDurabilityListener.elytraRate),"10"));},
                                                i -> { multipleDurabilityListener.elytraRate=i; }),
                                        new SetIntCommand("other","其他材质的倍率", config,"multipleDurability.rate.other",
                                                ()->{return new ArrayList<>(Arrays.asList("0",String.valueOf(multipleDurabilityListener.otherRate),"10"));},
                                                i -> { multipleDurabilityListener.otherRate=i; })
                                ))
                        )
                ))
        ));


    }

    public SubCommand getCommand(){return command;}
}

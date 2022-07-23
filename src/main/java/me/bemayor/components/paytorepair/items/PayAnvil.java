package me.bemayor.components.paytorepair.items;

import me.bemayor.api.customitem.CustomItemStack;
import me.bemayor.components.paytorepair.PayToRepair;
import me.bemayor.components.transition.items.HeadTexture;


/*
自定义ItemStack在创建时就会带有独特的NBT标签
所有的自定义ItemStack具有三态：物品、方块、界面
物品和方块由CustomItemStack掌控，界面由CustomGui掌控
ItemStack与CustomItemStack构成继承关系，与CustomGui构成调用关系
CustomItemStack由使用事件(玩家用物品右击方块的交互事件)、原版对应物、ID、名称、物品可否放置为方块、物品或方块使用时是否消耗、显示标签、识别标签组成
在游戏中实际是带有识别标签的原版物品，在处理事件时，通过标签找到对应的CustomItemStack并获得存放的原版物品模板，CustomItemStack用来提供事件动作，原版物品模板提供meta
当一次交互事件中物品和方块均存在使用事件，则触发物品的事件而不触发方块的事件
物品可放置为方块时，使用事件由方块触发；物品不可放置为方块时，使用事件由物品触发
使用事件由物品和方块中的谁触发，则谁就可以被消耗
界面通过使用事件的打开

 */
/*
修理台
作用：一种用于付费维修的工作台
性质：放置使用；该方块不会因使用而消耗
外观：不可堆叠；物品和方块都是铁砧
界面：1格输入，1个输出，预算按钮，修理按钮
 */
public class PayAnvil extends CustomItemStack {
    public PayAnvil(PayToRepair upper) {
        super("PAY_ANVIL",
                HeadTexture.PIRATE_RELIC.getTexture(),
                "&6修理台",
                "&a&o花费金币来修理工具、武器、装备","", "&e放置在地上&7进行使用");
        /*
        如果需要更改才用到这两句
        this.setPlaceable(true);//设置可否放置
        this.setConsumable(false);//设置是否消耗
         */

        //编写该CustomItemStack的使用动作
        this.setUseAction(event->{
            upper.openPayAnvil(event.getPlayer());
        });


    }
}

package me.bemayor.components.paytorepair.items;

import me.bemayor.api.common.LoreBuilder;
import me.bemayor.api.customitem.CustomItemStack;

import me.bemayor.components.paytorepair.PayToRepair;
import me.bemayor.components.transition.items.HeadTexture;

/*
便携修理台
作用：一种便携版的修理台
性质：直接使用不可放置；该物品不会因使用而消耗
外观：不可堆叠；物品是铁砧
界面：同修理台
 */
public class PortablePayAnvil extends CustomItemStack {

    public PortablePayAnvil(PayToRepair upper) {
        super("PORTABLE_PAY_ANVIL",
                HeadTexture.PIRATE_RELIC.getTexture(),
                "&6便携修理台",
                "&a&o一个便于使用的修理台", "", LoreBuilder.RIGHT_CLICK_TO_OPEN);

        this.setPlaceable(false);

        this.setUseAction(event->{
            upper.openPayAnvil(event.getPlayer());
        });
    }

}

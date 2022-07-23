package me.bemayor.api.common;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

/**
 * This utility class provides a few handy methods and constants to build the lore of any
 * {@link SlimefunItemStack}. It is mostly used directly inside the class {@link SlimefunItems}.
 *
 * @author TheBusyBiscuit
 * @see SlimefunItems
 */
public final class LoreBuilder {

    public static final String HAZMAT_SUIT_REQUIRED = "&8\u21E8 &4需要防护服!";

    public static final String RIGHT_CLICK_TO_USE = "&e右击&7使用";
    public static final String RIGHT_CLICK_TO_OPEN = "&e手持右击&7使用";
    public static final String CROUCH_TO_USE = "&e蹲下&7使用";

    private static final DecimalFormat hungerFormat = new DecimalFormat("#.0", DecimalFormatSymbols.getInstance(Locale.ROOT));

    private LoreBuilder() {
    }

    public static String speed(float speed) {
        return "&8\u21E8 &b\u26A1 &7速度值: &b" + speed + 'x';
    }

    public static String powerBuffer(int power) {
        return power(power, " Buffer");
    }

    public static String powerPerSecond(int power) {
        return power(power, "/s");
    }

    public static String power(int power, String suffix) {
        return "&8\u21E8 &e\u26A1 &7" + power + " J" + suffix;
    }

    public static String powerCharged(int charge, int capacity) {
        return "&8\u21E8 &e\u26A1 &7" + charge + " / " + capacity + " J";
    }

    public static String material(String material) {
        return "&8\u21E8 &7材料: &b" + material;
    }

    public static String hunger(double value) {
        return "&7&oRestores &b&o" + hungerFormat.format(value) + " &7&oHunger";
    }

    public static String range(int blocks) {
        return "&7Range: &c" + blocks + " blocks";
    }

    public static String usesLeft(int usesLeft) {
        return "&e" + usesLeft + ' ' + (usesLeft > 1 ? "Uses" : "Use") + " &7left";
    }

}

package me.bemayor.api.common;

import io.github.bakedlibs.dough.chat.ChatInput;
import io.github.bakedlibs.dough.common.ChatColors;
import io.github.bakedlibs.dough.common.CommonPatterns;
import me.bemayor.api.ApiManagement;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;

/**
 * This utility class contains a few static methods that are all about {@link String} manipulation
 * or sending a {@link String} to a {@link Player}.
 * 
 * @author TheBusyBiscuit
 *
 */
public final class ChatUtils {

    public static List<String> amountListForTab= new ArrayList<>(Arrays.asList("1","2","4","8","16","32","48","64"));
    public static List<String> onOffListForTab= new ArrayList<>(Arrays.asList("true","false"));
    public static List<String> helpListForTab= new ArrayList<>(Arrays.asList("help"));
    public static List<String> nullListForTab= new ArrayList<>();

    private ChatUtils() {}

    public static String colorize(String str){
        if(str==null){
            return null;
        }
        return str.replace("&","§");
    }

    public static String removeColorCodes(String string) {
        return ChatColor.stripColor(ChatColors.color(string));
    }

    public static String crop(ChatColor color, String string) {
        if (ChatColor.stripColor(color + string).length() > 19) {
            return (color + ChatColor.stripColor(string)).substring(0, 18) + "...";
        } else {
            return color + ChatColor.stripColor(string);
        }
    }

    public static String christmas(String text) {
        return ChatColors.alternating(text, ChatColor.GREEN, ChatColor.RED);
    }

    public static void awaitInput(Player p, Consumer<String> callback) {
        ChatInput.waitForPlayer(ApiManagement.getPlugin(), p, callback);
    }

    /**
     * This converts a given {@link String} to a human-friendly version.
     * This can be used to convert enum constants to easier to read words with
     * spaces and upper case word starts.
     * 
     * For example:
     * {@code ENUM_CONSTANT: Enum Constant}
     * 
     * @param string
     *            The {@link String} to convert
     * 
     * @return A human-friendly version of the given {@link String}
     */
    public static String humanize(String string) {
        StringBuilder builder = new StringBuilder();
        String[] segments = CommonPatterns.UNDERSCORE.split(string.toLowerCase(Locale.ROOT));

        builder.append(Character.toUpperCase(segments[0].charAt(0))).append(segments[0].substring(1));

        for (int i = 1; i < segments.length; i++) {
            String segment = segments[i];
            builder.append(' ').append(Character.toUpperCase(segment.charAt(0))).append(segment.substring(1));
        }

        return builder.toString();
    }

    public static double monetization(double money) {
        DecimalFormat number = new DecimalFormat("#.00");
        return Double.parseDouble(number.format(money));
    }
}

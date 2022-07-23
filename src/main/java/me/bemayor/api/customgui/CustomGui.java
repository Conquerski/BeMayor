package me.bemayor.api.customgui;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;

import dev.triumphteam.gui.guis.Gui;
import net.kyori.adventure.text.Component;

public abstract class CustomGui {
    protected final Gui gui;
    protected final int rowNum;
    protected final int itemNum;
    protected String description;
    protected String text;

    public CustomGui(String guiTitle, String guiDescription, String closeText, int... rowNums) {
        int num = 1;
        for (int n : rowNums) {
            if (num < n) {
                num = n;
            }
        }
        itemNum = numCheck(num);
        if (itemNum > 4) {
            rowNum = 6;
        } else {
            rowNum = itemNum + 2;
        }
        gui = Gui.gui().title(Component.text(guiTitle)).rows(rowNum).create();

        description = guiDescription;
        text = closeText;
        gui.setOpenGuiAction(event -> {
            if (description != null && !description.isEmpty()) {
                Player p = (Player) event.getPlayer();
                p.sendMessage(description);
            }
            openEvent(event);
        });
        gui.setDefaultTopClickAction(event -> {
            if (hasPadding(event)) {
                event.setCancelled(true); //如果是填充物，直接取消动作
            } else {
                boolean setB = hasCursorItemStack(event);
                ItemStack getIS = getItemStack(event);
                if (setB || getIS != null) {
                    changeEvent(event, setB, getIS);
                }
            }
        });
        gui.setCloseGuiAction(event -> {
            if (text != null && !text.isEmpty()) {
                Player p = (Player) event.getPlayer();
                p.sendMessage(text);
            }
            closeEvent(event);
        });
    }

    public CustomGui(String guiTitle, String guiDescription, int... rowNums) {
        this(guiTitle, guiDescription, "", rowNums);
    }

    public CustomGui(String guiTitle, int... rowNums) {
        this(guiTitle, "", "", rowNums);
    }

    public Gui getGui() {
        return gui;
    }

    public int getRowNum() {
        return rowNum;
    }

    public int getMaxItemNum() {
        return itemNum;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String newDescription) {
        description = newDescription;
    }

    public String getCloseText() {
        return text;
    }

    public void setCloseText(String newCloseText) {
        text = newCloseText;
    }

    private ItemStack getItemStack(InventoryClickEvent event) {
        return event.getCurrentItem();
    }

    private boolean hasItemStack(InventoryClickEvent event) {
        return getItemStack(event) != null;
    }

    public void open(Player player) {
        gui.open(player);
    }

    //处理的事件有：界面打开、放入拿走物品、界面关闭
    protected abstract void changeEvent(InventoryClickEvent event, boolean isSet, ItemStack getItemStack);

    protected abstract void openEvent(InventoryOpenEvent event);

    protected abstract void closeEvent(InventoryCloseEvent event);


    public boolean hasPadding(final int slot) {
        return gui.getGuiItem(slot) != null;
    }

    public boolean hasPadding(final int row, final int col) {
        return hasPadding(toSlot(row, col));
    }

    public boolean hasPadding(InventoryClickEvent event) {
        return hasPadding(event.getSlot());
    }

    public boolean hasItem(final int slot) {
        if (hasPadding(slot)) {
            return false;
        }
        return gui.getInventory().getItem(slot) != null;
    }

    public boolean hasItem(final int row, final int col) {
        return hasItem(toSlot(row, col));
    }

    public boolean hasItem(InventoryClickEvent event) {
        return hasItem(event.getSlot());
    }

    public ItemStack getItem(int slot) {
        if (hasPadding(slot)) {
            return null;
        }
        return gui.getInventory().getItem(slot);
    }

    public ItemStack getItem(int row, int col) {
        return getItem(toSlot(row, col));
    }

    public ItemStack getItem(InventoryClickEvent event) {
        return getItem(event.getSlot());
    }

    public boolean hasItems(int startRow, int startCol, int endRow, int endCol) {
        boolean b = false;
        for (int i = startCol; i <= endCol; i++) {
            for (int j = startRow; j <= endRow; j++) {
                if (hasItem(toSlot(j, i))) {
                    b = true;
                    break;
                }
            }
            if (b) {
                break;
            }
        }
        return b;
    }

    public List<ItemStack> getItems(int startRow, int startCol, int endRow, int endCol) {
        List<ItemStack> list = new ArrayList<ItemStack>();
        for (int i = startCol; i <= endCol; i++) {
            for (int j = startRow; j <= endRow; j++) {
                ItemStack is = getItem(toSlot(j, i));
                if (is != null) {
                    list.add(is);
                }
            }
        }
        return list;
    }

    public void removeItems(int startRow, int startCol, int endRow, int endCol) {
        for (int i = startCol; i <= endCol; i++) {
            for (int j = startRow; j <= endRow; j++) {
                gui.removeItem(j, i);
            }
        }
    }


    public static boolean hasCursorItemStack(InventoryClickEvent event) {
        return event.getCursor() != null && event.getCursor().getType() != Material.AIR;
    }

    public static int toCol(final int slot) {
        return slot % 9 + 1;
    }

    public static int toRow(final int slot) {
        return slot / 9 + 1;
    }

    public static int toSlot(final int row, final int col) {
        return (col + (row - 1) * 9) - 1;
    }


    protected int numCheck(int num) {
        if (num > 6) {
            num = 6;
        }
        if (num <= 0) {
            num = 1;
        }
        return num;
    }
}

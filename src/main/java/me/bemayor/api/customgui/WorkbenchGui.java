package me.bemayor.api.customgui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import me.bemayor.api.common.ItemStackUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.GuiItem;

public class WorkbenchGui extends CustomGui {
    private static int inputColumn = 3;
    private static int outputColumn = 7;
    private final int inNum;
    private final int outNum;
    private List<GuiButton> buttons;
    private Consumer<InventoryClickEvent> inputChangeAction;
    private Consumer<InventoryClickEvent> outputGetAction;
    private Consumer<InventoryCloseEvent> guiCloseAction;

    public WorkbenchGui(String guiTitle, String guiDescription, String closeText, int inputNum, int outputNum, int buttonsNum) {
        super(guiTitle, guiDescription, inputNum, outputNum, buttonsNum);
        inNum = numCheck(inputNum);
        outNum = numCheck(outputNum);

        //输入栏与输出栏
        GuiItem groundPane = ItemBuilder.from(Material.BLUE_STAINED_GLASS_PANE).asGuiItem();
        GuiItem outputPane = ItemBuilder.from(Material.GREEN_STAINED_GLASS_PANE).asGuiItem();
        GuiItem intputPane = ItemBuilder.from(Material.RED_STAINED_GLASS_PANE).asGuiItem();

        //GUI底色
        gui.getFiller().fill(groundPane);
        //输入栏样式
        drawBox(inNum, inputColumn, intputPane);
        //输出栏样式
        drawBox(outNum, outputColumn, outputPane);

        //按钮
        if (buttons == null || buttons.size() == 0) {
            buttons = new ArrayList<GuiButton>(Arrays.asList(getDefaultButton()));
        }
        drawButtons();
    }

    public WorkbenchGui(String guiTitle, String guiDescription, int inputNum, int outputNum, int buttonsNum) {
        this(guiTitle, guiDescription, "", inputNum, outputNum, buttonsNum);
    }

    public WorkbenchGui(String guiTitle, int inputNum, int outputNum, int buttonsNum) {
        this(guiTitle, "", "", inputNum, outputNum, buttonsNum);
    }

    public WorkbenchGui(String guiTitle, int inputNum, int outputNum) {
        this(guiTitle, "", "", inputNum, outputNum, 1);
    }

    public List<GuiButton> getButtons() {
        return buttons;
    }

    public void setButtons(List<GuiButton> newButtons) {
        buttons = newButtons;
        drawButtons();
    }

    public GuiButton getButton() {
        return buttons.get(0);
    }

    public void setButton(GuiButton newButtons) {
        buttons.remove(buttons.size() - 1);
        buttons.add(newButtons);
        drawButtons();
    }

    public Consumer<InventoryClickEvent> getInputChangeAction() {
        return inputChangeAction;
    }

    public Consumer<InventoryClickEvent> getOutputGetAction() {
        return outputGetAction;
    }

    public Consumer<InventoryCloseEvent> getGuiCloseAction() {
        return guiCloseAction;
    }

    public void setInputChangeAction(Consumer<InventoryClickEvent> consumer) {
        inputChangeAction = consumer;
    }

    public void setOutputGetAction(Consumer<InventoryClickEvent> consumer) {
        outputGetAction = consumer;
    }

    public void setGuiCloseAction(Consumer<InventoryCloseEvent> consumer) {
        guiCloseAction = consumer;
    }

    protected void changeEvent(InventoryClickEvent event, boolean isSet, ItemStack getIS) {
        //注意容器的单击事件是发生在指针已经单击了指针上没有物品了，但是容器还没有响应容器里也没有变化的时候
        if (isInputClickEvent(event)) {
            //输入框可放置可拿取
            if (inputChangeAction != null) {
                inputChangeAction.accept(event);//触发输入框放置拿取事件
            }
        } else if (isOutputClickEvent(event)) {
            //输出框禁放置可拿取
            if (!isSet && getIS != null) {
                if (outputGetAction != null) {
                    outputGetAction.accept(event);//触发输出框拿取事件
                }
            } else {
                event.setCancelled(true);
            }
        }
    }

    protected void openEvent(InventoryOpenEvent event) {
    }

    protected void closeEvent(InventoryCloseEvent event) {
        if (guiCloseAction != null) {
            guiCloseAction.accept(event);//触发输出框拿取事件
        }
        Player p = (Player) event.getPlayer();
        List<ItemStack> list = new ArrayList<ItemStack>();
        if (this.hasInputItem()) {
            list.addAll(this.getInputItems());
        } //获取输入框所有物品
        if (this.hasOutputItem()) {
            list.addAll(this.getOutputItems());
        } //获取输出框所有物品
        if (!list.isEmpty()) {//将物品放入玩家背包，放不下就丢到玩家脚下
            for (ItemStack putItem : list) {
                ItemStackUtils.putItemToPlayer(p, putItem);
            }
        }
    }

    public void setInputToOutput(List<ItemStack> outputItems, Player player) {
        setOutputItems(outputItems, player);
        consumeInput();
    }

    public void setInputToOutput(ItemStack outputItem, Player player) {
        setOutputItem(outputItem, player);
        consumeInput();
    }

    public void consumeInput() {
        for (ItemStack is : getInputItems()) {
            if (is != null) {
                int n = is.getAmount();
                if (n > 1) {
                    is.setAmount(n - 1);
                } else {
                    is.setAmount(0);
                }
            }
        }
    }

    public boolean hasInputItem() {
        return hasItems(getStartRow(inNum), inputColumn, getEndRow(inNum), inputColumn);
    }

    public ItemStack getInputItem() {
        return getItem(getStartRow(inNum), inputColumn);
    }

    public List<ItemStack> getInputItems() {
        return getItems(getStartRow(inNum), inputColumn, getEndRow(inNum), inputColumn);
    }

    public void clearInputItem() {
        removeItems(getStartRow(inNum), inputColumn, getEndRow(inNum), inputColumn);
    }

    public boolean hasOutputItem() {
        return hasItems(getStartRow(outNum), outputColumn, getEndRow(outNum), outputColumn);
    }

    public ItemStack getOutputItem() {
        return getItem(getStartRow(outNum), outputColumn);
    }

    public List<ItemStack> getOutputItems() {
        return getItems(getStartRow(outNum), outputColumn, getEndRow(outNum), outputColumn);
    }

    public void setOutputItem(ItemStack outputItem, Player player, int startRow) {
        boolean b = false;
        int index = startRow, endRow = getEndRow(outNum);
        for (int i = index; i <= endRow; i++) {
            if (hasItem(i, outputColumn)) {
                continue;
            } else {
                index = i;
                b = true;
            }
        }
        if (b) {
            gui.getInventory().setItem(toSlot(index, outputColumn), outputItem);
        } else {
            ItemStackUtils.putItemToPlayer(player, getItem(startRow, outputColumn));
            gui.getInventory().setItem(toSlot(startRow, outputColumn), outputItem);
        }
    }

    public void setOutputItem(ItemStack outputItem, Player player) {
        setOutputItem(outputItem, player, getStartRow(outNum));
    }

    public void setOutputItems(List<ItemStack> outputItems, Player player) {
        if (outputItems != null && !outputItems.isEmpty()) {
            int index = getStartRow(outNum);
            for (ItemStack is : outputItems) {
                setOutputItem(is, player, index);
                if (index < getEndRow(outNum)) {
                    index++;
                } else {
                    index = getStartRow(outNum);
                }
            }
        }
    }

    public void refreshButton(int buttonIndex) {
        buttons.get(buttonIndex).refreshDisplay();
        drawButton(buttonIndex);
    }

    public void refreshButtons() {
        for (GuiButton b : buttons) {
            b.refreshDisplay();
        }
        drawButtons();
    }

    protected static GuiButton getDefaultButton() {
        return new GuiButton("§6✦ 确认 ✦", Material.ANVIL);
    }


    private boolean isInputClickEvent(InventoryClickEvent event) {
        return isClickEvent(event, inputColumn, inNum);
    }

    private boolean isOutputClickEvent(InventoryClickEvent event) {
        return isClickEvent(event, outputColumn, outNum);
    }

    private boolean isClickEvent(InventoryClickEvent event, int column, int num) {
        boolean b = false;
        int slot = event.getSlot();
        int row = toRow(slot);
        int col = toCol(slot);
        if (col == column) {
            if (row >= getStartRow(num) && row <= getEndRow(num))
                b = true;
        }
        return b;
    }

    private int getStartRow(int num) {
        return num < 6 ? 2 : 1;
    }

    private int getEndRow(int num) {
        int i = num < 6 ? 2 : 1;
        return num < 5 ? (i + num - 1) : numCheck(num + 1);
    }

    private void drawBox(int itemNum, int columnIndex, GuiItem boxPane) {
        int startIndex = 1;
        if (itemNum < 6) {
            gui.setItem(1, columnIndex, boxPane); //上边框
            startIndex = 2;
        }
        int leftColumn = columnIndex - 1, rightColumn = columnIndex + 1;
        //两侧边框
        for (int i = 0; i < itemNum; i++) {
            int j = startIndex + i;
            gui.setItem(j, leftColumn, boxPane);
            gui.removeItem(j, columnIndex);
            gui.setItem(j, rightColumn, boxPane);
        }
        if (itemNum < 5) {
            gui.setItem(startIndex + itemNum, columnIndex, boxPane);
        }
    }

    private void drawButton(int buttonIndex) {
        int row = buttonIndex + 1;
        if (buttons.size() < 6) {
            row++;
        }
        gui.updateItem(row, 5, buttons.get(buttonIndex).getGuiItem());
    }

    private void drawButtons() {
        int index = 1;
        if (buttons.size() < 6) {
            index = 2;
        }
        for (GuiButton button : buttons) {
            gui.updateItem(index, 5, button.getGuiItem());
            index++;
        }
    }
}

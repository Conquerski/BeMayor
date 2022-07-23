package me.bemayor.api.customitem;

/*
自定义物品cItem的管理
-所有cItem的列表
-所有cItem的ID查询表
-cItem的注册
-cItem的物品NBT服务
-cItem的方块NBT服务
-cItem的贴图服务
 */

import io.github.bakedlibs.dough.config.Config;
import me.bemayor.api.ApiManagement;
import me.bemayor.api.ApiMember;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public final class CustomItemManagement extends ApiMember {

    private final Map<String, CustomItemStack> customItems = new HashMap<>();
    private final List<String> customItemIds = new ArrayList<String>();


    private final CustomItemListener listener=new CustomItemListener(this);


    private static CustomItemDataService itemDataService= new CustomItemDataService(ApiManagement.getPlugin(), ApiManagement.ITEM_KEY);
    private static BlockDataService blockDataService= new BlockDataService(ApiManagement.getPlugin(), ApiManagement.BLOCK_KEY);
    private static CustomTextureService textureService= new CustomTextureService(new Config(ApiManagement.getPlugin(),ApiManagement.ITEM_MODELS_YML));


    public CustomItemManagement(ApiManagement apiManagement) { super(apiManagement); }

    /**
     * ======================================================================================================
     * ======================================================================================================
     * 以下为CustomItemManagement的管理区
     * 主要包括：
     *      -物质样本库的管理（样本的注册、注销）
     *      -物质的使用事件的监听器的管理(暂时没有，在本对象创建时绑定一个监听器的实例)
     *      -原版物品和方块的自定义化、自定义物品和方块的识别等服务的管理(暂时没有，在本对象创建时绑定一份服务)
     */

    //样本库的管理
    public boolean add(CustomItemStack newCustomItem){
        String id=newCustomItem.getId();
        if(hasId(id)){return false;}
        customItems.put(id,newCustomItem);
        customItemIds.add(id);
        return true;
    }
    public boolean remove(String id){
        if(!hasId(id)){return false;}
        customItems.remove(id);
        customItemIds.remove(id);
        return true;
    }
    public boolean remove(CustomItemStack customItem){
        return remove(customItem.getId());
    }
    public void clear(){
        customItems.clear();
        customItemIds.clear();
    }

    //监听器的管理-无
    //自定义化和识别服务的管理-无

    /**
     * 以上为CustomItemManagement的管理区
     * ======================================================================================================
     * ======================================================================================================
     * 以下为CustomItemManagement的功能区
     * 作用是原版物质的自定化、自定义物质的识别、样本库的查询、提供标准自定义物质
     * 主要包括：
     *      -物品的自定义化(NBT、外观)
     *      -方块的自定义化(NBT)
     *      -对原版物品的识别
     *      -对原版方块的识别
     *      -提供样本库的所有样本的ID(提供ID列表的克隆)
     *      -用ID查看某自定义物质是否在样本库中
     *      -用ID对样本库进行查询（该功能提供了样本用于获取样本信息，请不要随意改动该样本）
     *      -提供基于某样本的自定义物质（提供该样本的克隆）
     */

    //物品的自定义化
    public static void setItemData(ItemMeta meta, String id) { itemDataService.setItemData(meta,id); }
    public static void setTexture(ItemMeta meta, String id) { textureService.setTexture(meta,id); }


    //方块的自定义化
    public static boolean isTileEntity(Material type) {return blockDataService.isTileEntity(type); }
    public static void setBlockData(Block block, String id) { blockDataService.setBlockData(block,id); }

    //对原版物品的识别
    public static String getIdByItem(ItemStack item) {
        if(item!=null){
            if (item instanceof CustomItemStack) {
                return ((CustomItemStack) item).getId();
            }
            Optional<String> itemID = itemDataService.getItemData(item);
            if (itemID.isPresent()) {
                return itemID.get();
            }
        }
        return null;
    }
    public CustomItemStack getTemplateByItem(ItemStack item) {
        String itemID =getIdByItem(item);
        if(itemID!=null && !itemID.isEmpty()){
            return getTemplateById(itemID);
        }
        return null;
    }
    public CustomItemStack getCloneByItem(ItemStack item) {
        String itemID =getIdByItem(item);
        if(itemID!=null && !itemID.isEmpty()){
            return getCloneById(itemID);
        }
        return null;
    }
    public CustomItemStack getCloneByItem(ItemStack item,int setAmount) {
        String itemID =getIdByItem(item);
        if(itemID!=null && !itemID.isEmpty()){
            return getCloneById(itemID,setAmount);
        }
        return null;
    }

    //对原版方块的识别
    public static String getIdByBlock(Block b) {
        // Only access the BlockState when on the main thread
        if (Bukkit.isPrimaryThread() && blockDataService.isTileEntity(b.getType())) {
            Optional<String> blockData = blockDataService.getBlockData(b);

            if (blockData.isPresent()) {
                return blockData.get();
            }
        }
        return null;
    }
    public CustomItemStack getTemplateByBlock(Block b) {
        String id = getIdByBlock(b);
        return id == null ? null : getTemplateById(id);
    }
    public CustomItemStack getCloneByBlock(Block b) {
        String id = getIdByBlock(b);
        return id == null ? null : getCloneById(id);
    }
    public CustomItemStack getCloneByBlock(Block b,int setAmount) {
        String id = getIdByBlock(b);
        return id == null ? null : getCloneById(id,setAmount);
    }


    //提供样本库的所有样本的ID列表的深克隆
    public List<String> getAllId() {
        List<String> newList=new ArrayList<String>();
        for(String id:customItemIds){
            newList.add(id);
        }
        return newList;
    }

    //用ID查看某自定义物质是否在样本库中
    public boolean hasId(String id) {
        if(customItemIds.isEmpty() || !customItemIds.contains(id)){return false;}
        return true;
    }


    //用ID对样本库进行查询并返回该样本
    public CustomItemStack getTemplateById(String id) {
        if(hasId(id)){
            return customItems.get(id);
        }
        return null;
    }
    public String getNameById(String id) {
        if(hasId(id)){
            return customItems.get(id).getName();
        }
        return null;
    }

    //提供标准自定义物质，即该样本的深克隆
    public CustomItemStack getCloneById(String id) {
        if(hasId(id)){
            return customItems.get(id).clone();
        }
        return null;
    }
    public CustomItemStack getCloneById(String id,int setAmount) {
        if(hasId(id)){
            CustomItemStack cItem=customItems.get(id).clone();
            cItem.setAmount(setAmount);
            return cItem;
        }
        return null;
    }
}

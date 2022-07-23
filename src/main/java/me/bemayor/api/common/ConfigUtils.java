package me.bemayor.api.common;

import org.apache.commons.lang.Validate;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.List;

public class ConfigUtils {
    public static void setFiles(List<String> fileNameList, String description){
        //检查配置文件的存在，并创建它们
        System.out.println("正在检查"+description+"配置文件...");
        if(fileNameList==null || fileNameList.isEmpty()){
            System.out.println("没有配置文件需要检查！");
            return;
        }
        for(String f:fileNameList){
            setFile(f,description);
        }
        System.out.println(description+"配置文件检查完毕");
    }
    public static void setFile(String fileName,String description){
        //检查配置文件的存在，并创建它们
        JavaPlugin plugin=JavaPlugin.getProvidingPlugin(ConfigUtils.class);
        Validate.notNull(plugin, "找不到插件，插件没有加载!");
        File f=new File(plugin.getDataFolder(),fileName);
        if(f==null || !f.exists()){
            plugin.saveResource(fileName,false);
        }
        System.out.println(description+"配置文件"+fileName+"已检查");
    }
}

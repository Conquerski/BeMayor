package me.bemayor.api.customgui;

import org.bukkit.Material;


public class WorkbenchRecipeItem {
    public Material recipeMaterial;
    public boolean isOriginal;
    public WorkbenchRecipeItem(Material recipeMaterial,boolean isOriginal){
        this.recipeMaterial=recipeMaterial;
        this.isOriginal=isOriginal;
    }
}

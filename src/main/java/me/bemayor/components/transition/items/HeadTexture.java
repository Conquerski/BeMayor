package me.bemayor.components.transition.items;

import io.github.bakedlibs.dough.common.CommonPatterns;
import io.github.bakedlibs.dough.skins.PlayerHead;
import io.github.bakedlibs.dough.skins.PlayerSkin;
import org.apache.commons.lang.Validate;
import org.bukkit.inventory.ItemStack;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.UUID;

/**
 * This enum holds all currently used Head textures in Slimefun.
 * Credit for most of these goes to our main head designer "AquaLazuryt".
 *
 * @author TheBusyBiscuit
 */
public enum HeadTexture {

    PIRATE_RELIC("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYWFmY2ZjYTM5MzU3NWE0NGQ4YTY2YTRjOTAwZjZjZDRiMjNmOGNlNjJjYjFlZGU5ZWU4ZjgwOGM4NGQ0Y2ZkOCJ9fX0="),
    NINTENDO_SWITCH("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzBhNDBhYTUzYWZlM2FjZTQyODJiNzBmOTI1ZDgwYzg5MDM1NmI1ZDJhNjM5MGI2NGMwZmU0YzUxMzk1MmFkYiJ9fX0="),
    EXPERIENCE_CUBE("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzk5YWQ3YTA0MzE2OTI5OTRiNmM0MTJjN2VhZmI5ZTBmYzQ5OTc1MjQwYjczYTI3ZDI0ZWQ3OTcwMzVmYjg5NCJ9fX0="),
    FANCY_CUBE("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYWNiOGU3MjBhMDI3MjRiN2MzNDNmZGQ0NDc5MGZhYjRjMGQxZjE2YWFmODExMzgxOTJjNzBmODEyY2U0ZjYyMiJ9fX0=");
    private final String texture;

    HeadTexture(String texture) {
        Validate.notNull(texture, "Texture cannot be null");

        this.texture = texture;
    }

    /**
     * This returns the texture hash for this particular head.
     *
     * @return The associated texture hash
     */
    public String getTexture() {
        return texture;
    }

}

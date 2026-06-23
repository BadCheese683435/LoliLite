package com.example.ten.inventory;

import com.example.ten.Ten;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtIo;
import net.minecraft.util.Identifier;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class LoliInventoryManager {
    private static final String PERSISTENT_KEY = "LoliInventoryPersistent";
    private static final int PAGE_SLOTS = 54;
    private static final int MAX_PAGES = 27;
    private static final int TOTAL_SLOTS = PAGE_SLOTS * MAX_PAGES;
    
    public static void loadInventoryToSaved(PlayerEntity player, SimpleInventory inventory) {
        if (player.getWorld().isClient) {
            return;
        }
        
        try {
            File saveFile = getSaveFile(player.getUuid());
            if (saveFile.exists()) {
                NbtCompound nbt = NbtIo.readCompressed(saveFile);
                if (nbt != null && nbt.contains(PERSISTENT_KEY)) {
                    NbtList items = nbt.getList(PERSISTENT_KEY, 10);
                    for (int i = 0; i < items.size() && i < inventory.size(); i++) {
                        NbtCompound itemTag = items.getCompound(i);
                        if (!itemTag.isEmpty()) {
                            inventory.setStack(i, ItemStack.fromNbt(itemTag));
                        } else {
                            inventory.setStack(i, ItemStack.EMPTY);
                        }
                    }
                }
            }
        } catch (IOException e) {
            Ten.LOGGER.error("Failed to load loli inventory", e);
        }
    }
    
    public static void saveInventoryToNbt(PlayerEntity player, SimpleInventory inventory) {
        if (player.getWorld().isClient) {
            return;
        }
        
        try {
            File saveFile = getSaveFile(player.getUuid());
            NbtCompound nbt = new NbtCompound();
            
            NbtList items = new NbtList();
            for (int i = 0; i < inventory.size(); i++) {
                ItemStack stack = inventory.getStack(i);
                NbtCompound itemTag = new NbtCompound();
                if (!stack.isEmpty()) {
                    stack.writeNbt(itemTag);
                }
                items.add(itemTag);
            }
            nbt.put(PERSISTENT_KEY, items);
            
            NbtIo.writeCompressed(nbt, saveFile);
        } catch (IOException e) {
            Ten.LOGGER.error("Failed to save loli inventory", e);
        }
    }
    
    private static File getSaveFile(UUID uuid) {
        File worldDir = new File("saves");
        if (!worldDir.exists()) {
            worldDir = new File(System.getProperty("user.dir"), "run/saves");
        }
        File saveDir = new File(worldDir, "loli_inventory");
        if (!saveDir.exists()) {
            saveDir.mkdirs();
        }
        return new File(saveDir, uuid.toString() + ".dat");
    }
    
    public static void saveInventoryToNbt(PlayerEntity player) {
    }
    
    public static boolean hasUltimateWeapon(PlayerEntity player) {
        ItemStack mainHand = player.getMainHandStack();
        ItemStack offHand = player.getOffHandStack();
        
        return isUltimateWeapon(mainHand) || isUltimateWeapon(offHand)
                || player.getInventory().contains(new net.minecraft.item.ItemStack(
                        net.minecraft.registry.Registries.ITEM.get(new Identifier("ten", "ultimate_weapon"))));
    }
    
    public static boolean isUltimateWeapon(ItemStack stack) {
        if (stack == null || stack.isEmpty()) {
            return false;
        }
        return stack.getItem().getTranslationKey().contains("ultimate_weapon");
    }
    
    public static int getMaxPages() {
        return MAX_PAGES;
    }
    
    public static int getPageSlots() {
        return PAGE_SLOTS;
    }
}

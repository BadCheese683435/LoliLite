package com.example.ten.event;

import com.example.ten.inventory.LoliInventoryManager;
import com.example.ten.item.ModItems;
import com.example.ten.item.UltimateWeaponItem;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public class KeyInputListener {
    private static final KeyBinding DIG_MODE_KEY = new KeyBinding(
            "key.ten.dig_mode",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_C,
            "category.ten"
    );
    
    private static final KeyBinding OPEN_INVENTORY_KEY = new KeyBinding(
            "key.ten.open_loli_inventory",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_B,
            "category.ten"
    );

    public static void register() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            // C键 - 挖掘模式切换
            if (DIG_MODE_KEY.wasPressed()) {
                if (client.player != null) {
                    var mainHand = client.player.getStackInHand(net.minecraft.util.Hand.MAIN_HAND);
                    var offHand = client.player.getStackInHand(net.minecraft.util.Hand.OFF_HAND);
                    
                    if (mainHand.isOf(ModItems.ULTIMATE_WEAPON) || offHand.isOf(ModItems.ULTIMATE_WEAPON)) {
                        sendModeSwitchPacket(client);
                        showModeMessage(client);
                    }
                }
            }
            
            // B键 - 打开背包
            if (OPEN_INVENTORY_KEY.wasPressed()) {
                if (client.player != null && client.getNetworkHandler() != null) {
                    if (LoliInventoryManager.hasUltimateWeapon(client.player)) {
                        client.player.networkHandler.sendChatCommand("ultimateweapon openinventory");
                    }
                }
            }
        });
    }
    
    private static void sendModeSwitchPacket(MinecraftClient client) {
        if (client.player != null && client.getNetworkHandler() != null) {
            client.player.networkHandler.sendChatCommand("ultimateweapon switchmode");
        }
    }
    
    private static void showModeMessage(MinecraftClient client) {
        if (client.player != null) {
            int mode = UltimateWeaponItem.getDigMode(client.player);
            String modeName;
            switch (mode) {
                case 0 -> modeName = "1x1";
                case 1 -> modeName = "3x3";
                case 2 -> modeName = "11x11";
                default -> modeName = "1x1";
            }
            
            client.player.sendMessage(Text.literal("§6[氪金萝莉] §a当前范围挖掘模式: §e" + modeName), false);
        }
    }
    
    public static KeyBinding getDigModeKey() {
        return DIG_MODE_KEY;
    }
    
    public static KeyBinding getOpenInventoryKey() {
        return OPEN_INVENTORY_KEY;
    }
}
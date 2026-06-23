package com.example.ten;

import com.example.ten.client.LoliInventoryScreen;
import com.example.ten.event.KeyInputListener;
import com.example.ten.inventory.LoliInventoryScreenHandler;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TenClient implements ClientModInitializer {
    private static final Logger LOGGER = LoggerFactory.getLogger("ten-client");
    
    @Override
    public void onInitializeClient() {
        LOGGER.info("TenClient 已初始化！");
        
        KeyBindingHelper.registerKeyBinding(KeyInputListener.getDigModeKey());
        KeyBindingHelper.registerKeyBinding(KeyInputListener.getOpenInventoryKey());
        KeyInputListener.register();
        
        ScreenRegistry.register(Ten.LOLI_INVENTORY_SCREEN_HANDLER, LoliInventoryScreen::new);
        
        LOGGER.info("按键已注册！");
    }
}

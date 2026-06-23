package com.example.ten;

import com.example.ten.command.WeaponCommand;
import com.example.ten.event.BlockBreakListener;
import com.example.ten.event.WeaponEventListener;
import com.example.ten.item.ModItems;
import com.example.ten.inventory.LoliInventoryScreenHandler;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Ten implements ModInitializer {
	public static final String MOD_ID = "ten";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	
	public static final ScreenHandlerType<LoliInventoryScreenHandler> LOLI_INVENTORY_SCREEN_HANDLER = 
		ScreenHandlerRegistry.registerExtended(new Identifier(MOD_ID, "loli_inventory"), 
			(syncId, inventory, buf) -> new LoliInventoryScreenHandler(syncId, inventory, buf.readInt()));

	@Override
	public void onInitialize() {
		ModItems.register();
		WeaponEventListener.register();
		BlockBreakListener.register();
		
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            WeaponCommand.register(dispatcher, registryAccess);
        });
		LOGGER.info("Ultimate Weapon mod loaded!");
	}
}

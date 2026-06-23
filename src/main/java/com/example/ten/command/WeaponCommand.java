package com.example.ten.command;

import com.example.ten.inventory.LoliInventoryManager;
import com.example.ten.inventory.LoliInventoryScreenHandler;
import com.example.ten.item.UltimateWeaponItem;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class WeaponCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess) {
        dispatcher.register(CommandManager.literal("ultimateweapon")
            .then(CommandManager.literal("switchmode")
                .executes(WeaponCommand::switchMode))
            .then(CommandManager.literal("openinventory")
                .executes(WeaponCommand::openInventory)
                .then(CommandManager.argument("page", IntegerArgumentType.integer(0))
                    .executes(WeaponCommand::openInventoryWithPage))));
    }

    private static int switchMode(CommandContext<ServerCommandSource> context) {
        var player = context.getSource().getPlayer();
        if (player == null) {
            return 0;
        }

        UltimateWeaponItem.cycleDigMode(player);
        return 1;
    }
    
    private static int openInventory(CommandContext<ServerCommandSource> context) {
        return openInventoryWithPage(context, 0);
    }
    
    private static int openInventoryWithPage(CommandContext<ServerCommandSource> context) {
        int page = 0;
        try {
            page = IntegerArgumentType.getInteger(context, "page");
        } catch (Exception e) {
            page = 0;
        }
        return openInventoryWithPage(context, page);
    }
    
    private static int openInventoryWithPage(CommandContext<ServerCommandSource> context, int page) {
        ServerPlayerEntity player = context.getSource().getPlayer();
        if (player == null) {
            return 0;
        }
        
        if (!LoliInventoryManager.hasUltimateWeapon(player)) {
            return 0;
        }
        
        int maxPages = LoliInventoryManager.getMaxPages();
        page = Math.max(0, Math.min(page, maxPages - 1));
        
        LoliInventoryManager.saveInventoryToNbt(player);
        
        player.openHandledScreen(new LoliInventoryScreenHandlerFactory(page));
        return 1;
    }
    
    private static class LoliInventoryScreenHandlerFactory implements net.minecraft.screen.NamedScreenHandlerFactory, net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory {
        private final int page;
        
        public LoliInventoryScreenHandlerFactory(int page) {
            this.page = page;
        }
        
        @Override
        public Text getDisplayName() {
            return Text.literal("氪金萝莉背包");
        }
        
        @Override
        public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
            buf.writeInt(page);
        }
        
        @Override
        public net.minecraft.screen.ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, net.minecraft.entity.player.PlayerEntity player) {
            return new LoliInventoryScreenHandler(syncId, playerInventory, page);
        }
    }
}

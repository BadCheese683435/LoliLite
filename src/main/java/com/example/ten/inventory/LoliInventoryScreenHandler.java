package com.example.ten.inventory;

import com.example.ten.Ten;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;

public class LoliInventoryScreenHandler extends ScreenHandler {
    private final SimpleInventory inventory;
    private final PlayerEntity player;
    private final int page;
    private final int maxPages;
    private static final int PAGE_SLOTS = 54;
    private static final int TOTAL_SLOTS = PAGE_SLOTS * 27;
    
    public LoliInventoryScreenHandler(int syncId, PlayerInventory playerInventory, PacketByteBuf buf) {
        this(syncId, playerInventory, buf.readInt());
    }
    
    public LoliInventoryScreenHandler(int syncId, PlayerInventory playerInventory, int page) {
        super(Ten.LOLI_INVENTORY_SCREEN_HANDLER, syncId);
        this.player = playerInventory.player;
        this.maxPages = 27;
        this.page = Math.max(0, Math.min(page, maxPages - 1));
        
        this.inventory = new SimpleInventory(TOTAL_SLOTS);
        
        if (!playerInventory.player.getWorld().isClient) {
            LoliInventoryManager.loadInventoryToSaved(player, inventory);
        }
        
        int slotStart = PAGE_SLOTS * this.page;
        
        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 9; col++) {
                int slotIndex = slotStart + row * 9 + col;
                this.addSlot(new LoliSlot(inventory, slotIndex, 8 + col * 18, 18 + row * 18));
            }
        }
        
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                this.addSlot(new Slot(playerInventory, 9 + row * 9 + col, 8 + col * 18, 140 + row * 18));
            }
        }
        
        for (int col = 0; col < 9; col++) {
            this.addSlot(new Slot(playerInventory, col, 8 + col * 18, 198));
        }
    }
    
    private void saveToSaved() {
        LoliInventoryManager.saveInventoryToNbt(player, inventory);
    }
    
    @Override
    public boolean canUse(PlayerEntity player) {
        return LoliInventoryManager.hasUltimateWeapon(player);
    }
    
    @Override
    public ItemStack quickMove(PlayerEntity player, int slotIndex) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(slotIndex);
        
        if (slot != null && slot.hasStack()) {
            ItemStack itemStack2 = slot.getStack();
            itemStack = itemStack2.copy();
            
            if (slotIndex < PAGE_SLOTS) {
                if (!this.insertItem(itemStack2, PAGE_SLOTS, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else {
                if (!this.insertItem(itemStack2, 0, PAGE_SLOTS, false)) {
                    return ItemStack.EMPTY;
                }
            }
            
            if (itemStack2.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }
        }
        
        return itemStack;
    }
    
    @Override
    public void onClosed(PlayerEntity player) {
        super.onClosed(player);
        if (!player.getWorld().isClient) {
            saveToSaved();
        }
    }
    
    public int getPage() {
        return page;
    }
    
    public int getMaxPages() {
        return maxPages;
    }
    
    private static class LoliSlot extends Slot {
        public LoliSlot(SimpleInventory inventory, int index, int x, int y) {
            super(inventory, index, x, y);
        }
        
        @Override
        public boolean canInsert(ItemStack stack) {
            if (stack == null || stack.isEmpty()) {
                return false;
            }
            return !LoliInventoryManager.isUltimateWeapon(stack);
        }
    }
}

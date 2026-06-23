package com.example.ten.client;

import com.example.ten.inventory.LoliInventoryScreenHandler;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;

public class LoliInventoryScreen extends HandledScreen<LoliInventoryScreenHandler> {
    private static final int BACKGROUND_WIDTH = 176;
    private static final int BACKGROUND_HEIGHT = 222;
    private int pendingPage = -1;
    private int ticksUntilChange = 0;
    
    public LoliInventoryScreen(LoliInventoryScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        this.backgroundWidth = BACKGROUND_WIDTH;
        this.backgroundHeight = BACKGROUND_HEIGHT;
        this.playerInventoryTitleY = this.backgroundHeight - 94;
    }
    
    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;
        
        context.drawTexture(new net.minecraft.util.Identifier("minecraft", "textures/gui/container/generic_54.png"), x, y, 0, 0, backgroundWidth, 126);
        context.drawTexture(new net.minecraft.util.Identifier("minecraft", "textures/gui/container/generic_54.png"), x, y + 126, 0, 126, backgroundWidth, 96);
        
        String pageText = (handler.getPage() + 1) + " / " + handler.getMaxPages();
        int pageTextWidth = textRenderer.getWidth(pageText);
        context.drawText(textRenderer, pageText, x + (backgroundWidth - pageTextWidth) / 2, y + 6, 0x404040, false);
    }
    
    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;
        
        int prevButtonX = x + 130;
        int nextButtonX = x + 150;
        int buttonY = y + 4;
        int buttonWidth = 12;
        int buttonHeight = 12;
        
        if (mouseX >= prevButtonX && mouseX <= prevButtonX + buttonWidth && 
            mouseY >= buttonY && mouseY <= buttonY + buttonHeight) {
            if (handler.getPage() > 0) {
                pendingPage = handler.getPage() - 1;
                ticksUntilChange = 2;
                return true;
            }
        }
        
        if (mouseX >= nextButtonX && mouseX <= nextButtonX + buttonWidth && 
            mouseY >= buttonY && mouseY <= buttonY + buttonHeight) {
            if (handler.getPage() < handler.getMaxPages() - 1) {
                pendingPage = handler.getPage() + 1;
                ticksUntilChange = 2;
                return true;
            }
        }
        
        return super.mouseClicked(mouseX, mouseY, button);
    }
    
    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        if (ticksUntilChange > 0) {
            ticksUntilChange--;
            if (ticksUntilChange == 0 && pendingPage >= 0) {
                client.player.closeHandledScreen();
                client.player.networkHandler.sendChatCommand("ultimateweapon openinventory " + pendingPage);
                pendingPage = -1;
            }
        }
        
        this.renderBackground(context);
        super.render(context, mouseX, mouseY, delta);
        this.drawMouseoverTooltip(context, mouseX, mouseY);
        
        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;
        
        context.drawText(textRenderer, "<", x + 133, y + 5, handler.getPage() > 0 ? 0xFFFFFF : 0x888888, false);
        context.drawText(textRenderer, ">", x + 153, y + 5, handler.getPage() < handler.getMaxPages() - 1 ? 0xFFFFFF : 0x888888, false);
    }
}

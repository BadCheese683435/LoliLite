package com.example.ten.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PickaxeItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.text.Text;

import java.util.List;

public class NormalLoliItem extends PickaxeItem {
    
    public NormalLoliItem(ToolMaterial material, int attackDamage, float attackSpeed, Settings settings) {
        super(material, attackDamage, attackSpeed, settings);
    }

    @Override
    public boolean hasGlint(ItemStack stack) {
        return false;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void appendTooltip(ItemStack stack, net.minecraft.world.World world, List<Text> tooltip, net.minecraft.client.item.TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);
        tooltip.add(Text.of(""));
        tooltip.add(Text.of("§6在主手时："));
        tooltip.add(Text.of("§77 §7攻击伤害"));
        tooltip.add(Text.of("§71.0 §7攻击速度"));
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        return super.postHit(stack, target, attacker);
    }
}
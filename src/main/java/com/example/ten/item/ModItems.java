package com.example.ten.item;

import com.example.ten.Ten;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterials;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModItems {
    public static final Item ULTIMATE_WEAPON = new UltimateWeaponItem(ToolMaterials.DIAMOND, 3, -2.4F, 
            new FabricItemSettings().maxCount(1).fireproof());
    
    public static final Item NORMAL_LOLI = new NormalLoliItem(ToolMaterials.DIAMOND, 7, -2.4F, 
            new FabricItemSettings().maxCount(1));
    
    public static final Item FLIGHT_UPGRADE = new FlightUpgradeItem(
            new FabricItemSettings().maxCount(1));
    
    public static final Item MINING_RANGE_UPGRADE_1 = new MiningRangeUpgradeItem(
            new FabricItemSettings().maxCount(64));
    public static final Item MINING_RANGE_UPGRADE_2 = new MiningRangeUpgradeItem(
            new FabricItemSettings().maxCount(64));
    public static final Item MINING_RANGE_UPGRADE_3 = new MiningRangeUpgradeItem(
            new FabricItemSettings().maxCount(64));
    
    public static final Item COURAGE_SHARD_1 = new CourageShardItem(
            new FabricItemSettings().maxCount(64));
    public static final Item COURAGE_SHARD_2 = new CourageShardItem(
            new FabricItemSettings().maxCount(64));
    public static final Item COURAGE_SHARD_3 = new CourageShardItem(
            new FabricItemSettings().maxCount(64));
    
    public static final Item TIME_REWIND_UPGRADE_1 = new TimeRewindUpgradeItem(
            new FabricItemSettings().maxCount(64));
    public static final Item TIME_REWIND_UPGRADE_2 = new TimeRewindUpgradeItem(
            new FabricItemSettings().maxCount(64));
    public static final Item TIME_REWIND_UPGRADE_3 = new TimeRewindUpgradeItem(
            new FabricItemSettings().maxCount(64));
    
    public static final Item STORAGE_CAPACITY_UPGRADE_1 = new StorageCapacityUpgradeItem(
            new FabricItemSettings().maxCount(64));
    public static final Item STORAGE_CAPACITY_UPGRADE_2 = new StorageCapacityUpgradeItem(
            new FabricItemSettings().maxCount(64));
    public static final Item STORAGE_CAPACITY_UPGRADE_3 = new StorageCapacityUpgradeItem(
            new FabricItemSettings().maxCount(64));
    
    public static final Item ATTACK_RANGE_UPGRADE_1 = new AttackRangeUpgradeItem(
            new FabricItemSettings().maxCount(64));
    public static final Item ATTACK_RANGE_UPGRADE_2 = new AttackRangeUpgradeItem(
            new FabricItemSettings().maxCount(64));
    public static final Item ATTACK_RANGE_UPGRADE_3 = new AttackRangeUpgradeItem(
            new FabricItemSettings().maxCount(64));
    
    public static final Item ATTACK_SPEED_UPGRADE_1 = new AttackSpeedUpgradeItem(
            new FabricItemSettings().maxCount(64));
    public static final Item ATTACK_SPEED_UPGRADE_2 = new AttackSpeedUpgradeItem(
            new FabricItemSettings().maxCount(64));
    public static final Item ATTACK_SPEED_UPGRADE_3 = new AttackSpeedUpgradeItem(
            new FabricItemSettings().maxCount(64));
    
    public static final Item ULTIMATE_CREATURE_SOUL = new UltimateCreatureSoulItem(
            new FabricItemSettings().maxCount(64));
    
    public static final SoundEvent LOLI_SUCCESS_SOUND = SoundEvent.of(new Identifier(Ten.MOD_ID, "item/lolissuccess"));
    public static final SoundEvent ULTIMATE_ATTACK_SOUND = SoundEvent.of(new Identifier(Ten.MOD_ID, "item/ultimate_attack"));

    public static void register() {
        Registry.register(Registries.ITEM, new Identifier(Ten.MOD_ID, "ultimate_weapon"), ULTIMATE_WEAPON);
        Registry.register(Registries.ITEM, new Identifier(Ten.MOD_ID, "normal_loli"), NORMAL_LOLI);
        Registry.register(Registries.ITEM, new Identifier(Ten.MOD_ID, "flight_upgrade"), FLIGHT_UPGRADE);
        Registry.register(Registries.ITEM, new Identifier(Ten.MOD_ID, "mining_range_upgrade_1"), MINING_RANGE_UPGRADE_1);
        Registry.register(Registries.ITEM, new Identifier(Ten.MOD_ID, "mining_range_upgrade_2"), MINING_RANGE_UPGRADE_2);
        Registry.register(Registries.ITEM, new Identifier(Ten.MOD_ID, "mining_range_upgrade_3"), MINING_RANGE_UPGRADE_3);
        Registry.register(Registries.ITEM, new Identifier(Ten.MOD_ID, "courage_shard_1"), COURAGE_SHARD_1);
        Registry.register(Registries.ITEM, new Identifier(Ten.MOD_ID, "courage_shard_2"), COURAGE_SHARD_2);
        Registry.register(Registries.ITEM, new Identifier(Ten.MOD_ID, "courage_shard_3"), COURAGE_SHARD_3);
        Registry.register(Registries.ITEM, new Identifier(Ten.MOD_ID, "time_rewind_upgrade_1"), TIME_REWIND_UPGRADE_1);
        Registry.register(Registries.ITEM, new Identifier(Ten.MOD_ID, "time_rewind_upgrade_2"), TIME_REWIND_UPGRADE_2);
        Registry.register(Registries.ITEM, new Identifier(Ten.MOD_ID, "time_rewind_upgrade_3"), TIME_REWIND_UPGRADE_3);
        Registry.register(Registries.ITEM, new Identifier(Ten.MOD_ID, "storage_capacity_upgrade_1"), STORAGE_CAPACITY_UPGRADE_1);
        Registry.register(Registries.ITEM, new Identifier(Ten.MOD_ID, "storage_capacity_upgrade_2"), STORAGE_CAPACITY_UPGRADE_2);
        Registry.register(Registries.ITEM, new Identifier(Ten.MOD_ID, "storage_capacity_upgrade_3"), STORAGE_CAPACITY_UPGRADE_3);
        Registry.register(Registries.ITEM, new Identifier(Ten.MOD_ID, "attack_range_upgrade_1"), ATTACK_RANGE_UPGRADE_1);
        Registry.register(Registries.ITEM, new Identifier(Ten.MOD_ID, "attack_range_upgrade_2"), ATTACK_RANGE_UPGRADE_2);
        Registry.register(Registries.ITEM, new Identifier(Ten.MOD_ID, "attack_range_upgrade_3"), ATTACK_RANGE_UPGRADE_3);
        Registry.register(Registries.ITEM, new Identifier(Ten.MOD_ID, "attack_speed_upgrade_1"), ATTACK_SPEED_UPGRADE_1);
        Registry.register(Registries.ITEM, new Identifier(Ten.MOD_ID, "attack_speed_upgrade_2"), ATTACK_SPEED_UPGRADE_2);
        Registry.register(Registries.ITEM, new Identifier(Ten.MOD_ID, "attack_speed_upgrade_3"), ATTACK_SPEED_UPGRADE_3);
        Registry.register(Registries.ITEM, new Identifier(Ten.MOD_ID, "ultimate_creature_soul"), ULTIMATE_CREATURE_SOUL);
        
        Registry.register(Registries.SOUND_EVENT, new Identifier(Ten.MOD_ID, "item/lolissuccess"), LOLI_SUCCESS_SOUND);
        Registry.register(Registries.SOUND_EVENT, new Identifier(Ten.MOD_ID, "item/ultimate_attack"), ULTIMATE_ATTACK_SOUND);
        
        Registry.register(
            Registries.ITEM_GROUP,
            new Identifier(Ten.MOD_ID, "ten_group"),
            FabricItemGroup.builder()
                .displayName(Text.translatable("itemGroup.ten.ten_group"))
                .icon(() -> new ItemStack(ULTIMATE_WEAPON))
                .entries((context, entries) -> {
                    entries.add(ULTIMATE_WEAPON);
                    entries.add(NORMAL_LOLI);
                    entries.add(FLIGHT_UPGRADE);
                    entries.add(MINING_RANGE_UPGRADE_1);
                    entries.add(MINING_RANGE_UPGRADE_2);
                    entries.add(MINING_RANGE_UPGRADE_3);
                    entries.add(COURAGE_SHARD_1);
                    entries.add(COURAGE_SHARD_2);
                    entries.add(COURAGE_SHARD_3);
                    entries.add(TIME_REWIND_UPGRADE_1);
                    entries.add(TIME_REWIND_UPGRADE_2);
                    entries.add(TIME_REWIND_UPGRADE_3);
                    entries.add(STORAGE_CAPACITY_UPGRADE_1);
                    entries.add(STORAGE_CAPACITY_UPGRADE_2);
                    entries.add(STORAGE_CAPACITY_UPGRADE_3);
                    entries.add(ATTACK_RANGE_UPGRADE_1);
                    entries.add(ATTACK_RANGE_UPGRADE_2);
                    entries.add(ATTACK_RANGE_UPGRADE_3);
                    entries.add(ATTACK_SPEED_UPGRADE_1);
                    entries.add(ATTACK_SPEED_UPGRADE_2);
                    entries.add(ATTACK_SPEED_UPGRADE_3);
                    entries.add(ULTIMATE_CREATURE_SOUL);
                })
                .build()
        );
    }
}

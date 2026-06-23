package com.example.ten.damage;

import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public class KillDamageType {
    public static final RegistryKey<DamageType> KILL = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, new Identifier("ten", "kill"));
}
// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode fieldsfirst 

package com.bioxx.tfc.WorldGen.Biomes;

import com.bioxx.tfc.WorldGen.TFCBiome;

import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityMagmaCube;
import net.minecraft.entity.monster.EntityPigZombie;


// Referenced classes of package net.minecraft.src:
//            BiomeGenBase, SpawnListEntry, EntityWolf, BiomeDecorator, 
//            WorldGenerator

public class BiomeGenHellTFC extends TFCBiome
{
	public BiomeGenHellTFC(int i)
	{
		super(i);
		this.spawnableMonsterList.clear();
		this.spawnableCreatureList.clear();
		this.spawnableWaterCreatureList.clear();
		this.spawnableCaveCreatureList.clear();

		this.spawnableMonsterList.add(new SpawnListEntry(EntityGhast.class, 50, 4, 4));
		this.spawnableMonsterList.add(new SpawnListEntry(EntityPigZombie.class, 100, 4, 4));
		this.spawnableMonsterList.add(new SpawnListEntry(EntityMagmaCube.class, 1, 4, 4));

		this.theBiomeDecorator.treesPerChunk = 0;
		this.theBiomeDecorator.grassPerChunk = 0;
	}
}
package com.wolfretbusiness.ferretshinies.machines;

import com.wolfretbusiness.ferretshinies.FerretShinies;
import com.wolfretbusiness.ferretshinies.gui.FerretShinyClient;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class CratingMachine extends Block {
	
	private final String name = "CratingMachine";

	public CratingMachine(Material arg0) {
		super(arg0);
		
        this.setBlockName(this.name);
        this.setBlockTextureName(FerretShinies.MODID + ":" + this.name);
        this.setCreativeTab(FerretShinyClient.tabFerretShinies);
        this.setHardness(2.0F);
        this.setResistance(6.0F);
        this.setLightLevel(1.0F);
        this.setHarvestLevel("pickaxe", 3);
        this.setStepSound(soundTypeMetal);

		GameRegistry.registerBlock(this, this.name);
	}

}

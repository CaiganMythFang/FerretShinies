package com.wolfretbusiness.ferretshinies.items;

import net.minecraft.item.Item;

import com.wolfretbusiness.ferretshinies.FerretShinies;
import com.wolfretbusiness.ferretshinies.FerretShinyItems;
import com.wolfretbusiness.ferretshinies.gui.FerretShinyClient;

import cpw.mods.fml.common.registry.GameRegistry;

public class CopperCoin extends Item {
	// TODO Erdenshire 2014-09-06 NullToken may supercede this.
	private String name = "copperCoin";

	public CopperCoin() {
		super();
		setUnlocalizedName(FerretShinies.MODID + "_" + name);
		GameRegistry.registerItem(this, name);
		setCreativeTab(FerretShinyClient.tabFerretShinies); // FerretShinyTab.tabMaterials);
		setTextureName(FerretShinies.MODID + ":" + name);
	}

}
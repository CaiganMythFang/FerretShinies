package com.wolfretbusiness.ferretshinies;

import com.wolfretbusiness.ferretshinies.gui.FerretShinyClient;
import com.wolfretbusiness.ferretshinies.items.BlindBag;
import com.wolfretbusiness.ferretshinies.items.IconAsset;
import com.wolfretbusiness.ferretshinies.items.LayeredIconAsset;
import com.wolfretbusiness.ferretshinies.items.NullToken;
import com.wolfretbusiness.ferretshinies.items.SealedShippingCrate;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.Item;

public enum FerretShinyItems {
	NULL_TOKEN(new NullToken()),
	ICON_ASSET(new IconAsset()),
	LAYERED_ICON_ASSET(new LayeredIconAsset()),
	BLIND_BAG(new BlindBag()),
	CONTRACTS(new LayeredIconAsset("Contracts")),
	SEALED_SHIPPING_CRATE(new SealedShippingCrate()),;

	public final FerretShinyItem item;

	FerretShinyItems(FerretShinyItem item) {
		this.item = item;
	}

	public static void init() {
		for (FerretShinyItems ferretShinyItem : FerretShinyItems.values()) {
			GameRegistry.registerItem(ferretShinyItem.item, ferretShinyItem.item.internalName);
		}
	}

	/*******************************************************************************/
	/**************************** ITEM TYPE DEFINITIONS ****************************/
	/*******************************************************************************/

	// All Ferret Shiny items will...
	// - Have an internal name consistent with their registered name
	// - Have a consistent UnlocalizedName of the mod ID, an underscore, and the internal name
	// - Be on the Ferret Shinies creative tab
	// TODO: Examine common FerretShinyItem functionality that belongs here.
	public static abstract class FerretShinyItem extends Item {
		protected final String internalName;

		public FerretShinyItem(String name) {
			super();
			this.internalName = name;
			this.setUnlocalizedName(FerretShinies.MODID + "_" + this.internalName);
			this.setCreativeTab(FerretShinyClient.tabFerretShinies);
		}
	}
}

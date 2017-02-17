package com.wolfretbusiness.ferretshinies;

import net.minecraft.item.Item;

import com.wolfretbusiness.ferretshinies.items.BlindBag;
import com.wolfretbusiness.ferretshinies.items.IconAsset;
import com.wolfretbusiness.ferretshinies.items.LayeredIconAsset;
import com.wolfretbusiness.ferretshinies.items.NullToken;

import cpw.mods.fml.common.registry.GameRegistry;

public enum FerretShinyItems {
	NULL_TOKEN(new NullToken()),
	ICON_ASSET(new IconAsset()),
	LAYERED_ICON_ASSET(new LayeredIconAsset()),
	BLIND_BAG(new BlindBag()),
	CONTRACTS(new LayeredIconAsset("Contracts"));
	
	public final BaseItem item;
	
	FerretShinyItems(BaseItem item) {
		this.item = item;
	}

	public static void init() {
		for (FerretShinyItems ferretShinyItem : FerretShinyItems.values()) {
			GameRegistry.registerItem(ferretShinyItem.item, ferretShinyItem.item.internalName);
		}
	}
	
	public static class BaseItem extends Item {
		protected final String internalName;
		
		public BaseItem(String name) {			
			super();
			this.internalName = name;
		}
	}
}

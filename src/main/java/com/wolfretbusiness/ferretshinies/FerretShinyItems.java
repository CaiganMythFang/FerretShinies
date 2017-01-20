package com.wolfretbusiness.ferretshinies;

import net.minecraft.item.Item;

import com.wolfretbusiness.ferretshinies.items.BlindBag;
import com.wolfretbusiness.ferretshinies.items.IconAsset;
import com.wolfretbusiness.ferretshinies.items.LayeredIconAsset;
import com.wolfretbusiness.ferretshinies.items.NullToken;

public final class FerretShinyItems {
	public static Item nullToken;
	public static Item iconAssets;
	public static Item layeredIconAssets;
	public static Item blindBag;
	public static Item contracts;

	public static void init() {
		nullToken = new NullToken();
		iconAssets = new IconAsset();
		layeredIconAssets = new LayeredIconAsset("LayeredIconAsset");
		blindBag = new BlindBag();
		contracts = new LayeredIconAsset("Contracts");
	}
}

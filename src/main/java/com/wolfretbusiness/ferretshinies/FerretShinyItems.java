package com.wolfretbusiness.ferretshinies;

import net.minecraft.item.Item;

import com.wolfretbusiness.ferretshinies.items.BlindBag;
import com.wolfretbusiness.ferretshinies.items.IconAsset;
import com.wolfretbusiness.ferretshinies.items.NullToken;

public final class FerretShinyItems {
	public static Item nullToken;
	public static Item iconAssets;
	public static Item blindBag;

	public static void init() {
		nullToken = new NullToken();
		iconAssets = new IconAsset();
		blindBag = new BlindBag();
	}
}

package com.wolfretbusiness.ferretshinies;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

@Deprecated
public class FerretShinyTab extends CreativeTabs {
	// TODO Erdenshire 2014-09-06 Identify what "icon" is.
	private Item icon;

	public FerretShinyTab(String label) {
		super(label);
	}

	public void setTabIconItem(Item item) {
		icon = item;
	}

	@Override
	public Item getTabIconItem() {
		return icon;
	}
}

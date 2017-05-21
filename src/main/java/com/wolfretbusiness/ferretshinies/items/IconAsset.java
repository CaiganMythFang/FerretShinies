package com.wolfretbusiness.ferretshinies.items;

import static com.wolfretbusiness.ferretshinies.utilities.CommonUtilities.camelCaseToTitle;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.wolfretbusiness.ferretshinies.FerretShinies;
import com.wolfretbusiness.ferretshinies.FerretShinyItems.BaseItem;
import com.wolfretbusiness.ferretshinies.gui.FerretShinyClient;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class IconAsset extends BaseItem {
	private static final List<String> SUB_ITEM_NAMES = new ArrayList<String>();

	public IconAsset() {
		super("IconAsset");
		this.extractIdentifiers();
		this.setUnlocalizedName(FerretShinies.MODID + "_" + this.internalName);
		this.setCreativeTab(FerretShinyClient.tabFerretShinies);
		this.setHasSubtypes(true);
		this.setMaxDamage(0);
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(this, 0,
				new ModelResourceLocation(FerretShinies.MODID + ":" + this.internalName, "inventory"));
	}

	@Override
	public String getItemStackDisplayName(final ItemStack stack) {
		String displayName = super.getItemStackDisplayName(stack);
		if (displayName.contains(".name")) {
			displayName = camelCaseToTitle(this.getStackName(stack));
		}
		return displayName;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(final Item item, final CreativeTabs tabs, final List list) {
		for (int subItem = 0; subItem < SUB_ITEM_NAMES.size(); ++subItem) {
			list.add(new ItemStack(item, 1, subItem));
		}
	}

	@Override
	public String getUnlocalizedName(final ItemStack stack) {
		return "item." + FerretShinies.MODID + "_" + this.getStackName(stack);
	}

	private void extractIdentifiers() {
		if (SUB_ITEM_NAMES.isEmpty()) {
			final InputStream in = this.getClass()
					.getResourceAsStream(FerretShinies.getClassConfigurationFile(this.getClass()));
			final BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			try {
				while (reader.ready()) {
					final String subItemName = reader.readLine();
					if (!subItemName.startsWith("#")) {
						SUB_ITEM_NAMES.add(subItemName);
					}
				}
				reader.close();
			} catch (final IOException e) {
				e.printStackTrace();
			}
		}
	}

	private String getStackName(final ItemStack stack) {
		return SUB_ITEM_NAMES.get(stack.getItemDamage());
	}
}

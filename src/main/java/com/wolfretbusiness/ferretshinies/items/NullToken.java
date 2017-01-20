package com.wolfretbusiness.ferretshinies.items;

import static com.wolfretbusiness.ferretshinies.utilities.CommonUtilities.camelCaseToTitle;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import com.wolfretbusiness.ferretshinies.FerretShinies;
import com.wolfretbusiness.ferretshinies.gui.FerretShinyClient;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class NullToken extends Item {
	private final String name = "NullToken";
	private final static List<IIcon> itemIcons = new ArrayList<IIcon>();

	private static final List<String> subItemNames = new ArrayList<String>();

	public NullToken() {
		super();
		this.extractIdentifiers();
		this.setUnlocalizedName(FerretShinies.MODID + "_" + this.name);
		this.setCreativeTab(FerretShinyClient.tabFerretShinies);
		this.setHasSubtypes(true);
		this.setMaxDamage(0);
		GameRegistry.registerItem(this, this.name);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIconFromDamage(final int index) {
		return itemIcons.get(index);
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
		for (int subItem = 0; subItem < subItemNames.size(); ++subItem) {
			list.add(new ItemStack(item, 1, subItem));
		}
	}

	@Override
	public String getUnlocalizedName(final ItemStack stack) {
		return "item." + FerretShinies.MODID + "_" + this.getStackName(stack);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(final IIconRegister iconRegister) {
		itemIcons.clear();
		for (final String subItemName : subItemNames) {
			final IIcon icon = iconRegister.registerIcon(FerretShinies.MODID + ":" + subItemName);
			itemIcons.add(icon);
		}
	}

	private void extractIdentifiers() {
		if (subItemNames.isEmpty()) {
			final InputStream in = this.getClass().getResourceAsStream(FerretShinies.getClassConfigurationFile(this.getClass()));
			final BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			try {
				while (reader.ready()) {
					final String subItemName = reader.readLine();
					if (!subItemName.startsWith("#")) {
						subItemNames.add(subItemName);
					}
				}
				reader.close();
			} catch (final IOException e) {
				e.printStackTrace();
			}
		}
	}

	private String getStackName(final ItemStack stack) {
		return subItemNames.get(stack.getItemDamage());
	}
}

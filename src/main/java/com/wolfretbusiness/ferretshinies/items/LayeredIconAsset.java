package com.wolfretbusiness.ferretshinies.items;

import static com.wolfretbusiness.ferretshinies.utilities.CommonUtilities.camelCaseToTitle;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import com.wolfretbusiness.ferretshinies.FerretShinies;
import com.wolfretbusiness.ferretshinies.FerretShinyItems.BaseItem;
import com.wolfretbusiness.ferretshinies.gui.FerretShinyClient;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class LayeredIconAsset extends BaseItem {
	private static final List<String> SUB_ITEM_NAMES = new ArrayList<String>();
	private static final Map<String, List<String>> ICON_NAMES_BY_ICON = new HashMap<String, List<String>>();
	private static final Map<String, IIcon> ICONS_BY_ICON_NAME = new HashMap<String, IIcon>();

	public LayeredIconAsset() {
		super("LayeredIconAsset");
		this.extractIdentifiers();
		this.setUnlocalizedName(FerretShinies.MODID + "_" + this.internalName);
		this.setCreativeTab(FerretShinyClient.tabFerretShinies);
		this.setHasSubtypes(true);
		this.setMaxDamage(0);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(final ItemStack stack, final int renderPass) {
		final IIcon icon = this.getIconFromDamageAndRenderPass(stack.getItemDamage(), renderPass);
		return icon;
	}

	@Override
	public String getItemStackDisplayName(final ItemStack stack) {
		String displayName = super.getItemStackDisplayName(stack);
		if (displayName.contains(".name")) {
			displayName = camelCaseToTitle(this.getStackName(stack));
		}
		return displayName;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getRenderPasses(final int damage) {
		final String itemName = SUB_ITEM_NAMES.get(damage);
		return ICON_NAMES_BY_ICON.get(itemName).size();
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

	@Override
	public boolean isFull3D() {
		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(final IIconRegister iconRegister) {
		ICONS_BY_ICON_NAME.clear();
		for (final String subItemName : SUB_ITEM_NAMES) {
			final List<String> iconNames = ICON_NAMES_BY_ICON.get(subItemName);

			for (final String iconName : iconNames) {
				final IIcon icon = iconRegister.registerIcon(FerretShinies.MODID + ":" + iconName);
				ICONS_BY_ICON_NAME.put(iconName, icon);
			}
		}
	}

	@SideOnly(Side.CLIENT)
	@Override
	public boolean requiresMultipleRenderPasses() {
		return true;
	}

	private void extractIdentifiers() {
		final String iconListPath = FerretShinies.configDirectory + File.separatorChar + "LayeredIconAsset.cfg";
		
		if (SUB_ITEM_NAMES.isEmpty()) {
			try {
				final FileInputStream in = new FileInputStream(iconListPath);
				final BufferedReader reader = new BufferedReader(new InputStreamReader(in));
				
				try {
					while (reader.ready()) {
						final String subIconRow = reader.readLine();
						if (!subIconRow.startsWith("#")) {
							String[] subItemFields = subIconRow.split(":");
							String subIconName = subItemFields[0];
							SUB_ITEM_NAMES.add(subIconName);
							final List<String> layeredIcons = new ArrayList<String>();
							String[] subItemIcons = subItemFields[1].split(",");
							for (String icon : subItemIcons) {
								layeredIcons.add(icon);
							}
							ICON_NAMES_BY_ICON.put(subIconName, layeredIcons);
						}
					}
					reader.close();
				} catch (final IOException e) {
					e.printStackTrace();
				}
			} catch (FileNotFoundException e1) {
				throw new IllegalStateException("LayeredIconAsset.cfg configuration file was not present: " + iconListPath);
			}
		}
	}

	private IIcon getIconFromDamageAndRenderPass(final int damage, final int renderPass) {
		final String itemName = SUB_ITEM_NAMES.get(damage);
		final String iconName = ICON_NAMES_BY_ICON.get(itemName).get(renderPass);
		return ICONS_BY_ICON_NAME.get(iconName);
	}

	private String getStackName(final ItemStack stack) {
		return SUB_ITEM_NAMES.get(stack.getItemDamage());
	}
}

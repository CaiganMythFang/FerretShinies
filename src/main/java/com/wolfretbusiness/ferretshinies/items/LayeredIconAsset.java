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
import com.wolfretbusiness.ferretshinies.gui.FerretShinyClient;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class LayeredIconAsset extends Item {
	private final String name = "LayeredIconAsset";

	private static final List<String> subItemNames = new ArrayList<String>();
	private static final Map<String, List<String>> iconNamesByItem = new HashMap<String, List<String>>();
	private static final Map<String, IIcon> iconsByIconName = new HashMap<String, IIcon>();

	public LayeredIconAsset() {
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
		final String itemName = subItemNames.get(damage);
		return iconNamesByItem.get(itemName).size();
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
	public boolean isFull3D() {
		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(final IIconRegister iconRegister) {
		iconsByIconName.clear();
		for (final String subItemName : subItemNames) {
			final List<String> iconNames = iconNamesByItem.get(subItemName);

			for (final String iconName : iconNames) {
				final IIcon icon = iconRegister.registerIcon(FerretShinies.MODID + ":" + iconName);
				iconsByIconName.put(iconName, icon);
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
		
		if (subItemNames.isEmpty()) {
			FileInputStream in;
			try {
				in = new FileInputStream(iconListPath);
				final BufferedReader reader = new BufferedReader(new InputStreamReader(in));
				
				try {
					while (reader.ready()) {
						final String subIconRow = reader.readLine();
						if (!subIconRow.startsWith("#")) {
							String[] subItemFields = subIconRow.split(":");
							subItemNames.add(subItemFields[0]);
							final List<String> layeredIcons = new ArrayList<String>();
							String[] subItemIcons = subItemFields[1].split(",");
							for (String icon : subItemIcons) {
								layeredIcons.add(icon);
							}
							iconNamesByItem.put(subIconRow, layeredIcons);
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
		final String itemName = subItemNames.get(damage);
		final String iconName = iconNamesByItem.get(itemName).get(renderPass);
		return iconsByIconName.get(iconName);
	}

	private String getStackName(final ItemStack stack) {
		return subItemNames.get(stack.getItemDamage());
	}
}

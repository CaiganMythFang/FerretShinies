package com.wolfretbusiness.ferretshinies.items;

import static com.wolfretbusiness.ferretshinies.utilities.CommonUtilities.camelCaseToTitle;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
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

public class LayeredIconAsset extends BaseItem {

	private final List<String> subItemNames = new ArrayList<String>();

	public LayeredIconAsset() {
		super("LayeredIconAsset");
		this.extractIdentifiers();
		this.setUnlocalizedName(FerretShinies.MODID + "_" + this.internalName);
		this.setCreativeTab(FerretShinyClient.tabFerretShinies);
		this.setHasSubtypes(true);
		this.setMaxDamage(0);
	}

	public LayeredIconAsset(String name) {
		super(name);
		this.extractIdentifiers();
		this.setUnlocalizedName(FerretShinies.MODID + "_" + this.internalName);
		this.setCreativeTab(FerretShinyClient.tabFerretShinies);
		this.setHasSubtypes(true);
		this.setMaxDamage(0);
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
	public boolean isFull3D() {
		return true;
	}

	private void extractIdentifiers() {
		final String iconListPath = FerretShinies.configDirectory + File.separatorChar + internalName + ".cfg";

		if (subItemNames.isEmpty()) {
			try {
				final FileInputStream in = new FileInputStream(iconListPath);
				final BufferedReader reader = new BufferedReader(new InputStreamReader(in));

				try {
					while (reader.ready()) {
						final String subIconRow = reader.readLine();
						if (!subIconRow.startsWith("#")) {
							String[] subItemFields = subIconRow.split(":");
							subItemNames.add(subItemFields[0]);
							Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(this,
									subItemNames.size() - 1, new ModelResourceLocation(subItemFields[1], "inventory"));
						}
					}
					reader.close();
				} catch (final IOException e) {
					e.printStackTrace();
				}
			} catch (FileNotFoundException e1) {
				throw new IllegalStateException(
						internalName + ".cfg configuration file was not present: " + iconListPath);
			}
		}
	}

	private String getStackName(final ItemStack stack) {
		return subItemNames.get(stack.getItemDamage());
	}
}

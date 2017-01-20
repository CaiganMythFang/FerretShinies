package com.wolfretbusiness.ferretshinies.items;

import static com.wolfretbusiness.ferretshinies.utilities.CommonUtilities.camelCaseToTitle;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import com.wolfretbusiness.ferretshinies.FerretShinies;
import com.wolfretbusiness.ferretshinies.gui.FerretShinyClient;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlindBag extends Item {
	private static final int BLIND_BAG_ITEM_LIST = 1;

	private static final int BLIND_BAG_NAME = 0;

	private final String name = "BlindBag";

	private static final List<String> subItemNames = new ArrayList<String>();
	private static final Map<String, List<String>> iconNamesByItem = new HashMap<String, List<String>>();
	private static final Map<String, IIcon> iconsByIconName = new HashMap<String, IIcon>();

	public BlindBag() {
		super();
		this.extractIdentifiers();
		this.setUnlocalizedName(FerretShinies.MODID + "_" + this.name);
		this.setCreativeTab(FerretShinyClient.tabFerretShinies);
		this.setHasSubtypes(true);
		this.setMaxDamage(0);
		this.setMaxStackSize(16);
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
	public int getMaxItemUseDuration(final ItemStack itemStack) {
		return 16;
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
	public ItemStack onItemRightClick(final ItemStack itemStack, final World world, final EntityPlayer entityPlayer) {
		final String bagName = subItemNames.get(itemStack.getItemDamage());
		final String bagContentFile = this.getBlindBagConfigDirectory() + bagName + ".cfg";
		try {
			if (!world.isRemote) {
				final List<String> potentialBagContents = this.getPotentialBagContents(bagContentFile);
				this.giveRandomItemToPlayer(world, entityPlayer, itemStack, potentialBagContents);
				entityPlayer.setItemInUse(itemStack, this.getMaxItemUseDuration(itemStack));
			}

			--itemStack.stackSize;

			if (!entityPlayer.capabilities.isCreativeMode && itemStack.stackSize == 0) {
				entityPlayer.destroyCurrentEquippedItem();
			}
		} catch (final FileNotFoundException e) {
			System.out.println("Blind bag " + bagName + " configuration file not found at " + bagContentFile);
		}
		return itemStack;
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
		final String bagListPath = FerretShinies.configDirectory + File.separatorChar + "BlindBags.cfg";
		if (subItemNames.isEmpty()) {
			try {
				final FileInputStream in = new FileInputStream(bagListPath);
				final BufferedReader reader = new BufferedReader(new InputStreamReader(in));

				try {
					while (reader.ready()) {
						final String blindBagRow = reader.readLine();
						if (!blindBagRow.startsWith("#")) {
							if (blindBagRow.contains(":")) {
								final String[] blindBagFields = blindBagRow.split(":");
								subItemNames.add(blindBagFields[BLIND_BAG_NAME]);

								final String[] blindBagIconNames = blindBagFields[BLIND_BAG_ITEM_LIST].split(",");
								final List<String> itemNameList = new ArrayList<String>();
								for (final String name : blindBagIconNames) {
									itemNameList.add(name);
								}
								iconNamesByItem.put(blindBagFields[BLIND_BAG_NAME], itemNameList);
							} else {
								subItemNames.add(blindBagRow);
								final List<String> blindBagIcons = new ArrayList<String>();
								blindBagIcons.add(blindBagRow);
								iconNamesByItem.put(blindBagRow, blindBagIcons);
							}
						}
					}
					reader.close();
				} catch (final IOException e) {
					e.printStackTrace();
				}
			} catch (final FileNotFoundException e) {
				throw new IllegalStateException("BlindBags.cfg configuration file was not present: " + bagListPath);
			}
		}
	}

	private String getBlindBagConfigDirectory() {
		return FerretShinies.configDirectory + File.separatorChar + "blindbags" + File.separatorChar;
	}

	private IIcon getIconFromDamageAndRenderPass(final int damage, final int renderPass) {
		final String itemName = subItemNames.get(damage);
		final String iconName = iconNamesByItem.get(itemName).get(renderPass);
		return iconsByIconName.get(iconName);
	}

	private List<String> getPotentialBagContents(final String bagContentFile) throws FileNotFoundException {
		final FileInputStream in = new FileInputStream(bagContentFile);
		final BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		final List<String> potentialBagContents = new ArrayList<String>();

		try {
			while (reader.ready()) {
				final String subItemName = reader.readLine();
				if (!subItemName.startsWith("#")) {
					potentialBagContents.add(subItemName);
				}
			}
			reader.close();
		} catch (final IOException e) {
			e.printStackTrace();
		}
		return potentialBagContents;
	}

	private String getStackName(final ItemStack stack) {
		return subItemNames.get(stack.getItemDamage());
	}

	private void giveRandomItemToPlayer(final World world, final EntityPlayer entityPlayer, final ItemStack bag, final List<String> potentialBagContents) {
		final String bagName = bag.getDisplayName();
		String currentBagContent = "";
		String message = "";
		ItemStack itemFromBag = bag.copy();
		itemFromBag.stackSize = 1;

		Collections.shuffle(potentialBagContents);

		// TODO Erdenshire 2015-02-18 Parse when the file is loaded, not when
		// giving the item to a player.
		boolean validBagFound = false;
		while (potentialBagContents.size() > 0 && !validBagFound) {
			try {
				currentBagContent = potentialBagContents.get(0);
				itemFromBag = this.parseBagContentAndGetItemStack(entityPlayer, currentBagContent);
				validBagFound = true;
			} catch (final ArrayIndexOutOfBoundsException|NullPointerException e) {
				message += "Invalid blind bag contents: " + currentBagContent + "\n";
				potentialBagContents.remove(0);
			}
		}

		if (!message.isEmpty()) {
			message = message + "Uh oh, this " + bagName + " seems to be defective. Better report it to the company! (Please report this to Caigan on the FTB Forums or his Player.me)\n";
		}
		message += "You opened " + (bagName.startsWith("A") ? "an " : "a ") + bagName + " and got " + itemFromBag.stackSize + " x [" + itemFromBag.getDisplayName() + "]!";

		final String announcements[] = message.split("\n");
		for (final String announcement : announcements) {
			entityPlayer.addChatMessage(new ChatComponentText(announcement));
		}

		world.spawnEntityInWorld(new EntityItem(world, entityPlayer.posX, entityPlayer.posY, entityPlayer.posZ, itemFromBag));
	}

	private ItemStack parseBagContentAndGetItemStack(final EntityPlayer entityPlayer, final String currentBagContent) throws ArrayIndexOutOfBoundsException {
		ItemStack itemFromBag;
		final String[] parts = currentBagContent.split(":");
		final String modId = parts[0];
		final String itemName = parts[1];
		final int metaId = Integer.parseInt(parts[2]);
		final int count = Integer.parseInt(parts[3]);
		String nbtString = null;
		if (parts.length > 4) {
			nbtString = parts[4];
			for (int i = 5; i < parts.length; i++) {
				nbtString += ":" + parts[i];
			}
		}

		final Item bagContent = GameRegistry.findItem(modId, itemName);
		itemFromBag = new ItemStack(bagContent);
		itemFromBag.setItemDamage(metaId);
		itemFromBag.stackSize = count;
		if (nbtString != null) {
			this.addNBTToStack(nbtString, itemFromBag, entityPlayer);
		}
		return itemFromBag;
	}

	private void addNBTToStack(final String nbtString, final ItemStack stack, final EntityPlayer player) {
		NBTBase base;
		try {
			base = JsonToNBT.func_150315_a(nbtString);
			if (base instanceof NBTTagCompound) {
				stack.setTagCompound((NBTTagCompound) base);
			} else {
				player.addChatMessage(new ChatComponentText("Error:  Invalid NBT type provided in JSON."));
			}
		} catch (final NBTException e) {
			player.addChatMessage(new ChatComponentText("Error:  Invalid NBT JSON data: " + e.getMessage()));
		}
	}

}

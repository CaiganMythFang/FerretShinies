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
import java.util.List;

import com.wolfretbusiness.ferretshinies.FerretShinies;
import com.wolfretbusiness.ferretshinies.FerretShinyItems.BaseItem;
import com.wolfretbusiness.ferretshinies.gui.FerretShinyClient;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlindBag extends BaseItem {
	private static final int BLIND_BAG_MODEL_NAME = 1;
	private static final int BLIND_BAG_NAME = 0;
	private static final List<String> BLIND_BAG_NAMES = new ArrayList<String>();
	// private static final Map<String, List<String>> ICON_NAMES_BY_ICON = new
	// HashMap<String, List<String>>();
	// private static final Map<String, IIcon> ICONS_BY_ICON_NAME = new
	// HashMap<String, IIcon>();

	public BlindBag() {
		super("BlindBag");
		this.extractIdentifiers();
		this.setUnlocalizedName(FerretShinies.MODID + "_" + this.internalName);
		this.setCreativeTab(FerretShinyClient.tabFerretShinies);
		this.setHasSubtypes(true);
		this.setMaxDamage(0);
		this.setMaxStackSize(16);
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

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(final Item item, final CreativeTabs tabs, final List list) {
		for (int subItem = 0; subItem < BLIND_BAG_NAMES.size(); ++subItem) {
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
	public EnumActionResult onItemUse(ItemStack itemStack, EntityPlayer playerIn, World worldIn, BlockPos pos,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		final String bagName = BLIND_BAG_NAMES.get(itemStack.getItemDamage());
		final String bagContentFile = this.getBlindBagConfigDirectory() + bagName + ".cfg";
		try {
			if (!worldIn.isRemote) {
				final List<String> potentialBagContents = this.getPotentialBagContents(bagContentFile);
				this.giveRandomItemToPlayer(worldIn, playerIn, itemStack, potentialBagContents);
			}

			--itemStack.stackSize;

			// if (!playerIn.capabilities.isCreativeMode && itemStack.stackSize
			// == 0) {
			// playerIn.destroyCurrentEquippedItem();
			// }
		} catch (final FileNotFoundException e1) {
			System.out.println("Blind bag " + bagName + " configuration file not found at " + bagContentFile);
		}

		super.onItemUse(itemStack, playerIn, worldIn, pos, hand, facing, hitX, hitY, hitZ);
		return EnumActionResult.SUCCESS;
	}

	private void extractIdentifiers() {
		final String bagListPath = FerretShinies.configDirectory + File.separatorChar + "BlindBags.cfg";

		if (BLIND_BAG_NAMES.isEmpty()) {
			try {
				final FileInputStream in = new FileInputStream(bagListPath);
				final BufferedReader reader = new BufferedReader(new InputStreamReader(in));

				try {
					while (reader.ready()) {
						final String blindBagRow = reader.readLine();
						if (!blindBagRow.startsWith("#")) {
							if (blindBagRow.contains(":")) {
								final String[] blindBagFields = blindBagRow.split(":");
								BLIND_BAG_NAMES.add(blindBagFields[BLIND_BAG_NAME]);
								final String blindBagModelName = blindBagFields[BLIND_BAG_MODEL_NAME];
								Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(this,
										BLIND_BAG_NAMES.size() - 1,
										new ModelResourceLocation(blindBagModelName, "inventory"));
							} else {
								BLIND_BAG_NAMES.add(blindBagRow);
								final List<String> blindBagIcons = new ArrayList<String>();
								blindBagIcons.add(blindBagRow);
								Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(this,
										BLIND_BAG_NAMES.size() - 1,
										new ModelResourceLocation(blindBagRow, "inventory"));
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
		return BLIND_BAG_NAMES.get(stack.getItemDamage());
	}

	private void giveRandomItemToPlayer(final World world, final EntityPlayer entityPlayer, final ItemStack bag,
			final List<String> potentialBagContents) {
		final String bagName = bag.getDisplayName();
		String currentBagContent = "";
		String message = "";
		ItemStack itemFromBag = bag.copy();
		itemFromBag.stackSize = 1;

		Collections.shuffle(potentialBagContents);

		boolean validBagFound = false;
		while (potentialBagContents.size() > 0 && !validBagFound) {
			try {
				currentBagContent = potentialBagContents.get(0);
				itemFromBag = this.parseBagContentAndGetItemStack(entityPlayer, currentBagContent);
				validBagFound = true;
			} catch (NullPointerException | ArrayIndexOutOfBoundsException e) {
				message += "Invalid blind bag contents: " + currentBagContent + "\n";
				potentialBagContents.remove(0);
			}
		}

		if (!message.isEmpty()) {
			message = message + "Uh oh, this " + bagName
					+ " seems to be defective. Better report it to the company! (Please report this to Caigan on the FTB Forums or his Player.me)\n";
		}
		message += "You opened " + (bagName.startsWith("A") ? "an " : "a ") + bagName + " and got "
				+ itemFromBag.stackSize + " x [" + itemFromBag.getDisplayName() + "]!";

		final String announcements[] = message.split("\n");
		for (final String announcement : announcements) {
			entityPlayer.addChatMessage(new TextComponentString(announcement));
		}

		world.spawnEntityInWorld(
				new EntityItem(world, entityPlayer.posX, entityPlayer.posY, entityPlayer.posZ, itemFromBag));
	}

	private ItemStack parseBagContentAndGetItemStack(final EntityPlayer entityPlayer, final String currentBagContent)
			throws ArrayIndexOutOfBoundsException {
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
			base = JsonToNBT.getTagFromJson(nbtString);
			if (base instanceof NBTTagCompound) {
				stack.setTagCompound((NBTTagCompound) base);
			} else {
				player.addChatMessage(new TextComponentString("Error:  Invalid NBT type provided in JSON."));
			}
		} catch (final NBTException e) {
			player.addChatMessage(new TextComponentString("Error:  Invalid NBT JSON data: " + e.getMessage()));
		}
	}
}

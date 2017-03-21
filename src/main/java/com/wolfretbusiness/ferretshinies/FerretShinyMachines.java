package com.wolfretbusiness.ferretshinies;

import com.wolfretbusiness.ferretshinies.blocks.ShippingCrate;
import com.wolfretbusiness.ferretshinies.gui.FerretShinyClient;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public enum FerretShinyMachines {
	SHIPPING_CRATE(new ShippingCrate()),
	// SHIPPING_MACHINE(new ShippingMachine()),
	// TELLER_MACHINE(new TellerMachine()),
	;

	public final FerretShinyMachine machine;

	FerretShinyMachines(FerretShinyMachine machine) {
		this.machine = machine;
	}

	public static final void init() {
		for (FerretShinyMachines ferretShinyMachine : FerretShinyMachines.values()) {
			FerretShinyMachine machine = ferretShinyMachine.machine;
			GameRegistry.registerBlock(machine, machine.internalName);
			GameRegistry.registerTileEntity(machine.getTileEntityClass(), machine.internalName);
		}
	}

	/*******************************************************************************/
	/************************** MACHINE TYPE DEFINITIONS ***************************/
	/*******************************************************************************/
	public static abstract class FerretShinyMachine extends BlockContainer {
		// All Ferret Shiny Machines will...
		// - Have an internal name consistent with their registered name
		// - Have a consistent BlockName of the mod ID, an underscore, and the internal name
		// - Be on the Ferret Shinies creative tab
		// - Have a step sound
		// - Be a multi-texture block
		// - Be a BlockContainer, implementing TileEntityProvider
		// - Provide a TileEntity class

		protected final String internalName;
		public IIcon[] icons = new IIcon[6];

		public FerretShinyMachine(String name) {
			super(Material.iron);
			this.internalName = name;
			this.setBlockName(FerretShinies.MODID + "_" + this.internalName);
			this.setCreativeTab(FerretShinyClient.tabFerretShinies);
			this.setStepSound(soundTypeMetal);
		}

		@Override
		public void registerBlockIcons(IIconRegister reg) {
			for (int i = 0; i < 6; i++) {
				this.icons[i] = reg.registerIcon(this.textureName + "_" + i);
			}
		}

		@Override
		public IIcon getIcon(int side, int meta) {
			return this.icons[side];
		}

		public abstract Class<? extends TileEntity> getTileEntityClass();
	}

	public static abstract class FerretShinyInventoryMachine extends FerretShinyMachine {
		public FerretShinyInventoryMachine(String name) {
			super(name);
		}

		@Override
		public void breakBlock(World world, int posX, int posY, int posZ, Block block, int unknown) {
			FerretShinyInventoryEntity entity = (FerretShinyInventoryEntity) world.getTileEntity(posX, posY, posZ);
			if (entity != null) {
				dropInventory(entity, world);
			}
			super.breakBlock(world, posX, posY, posZ, block, unknown);
		}

		@Override
		public void onBlockPlacedBy(World world, int posX, int posY, int posZ, EntityLivingBase placer, ItemStack stack) {
			if (stack.hasDisplayName()) {
				((FerretShinyInventoryEntity) world.getTileEntity(posX, posY, posZ)).setCustomInventoryName(stack.getDisplayName());
			}
		}

		public <Inventory extends TileEntity & IInventory> void dropInventory(Inventory inventory, World world) {
			for (int slot = 0; slot < inventory.getSizeInventory(); slot++) {
				ItemStack itemstack = inventory.getStackInSlot(slot);
				if (itemstack != null) {
					double destinationX = inventory.xCoord + Math.random() - 0.5;
					double destinationY = inventory.yCoord + Math.random() - 0.5;
					double destinationZ = inventory.zCoord + Math.random() - 0.5;
					ItemStack stack = new ItemStack(itemstack.getItem(), itemstack.stackSize, itemstack.getItemDamage());
					EntityItem entityitem = new EntityItem(world, destinationX, destinationY, destinationZ, stack);
					if (itemstack.hasTagCompound()) {
						entityitem.getEntityItem().setTagCompound((NBTTagCompound) itemstack.getTagCompound().copy());
					}
					itemstack.stackSize = 0;
					world.spawnEntityInWorld(entityitem);
				}
			}
		}
	}

	/*******************************************************************************/
	/*************************** TILE ENTITY DEFINITIONS ***************************/
	/*******************************************************************************/
	public static abstract class FerretShinyInventoryEntity extends TileEntity implements IInventory {
		// Some Ferret Shiny Machines will...
		// - Provide an Inventory
		private int inventorySize;
		private ItemStack[] inventory;
		private String customName;

		public FerretShinyInventoryEntity(int inventorySize) {
			super();
			this.inventorySize = inventorySize;
			this.inventory = new ItemStack[inventorySize];
		}

		public String setCustomInventoryName(String newName) {
			return this.customName = newName;
		}

		// TileEntity
		@Override
		public void writeToNBT(NBTTagCompound nbt) {
			super.writeToNBT(nbt);

			NBTTagList list = new NBTTagList();
			for (int i = 0; i < this.getSizeInventory(); ++i) {
				if (this.getStackInSlot(i) != null) {
					NBTTagCompound stackTag = new NBTTagCompound();
					stackTag.setByte("Slot", (byte) i);
					this.getStackInSlot(i).writeToNBT(stackTag);
					list.appendTag(stackTag);
				}
			}
			nbt.setTag("Items", list);

			if (this.hasCustomInventoryName()) {
				nbt.setString("CustomName", this.getInventoryName());
			}
		}

		@Override
		public void readFromNBT(NBTTagCompound nbt) {
			super.readFromNBT(nbt);

			NBTTagList list = nbt.getTagList("Items", 10);
			for (int i = 0; i < list.tagCount(); ++i) {
				NBTTagCompound stackTag = list.getCompoundTagAt(i);
				int slot = stackTag.getByte("Slot") & 255;
				this.setInventorySlotContents(slot, ItemStack.loadItemStackFromNBT(stackTag));
			}

			if (nbt.hasKey("CustomName", 8)) {
				this.customName = nbt.getString("CustomName");
			}
		}

		// IInventory
		@Override
		public void closeInventory() {
		}

		@Override
		public ItemStack decrStackSize(int index, int count) {
			if (this.getStackInSlot(index) != null) {
				ItemStack itemstack;

				if (this.getStackInSlot(index).stackSize <= count) {
					itemstack = this.getStackInSlot(index);
					this.setInventorySlotContents(index, null);
					this.markDirty();
					return itemstack;
				} else {
					itemstack = this.getStackInSlot(index).splitStack(count);

					if (this.getStackInSlot(index).stackSize <= 0) {
						this.setInventorySlotContents(index, null);
					} else {
						this.setInventorySlotContents(index, this.getStackInSlot(index));
					}

					this.markDirty();
					return itemstack;
				}
			} else {
				return null;
			}
		}

		@Override
		public String getInventoryName() {
			return this.hasCustomInventoryName() ? this.customName : "container.tutorial_tile_entity";
		}

		@Override
		public int getInventoryStackLimit() {
			return 64;
		}

		@Override
		public int getSizeInventory() {
			return inventorySize;
		}

		@Override
		public ItemStack getStackInSlot(int index) {
			if (index < 0 || index >= this.getSizeInventory()) {
				return null;
			}
			return this.inventory[index];
		}

		@Override
		public ItemStack getStackInSlotOnClosing(int index) {
			ItemStack stack = this.getStackInSlot(index);
			this.setInventorySlotContents(index, null);
			return stack;
		}

		@Override
		public boolean hasCustomInventoryName() {
			return this.customName != null && !this.customName.equals("");
		}

		@Override
		public boolean isItemValidForSlot(int index, ItemStack stack) {
			return true;
		}

		@Override
		public boolean isUseableByPlayer(EntityPlayer player) {
			if (worldObj == null) {
				return true;
			}
			if (worldObj.getTileEntity(xCoord, yCoord, zCoord) != this) {
				return false;
			}
			return player.getDistanceSq(xCoord + 0.5D, yCoord + 0.5D, zCoord + 0.5D) <= 64D;
		}

		@Override
		public void markDirty() {

		}

		@Override
		public void openInventory() {
		}

		@Override
		public void setInventorySlotContents(int index, ItemStack stack) {
			if (index < 0 || index >= this.getSizeInventory()) {
				return;
			}

			if (stack != null && stack.stackSize > this.getInventoryStackLimit()) {
				stack.stackSize = this.getInventoryStackLimit();
			}

			if (stack != null && stack.stackSize == 0) {
				stack = null;
			}

			this.inventory[index] = stack;
			this.markDirty();
		}
	}
}

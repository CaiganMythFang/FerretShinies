package com.wolfretbusiness.ferretshinies.blocks;

import com.wolfretbusiness.ferretshinies.FerretShinyMachines.FerretShinyInventoryEntity;
import com.wolfretbusiness.ferretshinies.FerretShinyMachines.FerretShinyInventoryMachine;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class ShippingCrate extends FerretShinyInventoryMachine {
	public ShippingCrate() {
		super("ShippingCrate");
		this.setHardness(2.0F);
		this.setResistance(6.0F);
		this.setLightLevel(1.0F);
	}

	@Override
	public TileEntity createNewTileEntity(World arg0, int arg1) {
		return new ShippingCrateEntity();
	}

	@Override
	public Class<? extends TileEntity> getTileEntityClass() {
		return ShippingCrateEntity.class;
	}

	public static class ShippingCrateEntity extends FerretShinyInventoryEntity {
		public ShippingCrateEntity() {
			super(30);
		}

	}
}

package com.wolfretbusiness.ferretshinies.blocks;

import com.wolfretbusiness.ferretshinies.FerretShinyMachines.FerretShinyMachine;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class TellerMachine extends FerretShinyMachine {
	public TellerMachine() {
		super("TellerMachine");
		this.setHardness(2.0F);
		this.setResistance(6.0F);
		this.setLightLevel(1.0F);
	}

	@Override
	public TileEntity createNewTileEntity(World arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Class<? extends TileEntity> getTileEntityClass() {
		// TODO Auto-generated method stub
		return null;
	}
}

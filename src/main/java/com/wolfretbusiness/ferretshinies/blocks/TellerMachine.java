package com.wolfretbusiness.ferretshinies.blocks;

import com.wolfretbusiness.ferretshinies.FerretShinyBlocks.FerretShinyBlock;
import net.minecraft.block.material.Material;

public class TellerMachine extends FerretShinyBlock {
	public TellerMachine() {
		super("TellerMachine", Material.iron, soundTypeMetal);
		this.setHardness(2.0F);
        this.setResistance(6.0F);
        this.setLightLevel(1.0F);
	}


}

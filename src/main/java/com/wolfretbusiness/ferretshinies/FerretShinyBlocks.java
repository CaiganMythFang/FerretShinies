package com.wolfretbusiness.ferretshinies;

import com.wolfretbusiness.ferretshinies.blocks.ShippingCrate;
import com.wolfretbusiness.ferretshinies.blocks.ShippingMachine;
import com.wolfretbusiness.ferretshinies.blocks.TellerMachine;
import com.wolfretbusiness.ferretshinies.gui.FerretShinyClient;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;

public enum FerretShinyBlocks {
	SHIPPING_CRATE(new ShippingCrate()),
	SHIPPING_MACHINE(new ShippingMachine()),
	TELLER_MACHINE(new TellerMachine()),;

	public final FerretShinyBlock block;

	FerretShinyBlocks(FerretShinyBlock block) {
		this.block = block;
	}

	public static final void init() {
		for (FerretShinyBlocks ferretShinyBlock : FerretShinyBlocks.values()) {
			GameRegistry.registerBlock(ferretShinyBlock.block, ferretShinyBlock.block.internalName);
		}
	}

	// All Ferret Shiny blocks will...
	// - Have an internal name consistent with their registered name
	// - Have a consistent BlockName of the mod ID, an underscore, and the internal name
	// - Be on the Ferret Shinies creative tab
	// - Have a step sound
	// - Be a multi-texture block
	public static class FerretShinyBlock extends Block {
		protected final String internalName;
		public IIcon[] icons = new IIcon[6];

		public FerretShinyBlock(String name, Material material, SoundType soundType) {
			super(material);
			this.internalName = name;
			this.setBlockName(FerretShinies.MODID + "_" + this.internalName);
			this.setCreativeTab(FerretShinyClient.tabFerretShinies);
			this.setStepSound(soundType);
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
	}
}

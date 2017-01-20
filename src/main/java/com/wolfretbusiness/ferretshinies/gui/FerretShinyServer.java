package com.wolfretbusiness.ferretshinies.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class FerretShinyServer implements CommonGuiProxy {

	@Override
	public Object getServerGuiElement(final int ID, final EntityPlayer player, final World world, final int x, final int y, final int z) {
		return null;
	}

	@Override
	public Object getClientGuiElement(final int ID, final EntityPlayer player, final World world, final int x, final int y, final int z) {
		return null;
	}

	@Override
	public void renderGUI() {
		// Actions on render GUI for the server (logging)

	}

}

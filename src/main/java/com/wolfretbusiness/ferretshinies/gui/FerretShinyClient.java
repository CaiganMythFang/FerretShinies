package com.wolfretbusiness.ferretshinies.gui;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.world.World;

import com.wolfretbusiness.ferretshinies.FerretShinyItems;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class FerretShinyClient implements CommonGuiProxy {
	public static final List<Class<? extends GuiScreen>> idToClassMap = new ArrayList<Class<? extends GuiScreen>>();
	public static final CreativeTabs tabFerretShinies = new CreativeTabs(CreativeTabs.getNextID(), "Ferret Shinies") {
		@Override
		@SideOnly(Side.CLIENT)
		public Item getTabIconItem() {
			return FerretShinyItems.NULL_TOKEN.item;
		}
	};

	@Override
	public Object getClientGuiElement(final int id, final EntityPlayer player, final World world, final int x, final int y, final int z) {
		GuiScreen guiElement = null;
		if (id < idToClassMap.size()) {
			try {
				guiElement = idToClassMap.get(id).getConstructor().newInstance();
			} catch (final Exception e) {
				e.printStackTrace();
			}
		}
		return guiElement;
	}

	@Deprecated
	@Override
	public Object getServerGuiElement(final int ID, final EntityPlayer player, final World world, final int x, final int y, final int z) {
		return null;
	}

	@Override
	public void renderGUI() {
		// Render GUI when on call from client
		System.out.println("FerretShinyClient renderGui");
	}

	public static int registerGui(final Class<? extends GuiScreen> screenClass) {
		idToClassMap.add(screenClass);
		return idToClassMap.size() - 1;
	}
}
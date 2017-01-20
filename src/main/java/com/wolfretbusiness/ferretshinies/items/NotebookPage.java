package com.wolfretbusiness.ferretshinies.items;

import static com.wolfretbusiness.ferretshinies.utilities.CommonUtilities.fromHex;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import com.wolfretbusiness.ferretshinies.FerretShinies;
import com.wolfretbusiness.ferretshinies.gui.FerretShinyClient;

import cpw.mods.fml.common.registry.GameRegistry;

public class NotebookPage extends Item {
	private final String name = "notebookPage";

	public NotebookPage() {
		super();
		this.setUnlocalizedName(FerretShinies.MODID + "_" + this.name);
		GameRegistry.registerItem(this, this.name);
		this.setCreativeTab(FerretShinyClient.tabFerretShinies); // FerretShinyTab.tabMaterials);
		this.setTextureName(FerretShinies.MODID + ":" + this.name);
	}

	@Override
	public ItemStack onItemRightClick(final ItemStack itemStack, final World world, final EntityPlayer player) {
		NotebookPageGui.openGui(player);
		return itemStack;
	}

	public static class NotebookPageGui extends GuiScreen {
		private static Integer notebookPageProxyId;

		public NotebookPageGui() {

		}

		@Override
		public void drawScreen(final int unknownArgument1, final int unknownArgument2, final float unknownArgument3) {
			// force a non-transparent background
			this.drawDefaultBackground();
			final int textColor = 0xFFFFFF;
			this.drawCenteredString(this.fontRendererObj, "Ferret Shiny Test", this.width / 2, 15, textColor);

			final int left = 30;
			final int right = this.width - 30;
			final int top = 30;
			final int bottom = this.height - 30;

			final int backgroundColor = fromHex("FFB0B0B0");
			final int borderColor = fromHex("A0404040");

			// Gui.drawGradientRect(left, top, right, bottom, topColor,
			// bottomColor);
			Gui.drawRect(left - 4, top - 4, right + 4, bottom + 4, borderColor);
			Gui.drawRect(left, top, right, bottom, backgroundColor);

			super.drawScreen(unknownArgument1, unknownArgument2, unknownArgument3);
		}

		@Override
		public void initGui() {
			System.out.println("NotebookPageGui.initGui");
		}

		public static void openGui(final EntityPlayer player) {
			player.openGui(FerretShinies.getModInstance(), getProxyId(), null, 0, 0, 0);
		}

		private static int getProxyId() {
			if (notebookPageProxyId == null) {
				notebookPageProxyId = FerretShinyClient.registerGui(NotebookPageGui.class);
			}
			return notebookPageProxyId;
		}
	}
}
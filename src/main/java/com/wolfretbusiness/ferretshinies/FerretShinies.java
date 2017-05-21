package com.wolfretbusiness.ferretshinies;

import com.wolfretbusiness.ferretshinies.gui.CommonGuiProxy;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;

@Mod(modid = FerretShinies.MODID, name = FerretShinies.MODNAME, version = FerretShinies.VERSION)
public class FerretShinies {
	// Constants
	public static final String MODID = "ferretshinies";
	public static final String MODNAME = "Ferret Shinies";
	public static final String VERSION = "1.0";
	public static String configDirectory;

	@Instance(MODID)
	public static FerretShinies instance;

	@SidedProxy(clientSide = "com.wolfretbusiness.ferretshinies.gui.FerretShinyClient", serverSide = "com.wolfretbusiness.ferretshinies.gui.FerretShinyServer")
	public static CommonGuiProxy proxy;

	@Mod.EventHandler
	public void preInit(final FMLPreInitializationEvent event) {
		NetworkRegistry.INSTANCE.registerGuiHandler(this, proxy);
		instance = this;
		configDirectory = event.getSuggestedConfigurationFile().getAbsolutePath().replace(".cfg", "");
	}

	@Mod.EventHandler
	public void init(final FMLInitializationEvent event) {
		FerretShinyItems.init();
	}

	@Mod.EventHandler
	public void postInit(final FMLPostInitializationEvent event) {

	}

	// TODO Erdenshire 2014-09-21 Move these to a ModFileUtility
	public static String getClassConfigurationFile(final Class<?> targetClass) {
		return getBaseAssetPath() + targetClass.getSimpleName() + ".cfg";
	}

	public static String getBaseAssetPath() {
		return "/assets/" + FerretShinies.MODID + "/";
	}

	public static Object getModInstance() {
		return instance;
	}
}

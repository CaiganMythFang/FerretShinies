package com.wolfretbusiness.ferretshinies;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.wolfretbusiness.ferretshinies.machines.CratingMachine;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class FerretShinyBlocks {
	public static Block cratingMachineBlock;
	public static Block shippingMachineBlock;
	public static Block tellerMachineBlock;

    public static final void init() {
    	cratingMachineBlock = new CratingMachine(Material.iron);
    	
    }
}

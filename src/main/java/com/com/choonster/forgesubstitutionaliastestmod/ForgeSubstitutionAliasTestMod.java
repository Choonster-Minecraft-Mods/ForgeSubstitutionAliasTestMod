package com.com.choonster.forgesubstitutionaliastestmod;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ObjectIntIdentityMap;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLModIdMappingEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.ExistingSubstitutionException;
import net.minecraftforge.fml.common.registry.GameData;
import net.minecraftforge.fml.common.registry.GameRegistry;
import org.apache.logging.log4j.Logger;

@Mod(modid = ForgeSubstitutionAliasTestMod.MODID, name = ForgeSubstitutionAliasTestMod.NAME)
public class ForgeSubstitutionAliasTestMod {
	public static final String MODID = "forgesubstitutionaliastestmod";
	public static final String NAME = "Forge Substitution Alias Test Mod";
	private Logger LOGGER;

	private BlockSnowLayerReplacement blockSnowLayerReplacement;

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		LOGGER = event.getModLog();

		blockSnowLayerReplacement = new BlockSnowLayerReplacement();
		try {
			GameRegistry.addSubstitutionAlias("minecraft:snow_layer", GameRegistry.Type.BLOCK, blockSnowLayerReplacement);
//			GameRegistry.addSubstitutionAlias("minecraft:snow_layer", GameRegistry.Type.ITEM, new ItemSnow(blockSnowLayerReplacement));
			LOGGER.info("Added substitution for minecraft:snow_layer");
		} catch (ExistingSubstitutionException e) {
			LOGGER.error("Unable to add substitution alias for minecraft:snow_layer", e);
		}

		ItemStickReplacement itemStickReplacement = new ItemStickReplacement();
		try {
			GameRegistry.addSubstitutionAlias("minecraft:stick", GameRegistry.Type.ITEM, itemStickReplacement);
		} catch (ExistingSubstitutionException e) {
			LOGGER.error("Unable to add substitution alias for minecraft:stick", e);
		}

		if (event.getSide().isClient()) {
			final Item item = Item.getItemFromBlock(blockSnowLayerReplacement);
			final ModelResourceLocation modelResourceLocation = new ModelResourceLocation(item.getRegistryName(), "layers=1,enabled=true");

			ModelBakery.registerItemVariants(item, modelResourceLocation);
			ModelLoader.setCustomMeshDefinition(item, new ItemMeshDefinition() {
				@Override
				public ModelResourceLocation getModelLocation(ItemStack stack) {
					return modelResourceLocation;
				}
			});

			final ModelResourceLocation stickModel = new ModelResourceLocation(MODID + ":" + "stick", "inventory");
			ModelLoader.setCustomModelResourceLocation(Items.stick, 0, stickModel);
			ModelLoader.setCustomModelResourceLocation(itemStickReplacement, 0, stickModel);
		}
	}

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		LOGGER.info("minecraft:snow_layer = {}", getSnowBlock().getClass());
	}

	/*
	 This is a hack and shouldn't be needed, but it does replace any occurrences of the vanilla block in the world with the replacement block.
	 The model renders properly, the F3 debug info displays the correct state and Block#onBlockActivated is called.

	 This breaks /setblock.
	  */
	@Mod.EventHandler
	public void idMapping(FMLModIdMappingEvent event) {
		ObjectIntIdentityMap<IBlockState> blockStateIDMap = GameData.getBlockStateIDMap();

		int id = GameData.getBlockRegistry().getId(Blocks.snow_layer);

		for (IBlockState state : blockSnowLayerReplacement.getBlockState().getValidStates()) {
			blockStateIDMap.put(state, id << 4 | blockSnowLayerReplacement.getMetaFromState(state));
		}

		LOGGER.info("minecraft:snow_layer = {}", getSnowBlock().getClass());
	}

	private Block getSnowBlock() {
		return GameRegistry.findBlock("minecraft", "snow_layer");
	}
}

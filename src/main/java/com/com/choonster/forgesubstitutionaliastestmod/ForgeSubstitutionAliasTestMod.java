package com.com.choonster.forgesubstitutionaliastestmod;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ObjectIntIdentityMap;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLModIdMappingEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.ExistingSubstitutionException;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameData;
import net.minecraftforge.fml.common.registry.GameRegistry;
import org.apache.logging.log4j.Logger;

@Mod(modid = ForgeSubstitutionAliasTestMod.MODID, name = ForgeSubstitutionAliasTestMod.NAME)
public class ForgeSubstitutionAliasTestMod {
	public static final String MODID = "forgesubstitutionaliastestmod";
	public static final String NAME = "Forge Substitution Alias Test Mod";

	private static final ResourceLocation SNOW_LAYER = new ResourceLocation("minecraft", "snow_layer");
	private static final ResourceLocation STICK = new ResourceLocation("minecraft", "stick");

	private Logger LOGGER;
	private BlockSnowLayerReplacement blockSnowLayerReplacement;

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		LOGGER = event.getModLog();

		blockSnowLayerReplacement = new BlockSnowLayerReplacement();
		blockSnowLayerReplacement.setRegistryName(SNOW_LAYER);
		try {
			GameRegistry.addSubstitutionAlias(SNOW_LAYER.toString(), GameRegistry.Type.BLOCK, blockSnowLayerReplacement);
			GameRegistry.addSubstitutionAlias(SNOW_LAYER.toString(), GameRegistry.Type.ITEM, new ItemSnowLayerReplacement(blockSnowLayerReplacement).setRegistryName(SNOW_LAYER));
			LOGGER.info("Added substitution for {}", SNOW_LAYER);
		} catch (ExistingSubstitutionException e) {
			LOGGER.error("Unable to add substitution alias for minecraft:snow_layer", e);
		}

		final ItemStickReplacement itemStickReplacement = new ItemStickReplacement();
		itemStickReplacement.setRegistryName(STICK);
		try {
			GameRegistry.addSubstitutionAlias(STICK.toString(), GameRegistry.Type.ITEM, itemStickReplacement);
			LOGGER.info("Added substitution for {}", STICK);
		} catch (ExistingSubstitutionException e) {
			LOGGER.error("Unable to add substitution alias for " + STICK, e);
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

			final ModelResourceLocation stickModel = new ModelResourceLocation(new ResourceLocation(MODID, "stick"), "inventory");
			ModelLoader.setCustomModelResourceLocation(itemStickReplacement, 0, stickModel);
		}
	}

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		LOGGER.info("{} (Block Registry) = {}", SNOW_LAYER, ForgeRegistries.BLOCKS.getValue(SNOW_LAYER).getClass());
		LOGGER.info("{} (Item Registry) = {}", SNOW_LAYER, ForgeRegistries.ITEMS.getValue(SNOW_LAYER).getClass());
		LOGGER.info("{} (Item.getItemFromBlock) = {}", SNOW_LAYER, Item.getItemFromBlock(blockSnowLayerReplacement).getClass());

		LOGGER.info("{} = {}", STICK, ForgeRegistries.ITEMS.getValue(STICK).getClass());
	}

	/*
	 This is a hack and shouldn't be needed, but it does replace any occurrences of the vanilla block in the world with the replacement block.
	 The model renders properly, the F3 debug info displays the correct state and Block#onBlockActivated is called.

	 /setblock still doesn't place the replacement block, but the vanilla block will be replaced with the replacement block when the world is reloaded.

	 Without this, the replacement block is removed from the world after a reload.
	  */
	@Mod.EventHandler
	public void idMapping(FMLModIdMappingEvent event) {
		ObjectIntIdentityMap<IBlockState> blockStateIDMap = GameData.getBlockStateIDMap();

		final int id = Block.blockRegistry.getIDForObject(Blocks.snow_layer);

		for (IBlockState state : blockSnowLayerReplacement.getBlockState().getValidStates()) {
			blockStateIDMap.put(state, id << 4 | blockSnowLayerReplacement.getMetaFromState(state));
		}

		LOGGER.info("{} = {}", SNOW_LAYER, ForgeRegistries.BLOCKS.getValue(SNOW_LAYER).getClass());
	}
}

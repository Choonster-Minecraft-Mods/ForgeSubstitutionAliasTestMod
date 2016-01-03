package com.com.choonster.forgesubstitutionaliastestmod;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.ExistingSubstitutionException;
import net.minecraftforge.fml.common.registry.GameRegistry;
import org.apache.logging.log4j.Logger;

@Mod(modid = ForgeSubstitutionAliasTestMod.MODID, name = ForgeSubstitutionAliasTestMod.NAME)
public class ForgeSubstitutionAliasTestMod {
	public static final String MODID = "forgesubstitutionaliastestmod";
	public static final String NAME = "Forge Substitution Alias Test Mod";
	private Logger LOGGER;

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		LOGGER = event.getModLog();

		BlockSnowLayerReplacement blockSnowLayerReplacement = new BlockSnowLayerReplacement();

		try {
			GameRegistry.addSubstitutionAlias("minecraft:snow_layer", GameRegistry.Type.BLOCK, blockSnowLayerReplacement);
//			GameRegistry.addSubstitutionAlias("minecraft:snow_layer", GameRegistry.Type.ITEM, new ItemSnow(blockSnowLayerReplacement));
			LOGGER.info("Added substitution for minecraft:snow_layer");
		} catch (ExistingSubstitutionException e) {
			LOGGER.error("Unable to add substitution alias for minecraft:snow_layer", e);
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
		}
	}

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		Block snow = GameRegistry.findBlock("minecraft", "snow_layer");
		LOGGER.info("minecraft:snow_layer = %s", snow.getClass());
	}
}

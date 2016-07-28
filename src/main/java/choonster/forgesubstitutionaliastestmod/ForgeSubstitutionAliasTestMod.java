package choonster.forgesubstitutionaliastestmod;

import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.ExistingSubstitutionException;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
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

		try {
			GameRegistry.addSubstitutionAlias(SNOW_LAYER.toString(), GameRegistry.Type.BLOCK, blockSnowLayerReplacement);
			GameRegistry.addSubstitutionAlias(SNOW_LAYER.toString(), GameRegistry.Type.ITEM, new ItemSnowLayerReplacement(blockSnowLayerReplacement));
			LOGGER.info("Added substitution for {}", SNOW_LAYER);
		} catch (ExistingSubstitutionException e) {
			LOGGER.error("Unable to add substitution alias for " + SNOW_LAYER, e);
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
			final ModelResourceLocation modelResourceLocation = new ModelResourceLocation(SNOW_LAYER, "layers=1,enabled=true");

			final Item itemSnowLayer = Item.getItemFromBlock(blockSnowLayerReplacement);
			ModelBakery.registerItemVariants(itemSnowLayer, modelResourceLocation);
			ModelLoader.setCustomMeshDefinition(itemSnowLayer, new ItemMeshDefinition() {
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
}

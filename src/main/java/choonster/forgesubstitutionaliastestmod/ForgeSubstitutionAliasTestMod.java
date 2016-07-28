package choonster.forgesubstitutionaliastestmod;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDirt;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
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

	private static final ResourceLocation DIRT = new ResourceLocation("minecraft", "dirt");

	private Logger LOGGER;
	private Block blockDirtReplacement;

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		LOGGER = event.getModLog();

		blockDirtReplacement = new BlockDirt() {
		};

		try {
			GameRegistry.addSubstitutionAlias(DIRT.toString(), GameRegistry.Type.BLOCK, blockDirtReplacement);
			LOGGER.info("Added substitution for {}", DIRT);
		} catch (ExistingSubstitutionException e) {
			LOGGER.error("Unable to add substitution alias for " + DIRT, e);
		}
	}

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		LOGGER.info("{} (Block Registry) = {}", DIRT, ForgeRegistries.BLOCKS.getValue(DIRT).getClass());
		LOGGER.info("{} (Item.getItemFromBlock) = {}", DIRT, Item.getItemFromBlock(blockDirtReplacement).getClass());
	}
}

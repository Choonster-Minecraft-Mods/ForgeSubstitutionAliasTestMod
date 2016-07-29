package choonster.forgesubstitutionaliastestmod;

import com.google.common.base.Function;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.SoundType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemMultiTexture;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.ExistingSubstitutionException;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;

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
			{
				setHardness(0.5F);
				setSoundType(SoundType.GROUND);
				setUnlocalizedName("dirt");
				setRegistryName(DIRT);
			}
		};

		try {
			GameRegistry.addSubstitutionAlias(DIRT.toString(), GameRegistry.Type.BLOCK, blockDirtReplacement);

			final ItemMultiTexture item = new ItemMultiTexture(blockDirtReplacement, blockDirtReplacement, new Function<ItemStack, String>() {
				@Nullable
				@Override
				public String apply(@Nullable ItemStack stack) {
					return BlockDirt.DirtType.byMetadata(stack.getMetadata()).getUnlocalizedName();
				}
			});

			item.setRegistryName(DIRT);
			GameRegistry.addSubstitutionAlias(DIRT.toString(), GameRegistry.Type.ITEM, item);

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

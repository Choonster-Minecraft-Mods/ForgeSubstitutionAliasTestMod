package com.com.choonster.forgesubstitutionaliastestmod;

import net.minecraft.block.BlockSnow;
import net.minecraft.block.SoundType;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class BlockSnowLayerReplacement extends BlockSnow {
	public static final IProperty<Boolean> ENABLED = PropertyBool.create("enabled");

	public BlockSnowLayerReplacement() {
		setUnlocalizedName("snowLayerReplacement");
		setHardness(0.1F);
		setStepSound(SoundType.SNOW);
		setLightOpacity(0);
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, LAYERS, ENABLED);
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		final boolean enabled = (meta & 8) != 0;

		return super.getStateFromMeta(meta).withProperty(ENABLED, enabled);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		final int enabled = state.getValue(ENABLED) ? 1 : 0;

		return super.getMetaFromState(state) | (enabled << 3);
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
		if (!worldIn.isRemote) {
			playerIn.addChatComponentMessage(new TextComponentString("This is a replacement snow layer block"));
		}

		worldIn.setBlockState(pos, state.cycleProperty(ENABLED));

		return true;
	}
}

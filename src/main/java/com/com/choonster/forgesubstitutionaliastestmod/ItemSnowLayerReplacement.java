package com.com.choonster.forgesubstitutionaliastestmod;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSnow;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemSnow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemSnowLayerReplacement extends ItemSnow {
	public ItemSnowLayerReplacement(Block block) {
		super(block);
	}

	@Override
	public boolean canPlaceBlockOnSide(World world, BlockPos pos, EnumFacing side, EntityPlayer player, ItemStack stack) {
		final IBlockState state = world.getBlockState(pos);
		return (state.getBlock() == block && state.getValue(BlockSnow.LAYERS) <= 7) || super.canPlaceBlockOnSide(world, pos, side, player, stack);
	}
}

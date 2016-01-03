package com.com.choonster.forgesubstitutionaliastestmod;

import net.minecraft.block.BlockSnow;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class BlockSnowLayerReplacement extends BlockSnow {
	public static final IProperty<Boolean> ENABLED = PropertyBool.create("enabled");

	public BlockSnowLayerReplacement() {
		setUnlocalizedName("snowLayerReplacement");
	}

	@Override
	protected BlockState createBlockState() {
		return new BlockState(this, LAYERS, ENABLED);
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		boolean enabled = (meta & 8) != 0;

		return super.getStateFromMeta(meta).withProperty(ENABLED, enabled);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		int enabled = state.getValue(ENABLED) ? 1 : 0;

		return super.getMetaFromState(state) | (enabled << 3);
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
		if (!worldIn.isRemote) {
			playerIn.addChatComponentMessage(new ChatComponentText("This is a replacement snow layer block"));
		}

		return true;
	}
}

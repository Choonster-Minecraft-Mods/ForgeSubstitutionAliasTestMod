package com.com.choonster.forgesubstitutionaliastestmod;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;

public class ItemStickReplacement extends Item {
	public ItemStickReplacement() {
		setUnlocalizedName("stickReplacement");
	}

	@Override
	public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn) {
		if (!worldIn.isRemote){
			playerIn.addChatComponentMessage(new ChatComponentText("This is a replacement stick!"));
		}

		return itemStackIn;
	}
}

package choonster.forgesubstitutionaliastestmod;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class ItemStickReplacement extends Item {
	public ItemStickReplacement() {
		setUnlocalizedName("stickReplacement");
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {
		if (!worldIn.isRemote) {
			playerIn.addChatComponentMessage(new TextComponentString("This is a replacement stick!"));
		}

		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemStackIn);
	}
}

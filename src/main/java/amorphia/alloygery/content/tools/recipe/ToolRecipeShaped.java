package amorphia.alloygery.content.tools.recipe;

import amorphia.alloygery.Alloygery;
import amorphia.alloygery.content.tools.ToolNBTHelper;
import amorphia.alloygery.content.tools.item.part.ToolBindingItem;
import amorphia.alloygery.content.tools.item.part.ToolHandleItem;
import amorphia.alloygery.content.tools.item.part.ToolHeadItem;
import amorphia.alloygery.content.tools.item.tool.IDynamicTool;
import com.google.gson.JsonObject;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.util.Identifier;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ToolRecipeShaped extends ShapedRecipe
{
	public ToolRecipeShaped(ShapedRecipe recipe)
	{
		super(recipe.getId(), recipe.getGroup(), recipe.getWidth(), recipe.getHeight(), recipe.getIngredients(), recipe.getOutput());
	}

	@Override
	public boolean isIgnoredInRecipeBook()
	{
		return true;
	}

	@Override
	public ItemStack getOutput()
	{
		final List<Ingredient> ingredients = super.getIngredients();

		ItemStack headStack = ingredients.stream().filter(ingredient -> Arrays.stream(ingredient.getMatchingStacks()).anyMatch(itemStack -> itemStack.getItem() instanceof ToolHeadItem)).findFirst().get().getMatchingStacks()[0];
		ItemStack bindingStack = ingredients.stream().filter(ingredient -> Arrays.stream(ingredient.getMatchingStacks()).anyMatch(itemStack -> itemStack.getItem() instanceof ToolBindingItem)).findFirst().get().getMatchingStacks()[0];
		ItemStack handleStack = ingredients.stream().filter(ingredient -> Arrays.stream(ingredient.getMatchingStacks()).anyMatch(itemStack -> itemStack.getItem() instanceof ToolHandleItem)).findFirst().get().getMatchingStacks()[0];

		if(headStack == null || headStack.isEmpty() || bindingStack == null || bindingStack.isEmpty() || handleStack == null || handleStack.isEmpty())
			return super.getOutput().copy();

		ItemStack toolStack = super.getOutput().copy();
		if(toolStack.getItem() instanceof IDynamicTool && headStack.getItem() instanceof ToolHeadItem headItem && bindingStack.getItem() instanceof ToolBindingItem bindingItem && handleStack.getItem() instanceof ToolHandleItem handleItem)
		{
			ToolNBTHelper.addAlloygeryNBTToToolStack(toolStack, ToolNBTHelper.createToolNBTFromToolPartItems(headItem, bindingItem, handleItem));
		}

		return toolStack;
	}

	@Override
	public ItemStack craft(CraftingInventory craftingInventory)
	{
		ItemStack headStack = null;
		ItemStack bindingStack = null;
		ItemStack handleStack = null;

		final List<Ingredient> ingredients = super.getIngredients();

		for (int slot = 0; slot < ingredients.size(); slot++)
		{
			if(Arrays.stream(ingredients.get(slot).getMatchingStacks()).anyMatch(itemStack -> itemStack.getItem() instanceof ToolHeadItem))
			{
				headStack = craftingInventory.getStack(slot);
			}

			if(Arrays.stream(ingredients.get(slot).getMatchingStacks()).anyMatch(itemStack -> itemStack.getItem() instanceof ToolBindingItem))
			{
				bindingStack = craftingInventory.getStack(slot);
			}

			if(Arrays.stream(ingredients.get(slot).getMatchingStacks()).anyMatch(itemStack -> itemStack.getItem() instanceof ToolHandleItem))
			{
				handleStack = craftingInventory.getStack(slot);
			}
		}

		if(headStack == null || headStack.isEmpty() || bindingStack == null || bindingStack.isEmpty() || handleStack == null || handleStack.isEmpty())
			return ItemStack.EMPTY;

		ItemStack toolStack = super.getOutput().copy();
		if(toolStack == null || toolStack.isEmpty())
			return ItemStack.EMPTY;

		if(toolStack.getItem() instanceof IDynamicTool && headStack.getItem() instanceof ToolHeadItem headItem && bindingStack.getItem() instanceof ToolBindingItem bindingItem && handleStack.getItem() instanceof ToolHandleItem handleItem)
		{
			ToolNBTHelper.addAlloygeryNBTToToolStack(toolStack, ToolNBTHelper.createToolNBTFromToolPartItems(headItem, bindingItem, handleItem));

			final Map<Enchantment, Integer> enchantments = EnchantmentHelper.get(headStack);
			EnchantmentHelper.set(enchantments, toolStack);
		}

		return toolStack;
	}

	public static class Type implements RecipeType<ToolRecipeShaped>
	{
		public static final Identifier ID = Alloygery.identifier("alloygery_tool_shaped");
		public static final Type INSTANCE = new Type();

		private Type(){} //no op
	}

	public static class Serializer extends ShapedRecipe.Serializer
	{
		public static final Serializer INSTANCE = new Serializer();

		private Serializer(){} //no op

		@Override
		public ToolRecipeShaped read(Identifier identifier, JsonObject jsonObject)
		{
			return new ToolRecipeShaped(super.read(identifier, jsonObject));
		}

		@Override
		public ShapedRecipe read(Identifier identifier, PacketByteBuf packetByteBuf)
		{
			return new ToolRecipeShaped(super.read(identifier, packetByteBuf));
		}
	}
}

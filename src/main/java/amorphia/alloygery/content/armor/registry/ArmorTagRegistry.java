package amorphia.alloygery.content.armor.registry;

import amorphia.alloygery.Alloygery;
import net.minecraft.item.Item;
import net.minecraft.tag.TagKey;
import net.minecraft.util.registry.Registry;

public class ArmorTagRegistry
{
	public static TagKey<Item> POWDER_WALK_BOOTS;
	public static TagKey<Item> OCCLUDES_VIBRATION_SIGNALS_WHEN_WORN;

	public static void init()
	{
		POWDER_WALK_BOOTS = TagKey.of(Registry.ITEM_KEY, Alloygery.identifier("armors/can_walk_on_powdered_snow"));
		OCCLUDES_VIBRATION_SIGNALS_WHEN_WORN = TagKey.of(Registry.ITEM_KEY, Alloygery.identifier("armors/occludes_vibration_signals_when_worn"));
	}
}

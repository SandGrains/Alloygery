package amorphia.alloygery.content.tools.item.part;

import java.util.Arrays;
import java.util.Locale;

public enum ToolPartType
{
	HEAD,
	BINDING,
	HANDLE,
	UPGRADE;

	public static ToolPartType getByName(String name)
	{
		return Arrays.stream(ToolPartType.values()).filter(value -> value.getName().equals(name.toLowerCase(Locale.ROOT)) || value.getName().equals(name.toUpperCase(Locale.ROOT))).findFirst().orElseThrow();
	}

	public String getName()
	{
		return this.name().toLowerCase(Locale.ROOT);
	}
}

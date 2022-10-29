package amorphia.alloygery.content.tools.data;

import amorphia.alloygery.Alloygery;
import amorphia.alloygery.content.tools.registry.ToolMaterialRegistry;
import com.google.gson.JsonObject;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceConditions;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ToolMaterialDataLoader implements SimpleSynchronousResourceReloadListener
{
	public static final ToolMaterialDataLoader INSTANCE = new ToolMaterialDataLoader();
	public static final Identifier ID = Alloygery.identifier("tool_material_data_loader");

	private ToolMaterialDataLoader(){} //no op

	@Override
	public Identifier getFabricId()
	{
		return ID;
	}

	@Override
	public void reload(ResourceManager manager)
	{
		for(Identifier id : manager.findResources("materials", path -> path.endsWith(".json")))
		{
			Alloygery.LOGGER.info("Reading material from datapack: " + id.toString());

			try (InputStream is = manager.getResource(id).getInputStream())
			{
				Identifier trimmedId = new Identifier(id.getNamespace(), id.getPath().substring(0, id.getPath().length() - ".json".length()));

				InputStreamReader reader = new InputStreamReader(is);
				JsonObject jsonObject = JsonHelper.deserialize(reader);
				reader.close();

				if (!ResourceConditions.objectMatchesConditions(jsonObject))
				{
					Alloygery.LOGGER.info("Load conditions not met for " + id.toString());
					continue;
				}

				ToolMaterialDataHelper.getToolMaterialDataFromJson(jsonObject).ifPresentOrElse(data -> ToolMaterialRegistry.load(trimmedId, data),
						() -> Alloygery.LOGGER.info("Could not validate resource " + id.toString() + ", it is either not an Alloygery Material, or is written using an unsupported data version."));
			}
			catch (IOException thrown)
			{
				Alloygery.LOGGER.error("Error occurred while loading material resource " + id.toString(), thrown);
			}
		}
	}
}

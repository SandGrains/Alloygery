package amorphia.alloygery.content.tools;

import amorphia.alloygery.IAlloygeryModule;
import amorphia.alloygery.content.tools.client.ToolClientReloadListener;
import amorphia.alloygery.content.tools.client.ToolPartClientReloadListener;
import amorphia.alloygery.content.tools.data.ToolMaterialDataLoader;
import amorphia.alloygery.content.tools.registry.ToolItemRegistry;
import amorphia.alloygery.content.tools.registry.ToolNetworkEventRegistry;
import amorphia.alloygery.content.tools.registry.ToolRecipeRegistry;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.resource.ResourceType;

public class ToolModule implements IAlloygeryModule
{
	@Override
	public void initialize()
	{
		ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(ToolMaterialDataLoader.INSTANCE);

		ToolNetworkEventRegistry.init();

		ToolItemRegistry.init();
		ToolRecipeRegistry.init();
	}

	@Override
	public void initializeClient()
	{
		ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(ToolClientReloadListener.INSTANCE);
		ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(ToolPartClientReloadListener.INSTANCE);

		ToolNetworkEventRegistry.initClient();
	}
}

package com.forgeessentials.compat.worldedit;

import com.forgeessentials.api.APIRegistry;
import com.forgeessentials.core.moduleLauncher.ModuleLauncher;
import com.forgeessentials.util.OutputHandler;
import com.forgeessentials.util.PlayerInfo;
import com.forgeessentials.util.events.FEModuleEvent.FEModulePostInitEvent;
import com.forgeessentials.util.events.FEModuleEvent.FEModulePreInitEvent;
import com.forgeessentials.util.events.FEModuleEvent.FEModuleServerInitEvent;
import com.forgeessentials.util.selections.SelectionHandler;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.event.platform.PlatformInitializeEvent;
import com.sk89q.worldedit.forge.ForgeWorldEdit;
import com.sk89q.worldedit.util.eventbus.Subscribe;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.event.world.WorldEvent;

public class WEIntegrationHandler
{

    @SuppressWarnings("unused")
    private CUIComms cuiComms;

    @SubscribeEvent
    public void postLoad(FEModulePostInitEvent e)
    {
        if (WEIntegration.disable)
        {
            OutputHandler.felog.severe("Requested to force-disable WorldEdit.");
            if (Loader.isModLoaded("WorldEdit"))
                //MinecraftForge.EVENT_BUS.unregister(ForgeWorldEdit.inst); //forces worldedit forge NOT to load
            ModuleLauncher.instance.unregister("WEIntegrationTools");
        }
    }

    @SubscribeEvent
    public void serverStart(FEModuleServerInitEvent e)
    {
        cuiComms = new CUIComms();
        ForgeWorldEdit.inst.setPermissionsProvider(new PermissionsHandler());
        WorldEdit.getInstance().getEventBus().register(this);
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load e)
    {
        SelectionHandler.selectionProvider = new WESelectionHandler();
    }

    @Subscribe
    public void onWEInitComplete(PlatformInitializeEvent e)
    {
        APIRegistry.perms.setDirty();
    }


}

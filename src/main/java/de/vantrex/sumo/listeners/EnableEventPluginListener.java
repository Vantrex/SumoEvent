package de.vantrex.sumo.listeners;

import de.vantrex.azure.AzurePlugin;
import de.vantrex.azure.event.EventInfo;
import de.vantrex.azure.event.EventPlugin;
import de.vantrex.azure.event.events.EnableEventPluginEvent;
import de.vantrex.azure.event.mode.ServerMode;
import de.vantrex.azure.event.utils.AzureHelper;
import de.vantrex.azure.listener.ListenerHandler;
import de.vantrex.sumo.SumoPlugin;
import de.vantrex.sumo.managers.ArenaManager;
import de.vantrex.sumo.profile.SumoProfile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;


public class EnableEventPluginListener implements Listener {

    private final SumoPlugin plugin;

    public EnableEventPluginListener(SumoPlugin plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onEnableEventPluginListener(EnableEventPluginEvent event){
        ServerMode mode = event.getMode();


        if(mode == ServerMode.EVENT){
            AzureHelper.loadCommandsFromPackage(plugin, "de.vantrex.sumo.commands.event"); // idk somehow intellij does not recognize the class CommandHandler and im to lazy to check it rn so I had to do this (other projects do recognize it tho)
            ListenerHandler.loadListenersFromPackage(plugin, "de.vantrex.sumo.listeners.event");
           // plugin.getServer().getPluginManager().registerEvents(new StartEventListener(plugin), plugin);
           // plugin.getServer().getPluginManager().registerEvents(new SumoEventListener(plugin), plugin);
            AzurePlugin.getInstance().getProfileManager().addProfileAdaption(SumoProfile.class);
        }else{
            AzureHelper.loadCommandsFromPackage(plugin, "de.vantrex.sumo.commands.setup"); // idk somehow intellij does not recognize the class CommandHandler and im to lazy to check it rn so I had to do this (other projects do recognize it tho)
            ListenerHandler.loadListenersFromPackage(plugin, "de.vantrex.sumo.listeners.setup");
        }
        this.plugin.setArenaManager(new ArenaManager(plugin));
        EventPlugin.getInstance().setCurrentEventPlugin(this.plugin);
        EventPlugin.getInstance().setEventInfo(new EventInfo("Sumo","§6§l","Sumo Event. Knock players off a platform to proceed to the next round.",2,-1));
        AzureHelper.loadCommandsFromPackage(EventPlugin.getInstance(), "de.vantrex.sumo.commands.shared"); // idk somehow intellij does not recognize the class CommandHandler and im to lazy to check it rn so I had to do this (other projects do recognize it tho)
        ListenerHandler.loadListenersFromPackage(plugin, "de.vantrex.sumo.listeners.shared");
      //  plugin.getServer().getPluginManager().registerEvents(new SumoSharedListener(plugin), plugin);


    }

}

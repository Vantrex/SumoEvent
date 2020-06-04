package de.vantrex.sumo.listeners.shared;

import de.vantrex.sumo.SumoPlugin;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;

public class SumoSharedListener implements Listener {

    private final SumoPlugin plugin;

    public SumoSharedListener(SumoPlugin plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent event){

        if(event.getEntityType() == EntityType.PLAYER || event.getEntityType() == EntityType.DROPPED_ITEM || event.getEntityType() == EntityType.SPLASH_POTION || event.getEntityType() == EntityType.FISHING_HOOK || event.getEntityType() == EntityType.ENDER_PEARL || event.getEntityType() == EntityType.SNOWBALL) return;
        event.setCancelled(true);

    }

}

package de.vantrex.sumo.listeners.event;

import de.vantrex.azure.AzurePlugin;
import de.vantrex.azure.event.events.StartEventEvent;
import de.vantrex.azure.others.SpigotType;
import de.vantrex.hardcorespigot.others.config.HardcoreSpigotConfig;
import de.vantrex.sumo.SumoEvent;
import de.vantrex.sumo.SumoPlugin;
import de.vantrex.sumo.arena.Arena;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class StartEventListener implements Listener {

    private final SumoPlugin plugin;

    public StartEventListener(SumoPlugin plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onStartEvent(StartEventEvent event){

        if(AzurePlugin.getInstance().getSpigotType() == SpigotType.HARDCORESPIGOT){
            HardcoreSpigotConfig.getInstance().SETTINGS.PERFORMANCE.WORLD.DO_CHUNK_UNLOAD = false;
        }
        Arena arena = plugin.getArenaManager().getRandomArena();

        arena.getSpawn().getChunk().load();

        Bukkit.getOnlinePlayers().forEach(player -> {
            player.getInventory().clear();
            player.setGameMode(GameMode.SURVIVAL);
            player.setFlying(false);
            player.setAllowFlight(false);
            player.getInventory().setArmorContents(null);
            player.teleport(arena.getSpawn());
        });


        new SumoEvent(plugin,arena);

    }
}

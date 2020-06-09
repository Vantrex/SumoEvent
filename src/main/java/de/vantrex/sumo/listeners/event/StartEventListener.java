package de.vantrex.sumo.listeners.event;

import de.vantrex.azure.AzurePlugin;
import de.vantrex.azure.event.EventPlugin;
import de.vantrex.azure.event.events.StartEventEvent;
import de.vantrex.azure.others.SpigotType;
import de.vantrex.hardcorespigot.HardcoreSpigot;
import de.vantrex.hardcorespigot.others.config.HardcoreSpigotConfig;
import de.vantrex.sumo.SumoEvent;
import de.vantrex.sumo.SumoPlugin;
import de.vantrex.sumo.arena.Arena;
import de.vantrex.sumo.scoreboard.ScoreboardAdapter;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
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
            HardcoreSpigot.getInstance().getKnockbackManager().setCurrentProfile(HardcoreSpigot.getInstance().getKnockbackManager().getProfiles().stream().filter(knockbackProfile -> knockbackProfile.getName().equalsIgnoreCase("kohi")).findFirst().orElse(HardcoreSpigot.getInstance().getKnockbackManager().getCurrentProfile()));
         //   HardcoreSpigot.getInstance().getKnockbackManager().getProfiles().stream().filter(knockbackProfile -> knockbackProfile.getName().equalsIgnoreCase("MineChaos")).findFirst().orElse(null).getComboMode().setValue(false);
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

        EventPlugin.getInstance().getAether().setUpdateDelay(1);
        EventPlugin.getInstance().getAether().setAdapter(new ScoreboardAdapter(plugin,EventPlugin.getInstance()));

        for(Player online : Bukkit.getOnlinePlayers()){
            EventPlugin.getInstance().getAether().forceClear(online);
            EventPlugin.getInstance().getAether().forceUpdate(online);
            AzurePlugin.getInstance().getNameTagHandler().addTeamsToScoreboard(online.getScoreboard());
            for(Player other : Bukkit.getOnlinePlayers()){
                AzurePlugin.getInstance().getNameTagHandler().addPlayerToScoreboard(online,other);
            }
        }
    }
}

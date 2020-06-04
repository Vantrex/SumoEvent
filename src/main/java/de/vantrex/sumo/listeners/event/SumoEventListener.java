package de.vantrex.sumo.listeners.event;

import de.vantrex.sumo.SumoEvent;
import de.vantrex.sumo.SumoPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCreativeEvent;
import org.bukkit.event.player.PlayerAchievementAwardedEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class SumoEventListener implements Listener {

    private final SumoPlugin plugin;

    public SumoEventListener(SumoPlugin plugin){
        this.plugin = plugin;
    }


    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event){
        Player player = event.getPlayer();
        if(plugin.getSumoEvent() != null){
            if(plugin.getSumoEvent().getFightState() == SumoEvent.FightState.START){
                if(plugin.getSumoEvent().getPlayer1() == player || plugin.getSumoEvent().getPlayer2() == player && (event.getFrom().getX() != event.getTo().getX() || event.getFrom().getZ() != event.getTo().getZ())){
                    event.setTo(event.getFrom());
                }
                return;
            }
            if((plugin.getSumoEvent().getPlayer1() == player || plugin.getSumoEvent().getPlayer2() == player) && event.getTo().getY() <= plugin.getSumoEvent().getArena().getDeathzone()){
                plugin.getSumoEvent().onFightEnd(plugin.getSumoEvent().getPlayer1() == player ? plugin.getSumoEvent().getPlayer2() : plugin.getSumoEvent().getPlayer1(), player);
            }
        }
    }

    @EventHandler
    public void onPLayerQuit(PlayerQuitEvent event){
        Player player = event.getPlayer();
        if(plugin.getSumoEvent() != null){
            if((plugin.getSumoEvent().getPlayer1() == player || plugin.getSumoEvent().getPlayer2() == player)){
                plugin.getSumoEvent().getParticipants().remove(player);
                plugin.getSumoEvent().onFightEnd(plugin.getSumoEvent().getPlayer1() == player ? plugin.getSumoEvent().getPlayer2() : plugin.getSumoEvent().getPlayer1(), null);
            }
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event){
        if(event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if (plugin.getSumoEvent() != null) {
              if(plugin.getSumoEvent().getPlayer1() == player || plugin.getSumoEvent().getPlayer2() == player){
                  event.setDamage(0);
              }else event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event){
        event.setCancelled(true);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event){
        event.setCancelled(true);
    }

    @EventHandler
    public void onAchievement(PlayerAchievementAwardedEvent event){
        event.setCancelled(true);
    }

    @EventHandler
    public void onGamemodeInventory(InventoryCreativeEvent event){
        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event){
        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event){
        event.setCancelled(true);
    }
}

package de.vantrex.sumo.listeners.event;

import de.vantrex.azure.AzurePlugin;
import de.vantrex.azure.event.EventPlugin;
import de.vantrex.azure.others.language.Language;
import de.vantrex.sumo.SumoEvent;
import de.vantrex.sumo.SumoPlugin;
import de.vantrex.sumo.profile.SumoProfile;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCreativeEvent;
import org.bukkit.event.player.*;
import org.bukkit.util.Vector;

public class SumoEventListener implements Listener {

    private final SumoPlugin plugin;

    public SumoEventListener(SumoPlugin plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event){
        if(event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK){
            Player player = event.getPlayer();

            if(plugin.getSumoEvent() != null && (plugin.getSumoEvent().getPlayer1() == player || plugin.getSumoEvent().getPlayer2() == player)){
                SumoProfile sumoProfile = (SumoProfile) AzurePlugin.getInstance().getProfileManager().getProfile(player).getAdaption(SumoProfile.class);
                /*
                if(sumoProfile.getCpsStart() <= System.currentTimeMillis() - 1000){
                    sumoProfile.setLastCPS(sumoProfile.getCurrentCPS());
                    sumoProfile.setCurrentCPS(1);
                    sumoProfile.setCpsStart(System.currentTimeMillis());
                    return;
                }
                 */
                sumoProfile.addClick();
            }
        }
    }

    private long delay = System.currentTimeMillis();

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
            if((plugin.getSumoEvent().getFightState() == SumoEvent.FightState.FIGHTING && plugin.getSumoEvent().getPlayer1() == player || plugin.getSumoEvent().getPlayer2() == player) && event.getTo().getY() <= plugin.getSumoEvent().getArena().getDeathzone()){
                if(delay > System.currentTimeMillis()) return;

                plugin.getSumoEvent().setTime(3 * 20);
                plugin.getSumoEvent().setFightState(SumoEvent.FightState.END);
                final String playerName = player.getName();

                Bukkit.getOnlinePlayers().forEach(online -> online.sendMessage(AzurePlugin.getInstance().getProfileManager().getProfile(online).getProfileData().getLanguage().get("message-game-win").replace("%winner%", plugin.getSumoEvent().getPlayer1() == player ? plugin.getSumoEvent().getPlayer2().getName() : plugin.getSumoEvent().getPlayer1().getName()).replace("%loser%", playerName)));
                delay = System.currentTimeMillis() + 5000;
                plugin.getSumoEvent().setCurrentFightWinner(plugin.getSumoEvent().getPlayer1() == player ? plugin.getSumoEvent().getPlayer2() : plugin.getSumoEvent().getPlayer1());
                plugin.getSumoEvent().setCurrentFightLoser(player);
                Vector v = player.getLocation().clone().getDirection().setX(0).setY(0).setY(1.7D).multiply(1.1F);
                player.setVelocity(v);
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerQuit(PlayerQuitEvent event){
        Player player = event.getPlayer();
        if(plugin.getSumoEvent() != null){
            plugin.getSumoEvent().getParticipants().remove(player);
            plugin.getSumoEvent().getSpectators().remove(player);
            if((plugin.getSumoEvent().getPlayer1() == player || plugin.getSumoEvent().getPlayer2() == player)){
                SumoProfile loserProfile = (SumoProfile) AzurePlugin.getInstance().getProfileManager().getProfile(player).getAdaption(SumoProfile.class);
                loserProfile.addDeath();
                plugin.getSumoEvent().setFightState(SumoEvent.FightState.END);
                final String playerName = player.getName();
                Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                    Bukkit.getOnlinePlayers().forEach(online -> {
                        Language language = AzurePlugin.getInstance().getProfileManager().getProfile(online).getProfileData().getLanguage();
                        online.sendMessage(language.get("message-logout").replace("%player%",playerName));
                        online.sendMessage(language.get("message-game-win").replace("%winner%", plugin.getSumoEvent().getPlayer1() == player ? plugin.getSumoEvent().getPlayer2().getName() : plugin.getSumoEvent().getPlayer1().getName()).replace("%loser%", playerName));
                    });
                });
                plugin.getSumoEvent().onFightEnd(plugin.getSumoEvent().getPlayer1() == player ? plugin.getSumoEvent().getPlayer2() : plugin.getSumoEvent().getPlayer1(), null);
            }
        }
    }

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent event){
        event.setFoodLevel(20);
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event){
        if(event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if (plugin.getSumoEvent() != null) {
              if((plugin.getSumoEvent().getPlayer1() == player || plugin.getSumoEvent().getPlayer2() == player) && plugin.getSumoEvent().getFightState() == SumoEvent.FightState.FIGHTING){
                  event.setDamage(0);
                  return;
              }
            }
            event.setCancelled(true);
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

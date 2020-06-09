package de.vantrex.sumo;

import de.vantrex.azure.AzurePlugin;
import de.vantrex.azure.utils.StringDefaults;
import de.vantrex.sumo.arena.Arena;
import de.vantrex.sumo.profile.SumoProfile;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

@Getter
public class SumoEvent {

    public final static String PREFIX = /*"§6§lSUMO §8§l┃ §b" */ StringDefaults.SHORT_PREFIX ;

    private final static Random RANDOM = new Random();

    private Arena arena;

    private final SumoPlugin plugin;

    private Player player1, player2, winner;

    private BukkitTask sumoTask = null;

    @Setter private FightState fightState;

    @Setter private Player currentFightWinner, currentFightLoser;

    private int allPlayers = 0;

    private final List<Player> participants = new ArrayList<>();
    private final Set<Player> spectators = new HashSet<>();
    @Setter long time = 5;

    public SumoEvent(SumoPlugin plugin, Arena arena){
        this.arena = arena;
        this.allPlayers = Bukkit.getOnlinePlayers().size();
        this.plugin = plugin;
        this.winner = null;
        this.player1 = null;
        this.player2 = null;
        this.plugin.setSumoEvent(this);
        sumoTask = Bukkit.getScheduler().runTaskTimer(plugin, this::nextFightTick,20, 20);
        participants.addAll(Bukkit.getOnlinePlayers());
        Bukkit.getScheduler().runTaskLater(plugin, () -> { // some delay here so we don´t spam
            AzurePlugin.getInstance().broadcast(PREFIX,"message-waiting-for-new-game");
        },30);
    }

    private void nextFightTick(){

        time--;
        Bukkit.getOnlinePlayers().forEach(player -> player.setLevel(Integer.parseInt(String.valueOf(time))));
        if(time == 0){
            sumoTask.cancel();

            player1 = participants.get(RANDOM.nextInt(participants.size()));
            if(participants.size() < 2){
                onFightEnd(player1, null);
                return;
            }
            player2 = participants.get(RANDOM.nextInt(participants.size()));
            while (player2 == player1){
                player2 = participants.get(RANDOM.nextInt(participants.size()));
            }
            player1.teleport(arena.getLoc1());
            player2.teleport(arena.getLoc2());
            Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
               Bukkit.getOnlinePlayers().forEach(player -> player.sendMessage(PREFIX + AzurePlugin.getInstance().getProfileManager().getProfile(player).getProfileData().getLanguage().get("message-game-start-soon").replace("%player1%", player1.getName()).replace("%player2%", player2.getName())));
            });
            startFightTask();

        }
    }

    public void onFightEnd(Player winner, Player loser){
        sumoTask.cancel();
        sumoTask = null;
        player1 = null;
        player2 = null;
        Bukkit.getScheduler().runTask(plugin, () -> {
            if(loser != null){
                loser.setGameMode(GameMode.CREATIVE);
                loser.setFlying(true);
                loser.setAllowFlight(true);
                SumoProfile loserProfile = (SumoProfile) AzurePlugin.getInstance().getProfileManager().getProfile(loser).getAdaption(SumoProfile.class);
                loserProfile.addDeath();
                participants.remove(loser);
                spectators.add(loser);
                loser.teleport(arena.getSpectate());
                for(Player participant : participants)
                    participant.hidePlayer(loser);

                participants.forEach(player -> loser.showPlayer(player));
                spectators.forEach(player -> loser.showPlayer(player));
            }
            winner.teleport(arena.getSpawn());
            SumoProfile winnerProfile = (SumoProfile) AzurePlugin.getInstance().getProfileManager().getProfile(winner).getAdaption(SumoProfile.class);
            winnerProfile.addKill();
            if(participants.size() > 1){
                Bukkit.getScheduler().runTaskLater(plugin, () -> { // some delay here so we don´t spam
                    AzurePlugin.getInstance().broadcast(PREFIX,"message-waiting-for-new-game");
                },30);
                startNextFight();
            }else if(participants.size() == 1){
                this.winner = winner;
                // WINNER
                Bukkit.getOnlinePlayers().forEach(player -> player.sendMessage(PREFIX + AzurePlugin.getInstance().getProfileManager().getProfile(player).getProfileData().getLanguage().get("message-sumo-win").replace("%player%", winnerProfile.getProfile().getProfileData().getLastKnownName())));
                winnerProfile.addWin();
                Bukkit.getScheduler().runTaskLater(this.plugin, () -> {
                    Bukkit.getOnlinePlayers().forEach(player -> player.kickPlayer(""));
                }, 20 * 9);
                Bukkit.getScheduler().runTaskLater(this.plugin, Bukkit::shutdown, 20 * 12);
            }else{
                // IDK WHAT HAPPENED HERE BUT WE DO NOT HAVE A WINNER I REPEAT, WE DO NOT HAVE A WINNER
                Bukkit.getScheduler().runTaskLater(this.plugin, () -> {
                    Bukkit.getOnlinePlayers().forEach(player -> player.kickPlayer(""));
                }, 20 * 4);
                Bukkit.getScheduler().runTaskLater(this.plugin, Bukkit::shutdown, 20 * 5);
            }
        });




    }

    private void fightTick(){
        if(this.fightState == FightState.END){
            if(time == 0){
                onFightEnd(currentFightWinner,currentFightLoser);
                return;
            }
            time--;
            return;
        }
        if(this.fightState == FightState.START){
            if(time == 0){
                Bukkit.getOnlinePlayers().forEach(player -> player.sendMessage(PREFIX + AzurePlugin.getInstance().getProfileManager().getProfile(player).getProfileData().getLanguage().get("message-game-starting").replace("%player1%", player1.getName()).replace("%player2%", player2.getName())));
                this.fightState = FightState.FIGHTING;
            }else {
                time--;
                return;
            }
        }
        time++;
    }

    private void startNextFight(){
        currentFightLoser = null;
        currentFightWinner = null;
        time = 5;
        sumoTask = Bukkit.getScheduler().runTaskTimer(plugin, this::nextFightTick,20, 20);

    }

    private void startFightTask(){
        time = 3 * 20;
        this.fightState = FightState.START;
        sumoTask = Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, this::fightTick,10, 1);
    }

    public enum FightState{
        START,
        FIGHTING,
        END
    }

}

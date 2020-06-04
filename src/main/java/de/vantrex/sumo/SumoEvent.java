package de.vantrex.sumo;

import de.vantrex.sumo.arena.Arena;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.omg.CORBA.ARG_IN;

import java.util.*;

@Getter
public class SumoEvent {

    private final static Random RANDOM = new Random();

    private Arena arena;

    private final SumoPlugin plugin;

    private Player player1, player2;

    private BukkitTask sumoTask = null;

    private FightState fightState;

    private final List<Player> participants = new ArrayList<>();
    private final Set<Player> spectators = new HashSet<>();
    int time = 5;

    public SumoEvent(SumoPlugin plugin, Arena arena){
        this.arena = arena;
        this.plugin = plugin;
        this.plugin.setSumoEvent(this);
        sumoTask = Bukkit.getScheduler().runTaskTimer(plugin, this::nextFightTick,20, 20);
        participants.addAll(Bukkit.getOnlinePlayers());
    }

    private void nextFightTick(){

        time--;
        Bukkit.getOnlinePlayers().forEach(player -> player.setLevel(time));
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
            Bukkit.broadcastMessage("player 1 is " + player1.getName());
            Bukkit.broadcastMessage("player 2 is " + player2.getName());
            player1.teleport(arena.getLoc1());
            player2.teleport(arena.getLoc2());
            startFightTask();
        }
    }

    public void onFightEnd(Player winner, Player loser){
        sumoTask.cancel();
        sumoTask = null;
        player1 = null;
        player2 = null;
        if(loser != null){
            loser.setGameMode(GameMode.CREATIVE);
            loser.setFlying(true);
            loser.setAllowFlight(true);
            participants.remove(loser);
            spectators.add(loser);
            loser.teleport(arena.getSpectate());
            for(Player participant : participants)
                participant.hidePlayer(loser);
            participants.forEach(player -> loser.showPlayer(player));
        }
        winner.teleport(arena.getSpawn());
        if(participants.size() > 1){
            startNextFight();
        }else if(participants.size() == 1){
            // WINNER
            Bukkit.broadcastMessage(winner.getName() + " hat gewonnen!");
            Bukkit.getScheduler().runTaskLater(this.plugin, Bukkit::shutdown, 20 * 10);
        }else{
            // IDK WHAT HAPPENED HERE BUT WE DO NOT HAVE A WINNER I REPEAT, WE DO NOT HAVE A WINNER
            Bukkit.broadcastMessage("Gewinner konnte nicht ermittelt werden!");
            Bukkit.getScheduler().runTaskLater(this.plugin, Bukkit::shutdown, 20 * 5);
        }


    }

    private void fightTick(){
        if(this.fightState == FightState.START){
            if(time == 0){
                this.fightState = FightState.FIGHTING;
            }else {
                time--;
                return;
            }
        }
        time++;
    }

    private void startNextFight(){
        time = 5;
        sumoTask = Bukkit.getScheduler().runTaskTimer(plugin, this::nextFightTick,20, 20);

    }

    private void startFightTask(){
        time = 3;
        this.fightState = FightState.START;
        sumoTask = Bukkit.getScheduler().runTaskTimer(plugin, this::fightTick,10, 1);
    }

    public enum FightState{
        START,
        FIGHTING
    }

}

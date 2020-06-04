package de.vantrex.sumo.managers;

import de.vantrex.azure.files.FileBase;
import de.vantrex.azure.utils.Util;
import de.vantrex.sumo.SumoPlugin;
import de.vantrex.sumo.arena.Arena;
import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.*;

public class ArenaManager extends FileBase {
    @Getter private final Set<Arena> arenas = new HashSet<>();

    private final SumoPlugin plugin;

    public ArenaManager(SumoPlugin plugin) {
        super(plugin,"","arena.yml");
        this.plugin = plugin;
        load();
    }

    public void addArena(Arena arena){
        if(arenas.contains(arena)) return;
        arenas.add(arena);
    }

    @SneakyThrows
    public void removeArena(Arena arena){
        arenas.remove(arena);
        FileConfiguration cfg = getConfig();
        cfg.set("arena." + arena, null);
        cfg.save(getFile());
    }

    public Arena getArena(String name){
        return this.arenas.stream().filter(arena -> arena.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    private void load(){
        FileConfiguration cfg = getConfig();

        if(cfg.getConfigurationSection("arena") == null) return;
        for(String arenaName : cfg.getConfigurationSection("arena").getKeys(false)){
            if(arenas.stream().anyMatch(arena -> arena.getName().equalsIgnoreCase(arenaName))) continue;
            String arenaString = "arena." + arenaName;
            int deathzone = 0;

            Location spawn = null;
            Location spectate = null;
            Location loc1 = null;
            Location loc2 = null;
            if(cfg.get(arenaString + ".deathzone") != null)
                deathzone = cfg.getInt(arenaString + ".deathzone");
            if(cfg.get(arenaString + ".spawn") != null)
                spawn = Util.stringToLocation(cfg.getString(arenaString + ".spawn"));
            if(cfg.get(arenaString + ".spectate") != null)
                spectate = Util.stringToLocation(cfg.getString(arenaString + ".spectate"));
            if(cfg.get(arenaString + ".loc1") != null)
                loc1 = Util.stringToLocation(cfg.getString(arenaString + ".loc1"));
            if(cfg.get(arenaString + ".loc2") != null)
                loc2 = Util.stringToLocation(cfg.getString(arenaString + ".loc2"));

            Arena arena = new Arena(arenaName);
            arena.setDeathzone(deathzone);
            arena.setLoc1(loc1);
            arena.setLoc2(loc2);
            arena.setSpectate(spectate);
            arena.setSpawn(spawn);
            arenas.add(arena);
        }
    }

    public Arena getRandomArena(){
        Random random = new Random();
        List<Arena> arenaList = new ArrayList<>(arenas);
        return arenaList.get(random.nextInt(arenaList.size()));
    }

    @SneakyThrows
    public void save(){
        FileConfiguration cfg = getConfig();

        for(Arena arena : this.arenas){

            String arenaString = "arena." + arena.getName();
            cfg.set(arenaString + ".loc1",arena.getLoc1() == null ? null :  Util.locationToString(arena.getLoc1()));
            cfg.set(arenaString + ".loc2", arena.getLoc2() == null ? null : Util.locationToString(arena.getLoc2()));
            cfg.set(arenaString + ".spawn", arena.getSpawn() == null ? null : Util.locationToString(arena.getSpawn()));
            cfg.set(arenaString + ".spectate", arena.getSpectate() == null ? null : Util.locationToString(arena.getSpectate()));
            cfg.set(arenaString + ".deathzone", arena.getDeathzone());
        }
        cfg.save(getFile());
    }


}

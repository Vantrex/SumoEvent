package de.vantrex.sumo.commands.setup;

import de.vantrex.azure.AzurePlugin;
import de.vantrex.azure.command.Command;
import de.vantrex.azure.command.parameter.Parameter;
import de.vantrex.azure.others.language.Language;
import de.vantrex.azure.utils.StringDefaults;
import de.vantrex.sumo.SumoPlugin;
import de.vantrex.sumo.arena.Arena;
import org.bukkit.entity.Player;

public class ArenaCommands {

    private final SumoPlugin plugin;

    public ArenaCommands(SumoPlugin plugin){
        this.plugin = plugin;
    }

    @Command(names = "arena", permissionNode = "sumo.command.arena")
    public void arenaCommand(Player player){

        player.sendMessage(StringDefaults.getHeader("§c§l","Sumo"));
        player.sendMessage("");
        player.sendMessage(StringDefaults.SHORT_PREFIX + "§7/arena §acreate §7<arena>");
        player.sendMessage(StringDefaults.SHORT_PREFIX + "§7/arena §cdelete §7<arena>");
        player.sendMessage(StringDefaults.SHORT_PREFIX + "§7/arena §esetSpawn §7<arena>");
        player.sendMessage(StringDefaults.SHORT_PREFIX + "§7/arena §esetSpectate §7<arena>");
        player.sendMessage(StringDefaults.SHORT_PREFIX + "§7/arena §esetLoc1 §7<arena>");
        player.sendMessage(StringDefaults.SHORT_PREFIX + "§7/arena §esetLoc2 §7<arena>");
        player.sendMessage(StringDefaults.SHORT_PREFIX + "§7/arena §6teleport §7<arena>");
        player.sendMessage(StringDefaults.SHORT_PREFIX + "§7/arena §cdeathzone §7<arena>");
        player.sendMessage(StringDefaults.SHORT_PREFIX + "§7/arena §blist");
        player.sendMessage(" ");
        player.sendMessage(StringDefaults.getFooter("Sumo"));

    }

    @Command(names = "arena deathzone", permissionNode = "sumo.command.arena")
    public void setArenaDeathzoneCommand(Player player, @Parameter(name = "arena") String arenaName){
        Language language = AzurePlugin.getInstance().getProfileManager().getProfile(player).getProfileData().getLanguage();
        Arena arena;
        if((arena = plugin.getArenaManager().getArena(arenaName)) == null){
            player.sendMessage(StringDefaults.SHORT_PREFIX + language.get("arena-not-valid"));
            return;
        }

        if(arena.getSpawn() == null){
            player.sendMessage(StringDefaults.SHORT_PREFIX + language.get("arena-spawn-not-set"));
            return;
        }
        arena.setDeathzone(player.getLocation().getBlockY());
        player.sendMessage(StringDefaults.SHORT_PREFIX + language.get("arena-deathzone-set").replace("%arena%",arena.getName()));
        plugin.getArenaManager().save();
    }

    @Command(names = "arena teleport", permissionNode = "sumo.command.arena")
    public void teleportToArenaCommand(Player player, @Parameter(name = "arena") String arenaName){
        Language language = AzurePlugin.getInstance().getProfileManager().getProfile(player).getProfileData().getLanguage();
        Arena arena;
        if((arena = plugin.getArenaManager().getArena(arenaName)) == null){
            player.sendMessage(StringDefaults.SHORT_PREFIX + language.get("arena-not-valid"));
            return;
        }

        if(arena.getSpawn() == null){
            player.sendMessage(StringDefaults.SHORT_PREFIX + language.get("arena-spawn-not-set"));
            return;
        }
        player.teleport(arena.getSpawn());
    }

    @Command(names = "arena list", permissionNode = "sumo.command.arena")
    public void arenaListCommand(Player player){
        player.sendMessage(StringDefaults.getHeader("§c§l","Sumo"));
        player.sendMessage(" ");
        player.sendMessage(" §7Arenas:");
        for(Arena arena : plugin.getArenaManager().getArenas()){
            player.sendMessage(StringDefaults.SHORT_PREFIX + "§7" + arena.getName());
        }
        player.sendMessage(" ");
        player.sendMessage(StringDefaults.getFooter("Sumo"));
    }

    @Command(names = "arena setSpawn", permissionNode = "sumo.command.arena")
    public void setArenaSpawnCommand(Player player, @Parameter(name = "arena") String arenaName){
        Language language = AzurePlugin.getInstance().getProfileManager().getProfile(player).getProfileData().getLanguage();

        Arena arena;
        if((arena = plugin.getArenaManager().getArena(arenaName)) == null){
            player.sendMessage(StringDefaults.SHORT_PREFIX + language.get("arena-not-valid"));
            return;
        }

        arena.setSpawn(player.getLocation().clone());
        player.sendMessage(StringDefaults.SHORT_PREFIX + language.get("arena-spawn-set").replace("%arena%",arena.getName()));
        plugin.getArenaManager().save();
    }

    @Command(names = "arena setSpectate", permissionNode = "sumo.command.arena")
    public void setArenaSpectateCommand(Player player, @Parameter(name = "arena") String arenaName){
        Language language = AzurePlugin.getInstance().getProfileManager().getProfile(player).getProfileData().getLanguage();

        Arena arena;
        if((arena = plugin.getArenaManager().getArena(arenaName)) == null){
            player.sendMessage(StringDefaults.SHORT_PREFIX + language.get("arena-not-valid"));
            return;
        }

        arena.setSpectate(player.getLocation().clone());
        player.sendMessage(StringDefaults.SHORT_PREFIX + language.get("arena-spectate-set").replace("%arena%",arena.getName()));
        plugin.getArenaManager().save();
    }

    @Command(names = "arena setLoc1", permissionNode = "sumo.command.arena")
    public void setArenaLoc1Command(Player player, @Parameter(name = "arena") String arenaName){
        Language language = AzurePlugin.getInstance().getProfileManager().getProfile(player).getProfileData().getLanguage();

        Arena arena;
        if((arena = plugin.getArenaManager().getArena(arenaName)) == null){
            player.sendMessage(StringDefaults.SHORT_PREFIX + language.get("arena-not-valid"));
            return;
        }

        arena.setLoc1(player.getLocation().clone());
        player.sendMessage(StringDefaults.SHORT_PREFIX + language.get("arena-loc1-set").replace("%arena%",arena.getName()));
        plugin.getArenaManager().save();
    }


    @Command(names = "arena setLoc2", permissionNode = "sumo.command.arena")
    public void setArenaLoc2Command(Player player, @Parameter(name = "arena") String arenaName){
        Language language = AzurePlugin.getInstance().getProfileManager().getProfile(player).getProfileData().getLanguage();

        Arena arena;
        if((arena = plugin.getArenaManager().getArena(arenaName)) == null){
            player.sendMessage(StringDefaults.SHORT_PREFIX + language.get("arena-not-valid"));
            return;
        }

        arena.setLoc2(player.getLocation().clone());
        player.sendMessage(StringDefaults.SHORT_PREFIX + language.get("arena-loc2-set").replace("%arena%",arena.getName()));
        plugin.getArenaManager().save();
    }


    @Command(names = "arena create", permissionNode = "sumo.command.arena")
    public void createArenaCommand(Player player, @Parameter(name = "arena") String arenaName){

        Language language = AzurePlugin.getInstance().getProfileManager().getProfile(player).getProfileData().getLanguage();
        if(plugin.getArenaManager().getArena(arenaName) != null){
            player.sendMessage(StringDefaults.SHORT_PREFIX + language.get("already-arena-with-name"));
            return;
        }

        Arena arena = new Arena(arenaName);
        plugin.getArenaManager().addArena(arena);
        player.sendMessage(StringDefaults.SHORT_PREFIX + language.get("arena-created").replace("%arena%",arena.getName()));
        plugin.getArenaManager().save(); // saving @ here since we just´ve created a new arena.
    }

    @Command(names = "arena delete", permissionNode = "sumo.command.arena")
    public void deleteArenaCommand(Player player, @Parameter(name = "arena") String arenaName){
        Language language = AzurePlugin.getInstance().getProfileManager().getProfile(player).getProfileData().getLanguage();

        Arena arena;
        if((arena = plugin.getArenaManager().getArena(arenaName)) == null){
            player.sendMessage(StringDefaults.SHORT_PREFIX + language.get("arena-not-valid"));
            return;
        }
        player.sendMessage(StringDefaults.SHORT_PREFIX + language.get("arena-deleted").replace("%arena%", arena.getName()));
        plugin.getArenaManager().removeArena(arena);
    }
}
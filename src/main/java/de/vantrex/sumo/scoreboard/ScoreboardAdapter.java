package de.vantrex.sumo.scoreboard;

import com.bizarrealex.aether.scoreboard.Board;
import com.bizarrealex.aether.scoreboard.BoardAdapter;
import com.bizarrealex.aether.scoreboard.SidebarEntry;
import com.bizarrealex.aether.scoreboard.cooldown.BoardCooldown;
import com.google.common.collect.Lists;
import de.vantrex.azure.AzurePlugin;
import de.vantrex.azure.event.EventPlugin;
import de.vantrex.azure.others.language.Language;
import de.vantrex.azure.others.profile.Profile;
import de.vantrex.sumo.SumoEvent;
import de.vantrex.sumo.SumoPlugin;
import de.vantrex.sumo.profile.SumoProfile;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ScoreboardAdapter implements BoardAdapter {

    private final EventPlugin eventPlugin;
    private final SumoPlugin plugin;
    private final SumoEvent sumoEvent;

    protected static final String STRAIGHT_LINE = "§m-----------------------------------".substring(0, 12);

    public ScoreboardAdapter(SumoPlugin plugin, EventPlugin eventPlugin){
        this.plugin = plugin;
        this.sumoEvent = plugin.getSumoEvent();
        this.eventPlugin = eventPlugin;
    }

    @Override
    public String getTitle(Player player) {
        return "§6§lSumo";
    }

    @Override
    public List<String> getScoreboard(Player player, Board board, Set<BoardCooldown> set) {
        List<SidebarEntry> lines = new ArrayList<>();

        Language language = AzurePlugin.getInstance().getProfileManager().getProfile(player).getProfileData().getLanguage();

        lines.add(lines.size(), new SidebarEntry(ChatColor.GRAY, STRAIGHT_LINE, STRAIGHT_LINE + "-"));
        lines.add(lines.size(),new SidebarEntry(""));

        lines.add(lines.size(), new SidebarEntry(language.get("scoreboard-players").replace("%players%",sumoEvent.getParticipants().size() + "/" + sumoEvent.getAllPlayers())));


        if(plugin.getSumoEvent().getWinner() != null){
            lines.remove(lines.size() - 1);
            lines.add(lines.size(), new SidebarEntry(language.get("scoreboard-winner").replace("%player%",sumoEvent.getWinner().getName())));
        }else if(sumoEvent.getPlayer1() == null){
            lines.add(lines.size(),new SidebarEntry(""));
            lines.add(lines.size(), new SidebarEntry(language.get("scoreboard-waiting-for-match")));
        }else if(sumoEvent.getCurrentFightWinner() != null){
            lines.add(lines.size(), new SidebarEntry(language.get("scoreboard-winner").replace("%player%",sumoEvent.getCurrentFightWinner().getName())));
            lines.add(lines.size(), new SidebarEntry(""));
            lines.add(lines.size(), new SidebarEntry(language.get("scoreboard-waiting-for-match")));
        }else if(sumoEvent.getPlayer1() != null){
            lines.add(lines.size(), new SidebarEntry("§6" + sumoEvent.getPlayer1().getName() + " §fvs. §6" + sumoEvent.getPlayer2().getName()));
            Profile profile1 = AzurePlugin.getInstance().getProfileManager().getProfile(sumoEvent.getPlayer1());
            Profile profile2 = AzurePlugin.getInstance().getProfileManager().getProfile(sumoEvent.getPlayer2());
            SumoProfile sumoProfile1 = (SumoProfile) profile1.getAdaption(SumoProfile.class);
            SumoProfile sumoProfile2 = (SumoProfile) profile2.getAdaption(SumoProfile.class);
            lines.add(lines.size(), new SidebarEntry(""));
            lines.add(lines.size(), new SidebarEntry("§6" + sumoProfile1.getClicks() + " CPS §fvs §6" + sumoProfile2.getClicks() + " CPS"));
            lines.add(lines.size(), new SidebarEntry("§6" + sumoEvent.getPlayer1().spigot().getPing() + " ms §fvs §6" + sumoEvent.getPlayer2().spigot().getPing() + " ms"));
        }
        lines.add(lines.size(),new SidebarEntry(""));
        lines.add(lines.size(), new SidebarEntry("§dAspireMC.de"));
        lines.add(lines.size(), new SidebarEntry(ChatColor.GRAY, STRAIGHT_LINE, STRAIGHT_LINE + "-"));
        List<String> translated = Lists.newArrayList();
        for(SidebarEntry sidebarEntry : lines){
            add(translated , sidebarEntry);
        }
        return translated;
    }

    private void add(List<String> list , SidebarEntry sidebarEntry){
        list.add(sidebarEntry.prefix + sidebarEntry.name + sidebarEntry.suffix);
    }
}

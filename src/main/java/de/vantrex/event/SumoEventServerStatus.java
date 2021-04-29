package de.vantrex.event;

import de.vantrex.azure.event.EventPlugin;
import de.vantrex.azure.event.state.EventState;
import de.vantrex.azure.event.status.EventServerStatus;
import de.vantrex.sumo.SumoPlugin;
import org.bukkit.Bukkit;

public class SumoEventServerStatus implements EventServerStatus {
    @Override
    public int getMaxUsers() {
        return EventPlugin.getInstance().getEventInfo() == null ? 255 : EventPlugin.getInstance().getEventInfo().getMaxPlayers();
    }

    @Override
    public int getUsersInEvent() {
        return SumoPlugin.getInstance().getSumoEvent() == null ? Bukkit.getOnlinePlayers().size() : SumoPlugin.getInstance().getSumoEvent().getParticipants().size();
    }

    @Override
    public EventState getEventState() {
        return EventPlugin.getInstance().getEventState();
    }
}

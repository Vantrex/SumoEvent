package de.vantrex.sumo.arena;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.Location;

@RequiredArgsConstructor
@Setter
@Getter
public class Arena {

    private final String name;

    private Location spawn;
    private Location spectate;
    private Location loc1;
    private Location loc2;

    private int deathzone;


}

package de.vantrex.sumo.knockback;

import de.vantrex.hardcorespigot.profiles.knockback.template.KnockbackTemplate;
import de.vantrex.hardcorespigot.profiles.knockback.type.KnockbackType;
import de.vantrex.hardcorespigot.profiles.knockback.value.KnockbackValue;

public class MineChaos2Template extends KnockbackTemplate {
    public MineChaos2Template(String name) {
        super(name);

        setOnePointOneKnockback(new KnockbackValue<>("onePointOneKB","1.1 Knockback", Boolean.class,true));
        setLimitVertical(new KnockbackValue<>("limit_vertical","Vertical Limit", Boolean.class, true));
        setyLimit(new KnockbackValue<>("ylimit","Vertical Limit", Double.class, 1.2));

        setHorizontal(new KnockbackValue<>("horizontal","Horizontal Knockback",Double.class, 0.45));
        setVertical(new KnockbackValue<>("vertical","Vertical Knockback",Double.class, 0.36));

        setFriction(new KnockbackValue<>("friction","Friction", Boolean.class,true));
        setFrictionHorizontal(new KnockbackValue<>("frictionHorizontal","Horizontal Friction",Double.class,0.91));
        setFrictionVertical(new KnockbackValue<>("frictionVertical","Vertical Friction", Double.class, 1.0));
        setFrictionValue(new KnockbackValue<>("frictionValue","Friction Value",Double.class, 8.0));
        setGroundHorizontal(new KnockbackValue<>("groundHorizontal", "Ground horizontal Knockback",Double.class,0.83));
        setGroundVertical(new KnockbackValue<>("groundVertical", "Ground vertical Knockback",Double.class,0.9));
        setSprintHorizontal(new KnockbackValue<>("sprintHorizontal","Sprint Horizontal",Double.class,0.83));
        setSprintVertical(new KnockbackValue<>("sprintVertical","Sprint Vertical",Double.class,0.89));
        setHitDelay(new KnockbackValue<>("hitDelay","Hit Delay",Integer.class,20));
        setSprintReset(new KnockbackValue<>("sprintReset","Sprint Reset",Boolean.class,true));
        setType(new KnockbackValue<>("type","Knockback Type", KnockbackType.class,KnockbackType.CUSTOM_MODIFIED));
        setComboMode(new KnockbackValue<>("comboMode","Combo Mode",Boolean.class,false));

        setSlowdown(new KnockbackValue<>("slowdown","Sprint Slowdown (AutoWTAP)",Double.class,1D));
        setInheritHorizontal(new KnockbackValue<>("inheritHorizontal","Inherit Horizontal",Boolean.class,true));
        setInheritVertical(new KnockbackValue<>("inheritVertical","Inherit Vertical",Boolean.class,false));
        setComboHeight(new KnockbackValue<>("comboHeight","Combo Height",Double.class,2.5));
        setComboTicks(new KnockbackValue<>("comboTicks","Combo Ticks",Integer.class,10));
        setComboVelocity(new KnockbackValue<>("comboVelocity","Combo Velocity", Double.class,-0.05));


        setCustomBowKnockback(new KnockbackValue<>("customBowKnockback","Custom Bow Knockback",Boolean.class,false));
        setBowHorizontal(new KnockbackValue<>("bowHorizontal","Bow Horizontal",Double.class,1.2));
        setBowVertical(new KnockbackValue<>("bowVertical","Bow Vertical",Double.class, 1.0));

        setCustomRodKnockback(new KnockbackValue<>("customRodKnockback","Custom Rod Knockback",Boolean.class,false));
        setRodHorizontal(new KnockbackValue<>("rodHorizontal","Rod Horizontal",Double.class,1.2));
        setRodVertical(new KnockbackValue<>("rodVertical","Rod Vertical",Double.class,1.0));

    }
}

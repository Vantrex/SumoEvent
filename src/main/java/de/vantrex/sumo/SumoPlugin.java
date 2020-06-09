package de.vantrex.sumo;

import de.vantrex.azure.AzurePlugin;
import de.vantrex.azure.event.mode.ServerMode;
import de.vantrex.azure.listener.ListenerHandler;
import de.vantrex.azure.manager.LanguageManager;
import de.vantrex.sumo.listeners.EnableEventPluginListener;
import de.vantrex.sumo.managers.ArenaManager;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.FileUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;
import java.util.Properties;

@Getter @Setter
public class SumoPlugin extends JavaPlugin {

    @Getter private static SumoPlugin instance;

    private ServerMode mode = null;
    private ArenaManager arenaManager;

    private SumoEvent sumoEvent;

    @Override
    public void onLoad() {
        instance = this;
    }

    @Override
    public void onEnable() {
        loadMessages("de_DE");
        loadMessages("en_EN");

        getServer().getPluginManager().registerEvents(new EnableEventPluginListener(this), this);


        Bukkit.getScheduler().runTaskLater(this, () -> {
            Bukkit.getWorlds().forEach(world -> {
                disableGameRules(world,
                        "doDaylightCycle",
                        "doEntityDrops",
                        "doMobSpawning",
                        "doFireTick",
                        "showDeathMessages"
                );

                world.getEntities().forEach(entity -> {
                    if(entity.getType().isAlive()) entity.remove();
                    if(entity.getType() == EntityType.DROPPED_ITEM) entity.remove();
                });
                world.setTime(1200);
            });
        }, 20 * 8);

        Connection connection = null;
        try{
            connection = AzurePlugin.getInstance().getDatabaseManager().getHikari().getConnection();
            PreparedStatement statement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS `SumoProfile` (`uuid` VARCHAR(40), `wins` int(11), `kills` int(11), `deaths` int(11), PRIMARY KEY(`uuid`)) ENGINE=InnoDB DEFAULT CHARSET=utf8;");
            statement.execute();
            AzurePlugin.getInstance().getDatabaseManager().close(statement);
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            try {
                if(connection != null && !connection.isClosed()){
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    private void disableGameRules(World world, String... gameRules) {
        for (String gameRule : gameRules) {
            world.setGameRuleValue(gameRule, "false");
        }
    }

    @Override
    public void onDisable() {
        if(mode == null) return;
        arenaManager.save();
    }

    @SneakyThrows
    public void loadMessages(String countryCode){
        Properties properties = new Properties();
        File propertiesFile = new File(this.getDataFolder(), "/properties/"+countryCode+".properties");
        this.getDataFolder().mkdir();
        new File(this.getDataFolder(),"properties").mkdir();
        try {
            File en = new File(this.getDataFolder(), "/properties/"+countryCode+"_tmp.properties");
            InputStream E = this.getResource(countryCode+".properties");
            copyFile(E, en);
            if(propertiesFile.exists() && en.length() != new File(this.getDataFolder(), "/properties/" + countryCode + ".properties").length()){


                Properties prop1 = new Properties();
                Properties prop2 = new Properties();
                prop1.load(new FileInputStream(en.getAbsolutePath()));
                prop2.load(new FileInputStream(propertiesFile.getAbsolutePath()));

                boolean edited = false;
                if(prop1.size() > prop2.size()){
                    for(Object key : prop1.keySet()){
                        if(!prop2.containsKey(key)){
                            prop2.put(key,prop1.get(key));
                            edited = true;
                        }
                    }
                }
                if(edited){
                    FileOutputStream fos = new FileOutputStream(propertiesFile);
                    prop2.store(fos,countryCode + " Language properties");
                }
            }else if(!propertiesFile.exists()){
                FileUtil.copy(en,propertiesFile);
            }
            en.delete();
        }catch(Exception e) {
            e.printStackTrace();
        }

        FileInputStream inputStream= new FileInputStream(propertiesFile.getAbsolutePath());
        properties.load(inputStream);
        LanguageManager.getLanguageManager().getLanguages().stream().filter(language -> language.getCountryCode().equals(countryCode)).forEach(language -> {
            for(Map.Entry<Object,Object> entry : properties.entrySet()){
                if(entry.getValue().getClass() == String.class && entry.getKey().getClass() == String.class){
                    String def = (String) entry.getKey();
                    String value = (String) entry.getValue();
                    language.addMessage(def,value);
                }
            }
        });
    }

    @SneakyThrows
    public static void copyFile(InputStream in, File out) {
        InputStream fis = in;
        FileOutputStream fos = new FileOutputStream(out);
        try {
            byte[] buf = new byte[1024];
            int i = 0;
            while((i = fis.read(buf)) != -1) {
                fos.write(buf, 0, i);
            }
        }catch(Exception e) {
            throw e;
        }finally {
            if(fis != null) {
                fis.close();
            }
            if(fos != null) {
                fos.close();
            }
        }
    }
}
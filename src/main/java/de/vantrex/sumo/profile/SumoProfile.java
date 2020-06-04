package de.vantrex.sumo.profile;

import de.vantrex.azure.AzurePlugin;
import de.vantrex.azure.db.TimedDatabaseUpdate;
import de.vantrex.azure.others.profile.Profile;
import de.vantrex.azure.others.profile.ProfileAdaption;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SumoProfile extends TimedDatabaseUpdate implements ProfileAdaption
{

    private int wins = 0;
    private int kills = 0;
    private int deaths = 0;

    private Profile profile;

    public SumoProfile(Profile profile) {
        super("Sumo Profile", true);
        this.profile = profile;
    }

    public void addWin(){
        wins++;
    }

    public void addKill(){
        kills++;
    }

    public void addDeath(){
        deaths++;
    }

    @Override
    public void onUnload() { }

    @Override
    public void saveData() {

        Connection connection = null;
        try {
            connection = AzurePlugin.getInstance().getDatabaseManager().getHikari().getConnection();

            PreparedStatement selectStatement = connection.prepareStatement("SELECT `wins` FROM `SumoProfile` WHERE `uuid` = ?;");
            selectStatement.setString(1, profile.getUUID().toString());
            ResultSet rs = selectStatement.executeQuery();
            if(rs.next()){
                PreparedStatement updateStatement = connection.prepareStatement("UPDATE `SumoProfile` SET `wins` = ?, `kills` = ?, `deaths` = ? WHERE `uuid` = ?;");
                updateStatement.setInt(1, wins);
                updateStatement.setInt(2, kills);
                updateStatement.setInt(3, deaths);
                updateStatement.setString(4, profile.getUUID().toString());
                updateStatement.execute();
                AzurePlugin.getInstance().getDatabaseManager().close(updateStatement);
            }else{
                PreparedStatement insertStatement = connection.prepareStatement("INSERT INTO `SumoProfile` (`uuid`,`wins`,`kills`,`deaths`) VALUES (?,?,?,?);");
                insertStatement.setString(1, profile.getUUID().toString());
                insertStatement.setInt(2, wins);
                insertStatement.setInt(3, kills);
                insertStatement.setInt(4, deaths);
                insertStatement.execute();
                AzurePlugin.getInstance().getDatabaseManager().close(insertStatement);
            }
            AzurePlugin.getInstance().getDatabaseManager().close(selectStatement,rs);

        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                if(connection != null && !connection.isClosed())
                    connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void loadData() {

        Connection connection = null;
        try {
            connection = AzurePlugin.getInstance().getDatabaseManager().getHikari().getConnection();

            PreparedStatement statement = connection.prepareStatement("SELECT * FROM `SumoProfile` WHERE `uuid` = ?;");
            statement.setString(1,profile.getUUID().toString());
            ResultSet rs = statement.executeQuery();
            if(rs.next()){
                this.kills = rs.getInt("kills");
                this.deaths = rs.getInt("deaths");
                this.wins = rs.getInt("wins");
            }

            AzurePlugin.getInstance().getDatabaseManager().close(statement,rs);

        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                if(connection != null && !connection.isClosed())
                    connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void deleteData() {
        AzurePlugin.getInstance().getDatabaseManager().executeUpdate("DELETE FROM `SumoProfile` WHERE `uuid` = '"+profile.getUUID()+"';");
    }
}

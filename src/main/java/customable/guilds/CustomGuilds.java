package customable.guilds;

import customable.guilds.commands.GuildHandler;
import customable.guilds.guild.Guild;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.sql.DriverManager;

public final class CustomGuilds extends JavaPlugin {

    @Override
    public void onEnable() {
        //Check saving and folders
        /* ------------------------------------------------ DATABASE ---------------------------------------------------*/

        /* ------------------------------------------------ FILE SYSTEM -------------------------------------------------*/
        //FIle Save
        File mainFolder = new File("plugins/CustomGuilds/");
        if (!mainFolder.exists()){
            mainFolder.mkdirs();
            File guildsFolder = new File("plugins/CustomGuilds/Guilds");
            File membersFolder = new File("plugins/CustomGuilds/Members");
            guildsFolder.mkdirs();
            membersFolder.mkdirs();
        }

        //Try to load eventual stored data
        Guild.Load();

        //Standard Initialization
        this.getCommand("Guild").setExecutor(new GuildHandler());
        System.out.println("Custom Guilds is Enable");
    }

    @Override
    public void onDisable() {
        // Saving Guilds and Members
        boolean save = Guild.Save();
        if(!save)
            System.out.println("Error saving file. Can't assure guild, members or invites integrity on next launch");
        else
            System.out.println("CustoMGuild data saved succesfully");
    }
}

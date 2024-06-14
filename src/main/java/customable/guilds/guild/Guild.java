package customable.guilds.guild;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.*;
import java.util.ArrayList;
import java.util.UUID;

public class Guild implements Serializable {

    //Attributes
    private static final long serialVersionUID = 6529685098267757690L;
    static public ArrayList<Guild> Guilds = new ArrayList<Guild>();
    private String name;
    private String label;
    private ArrayList<Member> members = new ArrayList<Member>();
    private int maxMembers = 10;
    private ArrayList<Invite> pendingInvites = new ArrayList<Invite>();

    //Method
    public Guild(String name, UUID creator){
        Member m = new Member(creator, "founder", name);
        members.add(m);
        this.name = name;
        this.label = name.substring(0,2);
        Guilds.add(this);
    }


    public void invite(Player p){
        pendingInvites.add(new Invite(p.getUniqueId(), this));
        p.sendMessage(name + " invited you to join");
    }

    public void join(Invite i){
        if(!(members.size() < 10))
            Bukkit.getPlayer(i.getPlayer()).sendMessage("Guild "+name+" is already full");
        Member m = new Member(i.getPlayer(), "Default", name);
        members.add(m);
        pendingInvites.remove(i);
        Bukkit.getPlayer(i.getPlayer()).sendMessage("You entered "+name);
    }

    public void decline(Invite i){
        pendingInvites.remove(i);
    }

    public void remove(Member m){
        members.remove(m);
        Member.Members.remove(m);
    }

    public ArrayList<Member> getMembers(){
        return members;
    }

    @Override
    public String toString(){
        return this.name;
    }

    public String getGuilds(){
        String s = ChatColor.GREEN + "Active Guilds:\n "+ChatColor.GRAY;
        for(Guild g : Guilds){
            s += g.toString() + "\n";
        };
        return s;
    }

    public String getName(){
        return name;
    }

    public String getInfo(){
        String s = "Va formattato meglio..\n";
        s += ChatColor.GREEN + "Name: " + ChatColor.GRAY + name + "\n";
        s += ChatColor.GREEN + "Label: " + ChatColor.GRAY + label + "\n";
        s += ChatColor.GREEN + "Members: " + ChatColor.GRAY + members.size() + "\n";
        for(Member m : members){
            s += ChatColor.WHITE + Bukkit.getOfflinePlayer(m.getPlayer()).getName() + ",";
        }
        s = s.substring(0, s.length() - 1);
        return s;
    }

    public static boolean Save(){
        FileOutputStream fos = null;
        try {
            //Saving GUilds
            for(Guild g : Guilds){
                fos = new FileOutputStream("plugins/CustomGuilds/Guilds/"+g.getName()+".ser");
                ObjectOutputStream goos = new ObjectOutputStream(fos);
                goos.writeObject(g);
                goos.close();
                fos.close();
            }
            //Saving Members
            for(Member m : Member.Members){
                fos = new FileOutputStream("plugins/CustomGuilds/Members/"+Bukkit.getOfflinePlayer(m.getPlayer()).getUniqueId()+".ser");
                ObjectOutputStream moos = new ObjectOutputStream(fos);
                moos.writeObject(m);
                moos.close();
                fos.close();
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    public static void Load(){
        File mdir = new File("plugins/CustomGuilds/Members");
        File[] mdirectoryListing = mdir.listFiles();
        if (mdirectoryListing != null) {
            for (File child : mdirectoryListing) {
                try (FileInputStream fis = new FileInputStream(child.getPath());
                     ObjectInputStream ois = new ObjectInputStream(fis)) {
                    Member.Members.add((Member) ois.readObject());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        File dir = new File("plugins/CustomGuilds/Guilds");
        File[] directoryListing = dir.listFiles();
        if (directoryListing != null) {
            for (File child : directoryListing) {
                try (FileInputStream fis = new FileInputStream(child.getPath());
                     ObjectInputStream ois = new ObjectInputStream(fis)) {
                    Guilds.add((Guild)ois.readObject());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}

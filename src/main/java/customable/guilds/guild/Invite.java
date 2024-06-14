package customable.guilds.guild;

import org.bukkit.entity.Player;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;

public class Invite implements Serializable{
    private static final long serialVersionUID = 6529685098267757690L;
    public static ArrayList<Invite> invites = new ArrayList<Invite>();
    //Attributes
    private UUID p;
    private Guild g;

    //Methods
    public Invite(UUID p, Guild g){
        this.p = p;
        this.g = g;
        invites.add(this);
    }

    public UUID getPlayer(){
        return p;
    }

    public Guild getGuild(){
        return g;
    }

    public void accept(){
        g.join(this);
        invites.remove(this);
    }

    public void decline(){
        g.decline(this);
        invites.remove(this);
    }

    @Override
    public String toString(){
        return g.getName()+ p;
    }

}

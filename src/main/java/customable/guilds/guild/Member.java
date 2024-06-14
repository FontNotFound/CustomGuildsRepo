package customable.guilds.guild;

import org.bukkit.entity.Player;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;

public class Member implements Serializable {

    //Attributes
    private static final long serialVersionUID = 6529685098267757690L;
    static public ArrayList<Member> Members = new ArrayList<Member>();
    private UUID player;
    private String ruolo;
    private String guild;

    //Methods
    public Member(UUID player, String ruolo, String Guild){
        this.player = player;
        this.ruolo = ruolo;
        Members.add(this);
    }

    public UUID getPlayer(){
        return player;
    }

    public String getGuild(){
        return guild;
    }
}

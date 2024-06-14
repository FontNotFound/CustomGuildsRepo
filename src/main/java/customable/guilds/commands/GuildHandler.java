package customable.guilds.commands;

import customable.guilds.GUI.BaseGuildGUI;
import customable.guilds.GUI.ExampleGUI;
import customable.guilds.guild.Guild;
import customable.guilds.guild.Invite;
import customable.guilds.guild.Member;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import javax.annotation.Nullable;
import java.util.Map;

public class GuildHandler implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        //Guilds Handler
        /*
        Options:
            create: Creates a new Guild
            delete: Deletes a Guild
            Invite: Invites a Player in the Guild
            join: Enters a Guild by Invitation
            decline: Declines a Guild Invitation
            leave: Leaves the Guild
            roles: Enters edit role mode
            list: Returns active guilds list
            info: Give the info of the specified player. if none specified, gives info of own guild.
         */
        if(args.length == 0)
            return false;
        switch(args[0]){
            case "create":
                if(!(sender instanceof Player))
                    return false;
                if(Member.Members != null && findMember((Player)sender) != null){
                    sender.sendMessage(formatMessage("You are already in a Guild!"));
                    return true;
                }
                if(args.length == 1){
                    sender.sendMessage(formatMessage("You need to provide a Guild Name!"));
                    return true;
                }
                Guild g = new Guild(args[1], ((Player) sender).getUniqueId());
                sender.sendMessage(formatMessage("Guild "+args[1]+" created successfully"));
                break;
            case "invite":
                if(!(sender instanceof Player))
                        return false;
                if(Member.Members != null && findMember((Player)sender) == null){
                    sender.sendMessage(formatMessage("You are not in a Guild!"));
                    return true;
                }
                if(args.length == 1){
                    sender.sendMessage(formatMessage("You need to enter a Player name"));
                    return true;
                }
                Player p = Bukkit.getPlayer(args[1]);
                if(p == null){
                    sender.sendMessage(formatMessage("Player not online"));
                    return true;
                }
                if(findMember(p) != null){
                    sender.sendMessage(formatMessage("Player already in a Guild"));
                    return true;
                }
                Guild invitedGuild = findGuild(findMember((Player)sender));
                invitedGuild.invite(p);
                break;
            case "join":
                if(!(sender instanceof Player))
                    return false;
                if(Member.Members != null && findMember((Player)sender) != null){
                    sender.sendMessage(formatMessage("You are already in a Guild!"));
                    return true;
                }
                if(args.length == 1){
                    sender.sendMessage(formatMessage("You need to specify Guild Name"));
                    return true;
                }
                Invite i = findInvite(args[1],(Player)sender);
                if(i == null){
                    sender.sendMessage(formatMessage("You have no pending invites from that Guild"));
                    return true;
                }
                i.accept();
                break;
            case "decline":
                if(!(sender instanceof Player))
                    return false;
                if(args.length == 1){
                    sender.sendMessage(formatMessage("You need to specify Guild Name"));
                    return true;
                }
                Invite invite = findInvite(args[1],(Player)sender);
                if(invite == null){
                    sender.sendMessage(formatMessage("You have no pending invites from that Guild"));
                    return true;
                }
                invite.decline();
                break;
            case "leave":
                if(!(sender instanceof Player))
                    return false;
                if(Member.Members != null && findMember((Player)sender) == null){
                    sender.sendMessage(formatMessage("You are not in a Guild!"));
                    return true;
                }
                Member member = findMember((Player) sender);
                Guild guild = findGuild(member);
                guild.remove(member);
                sender.sendMessage(formatMessage("You are no longer part of "+guild.getName()));
                break;
            case "kick":
                if(!(sender instanceof Player))
                    return false;
                if(Member.Members != null && findMember((Player)sender) == null){
                    sender.sendMessage(formatMessage("You are not in a Guild!"));
                    return true;
                }
                //Permission check
                if(args.length == 1) {
                    sender.sendMessage(formatMessage("You need to specify the player to kick"));
                    return true;
                }
                Member kickedmember = findMemberByName(args[1]);
                if(kickedmember == null){
                    sender.sendMessage(formatMessage(args[1]+" is not a member of your Guild"));
                    return true;
                }
                Guild kickingGuild = findGuild(kickedmember);
                if(!(kickingGuild == findGuild(findMember((Player)sender)))){
                    sender.sendMessage(formatMessage(args[1]+" is not a member of your Guild"));
                    return true;
                }
                kickingGuild.remove(kickedmember);
                sender.sendMessage(formatMessage(args[1]+" has been kicked from the Guild"));
                break;
            case "info":
                if(!(sender instanceof Player))
                    return false;
                if(args.length == 1) {
                    if (Member.Members != null && findMember((Player) sender) == null) {
                        sender.sendMessage("You are not in a Guild");
                        return true;
                    }
                    Guild ownGuild = findGuild(findMember((Player) sender));
                    sender.sendMessage(ownGuild.getInfo());
                } else {
                    if (Member.Members != null && findMemberByName(args[1]) == null) {
                        sender.sendMessage(formatMessage(args[1] + "is not in a Guild"));
                        return true;
                    }
                    Guild playerGuild = findGuild(findMemberByName(args[1]));
                    sender.sendMessage(formatMessage(playerGuild.getInfo()));
                }
                break;
            case "infoGuild":
                if(args.length == 1){
                    sender.sendMessage(formatMessage(("You need to provide a Guild name")));
                    return true;
                }
                Guild guildbyname = findGuildByName(args[1]);
                if(guildbyname == null){
                    sender.sendMessage(formatMessage("Guild not found"));
                }
                sender.sendMessage(formatMessage(guildbyname.getInfo()));
                break;
            case "list":
                sender.sendMessage(formatMessage(Guild.Guilds.toString()));
                break;
            default:
                Player playerSender = (Player) sender;
                BaseGuildGUI e = new BaseGuildGUI(playerSender.getUniqueId());
                e.openInventory((Player)sender);
                break;
        }

        return true;
    }

    @Nullable
    private Member findMemberByName(String playername){
        for(Member m : Member.Members){
            if(Bukkit.getOfflinePlayer(m.getPlayer()).getName().equals(playername))
                return m;
        }
        return null;
    }
    @Nullable
    private Member findMember(Player p){
        for(Member m : Member.Members){
            if(Bukkit.getOfflinePlayer(m.getPlayer()) == p){
                System.out.println("member found");
                return m;
            }
        }
        return null;
    }

    @Nullable
    private Guild findGuild(Member m){
        for(Guild g : Guild.Guilds){
            for(Member  m1 : g.getMembers()){
                if(m1.getPlayer().equals(m.getPlayer()))
                    return g;
            }
        }
        return null;
    }

    @Nullable
    private Guild findGuildByName(String name){
        for(Guild g : Guild.Guilds){
            if(g.getName().equals(name)) {
                return g;
            }
        }
        return null;
    }

    @Nullable
    private Invite findInvite(String guildname, Player p){
        for(Invite i : Invite.invites){
            Guild g = findGuildByName(guildname);
            if(Bukkit.getOfflinePlayer(i.getPlayer()) == p && i.getGuild() == g) {
                return i;
            }
        }
        return null;
    }

    private String formatMessage(String message){
        return ChatColor.GREEN + "CustomGuilds" + ChatColor.DARK_GRAY +  ">> " + ChatColor.GRAY + message;
    }
}
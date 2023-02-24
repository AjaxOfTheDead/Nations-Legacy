package events.nation.alliance;

import java.util.HashMap;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import commands.alliance.AllyCommand;
import events.nation.NationEvent;
import general.GeneralMethods;
import main.Main;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import persistency.NationMapping;
import persistency.PlayerMapping;

public class RequestAllianceEvent extends NationEvent{
	private NationMapping receiverNation;
	
	public RequestAllianceEvent(NationMapping nation, NationMapping receiver, AllyCommand allyCommand, CommandSender sender) {
		super(nation, sender);
		this.receiverNation = receiver;

		if(super.isCancelled) return;
		
		Main main = Main.getInstance();
		Player player = (Player) sender;
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(main, new Runnable() {
			
			@Override
			public void run() {
				FileConfiguration language = main.getLanguage();
				if(allyCommand.getAllianceRequests().containsKey(receiverNation.getNationID()) && allyCommand.getAllianceRequests().get(receiverNation.getNationID()).contains(nation.getNationID())) player.sendMessage(GeneralMethods.format(sender, language.getString("Command.Nations.Alliance.Add.Send.AlreadySent.Message"), receiverNation.getName()));
				else {
					allyCommand.addAllianceRequest(receiverNation.getNationID(), nation.getNationID());
					
					TextComponent accept = GeneralMethods.createHoverText("Accept", "Click to accept", "/nations ally accept " + nation.getName(), ChatColor.GREEN), 
							deny = GeneralMethods.createHoverText("Deny", "Click to deny", "/nations ally deny " + nation.getName(), ChatColor.RED), 
							cancel = GeneralMethods.createHoverText("Cancel", "Click to cancel", "/nations ally cancelrequest " + receiverNation.getName(), ChatColor.RED);
					
					TextComponent text = new TextComponent(GeneralMethods.format((OfflinePlayer)player, language.getString("Command.Nations.Alliance.Add.Receive.RequestReceived.Message"), nation.getName()));
					text.addExtra(accept);
					text.addExtra(" | ");
					text.addExtra(deny);
					
					TextComponent senderText = new TextComponent(GeneralMethods.format(sender, language.getString("Command.Nations.Alliance.Add.Send.RequestSent.Message"), receiverNation.getName()));
					senderText.addExtra(cancel);
					
					for(PlayerMapping playerMap : nation.getLeaders()) {
						Player senderPlay = Bukkit.getPlayer(playerMap.getUUID());
						senderPlay.spigot().sendMessage(senderText);
					}
					
					for(PlayerMapping playerMap : receiverNation.getLeaders()) {
						Player receiverPlay = Bukkit.getPlayer(playerMap.getUUID());
						if(receiverPlay == null) continue;
						
						main.getCommandManager().setLastArg(sender.getName(), receiverNation.getName().toLowerCase());
						receiverPlay.spigot().sendMessage(text);
					}
					
					//create runnable that runs code after 5min
					new BukkitRunnable() {
					    public void run() {
					    	//if player hasn't accepted, expire the invite
					    	HashMap<Integer, Set<Integer>> allianceRequests = allyCommand.getAllianceRequests();
					        if(!allianceRequests.containsKey(receiverNation.getNationID()) || !allianceRequests.get(receiverNation.getNationID()).contains(nation.getNationID())) return;
					        for(PlayerMapping players : nation.getLeaders()) {
					        	Player play = Bukkit.getPlayer(players.getUUID());
					        	play.sendMessage(GeneralMethods.format(sender, language.getString("Command.Nations.Alliance.Add.Send.Expired.Message"), play.getName()));	
					        }
					        allyCommand.removeAllianceRequest(receiverNation.getNationID(), nation.getNationID());
					    }
					}.runTaskLater(main, 20*60);	
				}
			}
		}, 1L);
		
	}
	
	public NationMapping getReceiverNation() {
		return receiverNation;
	}
	
}
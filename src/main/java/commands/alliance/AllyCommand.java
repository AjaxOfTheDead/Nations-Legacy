package commands.alliance;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import commands.alliance.add.AllyAccept;
import commands.alliance.add.AllyAdd;
import commands.alliance.add.AllyCancel;
import commands.alliance.add.AllyDeny;
import commands.alliance.remove.AllyRemove;
import me.resurrectajax.ajaxplugin.help.HelpCommand;
import me.resurrectajax.ajaxplugin.interfaces.ParentCommand;

public class AllyCommand extends ParentCommand{
	private ParentCommand parent;
	private HashMap<Integer, Set<Integer>> allianceRequests = new HashMap<Integer, Set<Integer>>();
	
	public AllyCommand(ParentCommand parent) {
		this.parent = parent;
	}
	
	@Override
	public String getPermissionNode() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasTabCompletion() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "ally";
	}

	@Override
	public String getSyntax() {
		// TODO Auto-generated method stub
		return "/nations ally <subcommand>";
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return "Run an alliance command";
	}

	@Override
	public List<ParentCommand> getSubCommands() {
		// TODO Auto-generated method stub
		return Arrays.asList(
				new AllyAdd(this),
				new AllyAccept(this),
				new AllyDeny(this),
				new AllyCancel(this),
				new AllyRemove(this),
				new HelpCommand(this)
				);
	}

	@Override
	public boolean isConsole() {
		// TODO Auto-generated method stub
		return false;
	}

	public HashMap<Integer, Set<Integer>> getAllianceRequests() {
		return allianceRequests;
	}
	
	public void addAllianceRequest(Integer receiverID, Integer senderID) {
		Set<Integer> requestIDs = new HashSet<Integer>();
		if(this.allianceRequests.containsKey(receiverID)) requestIDs = new HashSet<Integer>(this.allianceRequests.get(receiverID));
		requestIDs.add(senderID);
		this.allianceRequests.put(receiverID, requestIDs);
	}
	
	public void removeAllianceRequest(Integer receiverID, Integer senderID) {
		if(!this.allianceRequests.containsKey(receiverID)) return;
		this.allianceRequests.get(receiverID).remove(senderID);
	}

	@Override
	public ParentCommand getParentCommand() {
		// TODO Auto-generated method stub
		return parent;
	}

}

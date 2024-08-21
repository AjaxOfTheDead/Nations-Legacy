package me.resurrectajax.nationslegacy.commands.invite.validators;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import me.resurrectajax.ajaxplugin.interfaces.ParentCommand;
import me.resurrectajax.nationslegacy.general.CommandValidator;
import me.resurrectajax.nationslegacy.general.GeneralMethods;
import me.resurrectajax.nationslegacy.main.Nations;
import me.resurrectajax.nationslegacy.persistency.MappingRepository;
import me.resurrectajax.nationslegacy.persistency.PlayerMapping;

public class NationInviteCancelValidator extends CommandValidator {

	public NationInviteCancelValidator(CommandSender sender, String[] args, ParentCommand command) {
		super((Nations)command.getMain(), sender, args, command);
	}

	@Override
	public boolean validate() {
		FileConfiguration language = main.getLanguage();
		MappingRepository mappingRepo = main.getMappingRepo();

		PlayerMapping receiver = args.length < 2 ? null : mappingRepo.getPlayerByName(args[1]);
		PlayerMapping send = mappingRepo.getPlayerByUUID(((Player) sender).getUniqueId());
		
		Integer nationID = send.getNationID();
		
		if (args.length < 2) sender.sendMessage(GeneralMethods.getBadSyntaxMessage(main, command.getSyntax()));
		else if (send.getNationID() == null) sender.sendMessage(GeneralMethods.format((OfflinePlayer)sender, language.getString("Command.Player.NotInNation.Message"), ""));
		else if (!mappingRepo.getPlayerInvites().containsKey(receiver.getUUID()) || nationID == null
				|| !mappingRepo.getPlayerInvites().get(receiver.getUUID()).contains(nationID))
			sender.sendMessage(GeneralMethods.format((OfflinePlayer)sender,
					language.getString("Command.Player.Invite.Cancel.NoInvite.Message"), args[1]));
		else return true;
		return false;
	}

}

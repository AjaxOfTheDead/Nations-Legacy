package main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.yaml.snakeyaml.Yaml;

import commands.NationsCommand;
import listeners.ClaimListener;
import listeners.JoinListener;
import listeners.PlayerMoveListener;
import me.resurrectajax.ajaxplugin.gui.GuiManager;
import me.resurrectajax.ajaxplugin.managers.CommandManager;
import me.resurrectajax.ajaxplugin.managers.FileManager;
import me.resurrectajax.ajaxplugin.plugin.AjaxPlugin;
import persistency.MappingRepository;
import placeholderapi.CustomPlaceHolders;
import placeholderapi.CustomRelationalPlaceholders;

/**
 * Main class
 * 
 * @author ResurrectAjax
 * */
public class Main extends AjaxPlugin{
	private GuiManager guiManager;
	
	private List<String> formats = new ArrayList<String>(Arrays.asList(
 			"%nations_player_argument%",
			"%nations_player_name%",
 			"%nations_player_rank%",
 			"%nations_player_killpoints%",
 			"%nations_nation_name%",
 			"%nations_nation_description%",
 			"%nations_remaining_chunkamount%",
 			"%rel_nations_syntax%",
 			"%rel_nations_nation_name%",
 			"%rel_nations_nation1_name%",
 			"%rel_nations_nation2_name%",
 			"%rel_nations_enemy_nation%"
			));
	
	public List<String> getFormats() {
		return formats;
	}

	public void addFormats(List<String> formats) {
		this.formats.addAll(formats);
	}

	/**
	 * Static method to get the {@link Main} instance
	 * @return {@link Main} instance
	 * */
	public static Main getInstance() {
		return (Main) AjaxPlugin.getInstance();
	}
	
	/**
	 * Enable plugin and load files/commands
	 * */
	public void onEnable() {
		super.onEnable();
		
		for(Player player : Bukkit.getOnlinePlayers()) {
			if(getMappingRepo().getPlayerByUUID(player.getUniqueId()) == null) getMappingRepo().addPlayer(player);	
		}
		
		hookIntoPlaceholderAPI();
	}
	
	/**
	 * Load all the classes that implement {@link Listener}
	 * */
	public void loadListeners() {
		getServer().getPluginManager().registerEvents(new JoinListener(this), this);
		getServer().getPluginManager().registerEvents(new PlayerMoveListener(this), this);
		getServer().getPluginManager().registerEvents(new ClaimListener(this), this);
	}
	
	/**
	 * Get the gui manager
	 * @return {@link CommandManager} manager
	 * */
	public GuiManager getGuiManager() {
		return guiManager;
	}

	/**
	 * Get the config file
	 * @return {@link FileConfiguration} config
	 * */
	public FileConfiguration getConfig() {
		return files.get("config.yml");
	}
	
	/**
	 * Get the gui file
	 * @return {@link FileConfiguration} gui
	 * */
	public FileConfiguration getGuiConfig() {
		return files.get("gui.yml");
	}

	/**
	 * Get the language file
	 * @return {@link FileConfiguration} language
	 * */
	public FileConfiguration getLanguage() {
		return files.get("language.yml");
	}
	
	/**
	 * Get the Mapping repository
	 * @return {@link MappingRepository} repository
	 * */
	public MappingRepository getMappingRepo() {
		return (MappingRepository) super.getMappingRepo();
	}
	
	/**
	 * Reload the {@link Yaml} files
	 * */
	public void reload() {
		super.files = FileManager.loadFiles(
				"config.yml",
				"language.yml",
				"gui.yml"
				);
    }

	/**
	 * Load the {@link Yaml} files and classes
	 * */
	public void loadFiles() {
		super.setInstance(this);
		
		//load files
		reload();
        //files
        
		super.setMappingRepository(new MappingRepository(this));
		
		//load classes
		super.setCommandManager(new CommandManager(new NationsCommand(this)));
	}
	
	private void hookIntoPlaceholderAPI() {
		Plugin placeHolderAPI = Bukkit.getPluginManager().getPlugin("PlaceholderAPI");
		if(placeHolderAPI == null || !placeHolderAPI.isEnabled()) return;
		new CustomPlaceHolders().register();
		new CustomRelationalPlaceholders().register();	
	}
}
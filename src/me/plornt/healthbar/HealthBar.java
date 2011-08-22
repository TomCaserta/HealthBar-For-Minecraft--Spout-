package me.plornt.healthbar;

import java.io.File;
import java.util.HashMap;
import java.util.logging.Logger;

import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.config.Configuration;
import org.getspout.spoutapi.SpoutManager;
import org.getspout.spoutapi.player.AppearanceManager;
import org.getspout.spoutapi.player.SpoutPlayer;

public class HealthBar extends JavaPlugin {
	public static HashMap<Player, String> hn = new HashMap<Player, String>();
	public static HealthBar plugin;
	public static Configuration config;
	public static Server server;
	public String goodHealthColor, hurtHealthColor, containerColor, container1, container2, barCharacter;
	public Boolean usePermissions;
	private final HealthBarEntityListener el = new HealthBarEntityListener(this);
	private final HealthBarPlayerListener pl = new HealthBarPlayerListener(this);
	public final Logger logger = Logger.getLogger("Minecraft");
	public boolean onCommand (CommandSender sender, Command cmd, String commandLabel, String[] args) {
		Player pl = (Player) sender;
		//SpoutPlayer sp = (SpoutPlayer) pl;
		//will do something with later..
		if ((pl.isOp() && !usePermissions) || (pl.hasPermission("healthbar.reload") && usePermissions)) {
			if (commandLabel.equalsIgnoreCase("HealthBar") && args[0].equalsIgnoreCase("reload")) {
				this.loadConfig();
				pl.sendMessage("§c[HealthBar] §9Reloaded Configuration");
				return true;
				}
		}
		return false;
	}
	public void setTitle (Player pl, int health, int tothealth, int death) {
		AppearanceManager sm =  SpoutManager.getAppearanceManager();
		String gh;
		if (health > 0 && health <= 20) {
			gh = new String(new char[health]).replace("\0", barCharacter);
			String bh;
			if (health < tothealth) bh = new String(new char[(tothealth - health)]).replace("\0", barCharacter);
			else bh = "";
			 String prevName;		 
			if (hn.get(pl) instanceof String) prevName = (String) sm.getTitle((SpoutPlayer) pl, (LivingEntity) pl).replace(hn.get(pl),"");
			else prevName = (String) sm.getTitle((SpoutPlayer) pl, (LivingEntity) pl);
			hn.remove(pl);
			hn.put(pl, "\n"+ "§" + containerColor + container1 +"§" + goodHealthColor + gh +"§" + hurtHealthColor + bh +"§" + containerColor + container2);
			if (usePermissions) {
				for (Player player : getServer().getOnlinePlayers()) { 
					if (player.hasPermission("healthbar.cansee")) {
						sm.setPlayerTitle((SpoutPlayer) player, (LivingEntity) pl,prevName + hn.get(pl));
					}
				}
			}
			else sm.setGlobalTitle((LivingEntity) pl,prevName + hn.get(pl));
		}
	}
	@Override
	public void onDisable () {
		this.logger.info("[HealthBar] Shutting Down");
	}
	public void loadConfig() {
	if (!new File(getDataFolder(), "config.yml").exists()) {
	try {
	    getDataFolder().mkdir();
	    new File(getDataFolder(),"config.yml").createNewFile();
	}
	catch(Exception e) {
	    e.printStackTrace();
	    this.logger.info("Unable to create config file");
	            getServer().getPluginManager().disablePlugin(this);
	            return;
	        }
	    }
	     config = this.getConfiguration(); 
	
	    
	   if (config.getKeys().isEmpty()) { 
		   config.setProperty("Colors.goodHealthColor", "a");
		   config.setProperty("Colors.hurtHealthColor", "c");
		   config.setProperty("Colors.containerColor", "9");
		   config.setProperty("Characters.container1", "[");
		   config.setProperty("Characters.container2", "]");
		   config.setProperty("Characters.barCharacter", "|");
		   config.setProperty("Permissions.usePermissions", "false");
		   config.save();
	   }
	   goodHealthColor = (String) config.getProperty("Colors.goodHealthColor");
	   if (goodHealthColor == null) goodHealthColor = "a";
	   hurtHealthColor = (String) config.getProperty("Colors.hurtHealthColor");
	   if (hurtHealthColor == null) hurtHealthColor = "9";
	   containerColor = (String) config.getProperty("Colors.containerColor");
	   if (containerColor == null) containerColor = "c";
	   container1 = (String) config.getProperty("Colors.container1");
	   if (container1 == null) container1 = "[";
	   container2 = (String) config.getProperty("Colors.container2");
	   if (container2 == null) container2 = "]";
	   barCharacter = (String) config.getProperty("Colors.barCharacter");
	   if (barCharacter == null) barCharacter = "|";
	   if (((String) config.getProperty("Permissions.usePermissions")).equalsIgnoreCase("true")) usePermissions = true;
	   else usePermissions = false;
	}
	@Override
	public void onEnable () {

		this.logger.info("[HealthBar] Loading..");	
		server = getServer();
		loadConfig();
		PluginManager pm = server.getPluginManager();
		pm.registerEvent(Event.Type.PLAYER_JOIN, this.pl, Event.Priority.Monitor, this);
		pm.registerEvent(Event.Type.PLAYER_RESPAWN, this.pl, Event.Priority.Monitor, this);
		pm.registerEvent(Event.Type.ENTITY_DAMAGE, this.el, Event.Priority.Monitor, this);
		pm.registerEvent(Event.Type.ENTITY_REGAIN_HEALTH, this.el, Event.Priority.Monitor, this);
		this.logger.info("[HealthBar] Loaded up plugin... Version 0.3.");		
	}
}
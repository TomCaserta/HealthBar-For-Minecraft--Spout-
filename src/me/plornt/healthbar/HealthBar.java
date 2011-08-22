package me.plornt.healthbar;

import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.getspout.spoutapi.SpoutManager;
import org.getspout.spoutapi.gui.GenericLabel;
import org.getspout.spoutapi.gui.GenericPopup;
import org.getspout.spoutapi.player.AppearanceManager;
import org.getspout.spoutapi.player.SpoutPlayer;

public class HealthBar extends JavaPlugin {
	public static HealthBar plugin;
	public static Server server;
	private final HealthBarEntityListener el = new HealthBarEntityListener(this);
	private final HealthBarPlayerListener pl = new HealthBarPlayerListener(this);
	public final Logger logger = Logger.getLogger("Minecraft");
	public boolean onCommand (CommandSender sender, Command cmd, String commandLabel, String[] args) {
		Player pl = (Player) sender;
		SpoutPlayer sp = (SpoutPlayer) pl;
		if (commandLabel.equalsIgnoreCase("ch")) {
			//Inputting Example Code From wiki to test...
			pl.sendMessage(ChatColor.GREEN + "Attempting to create dialog");
			GenericPopup somepopup = new GenericPopup();
			GenericLabel label = new GenericLabel("This is a label");
			somepopup.attachWidget(label); 
			sp.getMainScreen().attachPopupScreen(somepopup);
			pl.sendMessage(ChatColor.GREEN + "Attempted to create dialog");
		}
		return false;
	}
	public void setTitle (Player pl, int health, int tothealth, String rpt) {
		String gh;
		if (health != 0) gh = new String(new char[health]).replace("\0", rpt);
		else gh = "";
		String bh;
		if (health != 20) bh = new String(new char[(tothealth - health)]).replace("\0", rpt);
		else bh = "";
		String n = pl.getName();
		AppearanceManager sm =  SpoutManager.getAppearanceManager();
		sm.setGlobalTitle((LivingEntity) pl,n + "\n" + ChatColor.BLUE + " [" + ChatColor.GREEN + gh + ChatColor.RED + bh + ChatColor.BLUE + "]");
	}
	@Override
	public void onDisable () {
		this.logger.info("[HealthBar] Shutting Down");
	}
	@Override
	public void onEnable () {
		server = getServer();
		PluginManager pm = server.getPluginManager();
		pm.registerEvent(Event.Type.PLAYER_JOIN, this.pl, Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.PLAYER_RESPAWN, this.pl, Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.ENTITY_DAMAGE, this.el, Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.ENTITY_REGAIN_HEALTH, this.el, Event.Priority.Normal, this);
		this.logger.info("[HealthBar] Loaded up plugin... Version 2.");		
	}
}
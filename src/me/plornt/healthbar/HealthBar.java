package me.plornt.healthbar;

import java.util.HashMap;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.getspout.spoutapi.SpoutManager;
import org.getspout.spoutapi.player.AppearanceManager;

public class HealthBar extends JavaPlugin {
	public static HealthBar plugin;
	public static Server server;
	private final HealthBarEntityListener el = new HealthBarEntityListener(this);
	private final HealthBarPlayerListener pl = new HealthBarPlayerListener(this);
	public final HashMap<Player, Boolean> smokeUser = new HashMap<Player, Boolean>();
	public final Logger logger = Logger.getLogger("Minecraft");
	public boolean isSmokeEnabled (Player pl) {
		return smokeUser.containsKey(pl);
	}
	public boolean toggleSmoke (Player pl) {
		Boolean b = isSmokeEnabled(pl);
		if (b) smokeUser.remove(pl);
		else smokeUser.put(pl, true);
		return b;
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
		this.logger.info("[HealthBar] Loaded up plugin... Version 1.");		
	}
}
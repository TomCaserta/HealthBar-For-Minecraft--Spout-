package me.plornt.healthbar;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class HealthBarPlayerListener extends PlayerListener {
	public static HealthBar plugin;
	public HealthBarPlayerListener (HealthBar instance) {
		plugin = instance;
		
	}
	public void onPlayerJoin (PlayerJoinEvent ev) {
		Player pl = ev.getPlayer();
		int h = pl.getHealth();
		plugin.setTitle(pl,h,20,0);
	}
	public void onPlayerRespawn (PlayerRespawnEvent ev) {
		plugin.setTitle(ev.getPlayer(),20,20,1);
	}
	public void onPlayerQuit (PlayerQuitEvent ev) {
	if (!HealthBar.hn.isEmpty()) if (HealthBar.hn.containsKey(ev.getPlayer())) HealthBar.hn.remove(ev.getPlayer());
	}
}

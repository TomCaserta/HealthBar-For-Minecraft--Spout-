package me.plornt.healthbar;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityListener;
import org.bukkit.event.entity.EntityRegainHealthEvent;

public class HealthBarEntityListener extends EntityListener {
	public static HealthBar plugin;
	public HealthBarEntityListener (HealthBar instance) {
		plugin = instance;
		
	}
	public void onEntityDamage (EntityDamageEvent ev) {
	Entity pl = ev.getEntity();
		if (pl instanceof Player) {
			int h = ((Player) pl).getHealth() - ev.getDamage();
			plugin.setTitle((Player) pl, h, 20, "|");
		}
	}
	public void onEntityRegainHealth (EntityRegainHealthEvent ev) {
	Entity pl = ev.getEntity();
		if (pl instanceof Player) {
			int h = ((Player) pl).getHealth() + ev.getAmount();
		plugin.setTitle((Player) pl, h, 20, "|");
		}
		
	}
}

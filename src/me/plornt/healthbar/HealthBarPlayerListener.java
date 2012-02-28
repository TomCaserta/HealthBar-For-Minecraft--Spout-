package me.plornt.healthbar;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class HealthBarPlayerListener implements Listener {
    public static HealthBar plugin;

    public HealthBarPlayerListener(HealthBar instance) {
        plugin = instance;

    }
    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoin(PlayerJoinEvent ev) {
        Player pl = ev.getPlayer();
        int h = pl.getHealth();
        plugin.setTitle(pl, h, 20, 0);
        System.out.println("playaaaaaajoin");
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerRespawn(PlayerRespawnEvent ev) {
        plugin.setTitle(ev.getPlayer(), 20, 20, 1);
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerQuit(PlayerQuitEvent ev) {
        if (!HealthBar.hn.isEmpty())
            if (HealthBar.hn.containsKey(ev.getPlayer()))
                HealthBar.hn.remove(ev.getPlayer());
    }
}

package me.plornt.healthbar;

import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

public class HealthBarPluginListener implements Listener {
    private HealthBar pl;

    public HealthBarPluginListener(HealthBar plugin) {
        pl = plugin;
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onPluginEnable(PluginEnableEvent ev) {
        // testing for Heroes without importing it! :D
        Plugin pla = ev.getPlugin().getServer().getPluginManager().getPlugin("Heroes");
        if (pla == ev.getPlugin()) {
            pl.logger.info("[HealthBar] Found heroes - implementing health system.");
            // Do nothing but you know, make the message sound like its actually
            // doing someting other than:
            new HealthBarHeroes(pla);
            HealthBar.useHeroes = true;
        }
    }
}

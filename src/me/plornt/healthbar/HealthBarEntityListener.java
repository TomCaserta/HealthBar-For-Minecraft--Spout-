package me.plornt.healthbar;

import org.bukkit.entity.Player;

public class HealthBarEntityListener implements Runnable {
    public static HealthBar plugin;
    public HealthBarEntityListener (HealthBar instance) {
        plugin = instance;
        
    }
    @Override
    public void run() {
            for (int x = 0; x < plugin.getServer().getOnlinePlayers().length; x++) {
                Player pl = plugin.getServer().getOnlinePlayers()[x];
                
                 if (!HealthBar.hn.isEmpty()) {
                     if (HealthBar.hn.containsKey(pl)) {
                         if (HealthBar.hn.get(pl) != pl.getHealth()) {
                             if (HealthBar.useHeroes != false)  plugin.setTitle(pl,pl.getHealth(),(int) HealthBarHeroes.pl.getHero(pl).getMaxHealth(),1);
                             else plugin.setTitle(pl,pl.getHealth(),20,1);
                         }
                     }
                     else HealthBar.hn.put(pl, pl.getHealth());
                 }
                 else HealthBar.hn.put(pl, pl.getHealth());
            }
        }
        
}

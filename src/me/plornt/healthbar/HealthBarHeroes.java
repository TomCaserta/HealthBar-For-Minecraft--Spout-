package me.plornt.healthbar;

import org.bukkit.plugin.Plugin;

import com.herocraftonline.dev.heroes.Heroes;
import com.herocraftonline.dev.heroes.hero.HeroManager;

public class HealthBarHeroes {
    static HeroManager pl;

    HealthBarHeroes(Plugin hl) {
        pl = (HeroManager) ((Heroes) hl).getHeroManager();
    }
}

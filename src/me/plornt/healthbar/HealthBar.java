package me.plornt.healthbar;

import java.util.HashMap;
import java.util.logging.Logger;

import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.getspout.spoutapi.player.SpoutPlayer;

public class HealthBar extends JavaPlugin {
    public static HashMap<Player, Integer> hn = new HashMap<Player, Integer>();
    public static HealthBar plugin;
    public static Server server;
    public static boolean useHeroes = false;
    private final HealthBarPlayerListener pl = new HealthBarPlayerListener(this);
    private final HealthBarPluginListener pe = new HealthBarPluginListener(this);
    public final Logger logger = Logger.getLogger("Minecraft");

    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        // will do something with later..
        if (sender instanceof ConsoleCommandSender || sender.isOp() && !getBoolean("Permissions.usePermissions") || sender.hasPermission("healthbar.reload") && getBoolean("Permissions.usePermissions")) {
            if (commandLabel.equalsIgnoreCase("HealthBar") && args.length == 1 && args[0].equalsIgnoreCase("reload")) {
                this.loadConfig();
                sender.sendMessage("§c[HealthBar] §9Reloaded Configuration");
                return true;
            }
        }
        return false;
    }

    public void setTitle(Player pl, int health, int tothealth, int death) {
       //AppearanceManager sm = SpoutManager.getAppearanceManager();
        if (health >= 0 && health <= tothealth) {
            String bh;
            String gh;
            if (health > 0)
                gh = new String(new char[health]).replace("\0", getString("Characters.barCharacter"));
            else
                gh = "";
            if (health < tothealth)
                bh = new String(new char[(tothealth - health)]).replace("\0", getString("Characters.barCharacter"));
            else
                bh = "";
            String hb = "§e§c§e\n" + "§" + getString("Colors.containerColor") + getString("Characters.container1") + "§" + getString("Colors.goodHealthColor") + gh + "§" + getString("Colors.hurtHealthColor") + bh + "§" + getString("Colors.containerColor") + getString("Characters.container2");
            if (getBoolean("Permissions.usePermissions")) {
                for (Player player : getServer().getOnlinePlayers()) {
                    if (player.hasPermission("healthbar.cansee")) {
                        if (player instanceof SpoutPlayer) {
                        	String[] plName = ((SpoutPlayer) pl).getTitle().split("§e§c§e");
                           // String[] plName = sm.getTitle((SpoutPlayer) pl, (LivingEntity) player).split("§e§c§e");
                            if (plName[0] != null) {
                            	((SpoutPlayer) player).setTitleFor((SpoutPlayer) pl, plName[0] + hb);
                                // sm.setPlayerTitle((SpoutPlayer) player, (LivingEntity) pl, plName[0] + hb);
                            }
                        }
                    }
                }
            } else if (pl instanceof SpoutPlayer) {
            	String[] plName = ((SpoutPlayer) pl).getTitle().split("§e§c§e");
                // String[] plName = sm.getTitle((SpoutPlayer) pl, (LivingEntity) pl).split("§e§c§e");
            	((SpoutPlayer) pl).setTitle(plName[0] + hb);
               // sm.setGlobalTitle((LivingEntity) pl, plName[0] + hb);        
            }
        }
    }
    
    public String getString(String key){
        return getConfig().getString(key,"");
    }

    public boolean getBoolean(String key){
    	return getConfig().getBoolean(key, false);
    }
    
    @Override
    public void onDisable() {
        this.logger.info("[HealthBar] Shutting Down");
    }

    public void loadConfig() {
        getConfig().addDefault("Colors.goodHealthColor",			"a");
    	getConfig().addDefault("Colors.hurtHealthColor",			"c");
    	getConfig().addDefault("Colors.containerColor",			"9");
    	getConfig().addDefault("Characters.container1",			"[");
    	getConfig().addDefault("Characters.container2",			"]");
    	getConfig().addDefault("Characters.barCharacter",		"|");
    	getConfig().addDefault("Permissions.usePermissions",		false);
    	getConfig().options().copyDefaults(true);
    	saveConfig();
    }

    // Not sure if its possible but if heroes loads before my plugin is loaded
    // im fairly sure the on plugin enable wont go...
    public void checkHeroes() {
        // testing for Heroes without importing it! :D
        Plugin pla = HealthBar.plugin.getServer().getPluginManager().getPlugin("Heroes");
        if (pla != null) {
            useHeroes = true;
            new HealthBarHeroes(pla);
        }
    }

    @Override
    public void onEnable() {

        this.logger.info("[HealthBar] Loading..");
        server = getServer();
        loadConfig();
        PluginManager pm = server.getPluginManager();
        pm.registerEvents(this.pl, this);
        pm.registerEvents(this.pe, this);
        getServer().getScheduler().scheduleSyncRepeatingTask(this, new HealthBarEntityListener(this), 0, 1);
        this.logger.info("[HealthBar] Loaded up plugin... Version 0.8.");
    }
}
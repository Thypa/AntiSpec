package nl.thypa.antispec;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public final class Main extends JavaPlugin implements Listener {

    private final HashMap<Player, Location> oldLocs = new HashMap<>();

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onChange(PlayerGameModeChangeEvent e){
        if(e.getPlayer().hasPermission("antispec.immune"))
            return;

        if(e.getNewGameMode() == GameMode.SPECTATOR && !(oldLocs.containsKey(e.getPlayer()))){
            oldLocs.put(e.getPlayer(), e.getPlayer().getLocation());
            Bukkit.getLogger().info(e.getPlayer().getDisplayName() + " switched into spectator mode!");
            e.getPlayer().sendMessage(ChatColor.DARK_AQUA + "You switched into spectator mode!");
            e.getPlayer().sendMessage(ChatColor.DARK_AQUA + "When you switch out of spectator you will be teleported back!");
        }else if(e.getNewGameMode() == GameMode.SPECTATOR && oldLocs.containsKey(e.getPlayer())){
            oldLocs.replace(e.getPlayer(), e.getPlayer().getLocation());
            Bukkit.getLogger().info(e.getPlayer().getDisplayName() + " switched into spectator mode!");
            e.getPlayer().sendMessage(ChatColor.DARK_AQUA + "You switched into spectator mode!");
            e.getPlayer().sendMessage(ChatColor.DARK_AQUA + "When you switch back into survival you will be teleported back!");
        }else if(e.getNewGameMode() == GameMode.SURVIVAL && oldLocs.containsKey(e.getPlayer())){
            e.getPlayer().teleport(oldLocs.get(e.getPlayer()));
            oldLocs.remove(e.getPlayer());
            e.getPlayer().sendMessage(ChatColor.DARK_AQUA + "You got teleported back to your pre-spectator location.");
        }
    }
}

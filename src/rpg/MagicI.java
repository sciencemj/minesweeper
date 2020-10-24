package rpg;

import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.LinkedList;
import java.util.Queue;

public class MagicI {
    String maker;
    String name;
    int length;
    int type;
    Color color;
    Queue<Location> path = new LinkedList<Location>();
    void play(Player p){
        int count = 0;
        switch(type){
            case 0:
                Location block = p.getEyeLocation();
                Vector loc = p.getLocation().getDirection().multiply(0.5f);
                for (int i = 0; i < length; i++) {
                    block = new Vector(block.getX() + loc.getX(), block.getY() + loc.getY(), block.getZ() + loc.getZ()).toLocation(p.getWorld());
                    if (!block.getBlock().getType().equals(Material.AIR)) {
                        //block.getWorld().createExplosion(block,0.1f);
                        break;
                    }
                    path.offer(block);
                }
                BukkitTask task = new CancelingTask(Main.pluginm, this, p).runTaskTimer(Main.pluginm,0L,1L);
                p.sendMessage("0");
                break;
            case 1:

        }
    }
    public void stop(int i){
        Bukkit.getScheduler().cancelTask(i);
    }
}
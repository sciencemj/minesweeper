package rpg;

import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
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
                p.sendMessage("0");
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
                int task = 0;
                task = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.pluginm, new Runnable() {
                    @Override
                    public void run() {
                        if(!path.isEmpty()) {
                            if (path.size() == 1){
                                Location l = path.poll();
                                p.getWorld().spawnParticle(Particle.FLASH, l, 1, 0, 0, 0, 0);
                                p.getWorld().playSound(l,Sound.ENTITY_FIREWORK_ROCKET_BLAST,10,10);
                                for (Entity e : p.getWorld().getNearbyEntities(l,3,3,3)){
                                    LivingEntity livingEntity = (LivingEntity)e;
                                    ((LivingEntity) e).damage(5);
                                }
                                //stop(task);
                            }else {
                                Particle.DustOptions dust = new Particle.DustOptions(color, 1);
                                p.getWorld().spawnParticle(Particle.REDSTONE, path.poll(), 1, 0, 0, 0, 0, dust);
                            }
                        }
                    }
                },0L,1L);
        }
    }
    public void stop(int i){
        Bukkit.getScheduler().cancelTask(i);
    }
}
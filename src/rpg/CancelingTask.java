package rpg;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Queue;


public class CancelingTask extends BukkitRunnable {
    private final JavaPlugin plugin;

    private int counter;
    private MagicI magicI;
    private Player p;
    private Queue<Location> path;

    public CancelingTask(JavaPlugin plugin, MagicI magicI,Player player){
        this.plugin = plugin;
        this.magicI = magicI;
        this.path = magicI.path;
        p = player;
    }

    @Override
    public void run() {
        if(!path.isEmpty()) {
            if (path.size() == 1){
                Location l = path.poll();
                p.getWorld().spawnParticle(Particle.FLASH, l, 1, 0, 0, 0, 0);
                p.getWorld().playSound(l, Sound.ENTITY_FIREWORK_ROCKET_BLAST,10,10);
                for (Entity e : p.getWorld().getNearbyEntities(l,3,3,3)){
                    LivingEntity livingEntity = (LivingEntity)e;
                    ((LivingEntity) e).damage(magicI.damage);
                }
                //this.cancel();
            }else {
                Particle.DustOptions dust = new Particle.DustOptions(magicI.color, 1);
                p.getWorld().spawnParticle(Particle.REDSTONE, path.poll(), 1, 0, 0, 0, 0, dust);
            }
        }else {
            p.sendMessage("stop");
            this.cancel();
        }
    }
}


package rpg;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.LinkedList;
import java.util.Queue;


public class CancelingTask extends BukkitRunnable {
    private final JavaPlugin plugin;

    private int counter;
    private MagicI magicI;
    private Player p;
    Queue<Location> pathi = new LinkedList<Location>();

    public CancelingTask(JavaPlugin plugin, MagicI magicI,Player player){
        this.plugin = plugin;
        this.magicI = magicI;
        while (!magicI.path.isEmpty()){
            pathi.offer(magicI.path.poll());
        }
        p = player;
    }

    @Override
    public void run() {
        if(!pathi.isEmpty()) {
            if (pathi.size() == 1){
                Location l = pathi.poll();
                p.getWorld().spawnParticle(Particle.FLASH, l, 1, 0, 0, 0, 0);
                p.getWorld().playSound(l, Sound.ENTITY_FIREWORK_ROCKET_BLAST,10,10);
                for (Entity e : p.getWorld().getNearbyEntities(l,3,3,3)){
                    LivingEntity livingEntity = (LivingEntity)e;
                    if (e instanceof Player) {
                        if (!((Player) e).getPlayer().equals(p))
                            ((LivingEntity) e).damage(magicI.damage);
                    }else {
                        ((LivingEntity) e).damage(magicI.damage);
                    }
                }
                p.sendMessage("last1");
                //this.cancel();
            }else {
                Location l = pathi.poll();
                if (l.getBlock().getType().equals(Material.AIR)) {
                    Particle.DustOptions dust = new Particle.DustOptions(magicI.color, 1);
                    p.getWorld().spawnParticle(Particle.REDSTONE, l, 1, 0, 0, 0, 0, dust);
                }else {
                    p.getWorld().spawnParticle(Particle.FLASH, l, 1, 0, 0, 0, 0);
                    p.getWorld().playSound(l, Sound.ENTITY_FIREWORK_ROCKET_BLAST,10,10);
                    for (Entity e : p.getWorld().getNearbyEntities(l,3,3,3)){
                        LivingEntity livingEntity = (LivingEntity)e;
                        if (e instanceof Player) {
                            if (!((Player) e).getPlayer().equals(p))
                                ((LivingEntity) e).damage(magicI.damage);
                        }else {
                            ((LivingEntity) e).damage(magicI.damage);
                        }

                    }
                    pathi.clear();
                }
            }
        }else {
            p.sendMessage("stop");
            this.cancel();
        }
    }
}


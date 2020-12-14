package rpg;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class MainEvent implements Listener {
    @EventHandler
    public void PlayerMove(PlayerMoveEvent e){
        Player p = e.getPlayer();
    }

    @EventHandler
    public void playerEvent(PlayerInteractEvent e){
        Player p = e.getPlayer();
        if (e.getAction() == Action.LEFT_CLICK_BLOCK){
            Block b = e.getClickedBlock();
            for (int i = 0;i < Main.mines.length;i++){
                for (int j = 0;j < Main.mines.length;j++){
                    if (Main.mines[i][j].getLocation().equals(b.getLocation())){
                        if (Main.map == null){
                            Main.map = Main.mapMaker(Main.size,Main.bombs,i,j);
                        }
                        e.setCancelled(true);
                        switch (Main.map[i][j]){
                            case -1:
                                b.setType(Material.TNT);
                                p.sendTitle(ChatColor.RED + "FAILED", "", 60,20,20);
                                p.getWorld().playSound(p.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 10,20);
                                p.getWorld().spawnParticle(Particle.EXPLOSION_HUGE,p.getLocation(),2);
                                Main.init();
                                break;
                            case 0:
                                b.setType(Material.WHITE_CONCRETE);
                                break;
                            case 1:
                                b.setType(Material.GREEN_CONCRETE);
                                nameMaker(b,"1");
                                break;
                            case 2:
                                b.setType(Material.BLUE_CONCRETE);
                                nameMaker(b,"2");
                                break;
                            case 3:
                                b.setType(Material.YELLOW_CONCRETE);
                                nameMaker(b,"3");
                                break;
                            case 4:
                                b.setType(Material.ORANGE_CONCRETE);
                                nameMaker(b,"4");
                                break;
                            case 5:
                                b.setType(Material.PINK_CONCRETE);
                                nameMaker(b,"5");
                                break;
                            case 6:
                                b.setType(Material.RED_CONCRETE);
                                nameMaker(b,"6");
                                break;
                            case 7:
                                b.setType(Material.BROWN_CONCRETE);
                                nameMaker(b,"7");
                                break;
                            case 8:
                                b.setType(Material.BLACK_CONCRETE);
                                nameMaker(b,"8");
                                break;
                        }
                    }
                }
            }
        }
    }

    public ArmorStand nameMaker(Block b,String s){
        ArmorStand stand = b.getWorld().spawn(b.getLocation().add(0.5,-1,0.5), ArmorStand.class);

        stand.setVisible(false);
        stand.setGravity(false);
        stand.setCustomNameVisible(true);
        stand.setCustomName(s);
        stand.setCollidable(false);
        Main.stands.add(stand);
        return stand;
    }
}

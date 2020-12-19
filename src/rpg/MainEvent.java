package rpg;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.plugin.Plugin;

public class MainEvent implements Listener {
    @EventHandler
    public void PlayerMove(PlayerMoveEvent e){
        Player p = e.getPlayer();
    }
    public static boolean over = true;
    public static int correct = 0;
    public static int wrong = 0;
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
            if (isOver()){
                p.sendTitle(ChatColor.GREEN + "WIN!!","",60,20,20);
                Firework firework = p.getWorld().spawn(p.getLocation(), Firework.class);
                FireworkMeta meta = firework.getFireworkMeta();
                FireworkEffect.Builder builder = FireworkEffect.builder();
                builder.flicker(true).trail(true).with(FireworkEffect.Type.STAR).withColor(Color.YELLOW).withFade(Color.RED);
                meta.addEffect(builder.build());
                meta.setPower(1);
                firework.setFireworkMeta(meta);
                Main.init();
            }
        }else if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            //p.sendMessage("right clicked");
            EquipmentSlot slot = e.getHand();
            if (slot.equals(EquipmentSlot.HAND)) {
                Block b = e.getClickedBlock();
                for (int i = 0; i < Main.mines.length; i++) {
                    for (int j = 0; j < Main.mines.length; j++) {
                        if (Main.mines[i][j].getLocation().equals(b.getLocation())) {
                            p.sendMessage(b.getType().toString());
                            if (b.getType().equals(Material.IRON_BLOCK)) {
                                b.setType(Material.STONE);
                                if (Main.map[i][j] == -1) {
                                    correct--;
                                } else {
                                    wrong--;
                                }
                            }else if (b.getType().equals(Material.STONE)) {
                                b.setType(Material.IRON_BLOCK);
                                if (Main.map[i][j] == -1) {
                                    correct++;
                                } else {
                                    wrong++;
                                }
                            }
                        }
                    }
                }
                if (isOver()) {
                    p.sendTitle(ChatColor.GREEN + "WIN!!", "", 60, 20, 20);
                    Firework firework = p.getWorld().spawn(p.getLocation(), Firework.class);
                    FireworkMeta meta = firework.getFireworkMeta();
                    FireworkEffect.Builder builder = FireworkEffect.builder();
                    builder.flicker(true).trail(true).with(FireworkEffect.Type.STAR).withColor(Color.YELLOW).withFade(Color.RED);
                    meta.addEffect(builder.build());
                    meta.setPower(1);
                    firework.setFireworkMeta(meta);
                    Main.init();
                }
            }
        }
    }
    public boolean isOver(){
        boolean isover = true;
        for (Block[] bs : Main.mines){
            for (Block b : bs){
                if (b.getType().equals(Material.STONE)) {
                    isover = false;
                }
            }
        }
        if (isover) {
            if (correct == Main.bombs && wrong == 0)
                return true;
        }
        return false;
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

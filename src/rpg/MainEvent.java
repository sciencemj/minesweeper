package rpg;

import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

public class MainEvent implements Listener {
    /*@EventHandler
    public void event(PlayerExpChangeEvent e){
        Player p = e.getPlayer();
        p.sendMessage(p.getTotalExperience() +  "s");
        p.sendMessage(p.getTotalExperience() + e.getAmount() +  "t");
        if (p.getTotalExperience() + e.getAmount() >= 7){
            p.sendMessage("over:" + (e.getAmount() - (7 - p.getTotalExperience())));
            e.setAmount( e.getAmount() - (7 - p.getTotalExperience()));
            p.setTotalExperience(0);
            p.setExp(0f);
            p.setLevel(0);
            if (Main.levels.containsKey(p)) {
                Main.levels.replace(p, Main.levels.get(p) + 1);
            }else {
                Main.levels.put(p, 1);
            }
            p.sendMessage("레벨:" + Main.levels.get(p));
        }

    }*/
    @EventHandler
    public void PlayerMove(PlayerMoveEvent e){
        Player p = e.getPlayer();
    }
    @EventHandler
    public  void playerEvent(PlayerInteractEvent e){
        Player p = e.getPlayer();
        if (e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
            ItemStack item = p.getInventory().getItemInMainHand();
            for (String s:item.getItemMeta().getLore()){
                for (MagicI m: Main.magicArray){
                    if (s.equals(m.name)){
                        m.play(p);
                    }
                }
            }
        }
    }
}

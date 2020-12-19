package rpg;

import com.mysql.fabric.xmlrpc.base.Array;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.CommandBlock;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.text.translate.EntityArrays;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import java.io.*;
import java.util.*;

public class Main extends JavaPlugin{
    public static Block[][] mines;
    public static ArrayList<ArmorStand> stands = new ArrayList<ArmorStand>();
    public static int[][] map;
    public static int size;
    public static int bombs;
    @Override
    public void onEnable() {
        Bukkit.getPluginCommand("minestart").setExecutor(this);
        Bukkit.getPluginCommand("minestop").setExecutor(this);
        Bukkit.getServer().getPluginManager().registerEvents(new MainEvent(),this);

    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equals("minestart") && args.length == 2) {
            if (sender instanceof Player){
                Location l = ((Player) sender).getLocation();
                Location p = l.clone();
                size = Integer.parseInt(args[0]);
                bombs = Integer.parseInt(args[1]);
                mines = new Block[size][size];
                for (int i = 0;i < size; i++){
                    for (int j = 0; j < size;j++) {
                        Block b = p.add(i,0,j).getBlock();
                        b.setType(Material.STONE);
                        mines[i][j] = b;
                        p = l.clone();
                    }
                }
                /*map = mapMaker(size, bombs);
                String s = "";
                for (int i = 0; i < size;i++) {
                    for (int j = 0; j < size;j++) {
                        s = s + String.valueOf(map[i][j]) + " ";
                    }
                    sender.sendMessage(s);
                    s="";
                }*/
            }
        }else if (command.getName().equals("minestop")){
            init();
        }
        return true;
    }

    public static int[][] mapMaker(int size, int bombs, int x, int y){
        int bsize = size*size;
        if (bsize >= bombs) {
            int[][] map = new int[size][size];
            map[x][y] = 100;
            int loc;
            int loc2;
            for (int i = 0; i < bombs; i++) {
                while (true) {
                    Random r = new Random();
                    loc = r.nextInt(size);
                    loc2 = r.nextInt(size);
                    if (map[loc][loc2] != -1 && map[loc][loc2] < 100) {
                        map[loc][loc2] = -1;
                        for (int a = -1;a < 2;a++){
                            for (int b = -1;b < 2;b++){
                                if (((loc + a) >= 0 && (loc2 + b) >= 0) && (((loc + a) < size) && ((loc2 + b) < size))){
                                    if (map[loc+a][loc2+b] != -1){
                                        map[loc+a][loc2+b]++;
                                    }
                                }
                            }
                        }
                        break;
                    }
                }
            }
            map[x][y] = map[x][y] - 100;
            return map;
        }else {
            return null;
        }
    }

    public static void init(){
        map = null;
        for (ArmorStand stand:stands) {
            stand.setHealth(0D);
        }
        mines = null;
        MainEvent.correct = 0;
        MainEvent.wrong = 0;
        //MainEvent.over = true; not need
    }

    @Override
    public void onDisable() {
        init();
    }
}

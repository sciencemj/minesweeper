package rpg;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.block.CommandBlock;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.text.translate.EntityArrays;
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
    public static HashMap<Player, Integer> levels = new HashMap<Player, Integer>();
    public static Queue<Character> cmds = new LinkedList<Character>();
    public static ArrayList<MagicI> magicArray = new ArrayList<MagicI>();
    public static Main pluginm;
    ArrayList<String> except = new ArrayList<String>();
    Player target;
    @Override
    public void onEnable() {
        pluginm = this;
        Bukkit.getPluginCommand("command").setExecutor(this);
        Bukkit.getPluginCommand("rmb").setExecutor(this);
        Bukkit.getPluginCommand("magic").setExecutor(this);
        Bukkit.getPluginManager().registerEvents(new MainEvent(),this);
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
                if (!cmds.isEmpty()){
                    Location l = target.getLocation();
                    switch (cmds.poll()){
                        case 'w':
                            l.setPitch(0f);
                            target.teleport(l);
                            target.setVelocity(target.getLocation().getDirection().setY(0).multiply(0.5f));
                            break;
                        case 's':
                            l.setPitch(0f);
                            target.teleport(l);
                            target.setVelocity(target.getLocation().getDirection().setY(0).multiply(-0.5f));
                            break;
                        case 'a':
                            l.setPitch(0f);
                            l.setYaw(l.getYaw() - 90);
                            target.teleport(l);
                            target.setVelocity(l.getDirection().setY(0).multiply(0.5f));
                            break;
                        case 'd':
                            l.setPitch(0f);
                            l.setYaw(l.getYaw() + 90);
                            target.teleport(l);
                            target.setVelocity(l.getDirection().setY(0).multiply(0.5f));
                            break;
                        case 'j':
                            target.setVelocity(new Vector(0,0.5,0));
                            break;
                        default:
                            target.sendMessage("wrong command");
                    }
                }
            }
        }, 0L, 5L);
        String line = null;
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("save.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        while(true){
            try {
                line = reader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (line == null)
                break;
            String[] str = line.split(" ");
            switch (str[2]){
                case "0":
                    Color color = Color.fromRGB(Integer.parseInt(str[4]),Integer.parseInt(str[5]),Integer.parseInt(str[6]));
                    magicArray.add(new MagicI(str[0],str[1],str[2],Integer.parseInt(str[3]),color,Float.parseFloat(str[7])));
            }
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equals("command")){
            target = Bukkit.getPlayer(args[0]);
            for (char ch : args[1].toCharArray()) {
                cmds.add(ch);
            }
        }else if(command.getName().equals("rmb")){
            if (args[0].equals("except")){
                sender.sendMessage("ex");
                except.add(args[1]);
            }else {
                Location l = new Location(Bukkit.getWorlds().get(0), Double.parseDouble(args[0]), Double.parseDouble(args[1]), Double.parseDouble(args[2]));
                Random r = new Random();
                boolean bool = true;
                int i = 0;
                EntityType type = null;
                while (bool) {
                    type = EntityType.values()[r.nextInt(EntityType.values().length)];
                    bool = false;
                    for (String s : except){
                        if (type.name().equals(s))
                            bool = true;
                    }
                    i++;
                    if (i >= EntityType.values().length){
                        break;
                    }
                }
                Objects.requireNonNull(l.getWorld()).spawnEntity(l, type);
                for (Player p : Bukkit.getOnlinePlayers()) {
                    p.sendTitle(ChatColor.RED + type.name(), "", 20, 0, 0);
                }
            }
        }else if (command.getName().equals("magic")){
            if (args[0].equals("make")) {
                switch (args[2]) {
                    case "0":
                        if (args.length == 8){
                            MagicI magic = new MagicI(sender.getName(), args[1], args[2], Integer.parseInt(args[3]),
                                    Color.fromRGB(Integer.parseInt(args[4]), Integer.parseInt(args[5]), Integer.parseInt(args[6])),
                                    Float.parseFloat(args[7]));

                            sender.sendMessage(magic.maker);
                            magicArray.add(magic);
                        }else {
                            sender.sendMessage("/magic make " + ChatColor.YELLOW + "<name> <type> <range> <red> <green> <blue> <damage>");
                        }
                        break;
                }
            }else if (args[0].equals("item")){
                if (sender instanceof Player) {
                    Player p = Bukkit.getPlayer(args[1]);
                    ItemMeta meta = p.getInventory().getItemInMainHand().getItemMeta();
                    MagicI magic = null;
                    for (MagicI m:magicArray)
                        if (m.name.equals(args[2])) magic = m;
                    if (meta.hasLore()) {
                        List<String> list = meta.getLore();
                        list.add(magic.name);
                        meta.setLore(list);
                        p.getInventory().getItemInMainHand().setItemMeta(meta);
                    } else {
                        List<String> list = new ArrayList<String>();
                        list.add(magic.name);
                        meta.setLore(list);
                        p.getInventory().getItemInMainHand().setItemMeta(meta);
                    }
                }
            }else if (args[0].equals("list")){
                for (MagicI magic : magicArray){
                    sender.sendMessage(magic.name);
                }
            }
        }
        return false;
    }

    void saveData() throws IOException {
        FileWriter writer = new FileWriter("save.txt");
        for (MagicI magic : magicArray){
            writer.write(magic.getInfo()+"\r\n");
        }
        writer.close();

    }

    @Override
    public void onDisable() {
        try {
            saveData();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

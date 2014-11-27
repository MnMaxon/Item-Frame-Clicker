package me.MnMaxon.ItemFrameClicker;

import java.io.File;
import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {
	public static String dataFolder;
	public static Main plugin;
	public static ArrayList<Player> willCreate;
	public static ArrayList<Player> willDestroy;

	@Override
	public void onEnable() {
		plugin = this;
		dataFolder = this.getDataFolder().getAbsolutePath();
		if(!new File(dataFolder).exists())
			new File(dataFolder).mkdirs();
		YamlConfiguration cfg = Config.Load(dataFolder + "/Data.yml");
		Config.Save(cfg, dataFolder + "/Data.yml");
		getServer().getPluginManager().registerEvents(new PlayerListener(), Main.plugin);
		willCreate = new ArrayList<Player>();
		willDestroy = new ArrayList<Player>();
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player) || !cmd.getName().equalsIgnoreCase("Clicker")) {
			return false;
		}
		Player p = (Player) sender;
		if (!p.isOp())
			return false;
		if (args.length == 0 || args[0].equalsIgnoreCase("help"))
			SendHelp(p);
		else if (args[0].equalsIgnoreCase("create")) {
			if (willCreate.contains(p))
				willCreate.remove(p);
			if (willDestroy.contains(p))
				willDestroy.remove(p);
			willCreate.add(p);
			p.sendMessage(ChatColor.DARK_GREEN + "[IFC] " + ChatColor.DARK_AQUA + "RIGHT CLICK on an empty item frame");
		} else if (args[0].equalsIgnoreCase("Destroy")) {
			if (willCreate.contains(p))
				willCreate.remove(p);
			if (willDestroy.contains(p))
				willDestroy.remove(p);
			willDestroy.add(p);
			p.sendMessage(ChatColor.DARK_GREEN + "[IFC] " + ChatColor.DARK_AQUA
					+ "LEFT CLICK on the item frame you want to break");
		} else
			SendHelp(p);
		return false;
	}

	public void SendHelp(Player p) {
		p.sendMessage("");
		p.sendMessage(ChatColor.DARK_AQUA + "    " + ChatColor.UNDERLINE + "ItemFrameClicker Commands");
		p.sendMessage(ChatColor.DARK_AQUA + "/Clicker Create");
		p.sendMessage(ChatColor.DARK_AQUA + "/Clicker Destroy");
		p.sendMessage("");
	}
}
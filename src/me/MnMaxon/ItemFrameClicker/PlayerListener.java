package me.MnMaxon.ItemFrameClicker;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Rotation;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class PlayerListener implements Listener {

	@EventHandler
	public void onBreak(EntityDamageByEntityEvent e) {
		if (!(e.getDamager() instanceof Player) || !(e.getEntity() instanceof ItemFrame))
			return;
		int frameNumber = frameNum(e.getEntity().getLocation());
		if (frameNumber == -1)
			return;
		YamlConfiguration cfg = Config.Load(Main.dataFolder + "/Data.yml");
		if (Main.willDestroy.contains((Player) e.getDamager())) {
			Main.willDestroy.remove(e.getDamager());
			cfg.set(frameNumber + "", null);
		} else
			e.setCancelled(true);
		Config.Save(cfg, Main.dataFolder + "/Data.yml");
	}

	@EventHandler
	public void onRightClick(PlayerInteractEntityEvent e) {
		if (!(e.getRightClicked() instanceof ItemFrame))
			return;
		int frameNumber = frameNum(e.getRightClicked().getLocation());
		if (frameNumber != -1) {
			e.setCancelled(true);
			if (Main.willCreate.contains(e.getPlayer()))
				e.getPlayer().sendMessage(ChatColor.DARK_RED + "This is already set as a plugin frame");
			else
				giveItems(frameNumber, e.getPlayer());
		} else if (Main.willCreate.contains(e.getPlayer())) {
			e.setCancelled(true);
			Main.willCreate.remove(e.getPlayer());
			createFrame(e.getPlayer(), (ItemFrame) e.getRightClicked());
		}
	}

	private void createFrame(Player p, ItemFrame frame) {
		YamlConfiguration cfg = Config.Load(Main.dataFolder + "/Data.yml");
		int frameNumber = -1;
		for (int i = 0; i < 100; i++) {
			if (cfg.get(i + "") == null) {
				frameNumber = i;
				break;
			}
		}
		int num = 1;
		for (int i = 0; i < 9; i++) {
			if (p.getInventory().getItem(i) != null) {
				ItemStack item = p.getInventory().getItem(i);
				if (num == 1) {
					frame.setItem(item);
					frame.setRotation(Rotation.NONE);
				}
				cfg.set(frameNumber + ".location.x", frame.getLocation().getBlockX());
				cfg.set(frameNumber + ".location.y", frame.getLocation().getBlockY());
				cfg.set(frameNumber + ".location.z", frame.getLocation().getBlockZ());
				cfg.set(frameNumber + "." + num + ".material", item.getType().name());
				cfg.set(frameNumber + "." + num + ".amount", item.getAmount());
				if (item.hasItemMeta())
					cfg.set(frameNumber + "." + num + ".name", item.getItemMeta().getDisplayName());
				if (item.getEnchantments().size() != 0) {
					ArrayList<Enchantment> enchantments = getEnchantments(item);
					ArrayList<String> enchantmentNames = new ArrayList<String>();
					for (int x = 0; x < enchantments.size(); x++) {
						cfg.set(frameNumber + "." + num + ".Enchantments." + enchantments.get(x).toString(),
								item.getEnchantmentLevel(enchantments.get(x)));
						enchantmentNames.add(enchantments.get(x).toString());
					}
					cfg.set(frameNumber + "." + num + ".Enchantments.list", enchantmentNames);
				}
				num++;
			}
		}
		if (num == 1)
			p.sendMessage(ChatColor.DARK_RED + "You're hotbar is empty");
		Config.Save(cfg, Main.dataFolder + "/Data.yml");
	}

	private ArrayList<Enchantment> getEnchantments(ItemStack item) {
		ArrayList<Enchantment> enchantments = new ArrayList<Enchantment>();
		if (item.getEnchantments().containsKey(Enchantment.ARROW_DAMAGE))
			enchantments.add(Enchantment.ARROW_DAMAGE);
		if (item.getEnchantments().containsKey(Enchantment.ARROW_FIRE))
			enchantments.add(Enchantment.ARROW_FIRE);
		if (item.getEnchantments().containsKey(Enchantment.ARROW_INFINITE))
			enchantments.add(Enchantment.ARROW_INFINITE);
		if (item.getEnchantments().containsKey(Enchantment.ARROW_KNOCKBACK))
			enchantments.add(Enchantment.ARROW_KNOCKBACK);
		if (item.getEnchantments().containsKey(Enchantment.DAMAGE_ALL))
			enchantments.add(Enchantment.DAMAGE_ALL);
		if (item.getEnchantments().containsKey(Enchantment.DAMAGE_ARTHROPODS))
			enchantments.add(Enchantment.DAMAGE_ARTHROPODS);
		if (item.getEnchantments().containsKey(Enchantment.DAMAGE_UNDEAD))
			enchantments.add(Enchantment.DAMAGE_UNDEAD);
		if (item.getEnchantments().containsKey(Enchantment.DIG_SPEED))
			enchantments.add(Enchantment.DIG_SPEED);
		if (item.getEnchantments().containsKey(Enchantment.DURABILITY))
			enchantments.add(Enchantment.DURABILITY);
		if (item.getEnchantments().containsKey(Enchantment.FIRE_ASPECT))
			enchantments.add(Enchantment.FIRE_ASPECT);
		if (item.getEnchantments().containsKey(Enchantment.KNOCKBACK))
			enchantments.add(Enchantment.KNOCKBACK);
		if (item.getEnchantments().containsKey(Enchantment.LOOT_BONUS_BLOCKS))
			enchantments.add(Enchantment.LOOT_BONUS_BLOCKS);
		if (item.getEnchantments().containsKey(Enchantment.LOOT_BONUS_MOBS))
			enchantments.add(Enchantment.LOOT_BONUS_MOBS);
		if (item.getEnchantments().containsKey(Enchantment.LUCK))
			enchantments.add(Enchantment.LUCK);
		if (item.getEnchantments().containsKey(Enchantment.LURE))
			enchantments.add(Enchantment.LURE);
		if (item.getEnchantments().containsKey(Enchantment.OXYGEN))
			enchantments.add(Enchantment.OXYGEN);
		if (item.getEnchantments().containsKey(Enchantment.PROTECTION_ENVIRONMENTAL))
			enchantments.add(Enchantment.PROTECTION_ENVIRONMENTAL);
		if (item.getEnchantments().containsKey(Enchantment.PROTECTION_EXPLOSIONS))
			enchantments.add(Enchantment.PROTECTION_EXPLOSIONS);
		if (item.getEnchantments().containsKey(Enchantment.PROTECTION_FALL))
			enchantments.add(Enchantment.PROTECTION_FALL);
		if (item.getEnchantments().containsKey(Enchantment.PROTECTION_FIRE))
			enchantments.add(Enchantment.PROTECTION_FIRE);
		if (item.getEnchantments().containsKey(Enchantment.PROTECTION_PROJECTILE))
			enchantments.add(Enchantment.PROTECTION_PROJECTILE);
		if (item.getEnchantments().containsKey(Enchantment.SILK_TOUCH))
			enchantments.add(Enchantment.SILK_TOUCH);
		if (item.getEnchantments().containsKey(Enchantment.THORNS))
			enchantments.add(Enchantment.THORNS);
		if (item.getEnchantments().containsKey(Enchantment.WATER_WORKER))
			enchantments.add(Enchantment.WATER_WORKER);
		return enchantments;
	}

	private Enchantment getEnchantment(String enchantment) {
		if (enchantment.contains("ARROW_DAMAGE"))
			return Enchantment.ARROW_DAMAGE;
		if (enchantment.contains("ARROW_FIRE"))
			return Enchantment.ARROW_FIRE;
		if (enchantment.contains("ARROW_INFINITE"))
			return Enchantment.ARROW_INFINITE;
		if (enchantment.contains("ARROW_KNOCKBACK"))
			return Enchantment.ARROW_KNOCKBACK;
		if (enchantment.contains("DAMAGE_ALL"))
			return Enchantment.DAMAGE_ALL;
		if (enchantment.contains("DAMAGE_ARTHROPODS"))
			return Enchantment.DAMAGE_ARTHROPODS;
		if (enchantment.contains("DAMAGE_UNDEAD"))
			return Enchantment.DAMAGE_UNDEAD;
		if (enchantment.contains("DIG_SPEED"))
			return Enchantment.DIG_SPEED;
		if (enchantment.contains("DURABILITY"))
			return Enchantment.DURABILITY;
		if (enchantment.contains("FIRE_ASPECT"))
			return Enchantment.FIRE_ASPECT;
		if (enchantment.contains("KNOCKBACK"))
			return Enchantment.KNOCKBACK;
		if (enchantment.contains("LOOT_BONUS_BLOCKS"))
			return Enchantment.LOOT_BONUS_BLOCKS;
		if (enchantment.contains("LOOT_BONUS_MOBS"))
			return Enchantment.LOOT_BONUS_MOBS;
		if (enchantment.contains("LUCK"))
			return Enchantment.LUCK;
		if (enchantment.contains("LURE"))
			return Enchantment.LURE;
		if (enchantment.contains("OXYGEN"))
			return Enchantment.OXYGEN;
		if (enchantment.contains("PROTECTION_ENVIRONMENTAL"))
			return Enchantment.PROTECTION_ENVIRONMENTAL;
		if (enchantment.contains("PROTECTION_EXPLOSIONS"))
			return Enchantment.PROTECTION_EXPLOSIONS;
		if (enchantment.contains("PROTECTION_FALL"))
			return Enchantment.PROTECTION_FALL;
		if (enchantment.contains("PROTECTION_FIRE"))
			return Enchantment.PROTECTION_FIRE;
		if (enchantment.contains("PROTECTION_PROJECTILE"))
			return Enchantment.PROTECTION_PROJECTILE;
		if (enchantment.contains("SILK_TOUCH"))
			return Enchantment.SILK_TOUCH;
		if (enchantment.contains("THORNS"))
			return Enchantment.THORNS;
		if (enchantment.contains("WATER_WORKER"))
			return Enchantment.WATER_WORKER;
		return null;
	}

	public void giveItems(int frameNumber, Player p) {
		YamlConfiguration cfg = Config.Load(Main.dataFolder + "/Data.yml");
		for (int i = 0; i < 10; i++)
			if (cfg.get(frameNumber + "." + i) != null) {
				ItemStack item = new ItemStack(Material.matchMaterial(cfg
						.getString(frameNumber + "." + i + ".material")));
				item.setAmount(cfg.getInt(frameNumber + "." + i + ".amount"));
				if (cfg.get(frameNumber + "." + i + ".name") != null) {
					ItemMeta im = item.getItemMeta();
					im.setDisplayName(cfg.getString(frameNumber + "." + i + ".name"));
					item.setItemMeta(im);
				}
				if (cfg.get(frameNumber + "." + i + ".Enchantments.list") != null) {
					for (int x = 0; x < cfg.getList(frameNumber + "." + i + ".Enchantments.list").size(); x++)
						item.addEnchantment(
								getEnchantment((String) cfg.getList(frameNumber + "." + i + ".Enchantments.list")
										.get(x)), cfg.getInt(frameNumber + "." + i + ".Enchantments."
										+ (String) cfg.getList(frameNumber + "." + i + ".Enchantments.list").get(x)));
				}
				p.getInventory().addItem(item);
			}
	}

	public int frameNum(Location loc) {
		for (int i = 0; i < 100; i++) {
			YamlConfiguration cfg = Config.Load(Main.dataFolder + "/Data.yml");
			if (cfg.get(i + "") != null && cfg.getInt(i + ".location.x") == loc.getBlockX()
					&& cfg.getInt(i + ".location.y") == loc.getBlockY()
					&& cfg.getInt(i + ".location.z") == loc.getBlockZ())
				return i;
		}
		return -1;
	}
}

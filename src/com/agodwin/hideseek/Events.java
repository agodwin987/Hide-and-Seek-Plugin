package com.agodwin.hideseek;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Events implements Listener {
	@EventHandler
	public void onBreak(BlockBreakEvent e) {
		if (Main.inArena.containsKey(e.getPlayer().getName())) {
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void onPlace(BlockPlaceEvent e) {
		if (Main.inArena.containsKey(e.getPlayer().getName())) {
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void onHit(EntityDamageByEntityEvent e) {
		if (e.getDamager().hasMetadata("team")
				&& e.getEntity().hasMetadata("team")
				&& e.getDamager().getMetadata("team").get(0).asString()
						.equals("seeker")
				&& e.getDamager() instanceof Player
				&& !e.getEntity().getMetadata("team").get(0).asString()
						.equals("seeker") && e.getEntity() instanceof Player) {
			// switch that team motha fucka
			Player killed = (Player) e.getEntity();
			killed.setHealth(0);
		}
		if (e.getCause() == DamageCause.PROJECTILE
				&& e.getDamager() instanceof Snowball) {
			Player p = (Player) e.getEntity();
			p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 100,
					1));
			p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 1));
		}
	}

	@EventHandler
	public void playerRespawnEvent(PlayerRespawnEvent e) {
		if (!Main.inArena.containsKey(e.getPlayer().getName())) {
			return;
		}
		// set respawn,
		// change death message
		// set new meta
		Main m = new Main();
		e.setRespawnLocation(m.loc(e.getPlayer()));
		if (e.getPlayer().hasMetadata("team")
				&& !e.getPlayer().getMetadata("team").get(0).asString()
						.equals("seeker")) {
			Bukkit.broadcastMessage(ChatColor.BLUE + e.getPlayer().getName()
					+ ChatColor.RED + " is now a seeker! Watch out!");
			if (m.inArena.get(e.getPlayer().getName()).seekers == m.inArena
					.get(e.getPlayer().getName()).getNumPlayers()) {
				m.inArena.get(e.getPlayer().getName()).endCurrentGame();
			}
			m.inArena.get(e.getPlayer().getName()).seekers++;
			e.getPlayer().setMetadata("team",
					new FixedMetadataValue(Main.getPlugin(), "seeker"));
		}
	}
}

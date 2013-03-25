package com.agodwin.hideseek;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

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
		if (e.getDamager().getMetadata("team").get(0).asString().equals("seeker")
				&& e.getDamager() instanceof Player && !e.getEntity().getMetadata("team").get(0).asString().equals("seeker") && e.getEntity() instanceof Player) {
			//switch that team motha fucka
			
		}
	}
}

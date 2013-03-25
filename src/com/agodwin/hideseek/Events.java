package com.agodwin.hideseek;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class Events implements Listener {
	@EventHandler
	public void onBreak(BlockBreakEvent e) {
		if (Main.inArena.containsKey(e.getPlayer().getName())) {
			e.setCancelled(true);
		}
	}
}

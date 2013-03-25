package com.agodwin.hideseek;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class Events implements Listener {
	@EventHandler
	public void onBreak(BlockBreakEvent e) {
		if (Main.inArena.containsKey(e.getPlayer().getName())) {
			e.setCancelled(true);
		}
	}
	@EventHandler
	public void onPlace(BlockPlaceEvent e){
		if (Main.inArena.containsKey(e.getPlayer().getName())){
			e.setCancelled(true);
		}
	}
}

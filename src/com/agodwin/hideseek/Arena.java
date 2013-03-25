package com.agodwin.hideseek;

import org.bukkit.Location;

public class Arena {
	private String arenaName;
	private Location seekerSpawnLoc;
	private Location hiderSpawnLoc;

	public String getArenaName() {
		return arenaName;
	}

	public void setArenaName(String arenaName) {
		this.arenaName = arenaName;
	}

	public Location getHiderSpawnLoc() {
		return hiderSpawnLoc;
	}

	public void setHiderSpawnLoc(Location hiderSpawnLoc) {
		this.hiderSpawnLoc = hiderSpawnLoc;
	}

	public Location getSeekerSpawnLoc() {
		return seekerSpawnLoc;
	}

	public void setSeekerSpawnLoc(Location seekerSpawnLoc) {
		this.seekerSpawnLoc = seekerSpawnLoc;
	}
	

}

package com.agodwin.hideseek;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class Arena {
	private String arenaName;
	private Location seekerSpawnLoc;
	private Location hiderSpawnLoc;
	private Location leaveLoc;
	private Location lobbyLocation;
	private ArrayList<Player> players = new ArrayList<Player>();
	private int maxPlayers;

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

	public Location getLeaveLoc() {
		return leaveLoc;
	}

	public void setLeaveLoc(Location leaveLoc) {
		this.leaveLoc = leaveLoc;
	}

	public ArrayList<Player> getPlayers() {
		return players;
	}

	public void addPlayer(Player p) {
		this.players.add(p);
	}
	
	public int getNumPlayers() {
		return this.players.size();
	}

	public int getMaxPlayers() {
		return maxPlayers;
	}

	public void setMaxPlayers(int maxPlayers) {
		this.maxPlayers = maxPlayers;
	}

	public Location getLobbyLocation() {
		return lobbyLocation;
	}

	public void setLobbyLocation(Location lobbyLocation) {
		this.lobbyLocation = lobbyLocation;
	}
	
	public void startArena() {
		
	}
}

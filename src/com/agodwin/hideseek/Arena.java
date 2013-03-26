package com.agodwin.hideseek;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

public class Arena implements ConfigurationSerializable {
	private String arenaName;
	private Location seekerSpawnLoc;
	private Location hiderSpawnLoc;
	private Location leaveLoc;
	private Location lobbyLocation;
	private ArrayList<Player> players = new ArrayList<Player>();
	private int maxPlayers;
	private boolean isInProgress = false;
	private Player seeker;

	public Arena(String arenaName, Location seekerSpawnLoc,
			Location hiderSpawnLoc, Location leaveLoc, Location lobbyLocation,
			ArrayList<Player> players, int maxPlayers, boolean isInProgress,
			Player seeker) {
		this.arenaName = arenaName;
		this.seekerSpawnLoc = seekerSpawnLoc;
		this.hiderSpawnLoc = hiderSpawnLoc;
		this.leaveLoc = leaveLoc;
		this.lobbyLocation = lobbyLocation;
		this.players = players;
		this.maxPlayers = maxPlayers;
		this.isInProgress = isInProgress;
		this.seeker = seeker;
	}

	public Arena(String name) {
		arenaName = name;
	}

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
		shuffleTeam();
		Random r = new Random();
		int rand = r.nextInt(players.size());
		if (players.size() > 0) {
			int count = 0;
			for (Player p : players) {
				if (p != null) {
					Main.inArena.put(p.getName(), this);
					if (rand == count) {
						p.setMetadata("team",
								new FixedMetadataValue(Main.getPlugin(),
										"seeker"));
						p.teleport(seekerSpawnLoc);
						seeker = p;
						p.sendMessage("You are a seeker!");
					} else {
						p.setMetadata("team",
								new FixedMetadataValue(Main.getPlugin(),
										"hider"));
						p.teleport(hiderSpawnLoc);
					}
				} else {
					players.remove(p);
				}
				count++;
			}
		}
		isInProgress = true;
	}

	public void safelyRemovePlayer(Player p) {
		if (!(players.contains(p) && Main.inArena.containsKey(p.getName()))) {
			p.sendMessage(Main.helper+ChatColor.RED+"Quit it you nigger.");
		}
		if (seeker != null && seeker.equals(p)) {
			players.remove(p);
			p.teleport(leaveLoc);
			p.removeMetadata("team", Main.getPlugin());
			seeker = null;
		} else {
			players.remove(p);
			p.teleport(leaveLoc);
			p.removeMetadata("team", Main.getPlugin());
		}
		Main.inArena.remove(p.getName());
		p.sendMessage(Main.helper + "Safely removed from arena "
				+ this.getArenaName() + ". Thanks!");
	}
	
	public void endCurrentGame () {
		for (Player p : players) {
			safelyRemovePlayer(p);
		}
		isInProgress = false;
	}

	public boolean playerInArena(Player p) {
		return players.contains(p);
	}

	public boolean arenaInProgress() {
		return isInProgress;
	}

	private void shuffleTeam() {
		Collections.shuffle(this.players);
	}
	
	public void broadcastMessage(String message) {
		for (Player p : players)
			p.sendMessage(message);
	}

	@Override
	public Map<String, Object> serialize() {
		Map<String, Object> m = new HashMap<String, Object>();
		m.put("name", this.arenaName);
		m.put("hiderSpawnLocation", new PaperLocation(this.hiderSpawnLoc).serialize());
		m.put("seekerSpawnLocation", new PaperLocation(this.seekerSpawnLoc).serialize());
		m.put("leaveLocation", new PaperLocation(this.leaveLoc).serialize());
		m.put("lobbyLocation", new PaperLocation(this.lobbyLocation).serialize());
		return m;
	}
}

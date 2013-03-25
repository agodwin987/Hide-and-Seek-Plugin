package com.agodwin.hideseek;

import java.util.HashMap;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

	private String wiki = "http://pornhum.com/";
	public static String helper = ChatColor.GOLD + "[H&S Helper]" + ChatColor.RED
			+ " ";
	public String info = ChatColor.GOLD + "[H&S info]" + ChatColor.AQUA
			+ " ";
	public final static HashMap<String, Arena> inArena = new HashMap<String, Arena>();
	public static int maxArenas = 100;
	public final static String[] arenaNames = new String[maxArenas];
	public final Location[] arenaLobby = new Location[maxArenas];
	public final static Location[] arenaSpawnSeek = new Location[maxArenas];
	public final Location[] arenaSpawnHide = new Location[maxArenas];
	public final Location[] arenaLeave = new Location[maxArenas];
	public final int[] players = new int[maxArenas];
	private HashMap<String, Arena> arenas = new HashMap<String, Arena>();
	public int arenaCounter = 0;
	private Events events;
	private static Plugin p = null;

	@Override
	public void onEnable() {
		getConfig().options().copyDefaults(true);
		saveConfig();
		events = new Events();
		register();
		getLogger().log(Level.SEVERE, "Sup, nigga bitch?");
		p = this;
	}

	@Override
	public void onDisable() {

	}

	public void register() {
		this.getServer().getPluginManager().registerEvents(events, this);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String CommandLabel, String[] args) {
		Player p = (Player) sender;
		if (CommandLabel.equalsIgnoreCase("hns")) {
			if (args.length == 0) {
				p.sendMessage(info + "Welcome to Hide and Seek version "
						+ this.getDescription().getVersion());
				p.sendMessage(info + "Do /hns help to get basic commands");

			} else if (args[0].equalsIgnoreCase("help")) {
				p.sendMessage(ChatColor.GOLD
						+ "****************************************************");
				p.sendMessage(ChatColor.GREEN
						+ "do /hns join <arena> to play Hide and Seek");
				p.sendMessage(ChatColor.GREEN
						+ "do /hns leave to leave Hide and Seek");
				p.sendMessage(ChatColor.GREEN + "do /hns list to see areanas");
				p.sendMessage(ChatColor.GREEN + "Visit the Wiki here: "
						+ ChatColor.BLUE + wiki + ChatColor.GREEN
						+ " for more advanced commands");
				p.sendMessage(ChatColor.GOLD
						+ "****************************************************");
			} else if (args[0].equalsIgnoreCase("join")) {
				if (inArena.containsKey(p.getName())) {
					p.sendMessage(helper
							+ "You must leave this game before you can join another!");
				} else {
					if (args.length == 1) {
						p.sendMessage(helper + "Please enter an arena to join.");
					} else {
						// for (int i = 0; i < arenaNames.length;) {
						// String requestedName = arenaNames[i];
						// if (args[1].equalsIgnoreCase(requestedName)) {
						// Location lobbyLocation = arenaLobby[i];
						// Location test = arenaSpawnHide[i];
						// p.teleport(lobbyLocation);
						// p.sendMessage(info + "You chose to join: " +
						// ChatColor.GOLD + args[1]);
						// inArena.put(p.getName(), arenaNames[i]);
						// players[i]++;
						// p.sendMessage(info+"There are "+ChatColor.GOLD+players[i]+"/10"+ChatColor.AQUA+"players");
						// if (players[i] == 2) {
						// players[i] = 0;
						// for(Player player : Bukkit.getOnlinePlayers()){
						// if(inArena.containsKey(player.getName())){
						// player.teleport(test);
						// player.setMetadata("team", new
						// FixedMetadataValue(this, "hider"));
						// }
						// }
						//
						// break;
						// }
						// break;
						// } else {
						// p.sendMessage(helper + "That is not a valid name");
						// break;
						// }
						// }

						if (arenas.containsKey(args[1])) {
							// put them in the arena
							Arena joining = arenas.get(args[1]);
							if (!joining.arenaInProgress()) {
								joining.addPlayer(p);
								inArena.put(p.getName(), joining);
								p.teleport(joining.getLobbyLocation());
								p.sendMessage(info + "You chose to join: "
										+ ChatColor.GOLD
										+ joining.getArenaName());
								p.sendMessage(info + "There are "
										+ ChatColor.GOLD
										+ joining.getNumPlayers() + "/"
										+ joining.getMaxPlayers()
										+ ChatColor.AQUA + " players");
								if (joining.getNumPlayers() >= 2) {
									joining.startArena();
								}
							} else {
								p.sendMessage(helper
										+ "That arena is in progress. Please try another arena or wait for the game to finish.");
							}
						} else {
							p.sendMessage(helper
									+ "That is not a valid arena name. Please try again.");
							for (String name : arenas.keySet()) {
								if (Utils.similar(name, args[1]) > .8D) {
									p.sendMessage("You may have meant: " + name
											+ ".");
								}
							}
						}

					}
				}
			} else if (args[0].equalsIgnoreCase("list")) {
				String message = "";
				for (String name : arenas.keySet()) {
					message += name + ", ";
				}
				p.sendMessage(info + message.substring(0, message.lastIndexOf((int)',')));
			}

			else if (args[0].equalsIgnoreCase("leave")) {
				if (inArena.containsKey(p.getName())) {
					if (inArena.get(p.getName()).arenaInProgress()) {
						inArena.get(p.getName()).safelyRemovePlayer(p);
					}
				}
			} else if (args[0].equalsIgnoreCase("create")) {
				if (args.length == 1) {
					p.sendMessage(helper + "You must enter an arena name!");
				} else if (!(arenaCounter > maxArenas)) {
					p.sendMessage(info + "You created an arena with the name: "
							+ ChatColor.GOLD + args[1]);
					p.sendMessage(info
							+ "To setup "
							+ ChatColor.GOLD
							+ args[1]
							+ ChatColor.AQUA
							+ ", Enter the commands "
							+ ChatColor.RED
							+ "/hns markpoint <lobby, hidespawn, seekspawn, leave>");
					arenaNames[arenaCounter] = args[1];
				} else {
					p.sendMessage(helper
							+ "You have reached the maximum number of arenas, this can be change in your config file.");
				}
			} else if (args[0].equalsIgnoreCase("markpoint")) {
				if (args.length == 1) {
					p.sendMessage(helper
							+ "Try like this: "
							+ ChatColor.RED
							+ "/hns markpoint <lobby, hidespawn, seekspawn, leave>");
				} else if (args[1].equalsIgnoreCase("lobby")) {
					p.sendMessage(info + "You marked the lobby location");
					arenaLobby[arenaCounter] = p.getLocation();
				} else if (args[1].equalsIgnoreCase("hidespawn")) {
					p.sendMessage(info + "You marked the Hider Spawn location");
					arenaSpawnHide[arenaCounter] = p.getLocation();
				} else if (args[1].equalsIgnoreCase("seekspawn")) {
					p.sendMessage(info + "You marked the Seeker Spawn location");
					arenaSpawnSeek[arenaCounter] = p.getLocation();
				} else if (args[1].equalsIgnoreCase("leave")) {
					p.sendMessage(info + "You marked the Leave location");
					arenaLeave[arenaCounter] = p.getLocation();
				} else if (args[1].equalsIgnoreCase("done")) {
					p.sendMessage(info + "You finished the arena");
					arenaCounter++;
				} else {
					p.sendMessage(helper + "Unknown argument");
				}
			} else {
				p.sendMessage(helper + "Unknown command do /hns help");
			}
		}
		return false;
	}

	public static Location loc(Player pl) {
		Location loc = null;
		for (int i = 0; i < arenaNames.length; i++) {
			if (inArena.containsKey(pl) && inArena.containsValue(arenaNames[i])) {
				loc = arenaSpawnSeek[i];
				break;
			}
		}
		return loc;
	}

	public static Plugin getPlugin() {
		return p;
	}
}

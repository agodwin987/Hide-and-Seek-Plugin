package com.agodwin.hideseek;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

	private String wiki = "http://pornhub.com/";
	public static String helper = ChatColor.GOLD + "[H&S Helper]"
			+ ChatColor.RED + " ";
	public String info = ChatColor.GOLD + "[H&S info]" + ChatColor.AQUA + " ";

	public final static HashMap<String, Arena> inArena = new HashMap<String, Arena>();
	private HashMap<String, Arena> arenas = new HashMap<String, Arena>();

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
		getLogger().log(Level.INFO,
				"Loading up the stuff. Hold on nigga bitch.");
		loadData();
		getLogger().log(Level.INFO, "Done loading up the stuff.");
	}

	@Override
	public void onDisable() {
		storeData();
		saveConfig();
	}

	@SuppressWarnings("unchecked")
	private void loadData() {
		for (String key : getConfig().getKeys(false)) {
			Arena a = Utils.deserializeArena((Map<String, Object>) getConfig()
					.getMapList(key).get(0));
			getLogger().log(Level.INFO, "Deserialized: " + a.getArenaName());
			arenas.put(a.getArenaName(), a);
		}
	}

	private void storeData() {
		getLogger().log(Level.INFO, info + "Storing Arena Data...");
		for (Arena a : arenas.values()) {
			getConfig().set(a.getArenaName(), a.serialize());
			getLogger().log(Level.INFO,
					info + "Saved arena: " + a.getArenaName());
		}
		getLogger().log(Level.INFO,
				info + "Finished saving the arenas. Bye mother fucker!");
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
			} else if (args[0].equalsIgnoreCase("slots")) {
				if (args.length == 1) {
					p.sendMessage(helper + "Please enter an arena to change.");
				} else {
					Arena joining = arenas.get(args[1]);
					if (joining != null) {
						joining.setMaxPlayers(Integer.parseInt(args[2]));
						p.sendMessage(helper + "Set the slots in arena "
								+ joining.getArenaName() + " to "
								+ Integer.parseInt(args[2]));
					}
				}
			} else if (args[0].equalsIgnoreCase("join")) {
				if (inArena.containsKey(p.getName())) {
					p.sendMessage(helper
							+ "You must leave this game before you can join another!");
				} else {
					if (args.length == 1) {
						p.sendMessage(helper + "Please enter an arena to join.");
					} else {
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
								if (joining.getNumPlayers() >= joining
										.getMaxPlayers()) {
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
				String message = "Arenas: ";
				Arena a;
				for (String name : arenas.keySet()) {
					a = arenas.get(name);
					message += name
							+ ((a.arenaInProgress()) ? " [" + ChatColor.GREEN
									+ "Arena In Progress" + ChatColor.AQUA
									+ "]" : "") + ", ";
				}
				if (!message.isEmpty())
					p.sendMessage(info
							+ message.substring(0,
									message.lastIndexOf((int) ',')));
			}

			else if (args[0].equalsIgnoreCase("leave")) {
				if (inArena.containsKey(p.getName())) {
					inArena.get(p.getName()).safelyRemovePlayer(p);
				} else {
					p.sendMessage(helper + "You aint in no arena dumb niggah!");
				}
			} else if (args[0].equalsIgnoreCase("create")) {
				if (args.length == 1) {
					p.sendMessage(helper + "You must enter an arena name!");
				} else if (args.length == 2) {
					p.sendMessage(info + "You created an arena with the name: "
							+ ChatColor.GOLD + args[1]);
					p.sendMessage(info
							+ "To setup "
							+ ChatColor.GOLD
							+ args[1]
							+ ChatColor.AQUA
							+ ", Enter the commands "
							+ ChatColor.RED
							+ "/hns markpoint <arena name> <lobby, hidespawn, seekspawn, leave>");
					arenas.put(args[1], new Arena(args[1]));
				} else {
					p.sendMessage(helper
							+ "You have entered the command incorrectly. You are a dumb shit.");
				}
			} else if (args[0].equalsIgnoreCase("markpoint")) {
				if (args.length != 3) {
					p.sendMessage(helper
							+ "Try like this: "
							+ ChatColor.RED
							+ "/hns markpoint <arena name> <lobby, hidespawn, seekspawn, leave>");
					return false;
				}

				String arg = args[2];
				String arenaName = args[1];

				if (!arenas.containsKey(arenaName)) {
					if (arenas.containsKey(arg)) {
						// they switched that shit up
						arg = args[1];
						arenaName = args[2];
					} else {
						p.sendMessage(helper
								+ "I was unable to determine the arena. \nTry like this: "
								+ ChatColor.RED
								+ "/hns markpoint <arena name> <lobby, hidespawn, seekspawn, leave>");
						return false;
					}
				}

				Arena a = arenas.get(arenaName);

				if (arg.equalsIgnoreCase("lobby")) {
					p.sendMessage(info + "You marked the lobby location");
					a.setLobbyLocation(p.getLocation());
				} else if (arg.equalsIgnoreCase("hidespawn")) {
					p.sendMessage(info + "You marked the Hider Spawn location");
					a.setHiderSpawnLoc(p.getLocation());
				} else if (arg.equalsIgnoreCase("seekspawn")) {
					p.sendMessage(info + "You marked the Seeker Spawn location");
					a.setSeekerSpawnLoc(p.getLocation());
				} else if (arg.equalsIgnoreCase("leave")) {
					p.sendMessage(info + "You marked the Leave location");
					a.setLeaveLoc(p.getLocation());
				} else {
					p.sendMessage(helper + "Unknown argument");
				}
			} else {
				p.sendMessage(helper + "Unknown command do /hns help");
			}
		}
		return false;
	}

	public Location loc(Player pl) {
		if (inArena.containsKey(pl.getName())) {
			return inArena.get(pl.getName()).getSeekerSpawnLoc();
		}
		return null;
	}

	public static Plugin getPlugin() {
		return p;
	}
}

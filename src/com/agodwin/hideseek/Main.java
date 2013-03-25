package com.agodwin.hideseek;

import java.util.HashMap;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

	private String wiki;
	private String helper = ChatColor.GOLD + "[H&S Helper]" + ChatColor.RED + " ";
	protected String info = ChatColor.GOLD + "[H&S info]" + ChatColor.AQUA + " ";
	public final static HashMap<String, String> inArena = new HashMap<String, String>();
	public int maxArenas = 100;
	public final String[] arenaNames = new String[maxArenas];
	public final Location[] arenaLobby = new Location[maxArenas];
	public final Location[] arenaSpawnSeek = new Location[maxArenas];
	public final Location[] arenaSpawnHide = new Location[maxArenas];
	public final Location[] arenaLeave = new Location[maxArenas];
	public final int[] players = new int[maxArenas];
	public int arenaCounter = 0;
	private Events events;
	

	@Override
	public void onEnable() {
		getConfig().options().copyDefaults(true);
		saveConfig();
		events = new Events();
		register();
		getLogger().log(Level.SEVERE, "Sup, nigga bitch?");
	}

	@Override
	public void onDisable() {

	}

	public void register() {
		this.getServer().getPluginManager().registerEvents(events, this);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String CommandLabel, String[] args) {
		Player p = (Player) sender;
		if (CommandLabel.equalsIgnoreCase("hns")) {
			if (args.length == 0) {
				p.sendMessage(info + "Welcome to Hide and Seek version " + this.getDescription().getVersion());
				p.sendMessage(info + "Do /hns help to get basic commands");

			} else if (args[0].equalsIgnoreCase("help")) {
				p.sendMessage(ChatColor.GOLD + "****************************************************");
				p.sendMessage(ChatColor.GREEN + "do /hns join <arena> to play Hide and Seek");
				p.sendMessage(ChatColor.GREEN + "do /hns leave to leave Hide and Seek");
				p.sendMessage(ChatColor.GREEN + "do /hns list to see areanas");
				p.sendMessage(ChatColor.GREEN + "Visit the Wiki here: " + ChatColor.BLUE + wiki + ChatColor.GREEN + " for more advanced commands");
				p.sendMessage(ChatColor.GOLD + "****************************************************");
			} else if (args[0].equalsIgnoreCase("join")) {
				if (inArena.containsKey(p.getName())) {
					p.sendMessage(helper + "You must leave this game before you can join another!");
				} else {
					if (args.length == 1) {
						p.sendMessage(helper + "Please enter an arena to join.");
					} else {
						for (int i = 0; i < arenaNames.length;) {
							String requestedName = arenaNames[i];
							if (args[1].equalsIgnoreCase(requestedName)) {
								Location lobbyLocation = arenaLobby[i];
								Location test = arenaSpawnHide[i];
								p.teleport(lobbyLocation);
								p.sendMessage(info + "You chose to join: " + ChatColor.GOLD + args[1]);
								inArena.put(p.getName(), arenaNames[i]);
								players[i]++;
								p.sendMessage(info+"There are "+ChatColor.GOLD+players[i]+"/10"+ChatColor.AQUA+"players");
								if (players[i] == 2) {
									players[i] = 0;
									for(Player player : Bukkit.getOnlinePlayers()){
										if(inArena.containsKey(player.getName())){
												player.teleport(test);
										}
									}
									
									break;
								}
								break;
							} else {
								p.sendMessage(helper + "That is not a valid name");
								break;
							}
						}

					}
				}
			} else if (args[0].equalsIgnoreCase("list")) {
				String message = "";
				for (int i = 0; i < arenaCounter; i++) {
					message += arenaNames[i] + ", ";
				}
				p.sendMessage(info + message);
			} 

			else if (args[0].equalsIgnoreCase("leave")) {
				for(int i = 0; i < arenaNames.length; i++){
				if (inArena.containsKey(p.getName())&&inArena.containsValue(arenaNames[i])) {
					inArena.remove(p.getName());
					p.sendMessage(info + "You left the arena");
					p.teleport(arenaLeave[i]);
					break;
				
				} else if (!inArena.containsKey(p.getName())) {
					p.sendMessage(helper + "You must be in an arena to leave one!");
					break;
				}
			}
			} else if (args[0].equalsIgnoreCase("create")) {
				if (args.length == 1) {
					p.sendMessage(helper + "You must enter an arena name!");
				} else if (!(arenaCounter > maxArenas)) {
					p.sendMessage(info + "You created an arena with the name: " + ChatColor.GOLD + args[1]);
					p.sendMessage(info + "To setup " +ChatColor.GOLD+ args[1] + ChatColor.AQUA+ ", Enter the commands " + ChatColor.RED + "/hns markpoint <lobby, hidespawn, seekspawn, leave>");
					arenaNames[arenaCounter] = args[1];
				} else {
					p.sendMessage(helper + "You have reached the maximum number of arenas, this can be change in your config file.");
				}
			} else if (args[0].equalsIgnoreCase("markpoint")) {
				if (args.length == 1) {
					p.sendMessage(helper + "Try like this: " + ChatColor.RED + "/hns markpoint <lobby, hidespawn, seekspawn, leave>");
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
	public static Location loc(){
		return null;
		
	}
}

package com.agodwin.hideseek;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class PaperLocation {
	private double x;
	private double y;
	private double z;
	private float pitch;
	private float yaw;
	private static final String SEPERATOR = "|";
	private String world;

	public PaperLocation(Location l) {
		x = l.getX();
		y = l.getY();
		z = l.getZ();
		pitch = l.getPitch();
		yaw = l.getYaw();
		world = l.getWorld().getName();
	}

	public static Location deserialize(String m) {
		String[] things = m.split(SEPERATOR);
		double x = Double.parseDouble(things[0]);
		double y = Double.parseDouble(things[1]);
		double z = Double.parseDouble(things[2]);
		float pitch = Float.parseFloat(things[3]);
		float yaw = Float.parseFloat(things[4]);
		World w = Bukkit.getWorld(things[5]);

		Location l = new Location(w, x, y, z, pitch, yaw);
		return l;
	}

	public String serialize() {
		// TODO Auto-generated method stub
		return x+SEPERATOR+y+SEPERATOR+z+SEPERATOR+pitch+SEPERATOR+yaw+SEPERATOR+world;
	}
}

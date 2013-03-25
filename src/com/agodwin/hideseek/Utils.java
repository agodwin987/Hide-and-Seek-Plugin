package com.agodwin.hideseek;

import java.util.Collection;
import java.util.HashSet;

public class Utils {
	public static double similar(String str1, String str2) {
		return ((double) intersect(str1, str2) / union(str1, str2));
	}

	private static int union(String str1, String str2) {
		Collection<String> col1 = new HashSet<String>();
		Collection<String> col2 = new HashSet<String>();
		for (char s : str1.toCharArray())
			col1.add(new String(s + ""));
		for (char s : str2.toCharArray())
			col2.add(new String(s + ""));
		col1.addAll(col2);
		return col1.size();
	}

	private static int intersect(String str1, String str2) {
		Collection<String> col1 = new HashSet<String>();
		Collection<String> col2 = new HashSet<String>();
		for (char s : str1.toCharArray())
			col1.add(new String(s + ""));
		for (char s : str2.toCharArray())
			col2.add(new String(s + ""));
		col1.retainAll(col2);
		return col1.size();
	}
}

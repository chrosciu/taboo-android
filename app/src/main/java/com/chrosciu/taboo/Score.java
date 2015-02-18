package com.chrosciu.taboo;

public class Score {
	
	private static int first = 0;
	private static int second = 0;
	
	public synchronized static void reset() {
		first = 0;
		second = 0;
	}
	
	public synchronized static void add(boolean isFirst, int count) {
		if (isFirst) {
			first += count;
		} else {
			second += count;
		}
	}
	
	public static int get(boolean isFirst) {
		return isFirst ? first : second;
	}

}

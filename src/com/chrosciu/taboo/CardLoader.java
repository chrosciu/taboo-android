package com.chrosciu.taboo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Random;

import android.content.res.AssetManager;
import android.util.Log;

public class CardLoader {
	
	private static final String TAG = "CardLoader";
	private static int cardCount = -1;
	private static Random random = new Random();
	
	public static void initialize(AssetManager assetManager) {
		if (cardCount < 0) {
			try {
				cardCount = assetManager.list("cards").length;
			} catch (IOException e) {
				Log.e(TAG, "", e);
				cardCount = 0;	
			}
		}
	}
	
	public static Card loadCard(AssetManager assetManager) {
		int tabooNumber = random.nextInt(cardCount) + 1;
		String tabooFile = "cards/taboo" + tabooNumber + ".txt";
		
		try {
			InputStream inputStream = assetManager.open(tabooFile, AssetManager.ACCESS_BUFFER);
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
			String line = bufferedReader.readLine();
			
			String[] taboos = line.split(";");
			Card card = new Card(taboos[0].toUpperCase(), Arrays.copyOfRange(taboos, 1, 6));
			return card;
		} catch (IOException e) {
			Log.e(TAG, "", e);
			return null;
		}
	}

}

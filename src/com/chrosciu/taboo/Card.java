package com.chrosciu.taboo;

import java.io.Serializable;

public class Card implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String word;
	private String[] taboos;
	
	public Card(String word, String[] taboos) {
		this.word = word;
		this.taboos = taboos;
	}
	
	public String getWord() {
		return word;
	}
	
	public String[] getTaboos() {
		return taboos;
	}

}

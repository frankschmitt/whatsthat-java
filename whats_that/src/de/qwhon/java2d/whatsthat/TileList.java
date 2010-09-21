package de.qwhon.java2d.whatsthat;

import java.util.ArrayList;
import java.util.Random;

class TileList {

	ArrayList<Integer> VisibleTiles;
	ArrayList<Integer> InvisibleTiles;
	Random random;
	private int NumTiles;

	public TileList(int NumTiles) {
		this.random = new Random();
		this.VisibleTiles = new ArrayList<Integer>();
		this.InvisibleTiles = new ArrayList<Integer>();
		this.NumTiles = NumTiles;
		initTileLists();
	}

	private void initTileLists() {
		VisibleTiles.clear();
		InvisibleTiles.clear();
		for (int i=0; i< NumTiles; ++i) {
			InvisibleTiles.add(i);
		}
	}

	public void setNumTiles(int NumTiles) {
	  this.NumTiles = NumTiles;
	  initTileLists();
	}
	
	public Boolean nextTile() {
		if (!InvisibleTiles.isEmpty()) {
			int index = random.nextInt(InvisibleTiles.size());
			VisibleTiles.add(InvisibleTiles.get(index));
			InvisibleTiles.remove(index);
			return true;
		}
		else {
			return false;
		}
	}

	public Boolean isTileVisible(int index) {
		return VisibleTiles.contains(index);
	}

	public void reset() {
		InvisibleTiles.addAll(VisibleTiles);
		VisibleTiles.clear();
	}

	public int getNumVisibleTiles() {
		return VisibleTiles.size();
	}

	public int getNumTiles() {
		return NumTiles;
	}
}
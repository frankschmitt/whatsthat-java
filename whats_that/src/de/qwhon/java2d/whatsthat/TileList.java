package de.qwhon.java2d.whatsthat;

import java.util.ArrayList;
import java.util.Random;

class TileList {

  ArrayList<Integer> VisibleTiles;
  ArrayList<Integer> InvisibleTiles;
  Random random;

  public TileList(int NumTiles) {
	  this.random = new Random();
	  this.VisibleTiles = new ArrayList<Integer>();
	  this.InvisibleTiles = new ArrayList<Integer>();
	  for (int i=0; i< NumTiles; ++i) {
		  InvisibleTiles.add(i);
	  }
	  
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
}
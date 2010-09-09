package de.qwhon.java2d.whatsthat;

import java.util.ArrayList;

class TileList {

  ArrayList<Integer> VisibleTiles;
  ArrayList<Integer> InvisibleTiles;

  public TileList(int NumTiles) {
	  this.VisibleTiles = new ArrayList<Integer>();
	  this.InvisibleTiles = new ArrayList<Integer>();
	  for (int i=0; i< NumTiles; ++i) {
		  InvisibleTiles.add(i);
	  }
	  
  }

  public Boolean nextTile() {
	  if (!InvisibleTiles.isEmpty()) {
		  int index = 0;
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
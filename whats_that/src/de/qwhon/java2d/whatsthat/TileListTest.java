package de.qwhon.java2d.whatsthat;

//import de.qwhon.java2d.whatsthat.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class TileListTest {

	@Test
	public void testNewTileListHasZeroVisible() {
		TileList tl = new TileList(1);
		assertEquals("new tile list: #visible = 0", false, tl.isTileVisible(0));
	}
	
	@Test
	public void testNextTileMakesOneVisible() {
		TileList tl = new TileList(1);
		assertEquals("new tile list: #visible = 0", false, tl.isTileVisible(0));
		tl.nextTile();
		assertEquals("after calling nextTile: #visible = 1", true, tl.isTileVisible(0));
	}
	
	@Test
	public void testGetNumTilesAfterSetNumTiles() {
		TileList tl = new TileList(1);
		assertEquals("after construction: #tiles = 1", 1, tl.getNumTiles() );
		tl.setNumTiles(2);
		assertEquals("after calling setNumTiles: #tiles = 2", 2, tl.getNumTiles() );
	}
	
	@Test
	public void testNextTileAfterSetNumTiles() {
		TileList tl = new TileList(1);
		tl.setNumTiles(2);
		assertEquals("first NextTile", true, tl.nextTile());
		assertEquals("2nd NextTile", true, tl.nextTile());
		assertEquals("3rd NextTile", false, tl.nextTile());
	}

}

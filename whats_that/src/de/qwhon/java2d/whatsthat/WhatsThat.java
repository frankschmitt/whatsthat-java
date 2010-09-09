package de.qwhon.java2d.whatsthat;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.awt.geom.*;
import java.io.*;
import javax.imageio.*;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JToolBar;

import java.util.Random;

public class WhatsThat extends Component {

	public class ImageList {
		private File directory;
		private File[] imageFiles;
		private Random random;
		
		public ImageList(String dirName) {
		  directory = new File(dirName);
		  imageFiles = directory.listFiles();
		  random = new Random();
		}
		
		public String nextImageFile() {
			int index = random.nextInt(imageFiles.length);
			try {
			return imageFiles[index].getCanonicalPath();
			}
			catch(IOException e) {
				return "";
			}
		}
	}
	
	private static final String S_WHATS_THAT = "What's that?";
	// strings for button captions etc.
	private static final String S_SHOW_NEXT_TILE = "Show next tile";
	private static final String S_NUM_VISIBLE_TILES = "NumVisibleTiles: ";

	private static final long serialVersionUID = 1L;

	final static Color black = Color.black;
	final static Color red = Color.red;
	final static Color white = Color.white;

	static final int NumRows = 3;
	static final int NumCols = 3;

	BufferedImage img;
	JButton button;
	JLabel countsLabel;
	TileList tileList;
	ImageList imageList;

	private void paintRectangle(Graphics2D g2, double x, double y,
			double width, double height, Color col, boolean fill) {
		if (fill) {
			g2.setPaint(col);
			g2.fill(new Rectangle2D.Double(x, y, width, height));
		} else {
			g2.draw(new Rectangle2D.Double(x, y, width, height));
		}
	}

	public void paint(Graphics g) {
		// draw whole image 1:1
		// g.drawImage(img, 0, 0, null);
		// scale image
		g.drawImage(img, 0, 0, getWidth(), getHeight(), 0, 0, img.getWidth(),
				img.getHeight(), null);

		// draw Rectangle2D.Double
		double img_height = this.getHeight();
		double img_width = this.getWidth();
		double x = 0;
		double y = 0;

		double tile_width = img_width / (double) NumCols;
		double tile_height = img_height / (double) NumRows;
		Color col;
		boolean fill;
		Graphics2D g2 = (Graphics2D) g;
		for (int i = 0; i < NumRows; ++i) {
			x = tile_width * i;
			for (int j = 0; j < NumCols; ++j) {
				// determine whether tile should be visible or not
				fill = !tileList.isTileVisible(i + j * NumRows);
				y = tile_height * j;
				if ((i % 2 + j % 2) % 2 == 0) {
					col = white;
				} else {
					col = black;
				}
				paintRectangle(g2, x, y, tile_width, tile_height, col, fill);
			}
		}
		// update counts label
		countsLabel
				.setText(S_NUM_VISIBLE_TILES + tileList.getNumVisibleTiles());
	}

	private void nextImage() {
		tileList.reset();
	  readNextImage();
	}

	public void buttonClicked(ActionEvent e) {
		if (!tileList.nextTile()) {
			nextImage();
		}
		repaint();
	}

	private void addBtnShowNextTile(JToolBar parent) throws Exception {
		// the "show next tile" button
		button = new JButton(S_SHOW_NEXT_TILE);
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				buttonClicked(e);
			}
		});
		parent.add(button);
	}

	private void addLblCounts(JToolBar parent) {
		countsLabel = new JLabel(WhatsThat.S_NUM_VISIBLE_TILES
				+ tileList.getNumVisibleTiles());
		parent.add(countsLabel);
	}

	private void initUI(JFrame f) throws Exception {
		JToolBar toolbar = new JToolBar();
		addBtnShowNextTile(toolbar);
		addLblCounts(toolbar);
		f.getContentPane().add(toolbar, BorderLayout.NORTH);
	}

	public WhatsThat() {
		tileList = new TileList(NumRows * NumCols);
		imageList = new ImageList("data");
		readNextImage();
	}

	private void readNextImage() {
		try {
			//img = ImageIO.read(new File("data/DSC00390.JPG"));
			img = ImageIO.read(new File(imageList.nextImageFile()));
				// img = ImageIO.read(new File("data/strawberry.jpg"));
		} catch (IOException e) {
		}
	}

	public Dimension getPreferredSize() {
		if (img == null) {
			return new Dimension(100, 100);
		} else {
			return new Dimension(1024, 768);
		}
	}

	public static void main(String[] args) throws Exception {

		JFrame f = new JFrame(S_WHATS_THAT);

		f.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		WhatsThat w = new WhatsThat();
		w.initUI(f);
		f.add(w);
		f.pack();
		f.setVisible(true);
	}
}

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

import javax.swing.*;
import javax.swing.event.*;

//import java.util.Collections;
//import java.util.Random;
import java.util.*;

public class WhatsThat extends Component implements ActionListener, ChangeListener {

	// strings for button captions etc.
	// english
	/* private static final String S_WHATS_THAT = "What's that?";
	private static final String S_SHOW_NEXT_TILE = "Show next tile";
	private static final String S_TILE = "Tile";
	private static final String S_FILE = "File";
	private static final String S_REVEAL = "Reveal";
	private static final String S_QUIT = "Quit";
	 */
	// german
	private static final String S_WHATS_THAT = "Was ist das?";
	private static final String S_SHOW_NEXT_TILE = "NŠchste Kachel";
	private static final String S_TILE = "Kachel";
	private static final String S_FILE = "Datei";
	private static final String S_REVEAL = "Bild zeigen";
	private static final String S_LAST_IMAGE = "Letztes Bild";
	private static final String S_QUIT = "Beenden";

	public void debug(String s) {
		System.out.println(s);
	}

	public class ImageList {
		private File directory;
		private java.util.List<File> imageFiles;
		private int currentImageIndex = -1;

		public ImageList(String dirName) {
			directory = new File(dirName);
			File[] tmp = directory.listFiles();

			// omit hidden files (e.g. .DS_Store)
			imageFiles = new java.util.ArrayList<File>(); 			
			for (int i=0; i<tmp.length; ++i) {
				if(!tmp[i].isHidden() && tmp[i].getName().endsWith(".jpg")) {
					imageFiles.add(tmp[i]);
				}
			}
			Collections.shuffle(imageFiles);
		}

		public String nextImageFile() {
			// last image? randomize the list + start new
			if (currentImageIndex == imageFiles.size()-1)
			{
				Collections.shuffle(imageFiles);
				currentImageIndex = 0;
			}
			else {
				currentImageIndex = currentImageIndex+1;
			}

			try {
				String nextFilename = imageFiles.get(currentImageIndex).getCanonicalPath();
				debug(nextFilename);
				return nextFilename;
			}
			catch(IOException e) {
				return "";
			}
		}

		public int getNumImages() {
			return imageFiles.size();
		}

		public int getCurrentImageIndex() {
			return currentImageIndex;
		}
	}

	private static final long serialVersionUID = 1L;

	final static Color black = Color.black;
	final static Color red = Color.red;
	final static Color white = Color.white;

	private int NumRows = 3;
	private int NumCols = 4;

	private BufferedImage img;
	private JButton btnShowNextTile;
	private JLabel countsLabel;
	private JLabel filesLabel;
	private TileList tileList;
	private ImageList imageList;
	private JFrame f;
	private JSpinner numColsSpinner;
	private JSpinner numRowsSpinner;
	private SpinnerModel numRowsModel;
	private SpinnerModel numColsModel;
	private JButton btnReveal;
	private JButton btnLastImage;
	private boolean isLastImage;

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
			y = tile_height * i;
			for (int j = 0; j < NumCols; ++j) {
				x = tile_width * j;
				// determine whether tile should be visible or not
				fill = !tileList.isTileVisible(i + j*NumRows);
				if ((i % 2 + j % 2) % 2 == 0) {
					col = white;
				} else {
					col = black;
				}
				paintRectangle(g2, x, y, tile_width, tile_height, col, fill);
			}
		}
		updateLabels();
	}

	public void stateChanged(ChangeEvent e) {
		SpinnerNumberModel source = (SpinnerNumberModel)(e.getSource());
		if (source == numRowsModel) {
			this.NumRows = (Integer)source.getValue();
		}
		else if (source == numColsModel) {
			this.NumCols = (Integer)source.getValue();
		}
		tileList.setNumTiles(NumRows*NumCols);
		nextImage();
	}

	private void updateLabels() {
		// update labels
		countsLabel.setText(S_TILE + ": " + tileList.getNumVisibleTiles() + "/" + tileList.getNumTiles());
		filesLabel.setText(S_FILE + ": " + (imageList.getCurrentImageIndex()+1) + "/" + imageList.getNumImages());
	}

	private void nextImage() {
		tileList.reset();
		readNextImage();
	}

	public void showNextTileClicked(ActionEvent e) {
		if (!tileList.nextTile()) {
			nextImage();
		}
		repaint();
	}

	public void revealClicked(ActionEvent e) {
		while (tileList.nextTile());
		repaint();
	}

	public void lastImageClicked(ActionEvent e) {
		isLastImage = true;
		nextImage();
		tileList.nextTile();
		repaint();
	}

	private void addBtnShowNextTile(JToolBar parent) throws Exception {
		// the "show next tile" button
		btnShowNextTile = new JButton(S_SHOW_NEXT_TILE);
		btnShowNextTile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showNextTileClicked(e);
			}
		});
		parent.add(btnShowNextTile);
	}

	private void addBtnReveal(JToolBar parent) throws Exception {
		btnReveal = new JButton(S_REVEAL);
		btnReveal.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				revealClicked(e);
			}
		});
		parent.add(btnReveal);
	}

	private void addBtnLastImage(JToolBar parent) throws Exception {
		btnLastImage = new JButton(S_LAST_IMAGE);
		btnLastImage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				lastImageClicked(e);
			}
		});
		parent.add(btnLastImage);
	}


	private void addLblCounts(JToolBar parent) {
		countsLabel = new JLabel(WhatsThat.S_TILE
				+ tileList.getNumVisibleTiles());
		parent.add(countsLabel);
	}

	private void addLblFiles(JToolBar parent) {
		filesLabel = new JLabel(WhatsThat.S_FILE
				+ this.imageList.getNumImages());
		parent.add(filesLabel);
	}

	private void initUI(JFrame f) throws Exception {
		this.f = f;
		JToolBar toolbar = new JToolBar();
		addButtons(toolbar);
		addLabels(toolbar);
		f.getContentPane().add(toolbar, BorderLayout.NORTH);
		createMenuBar();
		createSpinners(toolbar);
	}

	private void addButtons(JToolBar toolbar) throws Exception {
		addBtnShowNextTile(toolbar);
		addBtnReveal(toolbar);
		addBtnLastImage(toolbar);
	}

	private void createSpinners(JToolBar toolbar) {
		numRowsModel = new SpinnerNumberModel(NumRows, //initial value
				1, //min
				10, //max
				1);
		numRowsSpinner = new JSpinner(numRowsModel);
		numRowsModel.addChangeListener(this);
		toolbar.add(numRowsSpinner);
		numColsModel = new SpinnerNumberModel(NumCols, //initial value
				1, //min
				10, //max
				1);
		numColsSpinner = new JSpinner(numColsModel);
		numColsModel.addChangeListener(this);
		toolbar.add(numColsSpinner);
	}

	private void addLabels(JToolBar toolbar) {
		addLblCounts(toolbar);
		addLblFiles(toolbar);
	}

	public WhatsThat() {
		tileList = new TileList(NumRows * NumCols);
		imageList = new ImageList("data");
		readNextImage();
	}

	private void readNextImage() {
		String fileName = "";
		try {
			//img = ImageIO.read(new File("data/DSC00390.JPG"));
			if (!isLastImage) {
				fileName = imageList.nextImageFile();
			}
			else {
				fileName = "data/.last.jpg";
			}
			img = ImageIO.read(new File(fileName));

			// img = ImageIO.read(new File("data/strawberry.jpg"));
		} catch (IOException e) {
			countsLabel.setText(e.getMessage() + fileName);
		}
	}

	public Dimension getPreferredSize() {
		if (img == null) {
			return new Dimension(100, 100);
		} else {
			return new Dimension(1024, 768);
		}
	}

	private void createMenuBar() {
		JMenuBar menuBar;
		JMenu menu; //, submenu;
		JMenuItem menuItem;
		//JRadioButtonMenuItem rbMenuItem;
		//JCheckBoxMenuItem cbMenuItem;

		//Create the menu bar.
		menuBar = new JMenuBar();

		//Build the first menu.
		menu = new JMenu(S_FILE);
		menu.setMnemonic(KeyEvent.VK_F);
		menu.getAccessibleContext().setAccessibleDescription(
		"The only menu in this program that has menu items");
		menuBar.add(menu);

		//a group of JMenuItems
		menuItem = new JMenuItem(S_QUIT,
				KeyEvent.VK_Q);
		//menuItem.setAccelerator(KeyStroke.getKeyStroke(
		//		KeyEvent.VK_1, ActionEvent.ALT_MASK));
		menuItem.getAccessibleContext().setAccessibleDescription(
		"This doesn't really do anything");
		menuItem.setActionCommand("quit");
		menuItem.addActionListener(this);
		menu.add(menuItem);
		menuBar.add(menu);
		f.setJMenuBar(menuBar);
	}


	public void actionPerformed(ActionEvent e) {
		if ("quit".equals(e.getActionCommand())) {
			exit();
		}
	}

	public void exit() {
		WindowEvent windowClosing = new WindowEvent(f, WindowEvent.WINDOW_CLOSING);
		Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(windowClosing);
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

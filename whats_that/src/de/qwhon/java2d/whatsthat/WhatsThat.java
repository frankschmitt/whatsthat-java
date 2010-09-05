package de.qwhon.java2d.whatsthat;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.awt.geom.*;
import java.io.*;
import javax.imageio.*;
//import javax.swing.*;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
//import javax.swing.JPanel;
//import javax.swing.JScrollPane;
import javax.swing.JToolBar;
//import javax.swing.SwingUtilities;



public class WhatsThat extends Component {

	private static final long serialVersionUID = 1L;
	private int NumVisibleTiles = 0;

	final static Color black = Color.black;
	final static Color red = Color.red;
	final static Color white = Color.white;

	static final int NumRows = 5;
	static final int NumCols = 5;

	BufferedImage img;
	JButton button;
	JLabel countsLabel;

	private void paintRectangle(Graphics2D g2, double x, double y, double width, double height, Color col, boolean fill) {
		if (fill) {	g2.setPaint(col);
		g2.fill(new Rectangle2D.Double(x, y,
				width,
				height));
		}
		else {
			g2.draw(new Rectangle2D.Double(x, y,
					width,
					height));
		}
	}


	public void paint(Graphics g) {
		// draw whole image 1:1
		//g.drawImage(img, 0, 0, null);
		// scale image
		g.drawImage(img,
                0, 0, getWidth(), getHeight(), 
                0, 0, img.getWidth(), img.getHeight(), null);


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
		for (int i=0; i<NumRows; ++i) {
			x = tile_width*i;
			for (int j=0; j<NumCols; ++j) {
				// determine whether tile should be visible or not
				//fill = (i > NumVisibleTiles);
				fill = ((i+j*NumRows) > NumVisibleTiles);
				y = tile_height*j;
				if ((i%2 + j%2)%2 == 0) {
					col = white;
					//fill = true;
				}
				else {
					col = black;
					//fill = false;
				}
				paintRectangle(g2, x, y, tile_width, tile_height, col, fill);
			}
		}
		// update counts label
		countsLabel.setText("NumVisibleTiles: " + NumVisibleTiles);
	}

	private void nextImage() {
		NumVisibleTiles = 0;
	}
	
	public void buttonClicked(ActionEvent e) {
if (NumVisibleTiles == NumRows*NumCols) {
			nextImage();
		}
//button.setEnabled(!button.isEnabled());
		++NumVisibleTiles;
		repaint();
		
	}

	private void addBtnShowNextTile(JToolBar parent) {
		// the "show next tile" button
		button= new JButton("Show next tile");
		//button.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { button.setEnabled(false);}});
		button.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { buttonClicked(e); }});
		parent.add(button);
	}
	
	private void addLblCounts(JToolBar parent) {
		countsLabel = new JLabel("NumVisibleTiles: " + NumVisibleTiles);
		parent.add(countsLabel);
	}
	
	private void initUI(JFrame f) {
		//setSize(200,00);
		//setLayout(new BorderLayout());

		JToolBar toolbar = new JToolBar();
		addBtnShowNextTile(toolbar);
		addLblCounts(toolbar);
		f.getContentPane().add(toolbar, BorderLayout.NORTH);
	}

	public WhatsThat() {
		try {
			img = ImageIO.read(new File("data/DSC00390.JPG"));
			//img = ImageIO.read(new File("data/strawberry.jpg"));
		} catch (IOException e) {
		}

	}

	public Dimension getPreferredSize() {
		if (img == null) {
			return new Dimension(100,100);
		} else {
			return new Dimension(1024, 768);
			//return new Dimension(img.getWidth(null), img.getHeight(null));
		}
	}

	public static void main(String[] args) {

		JFrame f = new JFrame("Load Image Sample");

		f.addWindowListener(new WindowAdapter(){
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


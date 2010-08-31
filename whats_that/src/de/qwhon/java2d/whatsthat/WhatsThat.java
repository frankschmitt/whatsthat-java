package de.qwhon.java2d.whatsthat;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.awt.geom.*;
import java.io.*;
import javax.imageio.*;
import javax.swing.*;


/**
 * This class demonstrates how to load an Image from an external file
 */
public class WhatsThat extends Component {
          
    final static Color black = Color.black;
    final static Color red = Color.red;
    final static Color white = Color.white;

    static final int NumRows = 10;
    static final int NumCols = 10;
    
    BufferedImage img;

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
        g.drawImage(img, 0, 0, null);

     // draw Rectangle2D.Double
        double img_height = img.getHeight();
        double img_width = img.getWidth();
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
        		y = tile_height*j;
        	if ((i%2 + j%2)%2 == 0) {
        		col = white;
        		fill = true;
        	}
        	else {
        		col = black;
        		fill = false;
        	}
        	   paintRectangle(g2, x, y, tile_width, tile_height, col, fill);
        	}
        }
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
           return new Dimension(img.getWidth(null), img.getHeight(null));
       }
    }

    public static void main(String[] args) {

        JFrame f = new JFrame("Load Image Sample");
            
        f.addWindowListener(new WindowAdapter(){
                public void windowClosing(WindowEvent e) {
                    System.exit(0);
                }
            });

        f.add(new WhatsThat());
        f.pack();
        f.setVisible(true);
    }
}


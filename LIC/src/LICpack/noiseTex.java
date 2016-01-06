package LICpack;

import java.awt.*;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;
import javax.swing.JFrame;

//import java.awt.color.*;

class MyCanvas extends JComponent 
{
	public static final int noiseWidth=512,noiseHeight=512;
	int[][] noise=new int[noiseWidth][noiseHeight]; //2d noise array
	int Initialx = 150;
	int Initialy = 100;
	private BufferedImage Canvas ;
	
	public MyCanvas(){
		Canvas = new BufferedImage(1000, 1000, BufferedImage.TYPE_INT_ARGB);
		fillCanvas(Color.WHITE);
		drawRect (Initialx, Initialy, 512, 512);  
	}	
	
	 public void fillCanvas(Color c) {
	        int color = c.getRGB();
	        for (int x = 0; x < Canvas.getWidth(); x++) {
	            for (int y = 0; y < Canvas.getHeight(); y++) {
	            	Canvas.setRGB(x, y, color);
	            }
	        }
	        repaint();
	    }
	void generateNoise()
	{
	    for ( int x = 0 ; x < noiseWidth; x++)
	    for ( int y = 0 ; y < noiseHeight; y++)
	    {
	        noise[x][y] =(int) (255 *(Math.random()));
	    }
	}
	public void drawRect( int x1, int y1, int width, int height) {
    	generateNoise();
    	
        //int color = c.getRGB();
        // Implement rectangle drawing
        for (int x = Initialx ; x < Initialx + noiseWidth; x++) {
            for (int y = Initialy; y < Initialy + noiseHeight; y++) {
            	Color mycolor = new Color(noise[x-Initialx][y-Initialy], noise[x-Initialx][y-Initialy], noise[x-Initialx][y-Initialy]);
                Canvas.setRGB(x, y, mycolor.getRGB());
            }
        }
        repaint();
    }
	 
	public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setFont(new Font("Arial", Font.BOLD, 12));
 	    String msg = "Noise Texture : ";
 	    int msgX = 10;
 	    int msgY = 20;	    
        g2d.drawImage(Canvas, null, null);
        g2d.drawString(msg, msgX, msgY); 
    }
}

public  class noiseTex {
  public static void main(String[] args) {
    JFrame window = new JFrame("Noise Texture ");
    //window.setForeground(Color.WHITE); //doesnt work?
    window.toString();
    window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    window.setBounds(30, 30, 800, 700);
    window.getContentPane().add(new MyCanvas());
    window.setVisible(true);
  }
}
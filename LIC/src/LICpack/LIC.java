package LICpack;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;


import javax.swing.JComponent;
import javax.swing.JFrame;
	
class MyCanvas3 extends JComponent 
{
	public static final int Dimension=512;
	int Initialx = 150;
	int Initialy = 100;
	private BufferedImage Canvas3 ;
	public float Guass; //1D component
	public boolean Outofrange;
	float[][] local_weight_Forward = new float[Dimension][Dimension];
	float[][] local_weight_Backward = new float[Dimension][Dimension];
	float[][] normalization_Forward = new float[Dimension][Dimension];
	float[][] normalization_Backward = new float[Dimension][Dimension];
	int ColorParam = 255;
	MyCanvas obj = new MyCanvas();
	MyCanvas2 obj2 = new MyCanvas2();
		
	public MyCanvas3(){
		Canvas3 = new BufferedImage(1000, 1000, BufferedImage.TYPE_INT_ARGB);
		fillCanvas(Color.LIGHT_GRAY);
		lic (Initialx, Initialy, 512, 512); 
				
	}		
	 public void fillCanvas(Color c) {
	        int color = c.getRGB();
	        for (int x = 0; x < Canvas3.getWidth(); x++) {
	            for (int y = 0; y < Canvas3.getHeight(); y++) {
	            	Canvas3.setRGB(x, y, color);
	            }
	        }
	        repaint();
	    }	 

	public void lic( int x1, int y1, int width, int height) {					
		
		// for forward advection
        for (int x = 0 ; x <  Dimension; x++) {
            for (int y = 0; y <  Dimension; y++) {
            	//initial particle position
            	          	
            	float for_weight=0;
            	float i_weight=0;
            	float step = (float) 0.1; //For VF3 use step= 0.3 //For VF1 use step= 0.3
            	//For VF2 and VF4 use step= 0.9 //for VF5 use step=
            	float px = x;
            	float py = y;
            	float LicIter= 96;
            	int actual_iter = 0;
            	float i_guass=0;
            	
            	float sumFor_local_weight=0;//sum forward local weights
            	for(int i=0;i<LicIter;i++) //loop i --till 4
            		{	
            		actual_iter++;
	            	//read pixel from noise image at px py;
            		float Noise= obj.noise[(int) px][(int) py];
            		//Noise=         		
            		i_guass = (float) (guassian((float) ((2.5/LicIter)*i)));
            		i_guass *= 2.5; 
            		i_weight = i_guass*Noise;           			        		
            		for_weight += i_weight;
            		
            		sumFor_local_weight += i_guass;
            		// to get varying values of vx and vy from a function
            		double[] vel = getVel(px,py);
	            	float vx=  (float) vel[0];
	            	float vy = (float) vel[1];
	            	float pxnew= px + vx*step;
	            	float pynew= py + vy*step;
	            	if(pxnew>=Dimension || pynew>=Dimension || pxnew < 0 || pynew <0 )
	            	{
	            		//System.out.println("new px and py exceeds Dimensions bound!!!!!");
	            		Outofrange = true;
	            		break;	            	
	            	}else{
		            	px=pxnew;
		            	py=pynew;	
	            		}	            	
            		}// for loop ends
            	
            	local_weight_Forward[x][y] = for_weight; //accumulate the weights here!! 
            	//local_weight_Forward[x][y] /= sumFor_local_weight;  //normalization
            	normalization_Forward[x][y] = sumFor_local_weight;
            	//local_weight /= actual_iter;  //normalization

           	           	
            	//store the value in the o/p pixel  	
            	//Color mycolor3 = new Color((int) local_weight_Forward, (int) local_weight_Forward , (int) local_weight_Forward);
                //Canvas3.setRGB(x+Initialx, y+Initialy, mycolor3.getRGB());
            }
        }//double for loop x,y ends  
        
     // for Backward advection
        for (int x = 0 ; x <  Dimension; x++) {
            for (int y = 0; y <  Dimension; y++) {
            	//initial particle position
            		
            	float back_weight=0;
            	float i_weight=0;
            	float step = (float) 0.1; //For VF3 use step= 0.3 //For VF1 use step= 0.6 
            	//For VF2 use step= 0.9 //for VF5 use step= 0.1
            	float px = x;
            	float py = y;
            	float LicIter= 96;
            	int actual_iter = 0;
            	float i_guass=0;
            	
            	float sumFor_local_weight=0;//sum forward local weights
            	for(int i=0;i<LicIter;i++) //loop i --till 4
            		{	
            		actual_iter++;
	            	//read pixel from noise image at px py;
            		float Noise= obj.noise[(int) px][(int) py];
            		//Noise=         		
            		i_guass = (float) (guassian((float) ((2.5/LicIter)*i)));
            		i_guass *= 2.5; 
            		i_weight = i_guass*Noise;           			        		
            		back_weight += i_weight;
            		
            		sumFor_local_weight += i_guass;
            		// to get varying values of vx and vy from a function
            		double[] vel = getVel(px,py);
	            	float vx=  (float) vel[0];
	            	float vy = (float) vel[1];
	            	float pxnew= px - vx*step;
	            	float pynew= py - vy*step;
	            	if(pxnew>=Dimension || pynew>=Dimension || pxnew < 0 || pynew <0 )
	            	{
	            		//System.out.println("new px and py exceeds Dimensions bound!!!!!");
	            		Outofrange = true;
	            		break;	            	
	            	}else{
		            	px=pxnew;
		            	py=pynew;	
	            		}	            	
            		}// for loop ends
            	
            	local_weight_Backward[x][y]  = back_weight; //accumulate the weights here!! 
            	//local_weight_Backward[x][y]  /= sumFor_local_weight;  //normalization
            	normalization_Backward[x][y] = sumFor_local_weight;
            	//local_weight_Backward[x][y] /= actual_iter;  //normalization           	           	            	
            }
        }//double for loop x,y ends
        //store the value in the o/p pixel
        for (int x = 0 ; x <  Dimension; x++) {
            for (int y = 0; y <  Dimension; y++) {
            	Color mycolor3 = new Color(((((int) local_weight_Backward[x][y]  +(int) local_weight_Forward[x][y] )/(normalization_Forward[x][y]+normalization_Backward[x][y]))/ColorParam), ((((int) local_weight_Backward[x][y]  +(int) local_weight_Forward[x][y] )/(normalization_Forward[x][y]+normalization_Backward[x][y]))/ColorParam), ((((int) local_weight_Backward[x][y]  +(int) local_weight_Forward[x][y] )/(normalization_Forward[x][y]+normalization_Backward[x][y]))/ColorParam));
            	//Color mycolor4 = new Color(((int) local_weight_Backward[x][y]  +(int) local_weight_Forward[x][y] )/2, ((int) local_weight_Backward[x][y]  +(int) local_weight_Forward[x][y] )/2, ((int) local_weight_Backward[x][y]  +(int) local_weight_Forward[x][y] )/2);
            	Canvas3.setRGB(x+Initialx, y+Initialy, mycolor3.getRGB());
            }
        }
        repaint();
    }
	 
	public float guassian(float r){
		float a=(float) (1/Math.sqrt(2*Math.PI));
		Guass=(float) (a*Math.pow(Math.E, -(Math.pow(r, 2))/2));		
		return Guass;
	}
	//Guassian function X range divided into 3 parts...bcoz of the actual function taken in above method
	// so X=0 in 1st iteration, X= 0.8 in 2nd iteration...and so on..X= 2.4 in 4th iteration!
	
	public double[] getVel(float r,float s){
		double velX;
		double velY;
		velX=obj2.xvec[(int) r][(int) s];
		velY=obj2.yvec[(int) r][(int) s];
		/*if(velX < 0.01)
			{
			velX=0.0;
			}
		if(velY < 0.01)
		{
		velY=0.0;
		}*/
		double[] velv={velX,velY};
		return velv;
	}
	
	public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setFont(new Font("Arial", Font.BOLD, 12));
 	    String msg = "2D LIC : ";
 	    int msgX = 10;
 	    int msgY = 20; 	     
        g2d.drawImage(Canvas3, null, null);
        g2d.drawString(msg, msgX, msgY);
    }
		
}

public  class LIC {
  public static void main(String[] args) {
    JFrame window = new JFrame("2-D Line Integral Convolution : ");
    window.toString();
    window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    window.setBounds(30, 30, 800, 700);
    window.getContentPane().add(new MyCanvas3());
    window.setVisible(true);
  }
}
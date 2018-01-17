import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;

import javax.imageio.ImageIO;


public class GraphGenerator {
	
	private BufferedImage graphe = new BufferedImage(400, 400, BufferedImage.TYPE_INT_RGB);
	private String polynom[];
	private String points="";
	private double x;
	private double y;
	final private Color blue = new Color(66, 134, 244);
	final private Color white = new Color(255, 255, 255);
	private String poly;
	
	public GraphGenerator(String polynom, String center){
		this.poly = polynom;
		String centerPoint[] = center.split(" ");
		this.x = Double.parseDouble(centerPoint[0]);
		this.y = Double.parseDouble(centerPoint[1]);
		this.polynom = polynom.split(" ");
		graphe = axes();
		
	}
	
	public String getGraphe(){
		return imgToBase64String(draw(), "png");
	}
	
	private BufferedImage draw()
	{
		int counter = 0;
	
		
		for (double i = x; i< x + 10 ; i+=0.025)
		{
			double result = polynomEvaluator(polynom, i-5);
			
			if( (result < 5+y) && (result > -5+y)){
				points += i-5 + " "+ result + " ";
				if( ((int) ((i-x)*40)+1 < 400) && ((int) (200- (result - y)*40)+1 < 400)){
					graphe.setRGB((int) ((i-x)*40)+1,(int) (200- (result-y)*40), blue.getRGB());
					graphe.setRGB((int) ((i-x)*40),(int) (200- (result-y)*40)+1, blue.getRGB());
				}
				counter +=1;
			}
		}

		return graphe;
		
	}
	
	private double polynomEvaluator(String polynom[], double x){
		double result = 0;
		
		for (int j = 0; j < polynom.length/2; j++){
			int tmp = j*2;
			result += (Double.parseDouble(polynom[tmp]))*(Math.pow(x, Double.parseDouble(polynom[tmp+1])));
		}
		
		return result;
	}
	
	private BufferedImage axes(){
		
		for (int i = 0; i<400; i++)
		{
			if((int)(200+40*y)> 0 && (int)(200+40*y)<400)
				graphe.setRGB(i,(int) (200+40*y), white.getRGB());
			if(i%40 == 0)
			{
				for(int j = -2; j<3; j++)
					if((int)(200+40*y)> 0 && (int)(200+(40*y)+j)<400)
						graphe.setRGB(i,(int) (200+(40*y)+j), white.getRGB());
			}
				
		}
		for (int i = 0; i<400; i++)
		{
			if((int)(200-40*x)>0 && (int)(200-40*x)<400)
				graphe.setRGB((int) (200-40*x),i, white.getRGB());
			if(i%40 == 0)
			{
				for(int j = -2; j<3; j++)
					if((int)(200-40*x)>0 && (int)(200-40*x)<400)
						graphe.setRGB((int) (200-(40*x)+j),i, white.getRGB());
			}
			
		}
		
		return graphe;
	}
	
	
	public String imgToBase64String(final RenderedImage img, final String formatName) {
	    final ByteArrayOutputStream os = new ByteArrayOutputStream();
	    try {
	        ImageIO.write(img, formatName, Base64.getEncoder().wrap(os));
	        return os.toString();
	    } catch (final IOException ioe) {
	        throw new UncheckedIOException(ioe);
	    }
	}
	
	public String getPloynom()
	{
		return poly;
	}

}

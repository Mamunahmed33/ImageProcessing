package FeatureExtractionAndMaleFemaleDetection;
import java.awt.Color;
import java.awt.image.BufferedImage;

import HarrisCornerDetection.WriteImage;

/*
Input: Colored Image
Output: XConvolition Image, YConvolution Image, Final Image
Process: 1. Takes a colored image 
		 2. Gets the grayscale matrix of that image by calling ConvertImageToMatrix class
		 3. Calculates xConvolution and yConvolution
		 4. Calculates magnitude from xConvolution and yConvolution and output final image
*/

public class FeatureExtractionUsingSobel {
	BufferedImage img, finalImg;
	int[][] imageMatrix;
	int height, width;
	private WriteImage writeImage;
	
	public FeatureExtractionUsingSobel(){
		writeImage = WriteImage.getInstance();
	} 
	
	public void EdgeDetection(BufferedImage img){

		BufferedImage[] smallImg= cropImage(img);
		for(int i=0; i< 8; i++){

			this.img = img;
			this.height = img.getHeight();
			this.width = img.getWidth();

			imageMatrix = new ConvertImageToGrayScaleMatrix().imageToMatrix(img);
			int[][] xMatrix = xConvolution();
			int[][] yMatrix = yConvolution();

			//finalImage(xMatrix, yMatrix);

			calculateDirection(xMatrix, yMatrix);
		}
	}
	
	public int[][] xConvolution(){
		int[][] xMatrix = new int[width][height]; 
		
		BufferedImage image1 = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
		
		int x = 0;
		
		int [][] xMask = {{-1, -2, -1}, {0, 0, 0}, {1, 2, 1}}; 
		
		for(int i = 1; i < height - 1; i++)
		{
			for(int j = 1; j < width - 1; j++)
			{
				x = xMask[0][0] * imageMatrix[j-1][i-1] + xMask[0][1] * imageMatrix[j][i-1]+ xMask[0][2] * imageMatrix[j+1][i-1]
					+ xMask[1][0] * imageMatrix[j-1][i]+ xMask[1][1] * imageMatrix[j][i]+ xMask[1][2] * imageMatrix[j+1][i]
					+ xMask[2][0] * imageMatrix[j-1][i+1]+ xMask[2][1] * imageMatrix[j][i+1]+ xMask[2][2] * imageMatrix[j+1][i+1];
			
				x = Math.abs(x);
				
				x = pixelValueChecker(x);
				
				xMatrix[j][i] = x;
				Color c = new Color(x, x ,x);
				
				image1.setRGB(j, i, c.getRGB());
			}
		}
		
		//writeImage.Write(image1, "src/FeatureExtractionAndMaleFemaleDetection/Images/", "xConvolution.jpg");
		
		return xMatrix;
	}
	
	public int[][] yConvolution(){
		
		int[][] yMatrix = new int[width][height]; 
		
		BufferedImage image2 = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
		
		int y = 0;
		
		int [][] yMask = {{-1, 0, 1}, {-2, 0, 2}, {-1, 0, 1}}; 		
		
		for(int i = 1; i < height - 1; i++)
		{
			for(int j = 1; j < width - 1; j++)
			{
				y = yMask[0][0] * imageMatrix[j-1][i-1] + yMask[0][1] * imageMatrix[j][i-1]+ yMask[0][2] * imageMatrix[j+1][i-1]
					+ yMask[1][0] * imageMatrix[j-1][i] + yMask[1][1] * imageMatrix[j][i]+ yMask[1][2] * imageMatrix[j+1][i]
					+ yMask[2][0] * imageMatrix[j-1][i+1] + yMask[2][1] * imageMatrix[j][i+1]+ yMask[2][2] * imageMatrix[j+1][i+1];
				
				y = Math.abs(y);
				
				y = pixelValueChecker(y);
				
				yMatrix[j][i] = y;
				
				Color c = new Color(y, y ,y);
				image2.setRGB(j, i, c.getRGB());
			}
		}
		
		//writeImage.Write(image2, "src/FeatureExtractionAndMaleFemaleDetection/Images/", "yConvolution.jpg");
		
		return yMatrix;
	}
	
	private int pixelValueChecker(int value){
		if(value > 255)
			value = 255;
		else if(value < 0)
			value = 0;
		
		return value;
	}
	
	/*public void finalImage(int[][] xMatrix, int[][] yMatrix){
		int M, Mx, My;
		finalImg = new BufferedImage( width, height, BufferedImage.TYPE_BYTE_GRAY);
		
		for(int i = 1; i < height-1; i++)
		{
			for(int j = 1; j < width-1; j++)
			{
				Mx = xMatrix[j][i];
				My = yMatrix[j][i];
				M = (int) Math.sqrt(Mx*Mx + My*My);
				
				M = pixelValueChecker(M);
				
				Color c = new Color(M, M, M);
				
				finalImg.setRGB(j, i, c.getRGB());
			}
		}
		
		writeImage.Write(finalImg, "src/FeatureExtractionAndMaleFemaleDetection/Images/", "FinalImg.jpg");
	}
	*/
	
	public int[][] calculateDirection(int[][] x, int[][] y){
		
		int direction[][] = new int[width][height];
		for(int i = 1; i < height-1; i++)
		{
			for(int j = 1; j < width-1; j++)
			{
				direction[j][i] = (int) Math.toDegrees(Math.atan(y[j][i]/x[j][i])) + 90;
			}
		}
		
		return direction;
	}

	public void generateHistogram(){

	}

	public BufferedImage[] cropImage(BufferedImage img){
		BufferedImage[] smallImages = new BufferedImage[8];

		int x = 0;
		int y = 0;
		int w = img.getWidth()/8;
		int h = img.getHeight()/8;

		for(int i=0; i<8 ; i++){
			smallImages[i] = img.getSubimage(x, y, w, h);
			x+=w;
			y+=y;
		}

		return smallImages;
	}
}
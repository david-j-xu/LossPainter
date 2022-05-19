package painter;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Painting {
	private static int NOISE = 20;
	private int[][][] painting;
	private int[][][] candidate_painting;
	private int width;
	private int height;
	private Brush[] brush;
	
	
	/** Creates a painting
	 * @param width
	 * @param height
	 * @param brush
	 */
	public Painting(int width, int height, Brush[] brush) {
		this.painting = new int[3][width][height];
		// currently alias painting, as it will be unaliased when a candidate is created
		this.candidate_painting = this.painting;
		this.brush = brush;
		this.width = width;
		this.height = height;
		
		fill(255, 255, 255);
	}
	
	/** Returns the painting in array form
	 * @return 3 layer matrix of r, g, b channels
	 */
	public int[][][] getPainting() {
		return painting;
	}
	
	
	/** Fills the entire painting with a certain color
	 * @param r
	 * @param g
	 * @param b
	 */
	public void fill(int r, int g, int b) {
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				this.painting[0][i][j] = r;
				this.painting[1][i][j] = g;
				this.painting[2][i][j] = b;
			}
		}
	}
	
	/**
	 * Paints a brush stroke on the painting at the designated location with the specified color, size, and angle
	 * @param brushChoice
	 * @param brushSize
	 * @param brushAngle
	 * @param x
	 * @param y
	 * @param r
	 * @param g
	 * @param b
	 */
	public void paintCandidate(int brushChoice, int brushSize, double brushAngle, int x, int y, int r, int g, int b) {
		candidate_painting = new int[3][width][height];
		for (int i = 0; i < painting.length; i++) {
			for (int j = 0; j < painting[0].length; j++) {
				for (int k = 0; k < painting[0][0].length; k++) {
					candidate_painting[i][j][k] = painting[i][j][k];
				}
			}
		}
		
		boolean[][] brush_shape = brush[brushChoice].getBrush(brushSize, brushAngle);
		for (int i = 0; i < brushSize; i++) {
			for (int j = 0; j < brushSize; j++) {
				int paint_x = x + i;
				int paint_y = y + j;
				if (brush_shape[i][j] && paint_x >= 0 && paint_x < width && paint_y >= 0 && paint_y < height) {
					this.candidate_painting[0][paint_x][paint_y] = clamp((int) (r + (Math.random() * NOISE) - (NOISE / 2)), 0, 255);
					this.candidate_painting[1][paint_x][paint_y] = clamp((int) (g + (Math.random() * NOISE) - (NOISE / 2)), 0, 255);
					this.candidate_painting[2][paint_x][paint_y] = clamp((int) (b + (Math.random() * NOISE) - (NOISE / 2)), 0, 255);

				}
					
			}
		}
	}
	
	/** 
	 * @return the candidate painting
	 */
	public int[][][] getCandidate() {
		return candidate_painting;
	}
	
	/**
	 * Sets the candidate to true painting
	 * @param painting
	 */
	public void paint() {
		this.painting = candidate_painting;
	}
	
	/** Writes the painting in PNG form to an output path
	 * @param outputPath
	 */
	public void writeImage(String outputPath) {
        try {
            BufferedImage output = new BufferedImage(painting[0].length, 
                                                    	painting[0][0].length, 
                                                        BufferedImage.TYPE_INT_RGB);
            for (int i = 0; i < output.getWidth(); i++) {
                for (int j = 0; j < output.getHeight(); j++) {
                    output.setRGB(i, j, toRGB(painting[0][i][j], painting[1][i][j], painting[2][i][j]));
                }
            }
            
            File outputFile = new File(outputPath);
            ImageIO.write(output, "png", outputFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
	}
	
	/**
	 * Returns the painting's brush
	 * @return brush
	 */
	public Brush[] getBrush() {
		return brush;
	}
	
	/** Helper function to turn 3 separate R, G, B integers into single RGB form for writing
	 * @param r
	 * @param g
	 * @param b
	 * @return
	 */
	public static int toRGB(int r, int g, int b) {
		return (r << 16) + (g << 8) + b;
	}
	
	/** Helper function to extract red
	 * @param color
	 * @return red
	 */
	public static int getR(int color) {
		return (color >> 16) & 0xFF;
	}

	/** Helper function to extract green
	 * @param color
	 * @return green
	 */
	public static int getG(int color) {
		return (color >> 8) & 0xFF;
	}
	
	/** Helper function to extract blue
	 * @param color
	 * @return blue
	 */
	public static int getB(int color) {
		return color & 0xFF;
	}
	
	
	/**
	 * Clamps a value within a range
	 * @param val
	 * @param min
	 * @param max
	 * @return
	 */
	public static int clamp(int val, int min, int max) {
		return Math.min(Math.max(0, val), 255);
	}
}

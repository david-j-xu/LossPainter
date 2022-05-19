package painter;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Painting {
	private int[][][] painting;
	private int width;
	private int height;
	private Brush brush;
	
	
	/** Creates a painting
	 * @param width
	 * @param height
	 * @param brush
	 */
	public Painting(int width, int height, Brush brush) {
		this.painting = new int[3][width][height];
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
	 * @param brushSize
	 * @param brushAngle
	 * @param x
	 * @param y
	 * @param r
	 * @param g
	 * @param b
	 */
	public void paint(int brushSize, int brushAngle, int x, int y, int r, int g, int b) {
		boolean[][] brush_shape = brush.getBrush(brushSize, brushAngle);
		for (int i = 0; i < brushSize; i++) {
			for (int j = 0; j < brushSize; j++) {
				int paint_x = x + i;
				int paint_y = y + j;
				if (brush_shape[i][j] && paint_x >= 0 && paint_x < width && paint_y >= 0 && paint_y < height) {
					this.painting[0][paint_x][paint_y] = r;
					this.painting[1][paint_x][paint_y] = g;
					this.painting[2][paint_x][paint_y] = b;
				}
					
			}
		}
		
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
            
            File outputFile = new File("./output/" + outputPath + ".png");
            ImageIO.write(output, "png", outputFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
	}
	
	/** Helper function to turn 3 separate R, G, B integers into single RGB form for writing
	 * @param r
	 * @param g
	 * @param b
	 * @return
	 */
	private static int toRGB(int r, int g, int b) {
		return (r << 16) + (g << 8) + b;
	}
}

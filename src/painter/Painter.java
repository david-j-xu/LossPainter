package painter;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * Package handling painting
 * @author davidxu
 *
 */
public class Painter {
	Painting painting;
	double currentLoss;
	// Source image
	int[][][] source;
	int width;
	int height;
	String outPath;
	

	public Painter(String[] brushPaths, String srcPath, String outPath) throws IOException {
		
		Brush[] brushes = new Brush[brushPaths.length];
		
		for (int i = 0; i < brushes.length; i++) {
			brushes[i] = new Brush(brushPaths[i]);
		}
		
		// read in source image
        BufferedImage buffer = ImageIO.read(new File(srcPath));
		
        width = buffer.getWidth(null);
        height = buffer.getHeight(null);
        
        source = new int[3][width][height];

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int rgb = buffer.getRGB(i, j);
                
                int b = rgb & 0xFF;
                int g = (rgb >> 8) & 0xFF;
                int r = (rgb >> 16) & 0xFF;
                
                source[0][i][j] = r;
                source[1][i][j] = g;
                source[2][i][j] = b;
            }
        }
        
        // create painting
        painting = new Painting(width, height, brushes);
       
        currentLoss = calculateGlobalLoss(source, painting.getPainting());
        
        this.outPath = outPath;
		
		
	}
	
	
	/** Paints the picture using specified settings
	 * 
	 */
	public void paint() {
		boolean[][] canvas = new boolean[width][height];
		for (int i = 0 ; i < canvas.length; i++) {
			for (int j = 0; j < canvas[i].length; j++) {
				canvas[i][j] = true;
			}
		}
		
		int[] globalAverage = getAverageColor(canvas, 0, 0);
		painting.fill(globalAverage[0], globalAverage[1], globalAverage[2]);
		currentLoss = calculateGlobalLoss(source, painting.getPainting());
		for (int i = 1; i < 35; i*=2) {
			step(i);
		}
	}
	
	private void step(int grid) {
		int gridSize = (int) (Math.min(width, height) / grid);
		int brushSize = (int) (.75 * gridSize);
		
		double newLoss = currentLoss;
		// iterate until loss stops decreasing by significant amount
		do {
			currentLoss = newLoss;
			for (int i = 0; i < width / gridSize; i++) {
				for (int j = 0; j < height / gridSize; j++) {
					int currBrushSize = (int) (brushSize * Math.random() * 2);
					double currBrushAngle = (Math.PI * Math.random());
					
					int brush_choice = (int) (Math.random() * painting.getBrush().length);
					
					// handle probability 0 case of having out of bounds exception
					if (brush_choice == painting.getBrush().length) {
						brush_choice--;
					}
					
					boolean[][] brush = painting.getBrush()[brush_choice].getBrush(currBrushSize, currBrushAngle);
					int xloc = (int) (i * gridSize + Math.random() * gridSize);
					int yloc = (int) (j * gridSize + Math.random() * gridSize);
					int[] color = getAverageColor(brush, xloc, yloc);
					int r = color[0];
					int g = color[1];
					int b = color[2];
					
					painting.paintCandidate(brush_choice, currBrushSize, currBrushAngle, xloc, yloc,  r, g, b);

					painting.paint();
				}
			}
			
			newLoss = calculateGlobalLoss(source, painting.getPainting());
			System.out.println(newLoss / currentLoss);
		} while (newLoss < .99 * currentLoss);

		
	}

	private int[] getAverageColor(boolean[][] brush, int x, int y) {
		int count = 0;
		int[] color = new int[] {0, 0, 0};
		for (int i = 0; i < brush.length; i++) {
			for (int j = 0; j < brush[i].length; j++) {
				int curX = x + i;
				int curY = y + j;
				if (brush[i][j] && curX >= 0 && curY >= 0 && curX < width && curY < height) {
					// color is going to be used
					count++;
					color[0] += source[0][curX][curY];
					color[1] += source[1][curX][curY];
					color[2] += source[2][curX][curY];
				}
			}
		}
		
		if (count > 0) {
			color[0] /= count;
			color[1] /= count;
			color[2] /= count;
		}

		return color;
	}
	
	/** Writes the current image to disk
	 * 
	 */
	public void output() {
		painting.writeImage(outPath);
	}
	
	/** Calculates the loss between two paintings. Does not throw errors as is private.
	 * @param src
	 * @param cand
	 */
	private static double calculateGlobalLoss(int[][][] src, int[][][] cand) {
		double loss = 0;
		for (int i = 0; i < src.length; i++) {
			for (int j = 0 ; j < src[i].length; j++) {
				for (int k = 0; k < src[i][j].length; k++) {
					// calculate the L2 norm
					loss += Math.sqrt(Math.pow(src[i][j][k] - cand[i][j][k], 2));
				}
			}
		}
		
		return loss;
	}
}

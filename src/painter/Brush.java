package painter;
import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.lang.Math;

public class Brush {
	
	private int width;
	private int height;
	private boolean [][] matrix;
	
	
	/** 
	 * Creates a class representing a brush *shape*
	 * @param imagePath the path for the brush image
	 * @throws IOException 
	 */
	public Brush(String imagePath) throws IOException {
        BufferedImage buffer = ImageIO.read(new File(imagePath));
		
        width = buffer.getWidth(null);
        height = buffer.getHeight(null);
        
        matrix = new boolean[width][height];

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int rgb = buffer.getRGB(i, j);
                
                // set the locations of non-empty/non-white pixels
                if (rgb < 0) {
                	matrix[i][j] = true;
                }
            }
        }
	}
	
	
	/**
	 * Returns a brush stroke of the desired size, rotated by a desired amount
	 * @param size length of the longer side of the brush you want returned
	 * @param angle angle to be rotated in radians (counterclockwise)
	 * @return matrix representing the correctly sized brush
	 */
	public boolean[][] getBrush(int size, double angle) {
		int resized_width;
		int resized_height;
		double scale;
		
		if (width > height) {
			scale = (double) size / (double) width;
			resized_width = size;
			resized_height = (int) (scale * height);
		} else {
			scale = (double) size / (double) height;
			resized_width = (int) (scale * width);
			resized_height = size;
		}
		
		boolean[][] unrotated_brush = new boolean[size][size];

		// transform into a square, centered
		for (int i = 0; i < resized_width; i++) {
			for (int j = 0; j < resized_height; j++) {
				int mapped_i = (int) (i / scale);
				int mapped_j = (int) (j / scale);
				// integer offsets
				int width_offset = (size - resized_width) / 2;
				int height_offset = (size - resized_height) / 2;
				
				unrotated_brush[width_offset + i][height_offset + j] = this.matrix[mapped_i][mapped_j];
			}
		}
		
		// rotate into final brush
		boolean[][] brush = new boolean[size][size];
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				int center = size / 2;
				int x_offset = i - center;
				int y_offset = j - center;
				
				// use rotation matrix to rotate negative amount (to go from rotated space back to unrotated)
				// cos   sin
				// -sin  cos
				double cos = Math.cos(-1 * angle);
				double sin = Math.sin(-1 * angle);
				
				// displacements in original picture space
				int original_x_offset = (int) (cos * x_offset + sin * y_offset);
				int original_y_offset = (int) (-1 * sin * x_offset + cos * y_offset);
				
				// default to false if out of bounds, otherwise use the mapped coordinates
				int x_orig = original_x_offset + center;
				int y_orig = original_y_offset + center;
				
				if (x_orig >= 0 && x_orig < size && y_orig >= 0 && y_orig < size) {
					brush[i][j] = unrotated_brush[x_orig][y_orig];
				} else {
					brush[i][j] = false;
				}
				
				
			}
		}
		
		return brush;
	}
	
    /**
     * Helper function to create a brush image
     * @param brush
     * @param outputName
     */
    public static void writeImage(boolean[][] brush, String outputName) {
        try {
            BufferedImage output = new BufferedImage(brush.length, 
                                                    	brush[0].length, 
                                                        BufferedImage.TYPE_INT_RGB);
            int white = 0xFFFFFF;
            for (int i = 0; i < output.getWidth(); i++) {
                for (int j = 0; j < output.getHeight(); j++) {
                    if (brush[i][j]) {
                    	output.setRGB(i, j, white);
                    }
                }
            }
            
            File outputFile = new File(outputName);
            ImageIO.write(output, "png", outputFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
}

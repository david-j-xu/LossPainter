package painter;

import java.io.IOException;

/**
 * @author davidxu
 *
 */
public class Program {
	
	private static final String BRUSH_PATH = "brushes/brush.png";

	/**
	 * Main function
	 * @param args The arguments from stdin, args[1] is the input file, args[2] is output
	 * @throws IOException 
	 * 
	 */
	public static void main(String[] args) throws IOException {
		//TODO: Handle terminal input
		
		Brush brush = new Brush(BRUSH_PATH);
		Painting painting = new Painting(1000, 1000, brush);
		
		painting.paint(100, 2, 600, 600, 24, 16, 29);
		
		painting.writeImage("painting");
		

	}
	
	

}

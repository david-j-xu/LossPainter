package painter;

import java.io.IOException;

/**
 * @author davidxu
 *
 */
public class Program {
	
	private static final String[] BRUSH_PATHS = new String[]  {"brushes/brush.png",
															   "brushes/brush2.png",
															   "brushes/brush3.png",
															   "brushes/brush4.png",
															   "brushes/brush5.png",
															 };

	/**
	 * Main function
	 * @param args The arguments from stdin
	 * @throws IOException 
	 * 
	 */
	public static void main(String[] args) throws IOException {
		Painter painter = new Painter(BRUSH_PATHS, "input/coco.png", "output/coco.png");
		painter.paint();
		painter.output();
		

	}

}

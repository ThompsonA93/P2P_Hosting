package exec;

import gui.View;

/**
 * Executable of the Leecher
 */
public class Leecher extends View{	
	public final static boolean DEBUG = true;
	
	/*
	 * FIXME :: Find a better workaround
	 *   For fucking what's sake :: Launching the View via Leecher.main()
	 *   -> Is not possible as not 'Subclass of Application' fucking shit
	 *   -> Is not possible using View.launch();
	 */
	public static void main(String[] args) {
		launch(args);
	}	
}

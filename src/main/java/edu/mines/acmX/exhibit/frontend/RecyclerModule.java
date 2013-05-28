package edu.mines.acmX.exhibit.frontend;

import edu.mines.acmX.exhibit.input_services.mouse.ModalMouseMotionInputDriver;
import edu.mines.acmX.exhibit.input_services.openni.OpenNIHandTrackerInputDriver;

import edu.mines.acmX.exhibit.module_manager.*;



public class RecyclerModule extends AWTModule {
	
	public void run() {
	        GameManager gameManager = new GameManager("Recycler", true);
	        GameLauncher gameLauncher = new GameLauncher(gameManager);
			ModalMouseMotionInputDriver mouse = new ModalMouseMotionInputDriver();
	        //OpenNIHandTrackerInputDriver kinect = new OpenNIHandTrackerInputDriver();
	        gameLauncher.getGameManager().installInputDriver(mouse);
			gameLauncher.getGameManager().setState(gameLauncher);
			gameLauncher.getGameManager().run();
	        gameLauncher.getGameManager().destroy();
	}


}

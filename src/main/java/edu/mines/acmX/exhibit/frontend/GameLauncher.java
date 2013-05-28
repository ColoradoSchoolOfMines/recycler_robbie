package edu.mines.acmX.exhibit.frontend;
import java.awt.Graphics2D;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import edu.mines.acmX.exhibit.frontend.graphics.GameScreen;
import edu.mines.acmX.exhibit.frontend.graphics.InstructionScreen;
import edu.mines.acmX.exhibit.frontend.graphics.PlayerOptionsScreen;
import edu.mines.acmX.exhibit.frontend.hands.Hand;
import edu.mines.acmX.exhibit.frontend.hands.PlayerHand;
import edu.mines.acmX.exhibit.frontend.items.PowerUp;
import edu.mines.acmX.exhibit.frontend.items.RecyclableType;
import edu.mines.acmX.exhibit.frontend.items.RecycleBins;
import edu.mines.acmX.exhibit.frontend.motion.ConveyorBelt;
import edu.mines.acmX.exhibit.frontend.motion.FeedbackDisplay;
import edu.mines.acmX.exhibit.frontend.utils.GameConstants;
import edu.mines.acmX.exhibit.frontend.utils.PlayerMode;
import edu.mines.acmX.exhibit.stdlib.sound.Song;


/**
 * This class launches 2 instances of GameLogic which represent the left and right games being played.
 * User: jzeimen
 * Date: 11/19/12
 * Time: 8:17 PM
 * To change this template use File | Settings | File Templates.
 */
public class GameLauncher extends GameState {
    private static final Logger logger = Logger.getLogger(GameLauncher.class);
    private static final String SONG_FILENAME = "src/main/resources/Sounds/recyclotron.mp3";

    private Song song;
	private GameManager gameManager;
	private GameLogic leftGame, rightGame;
    private GameStatusDisplay leftGameStatusDisplay, rightGameStatusDisplay;
	private GameScreen gameScreen;
    private boolean gameCanStart;
    private boolean gameStarted;
    private long timeInstructionsStarted;
    private Thread preloading;
    private ArrayList<Hand> hands = new ArrayList<Hand>();
    private InstructionScreen instructionScreen;
    private PlayerOptionsScreen playerOptions;

	public GameLauncher(GameManager gameManager) {
        this.gameManager = gameManager;
         //Preloading the images will prevent some flickering.
        preloading = new Thread() {
            public void run() {
                long startTime = System.currentTimeMillis();
                RecyclableType.preLoadImages();
                GameScreen.getInstance().preLoadImages();
                PowerUp.PowerUpType.preLoadImages();
                FeedbackDisplay.preLoadImages();
                double totalTime = (System.currentTimeMillis()-startTime)/1000.0;
                logger.info("Image Loading finished took "+ totalTime+" seconds.");
            }
        };
        //If we name the thread it will show up in the debugger/profiler with that name.
        preloading.setName("preload-images");
        preloading.start();

		gameScreen = GameScreen.getInstance();
        leftGameStatusDisplay = new GameStatusDisplay(Side.LEFT);
        rightGameStatusDisplay = new GameStatusDisplay(Side.RIGHT);
        makeHands();

        song = new Song();
				System.out.println("LOADING A SONG!");
        //song.addTrack(new Track(SONG_FILENAME));
        song.setLooping(true);
        song.startPlaying();


        leftGame = new GameLogic(
                new RecycleBins(Side.LEFT),
				ConveyorBelt.getConveyorBeltPathLeft(),
                leftGameStatusDisplay,
                false,
                hands);
		rightGame = new GameLogic(
                new RecycleBins(Side.RIGHT),
                ConveyorBelt.getConveyorBeltPathRight(),
                rightGameStatusDisplay,
                GameConstants.DEBUG_COLLISIONS,
                hands);
        leftGame.addLinkToOtherScreen(rightGame);
        rightGame.addLinkToOtherScreen(leftGame);

        gameScreen.addTextSpriteHolder(leftGameStatusDisplay);
        gameScreen.addTextSpriteHolder(rightGameStatusDisplay);

        instructionScreen = new InstructionScreen();
        gameCanStart = false;
        timeInstructionsStarted = System.currentTimeMillis() / 1000;

        playerOptions = new PlayerOptionsScreen(gameManager);
	}

    /**
     * Creates the 4 hands provided by the backend and adds them to the game screen
     */
    private void makeHands() {
        hands = new ArrayList<Hand>();
        for (int i = 0; i < 4; i++) {
            hands.add(new PlayerHand(gameManager, i));
            gameScreen.addHandSprite(hands.get(hands.size() - 1).getSprite());
        }
    }

    /**
     * Gets an updated location of where the hands are in terms of screen coordinates.
     */
    private void updateHands() {
        for(Hand h : hands) {
            h.updateLocation();
        }
    }


    public GameManager getGameManager() {
        return gameManager;
    }

    protected void setUpPlayerMode(PlayerMode mode) {
        if(mode == PlayerMode.ONE_PLAYER) rightGame.turnOnComputer();
    }

    public GameLauncher updateThis(float time) {
        if (gameCanStart) {
            if (!playerOptions.canGameStart()) {
                playerOptions.updateThis();
            }
            else {
                if (!gameStarted) {
                    gameStarted = true;
                    setUpPlayerMode(playerOptions.getPlayerMode());
                }
                updateHands();
                rightGame.updateThis();
                leftGame.updateThis();

                if ( (leftGame.isPlaying() == false) && (rightGame.isPlaying() == false) ){
                    song.stopPlaying();
                    return null;
                }else if( (leftGame.isPlaying() == false) && (rightGame.isComputerPlayer() == true)){
                    song.stopPlaying();
                    return null;
                }
            }
        }
        else if ((System.currentTimeMillis() / 1000) > timeInstructionsStarted + 5 && !preloading.isAlive() ) {
            gameCanStart = true;
        }
        return this;
    }

	protected void drawThis(Graphics2D g2d) {
        if (gameCanStart) {
            if (!playerOptions.canGameStart()) {
                playerOptions.paint(g2d, gameManager.getCanvas());
            }
            else {
                gameScreen.paint(g2d, gameManager.getCanvas());
            }
        }
        else {
            instructionScreen.paint(g2d, gameManager.getCanvas());
        }
	}



}



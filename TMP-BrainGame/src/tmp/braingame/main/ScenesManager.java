package tmp.braingame.main;

import org.andengine.engine.Engine;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.ui.IGameInterface.OnCreateSceneCallback;

import tmp.braingame.scene.BaseScene;
import tmp.braingame.scene.LoadingScene;
import tmp.braingame.scene.MainMenuScene;
import tmp.braingame.scene.SplashScene;
import tmp.braingame.type.GameQueue;

public class ScenesManager {

	private static final ScenesManager INSTANCE = new ScenesManager();

	public enum SceneType {
		SCENE_SPLASH, SCENE_MENU, SCENE_GAME, SCENE_SUMMARY, SCENE_LOADING
	}

	private BaseScene splashScene;
	private BaseScene gameScene;
	private BaseScene loadingScene;
	private BaseScene menuScene;
	private BaseScene currentScene;
	
	private GameQueue gameQueue;
	
	private Engine mEngine = ResourcesManager.getInstance().engine;
	
	private ScenesManager() {
	}

	public static ScenesManager getInstance() {
		return INSTANCE;
	}

	private void setScene(BaseScene scene){
		currentScene = scene;
		mEngine.setScene(scene);
	}
	
	public void setScene(SceneType sceneType) {
		switch (sceneType) {
		case SCENE_MENU:
			currentScene.unloadResources();
			setScene(menuScene);
			break;
		case SCENE_GAME:
			setScene(gameScene);
			break;
		case SCENE_SPLASH:
			setScene(splashScene);
			break;
		case SCENE_LOADING:
			setScene(loadingScene);
			break;
		default:
			break;
		}
	}
	
	public void loadSplashSceeen(OnCreateSceneCallback pOnCreateSceneCallback){
//		ResourcesManager.getInstance().loadFont();
		splashScene = new SplashScene();
		splashScene.loadResouces();
		splashScene.createScene();
		currentScene = splashScene;
		pOnCreateSceneCallback.onCreateSceneFinished(splashScene);
	}
	
	public void loadMenuScene(){
		menuScene = new MainMenuScene();
		menuScene.loadResouces();
		menuScene.createScene();
		
		loadingScene = new LoadingScene();
		loadingScene.createScene();
		currentScene = menuScene;
		setScene(menuScene);
		ResourcesManager.getInstance().setBackgroundMusic(true);
	}
	
	public void loadGameScene(final Engine pEngine, final GameQueue pGameQueue){
		setScene(loadingScene);
		gameQueue = pGameQueue;
		
		menuScene.unloadResources();
		pEngine.registerUpdateHandler(new TimerHandler(2f, new ITimerCallback() {
			
			@Override
			public void onTimePassed(TimerHandler pTimerHandler) {
				mEngine.unregisterUpdateHandler(pTimerHandler);
				gameQueue.loadAllResource();
				gameScene = gameQueue.getGame();
				gameScene.createScene();
				setScene(gameScene);
			}
		}));
	}
	
	public BaseScene getCurrentScene(){
		return currentScene;
	}
	
	public void onGameFinish(){
		if(gameQueue.hasNext()){
			gameScene.unloadResources();
			gameScene.disposeScene();
			
			gameScene = gameQueue.getGame();
			setScene(gameScene);
		}
	}
}

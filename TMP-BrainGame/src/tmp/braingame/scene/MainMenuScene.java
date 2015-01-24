package tmp.braingame.scene;

import java.util.ArrayList;
import java.util.List;

import org.andengine.entity.scene.background.Background;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.TextMenuItem;
import org.andengine.entity.scene.menu.item.decorator.ScaleMenuItemDecorator;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.util.adt.color.Color;
import org.andengine.util.debug.Debug;

import tmp.braingame.main.GameSceneFactory;
import tmp.braingame.main.ResourcesManager;
import tmp.braingame.main.ScenesManager;
import tmp.braingame.main.ScenesManager.SceneType;
import tmp.braingame.type.GameQueue;
import android.app.AlertDialog;
import android.content.DialogInterface;

public class MainMenuScene extends BaseScene implements
		IOnMenuItemClickListener {

	private MenuScene menuMain;
	private MenuScene menuCatalog;
	private List<MenuScene> menuGame;

	private List<String> gameCatalog;
	private List<List<String>> gameArray;

	private Font font;

	private final int MENU_PLAY_DAILY = 0;
	private final int MENU_PLAY_PRACTICE = 1;

	private int currentCatalog = 0;

	@Override
	public void createScene() {
		// Create Background
		setBackground(new Background(Color.BLUE));
		// attachChild(new Text(CameraConfig.CAMERA_WIDTH / 2,
		// CameraConfig.CAMERA_HEIGHT / 2, font, "This is Menu Screen!",
		// vbom));

		// dailyPlay.setPosition(dailyPlay.getX(), dailyPlay.getY() - 25);
		// pratice.setPosition(pratice.getX(), pratice.getY() - 40);

		menuMain.buildAnimations();
		menuMain.setBackgroundEnabled(false);
		menuMain.setOnMenuItemClickListener(this);

		menuCatalog.buildAnimations();
		menuCatalog.setBackgroundEnabled(false);
		menuCatalog.setOnMenuItemClickListener(this);

		for (MenuScene menu : menuGame) {
			menu.buildAnimations();
			menu.setBackgroundEnabled(false);
			menu.setOnMenuItemClickListener(this);
		}

		setChildScene(menuMain);

	}

	@Override
	public void loadResouces() {

		// ------------------
		// Load Font
		// ------------------
		FontFactory.setAssetBasePath("font/");
		final ITexture fontTextTureManager = new BitmapTextureAtlas(
				ResourcesManager.getInstance().gameActivity.getTextureManager(),
				256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);

		font = FontFactory.createStrokeFromAsset(
				ResourcesManager.getInstance().gameActivity.getFontManager(),
				fontTextTureManager,
				ResourcesManager.getInstance().gameActivity.getAssets(),
				"BADABB.TTF", 50, true, Color.WHITE_ABGR_PACKED_INT, 2,
				Color.BLACK_ABGR_PACKED_INT);
		font.load();

		// -----------------
		// Load Menu content
		// -----------------

		// Load game name and catalog
		gameCatalog = GameSceneFactory.getInstance().getAllGameCatalog();
		gameArray = new ArrayList<List<String>>();
		for (String game : gameCatalog) {
			gameArray.add(GameSceneFactory.getInstance().getAllGameOfCatalog(
					game));
		}

		// ---------------------------------
		// Create Main Menu Scene
		// ---------------------------------
		menuMain = new MenuScene(mCamera);
		menuMain.setPosition(0, 0);

		final IMenuItem dailyPlay = new ScaleMenuItemDecorator(
				new TextMenuItem(MENU_PLAY_DAILY, font, "Daily Test", vbom),
				1.2f, 1);
		final IMenuItem pratice = new ScaleMenuItemDecorator(new TextMenuItem(
				MENU_PLAY_PRACTICE, font, "Pratice", vbom), 1.2f, 1);

		menuMain.addMenuItem(dailyPlay);
		menuMain.addMenuItem(pratice);

		// ----------------------------------
		// Create catalog menu scene and game of that catalog
		// ----------------------------------

		menuCatalog = new MenuScene(mCamera);
		menuGame = new ArrayList<MenuScene>();
		for (int i = 0; i < gameCatalog.size(); i++) {
			// Catalog menu scene item
			final IMenuItem itemCatalog = new ScaleMenuItemDecorator(
					new TextMenuItem(i, font, gameCatalog.get(i), vbom), 1.2f,
					1);
			menuCatalog.addMenuItem(itemCatalog);

			// Game menu scene item
			MenuScene gameMenu = new MenuScene(mCamera);
			List<String> gameName = gameArray.get(i);
			for (String name : gameName) {
				final IMenuItem itemGame = new ScaleMenuItemDecorator(
						new TextMenuItem(i, font, name, vbom), 1.2f, 1);
				gameMenu.addMenuItem(itemGame);
			}
			menuGame.add(gameMenu);
		}

	}

	@Override
	public void onBackKeyPressed() {
		// TODO Prompt user to confirm the exit/
		DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case DialogInterface.BUTTON_POSITIVE:
					// Yes button clicked
					mActivity.finish();
					break;
				}
			}
		};

		AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
		builder.setMessage("Are you sure?")
				.setPositiveButton("Yes", dialogClickListener).show();
	}

	@Override
	public void disposeScene() {
		// TODO Auto-generated method stub

	}

	@Override
	public SceneType getSceneType() {

		return SceneType.SCENE_MENU;
	}

	@Override
	public boolean onMenuItemClicked(MenuScene pMenuScene, IMenuItem pMenuItem,
			float pMenuItemLocalX, float pMenuItemLocalY) {

		if (pMenuScene == menuMain) {
			switch (pMenuItem.getID()) {
			case MENU_PLAY_DAILY:
				GameQueue queue = GameSceneFactory.getInstance()
						.getAllRandomGame();
				ScenesManager.getInstance().loadGameScene(mEngine, queue);
				break;
			case MENU_PLAY_PRACTICE:
				setChildScene(menuCatalog);
			default:
				break;
			}
		} else if (pMenuScene == menuCatalog) {
			currentCatalog = pMenuItem.getID();
			setChildScene(menuGame.get(pMenuItem.getID()));
		} else if (menuGame.contains(pMenuScene)) {
			GameQueue queue = GameSceneFactory.getInstance().getSpecificGame(
					gameCatalog.get(currentCatalog),
					gameArray.get(currentCatalog).get(pMenuItem.getID()));
			Debug.d(gameArray.get(currentCatalog).get(pMenuItem.getID()));
			ScenesManager.getInstance().loadGameScene(mEngine, queue);
		}

		// switch (pMenuItem.getID()) {
		// case MENU_PLAY_DAILY:
		// GameQueue queue = GameSceneFactory.getInstance().getAllRandomGame();
		// ScenesManager.getInstance().loadGameScene(mEngine, queue);
		// break;
		// default:
		// break;
		// }
		return false;
	}

	@Override
	public void start() {
		// TODO Auto-generated method stub

	}

	@Override
	public void unloadResources() {
		// TODO Auto-generated method stub

	}

}

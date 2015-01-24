package tmp.braingame.scene;

import java.util.ArrayList;

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

import tmp.braingame.main.GameSceneFactory;
import tmp.braingame.main.ResourcesManager;
import tmp.braingame.main.ScenesManager;
import tmp.braingame.main.ScenesManager.SceneType;
import tmp.braingame.type.GameQueue;
import android.app.AlertDialog;
import android.content.DialogInterface;

public class MainMenuScene extends BaseScene implements
		IOnMenuItemClickListener {

	private MenuScene menuChildScene;

	private Font font;

	private final int MENU_PLAY_DAILY = 0;
	private final int MENU_PLAY_PRACTICE = 1;

	@Override
	public void createScene() {
		// Create Background
		setBackground(new Background(Color.BLUE));
		// attachChild(new Text(CameraConfig.CAMERA_WIDTH / 2,
		// CameraConfig.CAMERA_HEIGHT / 2, font, "This is Menu Screen!",
		// vbom));

		// Create Menu child Scene
		menuChildScene = new MenuScene(mCamera);
		menuChildScene.setPosition(0, 0);

		final IMenuItem dailyPlay = new ScaleMenuItemDecorator(
				new TextMenuItem(MENU_PLAY_DAILY, font, "Daily Test", vbom),
				1.2f, 1);
		final IMenuItem pratice = new ScaleMenuItemDecorator(new TextMenuItem(
				MENU_PLAY_PRACTICE, font, "Pratice", vbom), 1.2f, 1);

		menuChildScene.addMenuItem(dailyPlay);
		menuChildScene.addMenuItem(pratice);

		menuChildScene.buildAnimations();
		menuChildScene.setBackgroundEnabled(false);

		dailyPlay.setPosition(dailyPlay.getX(), dailyPlay.getY() - 25);
		pratice.setPosition(pratice.getX(), pratice.getY() - 40);

		menuChildScene.setOnMenuItemClickListener(this);

		setChildScene(menuChildScene);

	}

	@Override
	public void loadResouces() {
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
		switch (pMenuItem.getID()) {
		case MENU_PLAY_DAILY:
			GameQueue queue = GameSceneFactory.getInstance().getSpecificGame("centralization", "Jumper Game");
			ScenesManager.getInstance().loadGameScene(mEngine, queue);
			break;
		default:
			break;
		}
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

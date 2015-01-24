package tmp.braingame.scene;

import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.util.adt.color.Color;

import tmp.braingame.config.CameraConfig;
import tmp.braingame.main.ResourcesManager;
import tmp.braingame.main.ScenesManager.SceneType;

public class SplashScene extends BaseScene {
	
	private Font font;

	@Override
	public void createScene() {
		setBackground(new Background(Color.YELLOW));
		attachChild(new Text(CameraConfig.CAMERA_WIDTH / 2,
				CameraConfig.CAMERA_HEIGHT / 2,
				font,
				"This is Splash Screen!", vbom));
	}

	@Override
	public void loadResouces() {
		FontFactory.setAssetBasePath("font/");
		final ITexture fontTextTureManager = new BitmapTextureAtlas(
				ResourcesManager.getInstance().gameActivity.getTextureManager(), 256, 256,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);

		font = FontFactory.createStrokeFromAsset(
				ResourcesManager.getInstance().gameActivity.getFontManager(), fontTextTureManager,
				ResourcesManager.getInstance().gameActivity.getAssets(), "BADABB.TTF", 50, true,
				Color.WHITE_ABGR_PACKED_INT, 2, Color.BLACK_ABGR_PACKED_INT);
		font.load();
	}

	@Override
	public void onBackKeyPressed() {
		// TODO Auto-generated method stub

	}

	@Override
	public void disposeScene() {
		this.detachSelf();
		this.dispose();
	}

	@Override
	public SceneType getSceneType() {
		// TODO Auto-generated method stub
		return SceneType.SCENE_SPLASH;
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

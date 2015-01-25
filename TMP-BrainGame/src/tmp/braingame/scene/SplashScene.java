package tmp.braingame.scene;

import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.util.adt.color.Color;

import tmp.braingame.config.CameraConfig;
import tmp.braingame.main.ResourcesManager;
import tmp.braingame.main.ScenesManager.SceneType;

public class SplashScene extends BaseScene {
	
	private Font font;
	private ITextureRegion splashRegion;
	private BitmapTextureAtlas splashAtlas;
	
	@Override
	public void createScene() {
		attachChild(new Sprite(CameraConfig.CAMERA_WIDTH/2, CameraConfig.CAMERA_HEIGHT/2, splashRegion, vbom));
	}

	@Override
	public void loadResouces() {
		// Load font
		FontFactory.setAssetBasePath("font/");
		final ITexture fontTextTureManager = new BitmapTextureAtlas(
				ResourcesManager.getInstance().gameActivity.getTextureManager(), 256, 256,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);

		font = FontFactory.createStrokeFromAsset(
				ResourcesManager.getInstance().gameActivity.getFontManager(), fontTextTureManager,
				ResourcesManager.getInstance().gameActivity.getAssets(), "BADABB.TTF", 50, true,
				Color.WHITE_ABGR_PACKED_INT, 2, Color.BLACK_ABGR_PACKED_INT);
		font.load();
		
		// Load image...
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		splashAtlas = new BitmapTextureAtlas(mActivity.getTextureManager(), 1024, 1024,TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		splashRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(splashAtlas, mActivity, "splash.png",0,0);
		
		splashAtlas.load();
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
		splashAtlas.unload();
	}

}

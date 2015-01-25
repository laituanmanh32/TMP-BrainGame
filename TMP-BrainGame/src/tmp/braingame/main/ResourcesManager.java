package tmp.braingame.main;

import java.io.IOException;

import org.andengine.audio.music.Music;
import org.andengine.audio.music.MusicFactory;
import org.andengine.engine.Engine;
import org.andengine.engine.camera.BoundCamera;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.color.Color;

public class ResourcesManager {

	private static final ResourcesManager INSTANCE = new ResourcesManager();

	public Engine engine;
	public GameActivity gameActivity;
	public BoundCamera camera;
	public VertexBufferObjectManager vbom;

	public Font generalFont;

	private Music mMusic;
	
	private ResourcesManager() {
	}
	
	public void loadResource(){
		
		// Load General font
		FontFactory.setAssetBasePath("font/");
		final ITexture fontTextTureManager = new BitmapTextureAtlas(
				ResourcesManager.getInstance().gameActivity.getTextureManager(), 256, 256,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);

		generalFont = FontFactory.createStrokeFromAsset(
				gameActivity.getFontManager(), fontTextTureManager,
				gameActivity.getAssets(), "BADABB.TTF", 50, true,
				Color.WHITE_ABGR_PACKED_INT, 2, Color.BLACK_ABGR_PACKED_INT);
		generalFont.load();
		
		// Load music
		MusicFactory.setAssetBasePath("sfx/backgroundmusic/");
		try {
			mMusic = MusicFactory.createMusicFromAsset(gameActivity.getMusicManager(), gameActivity, "bgm_menu.mp3");
			mMusic.setLooping(true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static ResourcesManager getInstance() {
		return INSTANCE;
	}
	
	public void setBackgroundMusic(boolean enable){
		if(enable) mMusic.play();
		else mMusic.stop();
	}

	public void prepareManager(Engine engine, GameActivity activity,
			BoundCamera camera2, VertexBufferObjectManager vbom) {
		getInstance().engine = engine;
		getInstance().gameActivity = activity;
		getInstance().camera = camera2;
		getInstance().vbom = vbom;
	}
}

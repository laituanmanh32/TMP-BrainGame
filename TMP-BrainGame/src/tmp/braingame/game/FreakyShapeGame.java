package tmp.braingame.game;

import android.util.Log;

import org.andengine.engine.camera.hud.HUD;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.region.ITextureRegion;

import tmp.braingame.main.ResourcesManager;
import tmp.braingame.main.ScenesManager;

/**
 * Created by HUNGPHONGPC on 24/01/2015.
 */
public class FreakyShapeGame extends GameBaseScene {
    private ButtonSprite button1;
    private ButtonSprite button2;
    private ITextureRegion button1texture;
    private ITextureRegion button2texture;
    private BuildableBitmapTextureAtlas buildBmp;

    private Background createFromRGB(float r,float g,float b){
        return new Background(r/0xff,g/0xff,b/0xff);
    }
    private Background bg_darkblue=createFromRGB(0x2c, 0x3e, 0x50);
    private Background bg_lightred=createFromRGB(0xe7,0x4c,0x3c);//#e74c3c
    private HUD gameHUD;

    @Override
    public void createScene() {
        createBackground();
        createHUD();
    }

    //Tao giao dien game
    private void createHUD() {

    }

    private void createBackground() {
        setBackground(bg_darkblue);
    }

    @Override
    public void loadResouces() {
        loadEntities();
    }

    ButtonSprite.OnClickListener buttonclick=new ButtonSprite.OnClickListener(){
        @Override
        public void onClick(ButtonSprite pButtonSprite, float pTouchAreaLocalX, float pTouchAreaLocalY) {
            Log.d("BUTTON", "FUCK YOU");
        }
    };
    private void loadEntities() {
        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/freaky/");
        buildBmp=new BuildableBitmapTextureAtlas(mActivity.getTextureManager(),512,512);
        button1texture=BitmapTextureAtlasTextureRegionFactory.createFromAsset(buildBmp,mActivity,"TMP.png");
        button2texture=BitmapTextureAtlasTextureRegionFactory.createFromAsset(buildBmp,mActivity,"TMP2.png");
        button1=new ButtonSprite(20,20,button1texture,button2texture,vbom,buttonclick);
        button2=new ButtonSprite(260,20,button1texture,button2texture,vbom,buttonclick);

    }

    @Override
    public void unloadResources() {

    }

    @Override
    public void start() {

    }

    @Override
    public void onBackKeyPressed() {
        ScenesManager.getInstance().setScene(ScenesManager.SceneType.SCENE_MENU);
    }

    @Override
    public void disposeScene() {

    }

    @Override
    public ScenesManager.SceneType getSceneType() {
        return ScenesManager.SceneType.SCENE_GAME;
    }

}

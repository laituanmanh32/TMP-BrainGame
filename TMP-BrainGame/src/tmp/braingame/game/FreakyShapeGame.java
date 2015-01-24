package tmp.braingame.game;

import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.input.touch.TouchEvent;

import tmp.braingame.main.ScenesManager;
import tmp.braingame.scene.BaseScene;

/**
 * Created by HUNGPHONGPC on 24/01/2015.
 */
public class FreakyShapeGame extends GameBaseScene implements IOnSceneTouchListener {
    private Background createFromRGB(float r,float g,float b){
        return new Background(r/0xff,g/0xff,b/0xff);
    }
    private Background bg_darkblue=createFromRGB(0x2c,0x3e,0x50);
    private Background bg_lightred=createFromRGB(0xe7,0x4c,0x3c);//#e74c3c

    @Override
    public void createScene() {
        createBackground();
        createHUD();
        setOnSceneTouchListener(this);
    }

    //Tao giao dien game
    private void createHUD() {

    }

    private void createBackground() {
        setBackground(bg_darkblue);
    }

    @Override
    public void loadResouces() {

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

    @Override
    public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
        return false;
    }
}

package tmp.braingame.game;

import android.util.Log;

import org.andengine.engine.camera.hud.HUD;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.buildable.builder.BlackPawnTextureAtlasBuilder;
import org.andengine.opengl.texture.atlas.buildable.builder.ITextureAtlasBuilder;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.util.debug.Debug;

import java.util.Random;

import tmp.braingame.main.ResourcesManager;
import tmp.braingame.main.ScenesManager;

/**
 * Created by HUNGPHONGPC on 24/01/2015.
 */
public class FreakyShapeGame extends GameBaseScene implements IOnSceneTouchListener {
    private ButtonSprite button1;
    private ButtonSprite button2;
    private ITextureRegion button1texture;
    private ITextureRegion button2texture;
    private Sprite shape1,shape2;
    private BitmapTextureAtlas buildBmp;
    private Text mQuestion;
    private GameData gameData;


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
        this.setTouchAreaBindingOnActionDownEnabled(true);
        this.registerTouchArea(button1);
        this.registerTouchArea(button2);
        attachChild(button1);
        attachChild(button2);
        attachChild(mQuestion);
        createGameData();
    }

    private void createGameData() {
        gameData=new GameData();
        gameData.setGameDataChangedListener(new GameDataChangedListener() {
            @Override
            public void onChanged() {
                mQuestion.setText(gameData.getQuestion());
                mQuestion.setPosition(20+mQuestion.getWidth()/2,400);
                String file1=gameData.getFirstAnswerFileName();
                String file2=gameData.getSecondAnswerFileName();
                /*if (shape1!=null){
                    button1.detachChild(shape1);
                    button1.detachChild(shape2);
                }
                shape1=new Sprite(40,40,
                        BitmapTextureAtlasTextureRegionFactory.createFromAsset(buildBmp,mActivity,file1,40,40),vbom);
                shape2=new Sprite(40,40,
                        BitmapTextureAtlasTextureRegionFactory.createFromAsset(buildBmp,mActivity,file2,40,40),vbom);
                button1.attachChild(shape1);
                button2.attachChild(shape2);*/
            }
        });
        gameData.GenerateRandomData();
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
            boolean ans=true;
            if (ans){
                gameData.GenerateRandomData();

            }
        }
    };
    private void loadEntities() {
        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/freaky/");
        buildBmp=new BitmapTextureAtlas(mActivity.getTextureManager(),1024,1024);
        button1texture=BitmapTextureAtlasTextureRegionFactory.createFromAsset(buildBmp,mActivity,"TMP.png",0,0);
        button2texture=BitmapTextureAtlasTextureRegionFactory.createFromAsset(buildBmp,mActivity,"TMP2.png",0,0);
        buildBmp.load();
            button1=new ButtonSprite(0,0,button1texture,button2texture,vbom,buttonclick);
        button1.setOnClickListener(buttonclick);
        button1.setScale(0.5f);
        button1.setPosition(25 + button1.getWidthScaled()/2 ,20 + button1.getHeightScaled()/2);

            button2=new ButtonSprite(0,0,button1texture,button2texture,vbom,buttonclick);
        button2.setScale(0.5f);
        button2.setPosition(250 + button2.getWidthScaled()/2,20 + button2.getHeightScaled()/2);

        mQuestion=new Text(0, 0, mResouceManager.generalFont,
                "Game Over!",100, vbom);
        mQuestion.setPosition(0,400);
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

    class GameData{
        private String mQuestion;
        private int mRequestShape;
        private int mRequestColor;
        private int mShape1;
        private int mShape2;
        private int mColor1;
        private int mColor2;

        public static final int SHAPE_COUNT=3;
        public static final int SHAPE_CIRCLE=0;
        public static final int SHAPE_RECTANGLE=1;
        public static final int SHAPE_TRIANGLE=2;

        public static final int COLOR_COUNT=6;
        public static final int COLOR_WHITE=0;
        public static final int COLOR_RED=1;
        public static final int COLOR_PINK=2;
        public static final int COLOR_PURPLE=3;
        public static final int COLOR_BLUE=4;
        public static final int COLOR_TEAL=5;

        private final String[] shapes={"circle","rectangle","triangle"};
        private final String[] colors={"white","red","pink","purple","blue","teal"};

        private GameDataChangedListener listener;

        public String getQuestion(){return mQuestion;}
        public String getFirstAnswerFileName(){return "TMP_"+colors[mColor1]+shapes[mShape1]+".png";}
        public String getSecondAnswerFileName(){return "TMP_"+colors[mColor2]+shapes[mShape2]+".png";}
        public void setGameDataChangedListener(GameDataChangedListener listener){
            this.listener=listener;
        }
        public void GenerateRandomData(){
            Random r=new Random();
            mColor1=r.nextInt(COLOR_COUNT);
            do{
                mColor2=r.nextInt(COLOR_COUNT);
            }while (mColor2==mColor1);
            mShape1=r.nextInt(SHAPE_COUNT);
            do{
                mShape2=r.nextInt(SHAPE_COUNT);
            }while (mShape2==mShape1);
            boolean mAns=r.nextBoolean();
            if (mAns){
                mRequestColor=mColor1;
                mRequestShape=mShape1;
            } else {
                mRequestColor=mColor2;
                mRequestShape=mShape2;
            }

            mQuestion="Select "+colors[mRequestColor]+"\n"+shapes[mRequestShape];
            //mQuestion=shapes[mRequestShape];
            //after generated all
            if (listener!=null)
                listener.onChanged();
        }
    }
    interface GameDataChangedListener{
        public void onChanged();
    }
}

package tmp.braingame.scene;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.BoundCamera;
import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.Scene;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import tmp.braingame.main.GameActivity;
import tmp.braingame.main.ResourcesManager;
import tmp.braingame.main.ScenesManager.SceneType;

public abstract class BaseScene extends Scene {
	protected Engine mEngine;
	protected GameActivity mActivity;
	protected ResourcesManager mResouceManager;
	protected VertexBufferObjectManager vbom;
	protected BoundCamera mCamera;
	
	public BaseScene(){
		this.mResouceManager = ResourcesManager.getInstance();
		this.mEngine = mResouceManager.engine;
		this.mActivity = mResouceManager.gameActivity;
		this.vbom = mResouceManager.vbom;
		this.mCamera = mResouceManager.camera;
	}
	
	public abstract void createScene();
	public abstract void loadResouces();
	public abstract void unloadResources();
	public abstract void start();
	public abstract void onBackKeyPressed();
	public abstract void disposeScene();
	public abstract SceneType getSceneType();

}

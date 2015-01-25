package tmp.braingame.scene;

import org.andengine.entity.scene.background.Background;
import org.andengine.entity.text.Text;
import org.andengine.util.adt.color.Color;

import tmp.braingame.config.CameraConfig;
import tmp.braingame.main.ScenesManager.SceneType;

public class LoadingScene extends BaseScene {

	@Override
	public void createScene() {
		setBackground(new Background(Color.WHITE));
		attachChild(new Text(CameraConfig.CAMERA_WIDTH / 2,
				CameraConfig.CAMERA_HEIGHT / 2,
				tmp.braingame.main.ResourcesManager.getInstance().generalFont,
				"Loading ...", vbom));
	}

	@Override
	public void loadResouces() {
		// TODO Auto-generated method stub

	}

	@Override
	public void unloadResources() {
		// TODO Auto-generated method stub

	}

	@Override
	public void start() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onBackKeyPressed() {
		// TODO Auto-generated method stub

	}

	@Override
	public void disposeScene() {
		// TODO Auto-generated method stub

	}

	@Override
	public SceneType getSceneType() {
		// TODO Auto-generated method stub
		return null;
	}

}

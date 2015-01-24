package tmp.braingame.game;

import tmp.braingame.main.ScenesManager;
import tmp.braingame.scene.BaseScene;

public abstract class GameBaseScene extends BaseScene {
	protected void onFinish(){
		ScenesManager.getInstance().onGameFinish();
	}
}

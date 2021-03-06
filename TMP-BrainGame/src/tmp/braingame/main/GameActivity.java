package tmp.braingame.main;

import java.io.IOException;

import org.andengine.engine.Engine;
import org.andengine.engine.LimitedFPSEngine;
import org.andengine.engine.camera.BoundCamera;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.WakeLockOptions;
import org.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.andengine.engine.options.resolutionpolicy.FixedResolutionPolicy;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.ui.activity.BaseGameActivity;

import tmp.braingame.config.CameraConfig;
import android.content.Intent;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;

public class GameActivity extends BaseGameActivity {

	private BoundCamera camera;

	@Override
	public EngineOptions onCreateEngineOptions() {

		camera = new BoundCamera(0, 0, CameraConfig.CAMERA_WIDTH,
				CameraConfig.CAMERA_HEIGHT);
		final EngineOptions engine = new EngineOptions(true,
				CameraConfig.SCREEN_OERIENTATION, new RatioResolutionPolicy(CameraConfig.CAMERA_WIDTH, CameraConfig.CAMERA_HEIGHT),
				camera);
		engine.getAudioOptions().setNeedsMusic(true).setNeedsSound(true);
		engine.setWakeLockOptions(WakeLockOptions.SCREEN_ON);
		engine.getRenderOptions().setDithering(true);
		return engine;
	}

	@Override
	public Engine onCreateEngine(EngineOptions pEngineOptions) {

		return new LimitedFPSEngine(pEngineOptions, 30);
	}

	@Override
	public void onCreateResources(
			OnCreateResourcesCallback pOnCreateResourcesCallback)
			throws IOException {
		ResourcesManager.getInstance().prepareManager(mEngine, this, camera,
				this.getVertexBufferObjectManager());
		pOnCreateResourcesCallback.onCreateResourcesFinished();
	}

	@Override
	public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback)
			throws IOException {
		ScenesManager.getInstance().loadSplashSceeen(pOnCreateSceneCallback);
	}

	@Override
	public void onPopulateScene(Scene pScene,
			OnPopulateSceneCallback pOnPopulateSceneCallback)
			throws IOException {
		mEngine.registerUpdateHandler(new TimerHandler(3f, new ITimerCallback() {
			
			@Override
			public void onTimePassed(TimerHandler pTimerHandler) {
				mEngine.unregisterUpdateHandler(pTimerHandler);
				ResourcesManager.getInstance().loadResource();
				GameSceneFactory.getInstance().loadData();
				ScenesManager.getInstance().loadMenuScene();
			}
		}));
		pOnPopulateSceneCallback.onPopulateSceneFinished();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		System.exit(0);
	}
	
	@Override
	public void onBackPressed() {
		ScenesManager.getInstance().getCurrentScene().onBackKeyPressed();
	}

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
    }

    public void facebookLogin() {
        Session.openActiveSession(this, true, new Session.StatusCallback() {
            @Override
            public void call(Session session, SessionState state, Exception exception) {
                if (session.isOpened()) {
                    Request.executeMeRequestAsync(session, new Request.GraphUserCallback() {
                        @Override
                        public void onCompleted(GraphUser user, Response response) {
                            if (user != null) {
                                // CALLBACK: USER IS LOGGED IN
                                // DO YOUR STUFF HERE
                                // e.g. fbUsername = user.getFirstName();
                                // this will save the users first name to a public variable fbUsername
                            }
                        }
                    });
                }
            }
        });
    }
}

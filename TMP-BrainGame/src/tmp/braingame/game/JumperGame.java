package tmp.braingame.game;

import java.io.IOException;

import org.andengine.engine.camera.BoundCamera;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.LoopEntityModifier;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.extension.physics.box2d.FixedStepPhysicsWorld;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.buildable.builder.BlackPawnTextureAtlasBuilder;
import org.andengine.opengl.texture.atlas.buildable.builder.ITextureAtlasBuilder.TextureAtlasBuilderException;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.SAXUtils;
import org.andengine.util.adt.color.Color;
import org.andengine.util.debug.Debug;
import org.andengine.util.level.EntityLoader;
import org.andengine.util.level.constants.LevelConstants;
import org.andengine.util.level.simple.SimpleLevelEntityLoaderData;
import org.andengine.util.level.simple.SimpleLevelLoader;
import org.xml.sax.Attributes;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

import tmp.braingame.config.CameraConfig;
import tmp.braingame.main.ResourcesManager;
import tmp.braingame.main.ScenesManager;
import tmp.braingame.main.ScenesManager.SceneType;

public class JumperGame extends GameBaseScene implements IOnSceneTouchListener {

	// ------------------------
	// GAME RESOURCES
	// ------------------------
	// Game Texture region
	public ITextureRegion platform1_region;
	public ITextureRegion platform2_region;
	public ITextureRegion platform3_region;
	public ITextureRegion coin_region;

	// Player layer
	public ITiledTextureRegion player_region;

	// Game Texture
	public BuildableBitmapTextureAtlas gameTextureAtlas;

	private HUD gameHUD;
	private Text gameScore;
	private PhysicsWorld physicsWorld;

	private Text gameOverText;
	private boolean gameOverDisplayed = false;

	private static final String TAG_ENTITY = "entity";
	private static final String TAG_ENTITY_ATTRIBUTE_X = "x";
	private static final String TAG_ENTITY_ATTRIBUTE_Y = "y";
	private static final String TAG_ENTITY_ATTRIBUTE_TYPE = "type";

	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLATFORM1 = "platform1";
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLATFORM2 = "platform2";
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLATFORM3 = "platform3";
	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_COIN = "coin";

	private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLAYER = "player";

	private Player player;

	private int score = 0;
	private boolean firstTouch = false;

	@Override
	protected void onFinish() {
		// TODO Auto-generated method stub

	}

	@Override
	public void createScene() {
		createBackground();
		createHUD();
		createPhysics();
		loadLevel(1);
		createGameOverText();
		setOnSceneTouchListener(this);
	}

	private void createBackground() {
		setBackground(new Background(Color.BLUE));
		
	}

	private void createHUD() {
		gameHUD = new HUD();

		gameScore = new Text(0, 0, ResourcesManager.getInstance().generalFont,
				"Score: 12345", vbom);
		gameScore.setPosition(20, mCamera.getHeight() - gameScore.getHeight());
		gameScore.setAnchorCenter(0, 0);
		gameScore.setText("Score: 0");
		gameHUD.attachChild(gameScore);

		mCamera.setHUD(gameHUD);
	}

	private void createPhysics() {
		physicsWorld = new FixedStepPhysicsWorld(60, new Vector2(0, -17), false);
		physicsWorld.setContactListener(contactListener());
		registerUpdateHandler(physicsWorld);
	}

	private void loadLevel(int levelID) {
		final SimpleLevelLoader levelLoader = new SimpleLevelLoader(vbom);

		final FixtureDef FIXTURE_DEF = PhysicsFactory.createFixtureDef(0,
				0.01f, 0.5f);

		levelLoader
				.registerEntityLoader(new EntityLoader<SimpleLevelEntityLoaderData>(
						LevelConstants.TAG_LEVEL) {
					public IEntity onLoadEntity(
							final String pEntityName,
							final IEntity pParent,
							final Attributes pAttributes,
							final SimpleLevelEntityLoaderData pSimpleLevelEntityLoaderData)
							throws IOException {
						final int width = SAXUtils.getIntAttributeOrThrow(
								pAttributes,
								LevelConstants.TAG_LEVEL_ATTRIBUTE_WIDTH);
						final int height = SAXUtils.getIntAttributeOrThrow(
								pAttributes,
								LevelConstants.TAG_LEVEL_ATTRIBUTE_HEIGHT);

						mCamera.setBounds(0, 0, width, height); // here we set
																// camera bounds
						mCamera.setBoundsEnabled(true);
						return JumperGame.this;
					}
				});

		levelLoader
				.registerEntityLoader(new EntityLoader<SimpleLevelEntityLoaderData>(
						LevelConstants.TAG_LEVEL) {
					public IEntity onLoadEntity(
							final String pEntityName,
							final IEntity pParent,
							final Attributes pAttributes,
							final SimpleLevelEntityLoaderData pSimpleLevelEntityLoaderData)
							throws IOException {
						final int width = SAXUtils.getIntAttributeOrThrow(
								pAttributes,
								LevelConstants.TAG_LEVEL_ATTRIBUTE_WIDTH);
						final int height = SAXUtils.getIntAttributeOrThrow(
								pAttributes,
								LevelConstants.TAG_LEVEL_ATTRIBUTE_HEIGHT);

						// TODO later we will specify camera BOUNDS and create
						// invisible walls
						// on the beginning and on the end of the level.

						return JumperGame.this;
					}
				});

		levelLoader
				.registerEntityLoader(new EntityLoader<SimpleLevelEntityLoaderData>(
						TAG_ENTITY) {
					public IEntity onLoadEntity(
							final String pEntityName,
							final IEntity pParent,
							final Attributes pAttributes,
							final SimpleLevelEntityLoaderData pSimpleLevelEntityLoaderData)
							throws IOException {
						final int x = SAXUtils.getIntAttributeOrThrow(
								pAttributes, TAG_ENTITY_ATTRIBUTE_X);
						final int y = SAXUtils.getIntAttributeOrThrow(
								pAttributes, TAG_ENTITY_ATTRIBUTE_Y);
						final String type = SAXUtils.getAttributeOrThrow(
								pAttributes, TAG_ENTITY_ATTRIBUTE_TYPE);

						final Sprite levelObject;

						if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLATFORM1)) {
							levelObject = new Sprite(x, y, platform1_region,
									vbom);
							PhysicsFactory.createBoxBody(physicsWorld,
									levelObject, BodyType.StaticBody,
									FIXTURE_DEF).setUserData("platform1");
						} else if (type
								.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLATFORM2)) {
							levelObject = new Sprite(x, y, platform2_region,
									vbom);
							final Body body = PhysicsFactory.createBoxBody(
									physicsWorld, levelObject,
									BodyType.StaticBody, FIXTURE_DEF);
							body.setUserData("platform2");
							physicsWorld
									.registerPhysicsConnector(new PhysicsConnector(
											levelObject, body, true, false));
						} else if (type
								.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLATFORM3)) {
							levelObject = new Sprite(x, y, platform3_region,
									vbom);
							final Body body = PhysicsFactory.createBoxBody(
									physicsWorld, levelObject,
									BodyType.StaticBody, FIXTURE_DEF);
							body.setUserData("platform3");
							physicsWorld
									.registerPhysicsConnector(new PhysicsConnector(
											levelObject, body, true, false));
						} else if (type
								.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_COIN)) {
							levelObject = new Sprite(x, y, coin_region, vbom) {
								@Override
								protected void onManagedUpdate(
										float pSecondsElapsed) {
									super.onManagedUpdate(pSecondsElapsed);

									/**
									 * TODO we will later check if player
									 * collide with this (coin) and if it does,
									 * we will increase score and hide coin it
									 * will be completed in next articles (after
									 * creating player code)
									 */
									if (player.collidesWith(this)) {
										addScore(10);
										this.setVisible(false);
										this.setIgnoreUpdate(true);
									}
								}
							};
							levelObject
									.registerEntityModifier(new LoopEntityModifier(
											new ScaleModifier(1, 1, 1.3f)));
						} else if (type
								.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLAYER)) {
							player = new Player(x, y, vbom, mCamera,
									physicsWorld) {

								@Override
								public void onDie() {
									if (!gameOverDisplayed) {
										displayGameOverText();
									}
								}
							};
							levelObject = player;
						} else {
							throw new IllegalArgumentException();
						}

						levelObject.setCullingEnabled(true);

						return levelObject;
					}
				});

		levelLoader.loadLevelFromAsset(mActivity.getAssets(), "level/"
				+ levelID + ".lvl");
	}

	@Override
	public void loadResouces() {
		loadEntityGraphic();

	}

	private void loadEntityGraphic() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/game/");
		gameTextureAtlas = new BuildableBitmapTextureAtlas(
				mActivity.getTextureManager(), 1024, 1024,
				TextureOptions.BILINEAR);
		platform1_region = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(gameTextureAtlas, mActivity, "plat1.png");
		platform2_region = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(gameTextureAtlas, mActivity, "plat2.png");
		platform3_region = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(gameTextureAtlas, mActivity, "plat3.png");
		coin_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				gameTextureAtlas, mActivity, "coin.png");
		player_region = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(gameTextureAtlas, mActivity,
						"player.png", 3, 1);

		try {
			this.gameTextureAtlas
					.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(
							0, 0, 0));
			this.gameTextureAtlas.load();
		} catch (TextureAtlasBuilderException e) {
			// TODO Auto-generated catch block
			Debug.e(e);
		}
	}

	private void createGameOverText() {
		gameOverText = new Text(0, 0, mResouceManager.generalFont,
				"Game Over!", vbom);
	}

	private void addScore(int i) {
		score += i;
		gameScore.setText("Score: " + score);
	}

	private void displayGameOverText() {
		mCamera.setChaseEntity(null);
		gameOverText.setPosition(mCamera.getCenterX(), mCamera.getCenterY());
		attachChild(gameOverText);
		gameOverDisplayed = true;
	}

	@Override
	public void unloadResources() {

	}

	@Override
	public void start() {

	}

	@Override
	public void onBackKeyPressed() {
		 ScenesManager.getInstance().setScene(SceneType.SCENE_MENU);
	}

	@Override
	public void disposeScene() {

	}

	@Override
	public SceneType getSceneType() {

		return SceneType.SCENE_GAME;
	}

	// Collision event handler
	private ContactListener contactListener() {
		ContactListener contactListener = new ContactListener() {
			public void beginContact(Contact contact) {
				final Fixture x1 = contact.getFixtureA();
				final Fixture x2 = contact.getFixtureB();

				if (x1.getBody().getUserData() != null
						&& x2.getBody().getUserData() != null) {
					if (x2.getBody().getUserData().equals("player")) {
						player.increaseFootContacts();
					}

					if (x1.getBody().getUserData().equals("platform3")
							&& x2.getBody().getUserData().equals("player")) {
						x1.getBody().setType(BodyType.DynamicBody);
					}

					if (x1.getBody().getUserData().equals("platform2")
							&& x2.getBody().getUserData().equals("player")) {
						mEngine.registerUpdateHandler(new TimerHandler(0.2f,
								new ITimerCallback() {
									public void onTimePassed(
											final TimerHandler pTimerHandler) {
										pTimerHandler.reset();
										mEngine.unregisterUpdateHandler(pTimerHandler);
										x1.getBody().setType(
												BodyType.DynamicBody);
									}
								}));
					}
				}
			}

			public void endContact(Contact contact) {
				final Fixture x1 = contact.getFixtureA();
				final Fixture x2 = contact.getFixtureB();

				if (x1.getBody().getUserData() != null
						&& x2.getBody().getUserData() != null) {
					if (x2.getBody().getUserData().equals("player")) {
						player.decreaseFootContacts();
					}
				}
			}

			public void preSolve(Contact contact, Manifold oldManifold) {

			}

			public void postSolve(Contact contact, ContactImpulse impulse) {

			}
		};
		return contactListener;
	}

	public abstract class Player extends AnimatedSprite {

		private Body body;
		private boolean canRun = false;
		private int footContacts = 0;

		public Player(float pX, float pY, VertexBufferObjectManager vbo,
				Camera camera, PhysicsWorld physicsWorld) {
			super(pX, pY, player_region, vbo);
			createPhysics(camera, physicsWorld);
			camera.setChaseEntity(this);
		}

		public abstract void onDie();

		private void createPhysics(final Camera camera,
				PhysicsWorld physicsWorld) {
			body = PhysicsFactory.createBoxBody(physicsWorld, this,
					BodyType.DynamicBody,
					PhysicsFactory.createFixtureDef(0, 0, 0));

			body.setUserData("player");
			body.setFixedRotation(true);

			physicsWorld.registerPhysicsConnector(new PhysicsConnector(this,
					body, true, false) {
				@Override
				public void onUpdate(float pSecondsElapsed) {
					super.onUpdate(pSecondsElapsed);
					camera.onUpdate(0.1f);

					if (getY() <= 0) {
						onDie();
					}

					if (canRun) {
						body.setLinearVelocity(new Vector2(5, body
								.getLinearVelocity().y));
					}
				}
			});
		}

		public void setRunning() {
			canRun = true;

			final long PLAYER_ANIMATED[] = new long[] { 100, 100, 100 };
			animate(PLAYER_ANIMATED, 0, 2, true);
		}

		public void jump() {
			if (footContacts < 1) {
				return;
			}
			body.setLinearVelocity(new Vector2(body.getLinearVelocity().x, 12));
		}

		public void increaseFootContacts() {
			footContacts++;
		}

		public void decreaseFootContacts() {
			footContacts--;
		}
	}

	@Override
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
		if (pSceneTouchEvent.isActionDown()) {
			if (!firstTouch) {
				player.setRunning();
				firstTouch = true;
			} else {
				player.jump();
			}
		}
		return false;
	}

}

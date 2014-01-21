package com.view.swimmingFish;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;
import org.andengine.audio.music.Music;
import org.andengine.audio.music.MusicFactory;
import org.andengine.audio.sound.Sound;
import org.andengine.audio.sound.SoundFactory;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.AutoParallaxBackground;
import org.andengine.entity.scene.background.ParallaxBackground.ParallaxEntity;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.TextMenuItem;
import org.andengine.entity.scene.menu.item.decorator.ColorMenuItemDecorator;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.ButtonSprite.OnClickListener;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.entity.util.FPSLogger;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.buildable.builder.BlackPawnTextureAtlasBuilder;
import org.andengine.opengl.texture.atlas.buildable.builder.ITextureAtlasBuilder.TextureAtlasBuilderException;
import org.andengine.opengl.texture.bitmap.BitmapTexture;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegionFactory;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.HorizontalAlign;
import org.andengine.util.adt.io.in.IInputStreamOpener;
import org.andengine.util.color.Color;
import org.andengine.util.debug.Debug;

import com.swimmingFish.Loading.AsyncTaskLoader;
import com.swimmingFish.Loading.IAsyncCallback;
import com.swimmingFish.Role.*;
import com.swimmingFish.sql.Score;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.PointF;
import android.hardware.SensorManager;
import android.opengl.GLES20;
import android.text.InputFilter;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.EditText;

/**
 * The main class to realize the AndEngine framework inheriting the
 * SimpleBaseGameActivity
 */
public class GameActivity extends SimpleBaseGameActivity implements
		OnClickListener, IOnMenuItemClickListener, IOnSceneTouchListener {

	private Camera mCamera;
	public static final int CAMERA_WIDTH = 720;
	public static final int CAMERA_HEIGHT = 480;
	public static final int ANIMATION_FRAMELENGTH = 250;
	private static final float TOUCH_TOLERANCE = 4;
	static final int BUBBLE = 0;
	static final int LIFE = 1;
	static final int STAR = 2;
	static final int SPEEDUP = 3;
	static final int LAUNCHER = 4;
	static final int propTypeCount = 5;

	static final int COMMOM_FISH1 = 0;
	static final int COMMOM_FISH2 = 1;
	static final int ATTACK_FISH = 2;
	static final int BOSS_FISH = 3;
	static final int TRASHCAN = 4;
	static final int COMMOM_FISH3 = 5;
	static final int SLOW_FISH = 6;
	static final int BLIND_FISH = 7;
	static final int LIGHT = 8;

	static final int ATTRACKENEMYCONT = 5;
	static final int FUNCTIONENEMYCONT = 3;
	static final float RESETSPEEDTIME = 6f;
	static final int DIALOG_SHOW_BACK = 0;
	static final int DIALOG_SHOW_QUIT = 1;
	static final int DIALOG_SHOW_RESTART = 2;
	static final int DIALOG_SHOW_MENURESTART = 3;
	static final int DIALOG_SHOW_MENUQUIT = 4;
	static final int DIALOG_SHOW_EDITTEXT = 5;
	static public int LEVEL = 1;
	public int mTime = 0;
	public long mMeter = 0;

	/**
	 * MeanFrame material
	 */
	private BitmapTexture backgroundTexture; // Mean background
	private BuildableBitmapTextureAtlas mBitmapButtonTextureAtlas;
	private Scene mainScene;
	private ITextureRegion mainBackground;
	private ITextureRegion startButton1;// Texture for the button
	private ITextureRegion startButton2;
	private ITextureRegion musicButton1;
	private ITextureRegion musicButton2;
	private ITextureRegion scoresButton1;
	private ITextureRegion otherButton1;
	private ITextureRegion topTenScores;
	private Sprite startButton;
	private ButtonSprite otherButton;
	private Sprite scoresButton;
	private ButtonSprite musicButton;
	private ButtonSprite quitButton;
	private ITextureRegion quitButton1;
	private ITextureRegion quitButton2;

	/**
	 * fishSprite Texture material
	 */
	public static Player fish;
	private BuildableBitmapTextureAtlas fishSpriteTextureAtlas;
	private TiledTextureRegion mainFish;
	private ArrayList<TiledTextureRegion> enemyITextureRegionList = new ArrayList<TiledTextureRegion>(); // Texture
																											// for
																											// the
																											// enemy
	public static ArrayList<BaseEnemy> enemySpriteList = new ArrayList<BaseEnemy>();// All
																					// the
																					// enemies
																					// in
																					// the
																					// scene
	private ArrayList<Enemy> enemyList = new ArrayList<Enemy>(); // For the
																	// store of
																	// the enemy
																	// texture
	private AnimatedSprite touchFish; //
	private TiledTextureRegion slowTiledTextureRegion;
	private TiledTextureRegion speedUpTextureRegion;
	private AnimatedSprite slowsprite;
	private Sprite iceSprite;

	/**
	 * Music material
	 */
	private Music menuBgMusic;
	private Music bgMusic;
	private Sound clickSound;
	private Sound hitSound;
	private Sound launcherSound;
	private Sound lightSound;
	private Sound loseSound;
	private Sound pickupSound;
	private Sound scoreSound;
	private Sound readySound;
	private Sound goSound;

	/**
	 * Loading material
	 */
	private BitmapTextureAtlas loadingTexture;
	protected Scene LoadingScene;
	private ITextureRegion loadingITextureRegion;

	/**
	 * Background material
	 */
	private BitmapTextureAtlas mAutoParallaxBackgroundTexture;
	private ITextureRegion mParallaxLayerBack;
	private ITextureRegion mParallaxLayerbootom;
	private ITextureRegion gameOverTextureRegion;
	private ITextureRegion resetartButton1;
	private ITextureRegion resetartButton2;
	private Sprite restartButton;
	private Sprite backButton;
	private ITextureRegion pauseBackground;
	/**
	 * help scene material
	 */
	private BitmapTextureAtlas helpBitmapTextureAtlas;
	private ITextureRegion helpMainViewITextureRegion;
	private ITextureRegion helpControlViewITextureRegion;
	private ITextureRegion helpAttrackFishITextureRegion;
	private ITextureRegion helpFunctionFishITextureRegion;
	private ITextureRegion helpPropViewITextureRegion;
	private ITextureRegion helpMainTextITextureRegion;
	private ITextureRegion helpButton1_1;
	private ITextureRegion helpButton1_2;
	private ITextureRegion helpButton2_1;
	private ITextureRegion helpButton2_2;
	private ITextureRegion helpBackITextureRegion;
	private ButtonSprite help_Button1_1;
	private ButtonSprite help_Button1_2;
	private ButtonSprite help_Button2_1;
	private ButtonSprite help_Button2_2;
	private ButtonSprite helpQuitButton;
	Scene helpScene;
	Scene helpControlScene;

	/**
	 * Font Material
	 */
	private Font mFont;
	private Font numberFont;
	private Font englishFont;
	private Font pointFont;
	private Text pointText;

	/**
	 * Props Material
	 */
	private BuildableBitmapTextureAtlas dynamicPropsTextureAtlas;
	private TiledTextureRegion explosion;
	private TiledTextureRegion paopao;
	private TiledTextureRegion star;
	private TiledTextureRegion life;
	private TiledTextureRegion cloud;
	private ITextureRegion blindITextureRegion;
	private ArrayList<TiledTextureRegion> dynPropsITextureRegionList = new ArrayList<TiledTextureRegion>(); // emeny的纹理图
	private float PROPSSTAYTIME = 0;
	private float PROPSFUNCTIONTIME = 0; // lasting time for the effect
	private boolean bubbleFunction = false; // the function of bubble
	private boolean explosionFunction = false; // the function of explosion
	private boolean currentPropsMove = false;
	private boolean isAttract = false; // explosion attract
	private BaseEnemy attackEnemy = null; // the attacked enemy
	private boolean isStar = false;
	AnimatedSprite speedUpprite;
	private int blindTempTime = 0;

	/**
	 * Sensor parameter
	 */
	SensorManager sensorManager;

	/**
	 * Gesture move parameter
	 */
	private ArrayList<PointF> graphics = new ArrayList<PointF>(); // The list
																	// for the
																	// point
																	// moved
	float x = 0, y = 0;
	private Boolean over = false;
	private Boolean back = false;
	private float mX, mY;
	private int of = 0;

	/**
	 * Pause submenu parameter
	 */
	protected static final int MENU_RESET = 0;
	protected static final int MENU_RESTART = MENU_RESET + 1;
	protected static final int MENU_MUSIC = MENU_RESTART + 1;
	protected static final int MENU_QUIT = MENU_MUSIC + 1;
	private static final int FONT_SIZE = 48;
	/**
	 * Time parameter
	 */
	private static float PROPSGENERATETIME = 15f; // The disappear gap of prop
	private static int ATTRACKFISHGENERATETIME = 180; // The disappear gap of
														// attrackFish
	private static int FUNCTIONFISHGENERATETIME = 150; // The disappear gap of
														// functionFish
	private static float cloudTime = 4f; // cloud time
	private static float SAFETIME = 1f; // safe time after crush

	/**
	 * Stochastic mode parameter
	 */
	Random random = new Random();
	double[] attrackEnemyW = { 0.2, 0.2, 0.25, 0.1, 0.25 };
	int[] attrackEnemyCount = new int[ATTRACKENEMYCONT];
	int attrackEnemyRandomType = -1;
	long attrackEnemyAllCount = 0;

	double[] functionEnemyW = { 0.3, 0.4, 0.3 };
	int[] functionEnemyCount = new int[FUNCTIONENEMYCONT];
	int functionEnemyRandomType = -1;
	long functionEnemyAllCount = 0;

	double[] yW = { 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1 };
	int[] yCount = new int[10];
	int yRandomType = -1;
	long yAllCount = 0;

	double[] propsW = { 0.3, 0.3, 0.2, 0.1, 0.1 };
	int[] propsCount = new int[propTypeCount];
	int propsRandomType = -1;
	long propsAllCount = 0;

	/**
	 * Persistence setup
	 */
	private boolean isMusic;
	Score score;
	Cursor cursor;

	/**
	 * Other
	 */
	private ButtonSprite pauseButton;
	private ITextureRegion pauseButton1;
	private int point; // score
	private int skillPoint;
	private int gameOverCount;
	private DynamicProps currentPropSprite = null;
	public static ArrayList<Sprite> lifeList = new ArrayList<Sprite>(); // Set
																		// for
																		// the
																		// icon
																		// indicating
																		// the
																		// life
	private boolean isSafe = false;
	private boolean isPropFunction = false;
	private float backgroundSpeed = 20f;
	private boolean touchBegin = false;
	private ITextureRegion iceITextureRegion;
	private int lastRandomYType;
	private int[] skillCount = new int[ATTRACKENEMYCONT + FUNCTIONENEMYCONT];
	static public boolean mGameRunning;// Indicate the state of the game

	/**
	 * Scene
	 */
	public Scene mMainScene;
	public Scene gameOverScene;
	protected MenuScene mMenuScene;

	public void resetParams() {

		LEVEL = 1;
		mTime = 0;
		point = 0;
		lifeList.clear();
		enemySpriteList.clear();
		PROPSSTAYTIME = 0;
		PROPSFUNCTIONTIME = 0;
		bubbleFunction = false;
		currentPropsMove = false;
		isAttract = false;
		attackEnemy = null;
		blindTempTime = 0;
		graphics.clear();
		over = false;
		back = false;
		of = 0;
		isSafe = false;
		isPropFunction = false;
		backgroundSpeed = 20f;
		isStar = false;
		skillPoint = 0;
		for (int temp : skillCount) {
			skillCount[temp] = 0;
		}
	}

	@Override
	public EngineOptions onCreateEngineOptions() {
		mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		final EngineOptions engineOptions = new EngineOptions(true,
				ScreenOrientation.LANDSCAPE_FIXED, new FillResolutionPolicy(),
				mCamera);
		engineOptions.getAudioOptions().setNeedsMusic(true);
		engineOptions.getAudioOptions().setNeedsSound(true);
		return engineOptions;

	}

	@Override
	public void onCreateResources() {
		loadingTexture();

	}

	@Override
	public Scene onCreateScene() {
		this.mEngine.registerUpdateHandler(new FPSLogger());
		this.runOnUiThread(new Runnable() {
			public void run() {
				IAsyncCallback callback = new IAsyncCallback() {// Inner class
																// to override
																// the method
					@Override
					public void workToDo() {
						loadOtherTexture();// Asynchronous operations to
											// continue the initialization
					}

					@Override
					public void onComplete() {
						Log.e("test", "onComplete");
						LoadingScene.setChildScene(onCreateMainViewScene());

					}
				};
				new AsyncTaskLoader().execute(callback);
			}
		});
		return creatLoadingScene();
	}

	TimerHandler safeHandler = new TimerHandler(SAFETIME, true,
			new ITimerCallback() {
				@Override
				public void onTimePassed(TimerHandler arg0) {
					isSafe = false;
					fish.setAlpha(1f);
					mMainScene.unregisterUpdateHandler(safeHandler);
				}
			});

	TimerHandler avoidAttrackHandler = new TimerHandler(0.8f, true,
			new ITimerCallback() {
				@Override
				public void onTimePassed(TimerHandler arg0) {
					isSafe = false;
					mMainScene.unregisterUpdateHandler(avoidAttrackHandler);
				}
			});

	TimerHandler resetSpeedHandler = new TimerHandler(RESETSPEEDTIME, true,
			new ITimerCallback() {

				@Override
				public void onTimePassed(TimerHandler arg0) {
					fish.resetSpeed();

					fish.unregisterUpdateHandler(resetSpeedHandler);
					Log.d("resetspeed", "slow_resetspeed" + fish.getSpeed());
				}
			});

	@Override
	public boolean onKeyDown(final int pKeyCode, final KeyEvent pEvent) {
		if (pKeyCode == KeyEvent.KEYCODE_MENU
				&& pEvent.getAction() == KeyEvent.ACTION_DOWN) {
			if (this.mMainScene.hasChildScene()) {
			} else {
				if (isMusic) {
					bgMusic.pause();
				}
				this.mMainScene.setChildScene(this.mMenuScene, false, true,
						true);
			}
			return true;
		} else {
			return super.onKeyDown(pKeyCode, pEvent);
		}
	}

	@Override
	public boolean onMenuItemClicked(final MenuScene pMenuScene,
			final IMenuItem pMenuItem, final float pMenuItemLocalX,
			final float pMenuItemLocalY) {
		if (isMusic)
			clickSound.play();
		switch (pMenuItem.getID()) { // Determine by the ID
		case MENU_RESET:
			this.mMenuScene.back();
			if (isMusic) {
				bgMusic.play();
			}
			return true;
		case MENU_RESTART:
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					showDialog(DIALOG_SHOW_MENURESTART);
				}
			});
			return true;
		case MENU_MUSIC:
			saveMusicSharePrefences(!isMusic);
			this.mMenuScene.back();
			mMenuScene = createMenuScene();
			this.mMainScene.setChildScene(mMenuScene, false, true, true);
			return true;
		case MENU_QUIT:
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					showDialog(DIALOG_SHOW_MENUQUIT);
				}
			});
			return true;
		default:
			return false;
		}
	}

	protected MenuScene createMenuScene() {
		final MenuScene menuScene = new MenuScene(this.mCamera);
		final Sprite pauseBack = new Sprite(
				(CAMERA_WIDTH - pauseBackground.getWidth()) / 2,
				(CAMERA_HEIGHT - pauseBackground.getHeight()) / 2,
				pauseBackground, getVertexBufferObjectManager());
		menuScene.attachChild(pauseBack);
		// MenuItem to be put on the menuScene
		final IMenuItem tabItem = new ColorMenuItemDecorator(new TextMenuItem(
				0, this.mFont, " ", this.getVertexBufferObjectManager()),
				new Color(1, 0, 0), new Color(0, 0, 0));
		menuScene.addMenuItem(tabItem);
		final IMenuItem tabItem3 = new ColorMenuItemDecorator(new TextMenuItem(
				0, this.mFont, " ", this.getVertexBufferObjectManager()),
				new Color(1, 0, 0), new Color(0, 0, 0));
		menuScene.addMenuItem(tabItem3);

		final IMenuItem resetMenuItem = new ColorMenuItemDecorator(
				new TextMenuItem(MENU_RESET, this.mFont, "resume",
						this.getVertexBufferObjectManager()),
				new Color(1, 0, 0), new Color(0, 0, 0));
		resetMenuItem.setBlendFunction(GLES20.GL_SRC_ALPHA,
				GLES20.GL_ONE_MINUS_SRC_ALPHA);
		menuScene.addMenuItem(resetMenuItem);

		final IMenuItem tabItem2 = new ColorMenuItemDecorator(new TextMenuItem(
				0, this.numberFont, " ", this.getVertexBufferObjectManager()),
				new Color(1, 0, 0), new Color(0, 0, 0));
		menuScene.addMenuItem(tabItem2);

		final IMenuItem restartMenuItem = new ColorMenuItemDecorator(
				new TextMenuItem(MENU_RESTART, this.mFont, "restart",
						this.getVertexBufferObjectManager()),
				new Color(1, 0, 0), new Color(0, 0, 0));
		restartMenuItem.setBlendFunction(GLES20.GL_SRC_ALPHA,
				GLES20.GL_ONE_MINUS_SRC_ALPHA);
		menuScene.addMenuItem(restartMenuItem);
		final IMenuItem tabItem4 = new ColorMenuItemDecorator(new TextMenuItem(
				0, this.numberFont, " ", this.getVertexBufferObjectManager()),
				new Color(1, 0, 0), new Color(0, 0, 0));
		menuScene.addMenuItem(tabItem4);
		String musicString = "music:on";
		if (!isMusic)
			musicString = "music:off";
		TextMenuItem musictextMenuItem = new TextMenuItem(MENU_MUSIC,
				this.mFont, musicString, this.getVertexBufferObjectManager());
		musictextMenuItem.setTag(MENU_MUSIC);
		final IMenuItem musicMenuItem = new ColorMenuItemDecorator(
				musictextMenuItem, new Color(1, 0, 0), new Color(0, 0, 0));
		musicMenuItem.setBlendFunction(GLES20.GL_SRC_ALPHA,
				GLES20.GL_ONE_MINUS_SRC_ALPHA);
		menuScene.addMenuItem(musicMenuItem);
		final IMenuItem tabItem5 = new ColorMenuItemDecorator(new TextMenuItem(
				0, this.numberFont, " ", this.getVertexBufferObjectManager()),
				new Color(1, 0, 0), new Color(0, 0, 0));
		menuScene.addMenuItem(tabItem5);

		final IMenuItem quitMenuItem = new ColorMenuItemDecorator(
				new TextMenuItem(MENU_QUIT, this.mFont, "QUIT",
						this.getVertexBufferObjectManager()),
				new Color(1, 0, 0), new Color(0, 0, 0));
		quitMenuItem.setBlendFunction(GLES20.GL_SRC_ALPHA,
				GLES20.GL_ONE_MINUS_SRC_ALPHA);
		menuScene.addMenuItem(quitMenuItem);

		menuScene.buildAnimations();
		menuScene.setBackgroundEnabled(false);
		menuScene.setOnMenuItemClickListener(this);// Binding
													// OnMenuItemClickListener
		return menuScene;
	}

	public void createNewEnemy(int index) {
		float enemyY = 0;
		final BaseEnemy baseEnemy = new BaseEnemy(730, 0,
				this.enemyITextureRegionList.get(index),
				this.getVertexBufferObjectManager(), this.getEngine(), index);

		if (index == TRASHCAN) {
			enemyY = (int) (CAMERA_HEIGHT - this.enemyITextureRegionList.get(
					index).getHeight());
			baseEnemy.setY(enemyY);
			baseEnemy.setSpeed((float) 0.12 * backgroundSpeed);
		} else {
			baseEnemy.setY(getRandomYType());
		}

		if (index == COMMOM_FISH1) {
			float lightGenTime = random.nextFloat() * 3 + 1;// shoot bullet
			final TimerHandler lightHandler = new TimerHandler(lightGenTime,//
					true, new ITimerCallback() {
						@Override
						public void onTimePassed(TimerHandler pTimerHandler) {
							runOnUpdateThread(new Runnable() {
								@Override
								public void run() {
									final BaseEnemy lightEnemy = new BaseEnemy(
											baseEnemy.getX()
													+ 10
													- enemyITextureRegionList
															.get(8).getWidth(),
											baseEnemy.getY(),
											enemyITextureRegionList.get(8),
											getVertexBufferObjectManager(),
											getEngine(), 8);
									mMainScene.attachChild(lightEnemy);
									if (isMusic) {
										lightSound.play();
									}
									enemySpriteList.add(lightEnemy);
								}
							});
						}
					});
			baseEnemy.registerUpdateHandler(lightHandler);
			baseEnemy.registerUpdateHandler(new TimerHandler(lightGenTime + 1f,
					true, new ITimerCallback() {
						@Override
						public void onTimePassed(TimerHandler arg0) {
							baseEnemy.unregisterUpdateHandler(lightHandler);
							// Avoid more bullet to shoot
						}
					}));
		}

		if (isStar && (index != TRASHCAN)) { // Eating star will increase the
												// speed
			baseEnemy.setSpeed(baseEnemy.getSpeed() * 2);
		}
		this.mMainScene.attachChild(baseEnemy);
		enemySpriteList.add(baseEnemy);
	}

	public void createNewProps(int index) {
		int x = (int) (Math.random() * 10000) % 300 + 400;
		int y = (int) (Math.random() * 10000) % 440;

		PROPSFUNCTIONTIME = 0;
		PROPSSTAYTIME = 0;
		currentPropSprite = new DynamicProps(x, y,
				this.dynPropsITextureRegionList.get(index),
				getVertexBufferObjectManager(), index);

		this.mMainScene.attachChild(currentPropSprite);
		isPropFunction = false;
		currentPropSprite.registerUpdateHandler(propTimerHandler);
	}

	public TimerHandler propTimerHandler = new TimerHandler(1f, true,
			new ITimerCallback() {
				@Override
				public void onTimePassed(TimerHandler arg0) {

					if (!currentPropSprite.isFunction()) {
						PROPSSTAYTIME++;
						Log.d("Props", "new_stayTime=" + PROPSSTAYTIME);
						if (PROPSSTAYTIME > currentPropSprite.getStayTime()) {
							runOnUpdateThread(new Runnable() {
								@Override
								public void run() {
									mMainScene.detachChild(currentPropSprite);
									currentPropSprite = null;
									mMainScene.detachChild(touchFish);
								}
							});
						}
					} else {
						PROPSSTAYTIME = 0;
						PROPSFUNCTIONTIME++;
						Log.d("Props", "new_functionTime=" + PROPSFUNCTIONTIME);
						if (PROPSFUNCTIONTIME > currentPropSprite
								.getfunctionTime()) {
							runOnUpdateThread(new Runnable() {
								@Override
								public void run() {
									if (isStar) {
										Log.d("Props", "new_setIsStarFalse");
										isStar = false;
										for (BaseEnemy enemy : enemySpriteList) {
											enemy.resetSpeed(enemy.getType());
										}
										if (backgroundSpeed < 35) {
											backgroundSpeed = 20 + mTime / 10;
										} else {
											backgroundSpeed = 35;
										}
										((AutoParallaxBackground) mMainScene
												.getBackground())
												.setParallaxChangePerSecond(backgroundSpeed);
										fish.resetSpeed();
										mMainScene
												.detachChild(currentPropSprite);
									} else {
										mMainScene
												.detachChild(currentPropSprite);
									}
									currentPropSprite = null;
								}
							});
							bubbleFunction = false;
							explosionFunction = false;
						}
					}
				}
			});

	/**
	 * Stochastic function
	 */
	public int getRandomAttrackEnemyType() {
		if (attrackEnemyRandomType == -1) {
			attrackEnemyRandomType = random.nextInt(ATTRACKENEMYCONT) % 3;
		} else {
			double[] tempW = new double[ATTRACKENEMYCONT + 1];

			for (int i = 0; i < ATTRACKENEMYCONT; i++) {
				tempW[i] = attrackEnemyW[i] * (attrackEnemyAllCount + 1)
						- attrackEnemyCount[i];
				if (tempW[i] < 0)
					tempW[i] = 0;
				tempW[ATTRACKENEMYCONT] += tempW[i];
			}

			for (int i = 0; i < ATTRACKENEMYCONT; i++) {
				tempW[i] /= tempW[ATTRACKENEMYCONT];
			}

			double temp = random.nextDouble();

			for (int i = 0; i < ATTRACKENEMYCONT; i++) {

				if (temp <= tempW[i]) {
					attrackEnemyRandomType = i;
					break;
				}
				temp -= tempW[i];
			}
		}
		attrackEnemyCount[attrackEnemyRandomType]++;
		attrackEnemyAllCount++;
		return attrackEnemyRandomType;
	}

	public int getRandomFunctionEnemyType() {
		if (functionEnemyRandomType == -1) {
			functionEnemyRandomType = random.nextInt(FUNCTIONENEMYCONT) % 3;
		} else {
			double[] tempW = new double[FUNCTIONENEMYCONT + 1];

			for (int i = 0; i < FUNCTIONENEMYCONT; i++) {
				tempW[i] = functionEnemyW[i] * (functionEnemyAllCount + 1)
						- functionEnemyCount[i];
				if (tempW[i] < 0)
					tempW[i] = 0;
				tempW[FUNCTIONENEMYCONT] += tempW[i];
			}

			for (int i = 0; i < FUNCTIONENEMYCONT; i++) {
				tempW[i] /= tempW[FUNCTIONENEMYCONT];
			}

			double temp = random.nextDouble();

			for (int i = 0; i < FUNCTIONENEMYCONT; i++) {

				if (temp <= tempW[i]) {
					functionEnemyRandomType = i;
					break;
				}
				temp -= tempW[i];
			}

		}
		functionEnemyCount[functionEnemyRandomType]++;
		functionEnemyAllCount++;
		return functionEnemyRandomType;
	}

	public int getRandomPropType() {
		if (propsRandomType == -1) {
			propsRandomType = random.nextInt(propTypeCount) % 2;
		} else {
			double[] tempW = new double[propTypeCount + 1];

			for (int i = 0; i < propTypeCount; i++) {
				tempW[i] = propsW[i] * (propsAllCount + 1) - propsCount[i];
				if (tempW[i] < 0)
					tempW[i] = 0;
				tempW[propTypeCount] += tempW[i];
			}

			for (int i = 0; i < propTypeCount; i++) {
				tempW[i] /= tempW[propTypeCount];
			}

			double temp = random.nextDouble();

			for (int i = 0; i < propTypeCount; i++) {
				if (temp <= tempW[i]) {
					propsRandomType = i;
					break;
				}
				temp -= tempW[i];
			}

		}
		propsCount[propsRandomType]++;
		propsAllCount++;
		// return 2;
		return propsRandomType;
	}

	public float getRandomYType() {
		if (yRandomType == -1) {
			yRandomType = random.nextInt(10);
			lastRandomYType = yRandomType;
		} else {
			double[] tempW = new double[10 + 1];
			for (int i = 0; i < 10; i++) {
				tempW[i] = yW[i] * (yAllCount + 1) - yCount[i];
				if (tempW[i] < 0)
					tempW[i] = 0;
				tempW[10] += tempW[i];
			}
			for (int i = 0; i < 10; i++) {
				tempW[i] /= tempW[10];
			}
			double temp = random.nextDouble();
			for (int i = 0; i < 10; i++) {
				if (temp <= tempW[i]) {
					yRandomType = i;
					break;
				}
				temp -= tempW[i];
			}
			if (lastRandomYType == yRandomType) {
				yRandomType = (yRandomType + 1) % 10;
			}
		}
		yCount[yRandomType]++;
		lastRandomYType = yRandomType;
		yAllCount++;
		return yRandomType * 36 + random.nextFloat() * 36;
	}

	public void gameOver() {
		if (isMusic) {
			loseSound.play();
			this.bgMusic.pause();
		}

		this.gameOverScene = new Scene();

		Sprite gameOverSprite = new Sprite(0, 0, gameOverTextureRegion,
				getVertexBufferObjectManager());
		restartButton = new ButtonSprite(440, 350, resetartButton1,
				resetartButton2, getVertexBufferObjectManager(), this);
		backButton = new ButtonSprite(CAMERA_WIDTH / 2 - 70,
				CAMERA_HEIGHT - 50, resetartButton1, resetartButton2,
				getVertexBufferObjectManager(), this);
		gameOverCount = -1;
		mMainScene.clearUpdateHandlers();
		gameOverScene.attachChild(gameOverSprite);
		final Text restartText = new Text(450, 350, englishFont, "Restart", 7,
				getVertexBufferObjectManager());
		final Text backText = new Text(CAMERA_WIDTH / 2 - 50,
				CAMERA_HEIGHT - 50, englishFont, "Back", 4,
				getVertexBufferObjectManager());
		backButton.setWidth(backText.getWidth() + 40);

		for (int i = 0; i < skillCount.length; i++) {
			int param = 0;
			switch (i) {
			case 0:
			case 1:
			case 2:
				param = 180;
				break;
			case 3:
				param = 250;
				break;
			case 4:
			case 5:
			case 6:
			case 7:
				param = 120;
				break;
			default:
				break;
			}
			skillPoint += skillCount[i] * param;
		}
		gameOverScene.attachChild(backButton);
		gameOverScene.attachChild(restartButton);
		gameOverScene.attachChild(restartText);
		gameOverScene.attachChild(backText);
		mMainScene.setChildScene(gameOverScene, true, true, true);
		gameOverScene.registerUpdateHandler(new TimerHandler(0.1f, true,
				new ITimerCallback() {
					@Override
					public void onTimePassed(TimerHandler arg0) {

						if (gameOverCount < skillCount.length + 3) {
							if (isMusic)
								scoreSound.play();
						}
						gameOverCount++;
						if (gameOverCount < skillCount.length) {
							int y = 140;
							int x = 120 + 107 * gameOverCount;
							if (gameOverCount >= 5) {
								x = 120 + 107 * (gameOverCount - 5);
								y = 260;
							}
							Text tempText = new Text(x, y, numberFont, "X"
									+ skillCount[gameOverCount], 3,
									getVertexBufferObjectManager());
							gameOverScene.attachChild(tempText);
						}

						if (gameOverCount == skillCount.length + 1) {
							Text skillScore = new Text(510, 230, numberFont, ""
									+ skillPoint, 5,
									getVertexBufferObjectManager());
							gameOverScene.attachChild(skillScore);
						}

						if (gameOverCount == skillCount.length + 2) {
							runOnUiThread(new Runnable() {
								@Override
								public void run() {
									showDialog(DIALOG_SHOW_EDITTEXT);
								}
							});
							int tempScore = skillPoint + point;
							Text score = new Text(230, 330, numberFont, ""
									+ tempScore, 5,
									getVertexBufferObjectManager());
							gameOverScene.attachChild(score);
						}

						if (gameOverCount == skillCount.length + 3) {
							Text distanceText = new Text(230, 375, numberFont,
									"" + point / 10 + "m", 5,
									getVertexBufferObjectManager());
							gameOverScene.attachChild(distanceText);
						}

						if (gameOverCount == skillCount.length + 3) {
							gameOverScene.registerTouchArea(restartButton);
							gameOverScene.registerTouchArea(backButton);
							gameOverScene
									.setTouchAreaBindingOnActionDownEnabled(true);
						}
					}
				}));
	}

	@Override
	public void onClick(ButtonSprite button, float arg1, float arg2) {
		if (isMusic) // Play the click sound
			this.clickSound.play();
		// Pause the game
		if (button.equals(this.pauseButton)) {
			if (this.mMainScene.hasChildScene()) {
			} else {
				if (isMusic) {
					bgMusic.pause();
				}
				this.mMainScene.setChildScene(this.mMenuScene, false, true,
						true);
			}
		}
		// Start the game
		if (button.equals(this.startButton)) {
			if (isMusic) {
				this.menuBgMusic.pause();
			}
			this.mainScene.setChildScene(creatMenuScene());
		}
		// Display the score interview
		if (button.equals(this.scoresButton)) {
			this.mainScene.setChildScene(this.createScoresSence(), false,
					false, true);
		}

		if (button.equals(this.musicButton)) {
			saveMusicSharePrefences(!isMusic);
			if (isMusic) {
				menuBgMusic.play();
				musicButton.setCurrentTileIndex(0);// Change the image
			} else {
				menuBgMusic.pause();
				musicButton.setCurrentTileIndex(1);
			}
		}
		// Open help interview
		if (button.equals(otherButton)) {
			this.mainScene.setChildScene(this.createHelpSence(), false, false,
					true);
		}
		// Quit help interview
		if (button.equals(helpQuitButton)) {
			helpScene.back();
		}
		// control method
		if (button.equals(help_Button1_1)) {
			this.helpScene.setChildScene(this.createHelpControlSence(), false,
					false, true);
			Log.d("helpScene", "help_Button1_1");
		}
		// property introduction
		if (button.equals(help_Button1_2)) {
			Log.d("helpScene", "help_Button1_2");
			this.helpScene.setChildScene(this.createHelpPropSence(), false,
					false, true);
		}
		// attacking fish description
		if (button.equals(help_Button2_1)) {
			this.helpScene.setChildScene(this.createHelpAttrackFishSence(),
					false, false, true);
		}
		// function fish description
		if (button.equals(help_Button2_2)) {
			this.helpScene.setChildScene(this.createHelpFuctionFishSence(),
					false, false, true);
		}
		// In the game-over scene
		if (button.equals(restartButton)) {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					showDialog(DIALOG_SHOW_RESTART);
				}
			});
		}

		if (button.equals(backButton)) {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					showDialog(DIALOG_SHOW_BACK);
				}
			});
		}

		if (button.equals(quitButton)) {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					showDialog(DIALOG_SHOW_QUIT);
				}
			});

		}
	}

	/**
	 * Initialize texture
	 */
	public void loadingTexture() {
		// Loading resource
		this.loadingTexture = new BitmapTextureAtlas(this.getTextureManager(),
				1024, 512);
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		this.loadingITextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(this.loadingTexture, this, "loading.png", 0, 0);
		this.loadingTexture.load();
	}

	public void loadOtherTexture() {// WorkToDo
		// Load other materials for the fish texture, font, music
		loadSharePrefences();
		this.mainViewInit();
		this.fishSpriteInit();
		this.backgroudInit();
		this.propsInit();
		this.soundInit();
		this.fontInit();
	}

	/** Build starting view */
	public Scene onCreateMainViewScene() {
		this.mainScene = new Scene();
		final Sprite background = new Sprite(0, 0, this.mainBackground,
				this.getVertexBufferObjectManager());
		final float startButtonX = (CAMERA_WIDTH - this.startButton1.getWidth()) / 2;
		final float startButtonY = (CAMERA_HEIGHT) / 2;
		startButton = new ButtonSprite(startButtonX, startButtonY,
				startButton1, startButton2,
				this.getVertexBufferObjectManager(), this);
		scoresButton = new ButtonSprite(startButtonX, startButtonY
				+ startButton.getHeight() * 1.5f, scoresButton1,
				this.getVertexBufferObjectManager(), this);
		otherButton = new ButtonSprite(25, 390, otherButton1, otherButton1,
				this.getVertexBufferObjectManager(), this);
		musicButton = new ButtonSprite(-30, 340, musicButton1, musicButton2,
				this.getVertexBufferObjectManager(), this);
		quitButton = new ButtonSprite(650, 10, quitButton1, quitButton2,
				this.getVertexBufferObjectManager(), this);
		musicButton.setScale(0.9f);
		otherButton.setScale(0.9f);
		mainScene.attachChild(background);
		mainScene.attachChild(startButton);
		mainScene.attachChild(scoresButton);
		mainScene.attachChild(musicButton);
		mainScene.attachChild(otherButton);
		mainScene.attachChild(quitButton);
		mainScene.registerTouchArea(startButton);
		mainScene.registerTouchArea(scoresButton);
		mainScene.registerTouchArea(musicButton);
		mainScene.registerTouchArea(otherButton);
		mainScene.registerTouchArea(quitButton);

		mainScene.setTouchAreaBindingOnActionDownEnabled(true);// Respond to the
																// screen touch
		if (isMusic)
			this.menuBgMusic.play();
		return mainScene;
	}

	public Scene createScoresSence() {
		Scene scoresScene = new Scene();
		final float pX = (CAMERA_WIDTH - this.topTenScores.getWidth()) / 2;
		final float pY = (CAMERA_WIDTH - this.topTenScores.getHeight()) / 2;
		final Sprite topTen = new Sprite(pX, pY - 100, this.topTenScores,
				this.getVertexBufferObjectManager());
		final Sprite background = new Sprite(0, 0, this.mainBackground,
				this.getVertexBufferObjectManager());
		scoresScene.attachChild(background);
		scoresScene.attachChild(topTen);
		scoresScene.setOnSceneTouchListener(this);// Touch to return the
													// mainView
		score = new Score(this);
		score.open();// Database operation to get the score
		ShowRecord(scoresScene);
		return scoresScene;
	}

	/**
	 * Help interface
	 */
	public Scene createHelpSence() {
		helpScene = new Scene();
		final Sprite backGroundSprite = new Sprite(0, 0,
				this.helpMainViewITextureRegion,
				this.getVertexBufferObjectManager());
		final Sprite helpMainTextSprite = new Sprite(
				(CAMERA_WIDTH - this.helpMainTextITextureRegion.getWidth()) / 2,
				35, this.helpMainTextITextureRegion, this
						.getVertexBufferObjectManager());
		help_Button1_1 = new ButtonSprite(95, 126, this.helpButton1_1,
				this.getVertexBufferObjectManager(), this);
		help_Button1_2 = new ButtonSprite(424, 126, this.helpButton1_2,
				this.getVertexBufferObjectManager(), this);
		help_Button2_1 = new ButtonSprite(95, 255, this.helpButton2_1,
				this.getVertexBufferObjectManager(), this);
		help_Button2_2 = new ButtonSprite(424, 255, this.helpButton2_2,
				this.getVertexBufferObjectManager(), this);
		helpQuitButton = new ButtonSprite(200, 10, helpBackITextureRegion,
				this.getVertexBufferObjectManager(), this);

		helpScene.attachChild(backGroundSprite);
		helpScene.attachChild(helpMainTextSprite);
		helpScene.attachChild(help_Button1_1);
		helpScene.attachChild(help_Button1_2);
		helpScene.attachChild(help_Button2_1);
		helpScene.attachChild(help_Button2_2);
		helpScene.attachChild(helpQuitButton);
		helpScene.registerTouchArea(help_Button1_1);
		helpScene.registerTouchArea(help_Button1_2);
		helpScene.registerTouchArea(help_Button2_1);
		helpScene.registerTouchArea(help_Button2_2);
		helpScene.registerTouchArea(helpQuitButton);
		helpScene.setTouchAreaBindingOnActionDownEnabled(true);
		return helpScene;
	}

	public Scene createHelpControlSence() {
		Log.d("HelpScene", "helpSceneCreate");
		helpControlScene = new Scene();
		final Sprite backGroundSprite = new Sprite(0, 0,
				this.helpMainViewITextureRegion,
				this.getVertexBufferObjectManager());
		final Sprite helpControl = new Sprite(15, 35,
				this.helpControlViewITextureRegion,
				this.getVertexBufferObjectManager());
		helpControlScene.attachChild(backGroundSprite);
		helpControlScene.attachChild(helpControl);
		helpControlScene.setOnSceneTouchListener(this);
		helpControlScene.setTouchAreaBindingOnActionDownEnabled(true);
		return helpControlScene;
	}

	public Scene createHelpAttrackFishSence() {
		Scene helpAttrackFishScene = new Scene();
		final Sprite backGroundSprite = new Sprite(0, 0,
				this.helpMainViewITextureRegion,
				this.getVertexBufferObjectManager());
		final Sprite helpAttrackFish = new Sprite(25, 35,
				this.helpAttrackFishITextureRegion,
				this.getVertexBufferObjectManager());
		helpAttrackFishScene.attachChild(backGroundSprite);
		helpAttrackFishScene.attachChild(helpAttrackFish);
		helpAttrackFishScene.setOnSceneTouchListener(this);
		helpAttrackFishScene.setTouchAreaBindingOnActionDownEnabled(true);
		return helpAttrackFishScene;
	}

	public Scene createHelpFuctionFishSence() {
		Scene helpFuctionFishScene = new Scene();
		final Sprite backGroundSprite = new Sprite(0, 0,
				this.helpMainViewITextureRegion,
				this.getVertexBufferObjectManager());
		final Sprite helpFuctionFish = new Sprite(10, 35,
				this.helpFunctionFishITextureRegion,
				this.getVertexBufferObjectManager());
		helpFuctionFishScene.attachChild(backGroundSprite);
		helpFuctionFishScene.attachChild(helpFuctionFish);
		helpFuctionFishScene.setOnSceneTouchListener(this);
		helpFuctionFishScene.setTouchAreaBindingOnActionDownEnabled(true);
		return helpFuctionFishScene;
	}

	public Scene createHelpPropSence() {
		Scene helpPropScene = new Scene();
		final Sprite backGroundSprite = new Sprite(0, 0,
				this.helpMainViewITextureRegion,
				this.getVertexBufferObjectManager());
		final Sprite helpProp = new Sprite(15, 30,
				this.helpPropViewITextureRegion,
				this.getVertexBufferObjectManager());
		helpPropScene.attachChild(backGroundSprite);
		helpPropScene.attachChild(helpProp);
		helpPropScene.setOnSceneTouchListener(this);
		helpPropScene.setTouchAreaBindingOnActionDownEnabled(true);
		return helpPropScene;
	}

	@Override
	public boolean onSceneTouchEvent(Scene scene, TouchEvent arg1) {
		if (isMusic) {
			clickSound.play();
		}
		scene.back();
		return true;
	}

	public Scene creatLoadingScene() {
		Log.e("test", "creatLoadingScene");
		LoadingScene = new Scene();
		Sprite testSprite = new Sprite(0, 0, this.loadingITextureRegion,
				getVertexBufferObjectManager());
		LoadingScene.attachChild(testSprite);
		return LoadingScene;
	}

	public Scene creatMenuScene() {
		this.mMenuScene = this.createMenuScene();
		this.mMainScene = new Scene();
		this.loadSence();

		/* The title-text. */
		final Text titleText = new Text(0, 0, this.mFont, "Ready!",
				new TextOptions(HorizontalAlign.CENTER),
				this.getVertexBufferObjectManager());
		titleText.setPosition((CAMERA_WIDTH - titleText.getWidth()) * 0.5f,
				(CAMERA_HEIGHT - titleText.getHeight()) * 0.5f);
		titleText.setScale(0.0f);
		titleText.registerEntityModifier(new ScaleModifier(2, 0.0f, 1.0f));// Register
																			// animation
																			// effect
		this.mMainScene.attachChild(titleText);

		mGameRunning = false;
		readySound.play();
		/* The handler that removes the title-text and starts the game. */
		this.mMainScene.registerUpdateHandler(new TimerHandler(1.5f,
				new ITimerCallback() {
					@Override
					public void onTimePassed(final TimerHandler pTimerHandler) {
						mMainScene.unregisterUpdateHandler(pTimerHandler);
						mMainScene.detachChild(titleText);
						final Text titleText2 = new Text(0, 0, mFont, "Go",
								new TextOptions(HorizontalAlign.CENTER),
								getVertexBufferObjectManager());
						titleText2.setPosition(
								(CAMERA_WIDTH - titleText2.getWidth()) * 0.5f,
								(CAMERA_HEIGHT - titleText2.getHeight()) * 0.5f);
						titleText2.setScale(0.0f);
						titleText2.registerEntityModifier(new ScaleModifier(1,
								0.0f, 1.0f));
						mMainScene.attachChild(titleText2);
						goSound.play();
						mMainScene.registerUpdateHandler(new TimerHandler(1.5f,
								new ITimerCallback() {
									@Override
									public void onTimePassed(
											final TimerHandler pTimerHandler) {
										mMainScene
												.unregisterUpdateHandler(pTimerHandler);
										mMainScene.detachChild(titleText2);
										mGameRunning = true;
										if (isMusic) {
											bgMusic.seekTo(0);
											bgMusic.play();
										}
									}
								}));
					}
				}));
		pauseButton = new ButtonSprite(650, 420, pauseButton1,
				this.getVertexBufferObjectManager(), this);
		mMainScene.attachChild(pauseButton);
		mMainScene.registerTouchArea(pauseButton);
		this.mMainScene.registerUpdateHandler(new IUpdateHandler() {
			@Override
			public void reset() {
			}

			@Override
			public void onUpdate(float arg0) {
				if (mGameRunning) {
					if (lifeList.size() <= 0) {
						gameOver(); // Game over
					}
					// set point
					// point++;
					point = (int) (point + backgroundSpeed / 20);
					pointText.setText("" + point);

					switch (LEVEL) {
					case 1:
						if ((int) (Math.random() * 10) % 2 == 0) {
							if (point % FUNCTIONFISHGENERATETIME == 0) {
								createNewEnemy(getRandomFunctionEnemyType() + 5);
							}
						}
						break;
					case 2:
						if ((int) (Math.random() * 9) % 3 == 0) {
							if (point % FUNCTIONFISHGENERATETIME == 0) {
								createNewEnemy(getRandomFunctionEnemyType() + 5);
							}
						}

						if ((int) (Math.random() * 9) % 3 == 0) {
							if (point % ATTRACKFISHGENERATETIME == 0) {
								createNewEnemy(getRandomAttrackEnemyType());
							}
						}
						break;
					case 3:
						if (point % FUNCTIONFISHGENERATETIME == 0) {
							createNewEnemy(getRandomFunctionEnemyType() + 5);
						}
						if ((int) (Math.random() * 9) % 3 == 0) {
							if (point % ATTRACKFISHGENERATETIME == 0) {
								createNewEnemy(getRandomAttrackEnemyType());
							}
						}
						break;
					case 4:
						if (point % FUNCTIONFISHGENERATETIME == 0) {
							createNewEnemy(getRandomFunctionEnemyType() + 5);
						}
						if ((int) (Math.random() * 10) % 2 == 0) {
							if (point % ATTRACKFISHGENERATETIME == 0) {
								createNewEnemy(getRandomAttrackEnemyType());
							}
						}
						break;
					case 5:
						if (point % FUNCTIONFISHGENERATETIME == 0) {
							createNewEnemy(getRandomFunctionEnemyType() + 5);
						}
						if ((int) (Math.random() * 10) % 2 == 0) {
							if (point % ATTRACKFISHGENERATETIME == 0) {
								createNewEnemy(getRandomAttrackEnemyType());
							}
						}
						if ((int) (Math.random() * 10) % 2 == 0) {
							if (point % ATTRACKFISHGENERATETIME == 0) {
								createNewEnemy(getRandomAttrackEnemyType());
							}
						}
						break;
					}
					if (point % ATTRACKFISHGENERATETIME == 0) {
						createNewEnemy(getRandomAttrackEnemyType());
					}
					// delete enemy when the enemy has left the scene
					for (int i = 0; i < enemySpriteList.size(); i++) {
						if (enemySpriteList.get(i).getX() < -enemySpriteList
								.get(i).getWidth()) {
							mMainScene.detachChild(enemySpriteList.get(i));
							enemySpriteList.remove(i);
							break;
						}
					}

					// attackFunction
					if (isAttract && currentPropSprite != null) {
						currentPropSprite.setAlpha(1.0f);
						if (!back && of < graphics.size()) {
							currentPropSprite.setX(graphics.get(of).x);
							currentPropSprite.setY(graphics.get(of).y);
							of++;
						}
						if (of == graphics.size()) {
							back = true;
							runOnUpdateThread(new Runnable() {
								@Override
								public void run() {

									mMainScene.detachChild(attackEnemy);
									enemySpriteList.remove(attackEnemy);
									attackEnemy = null;
								}
							});
							// get Point
							skillCount[attackEnemy.getType()]++;
							of--;
						}

						if (back && of >= 0) {
							currentPropSprite.setX(graphics.get(of).x);
							currentPropSprite.setY(graphics.get(of).y);
							if (of == 0) {
								isAttract = false;
								currentPropSprite.setScale(2.0f);
								currentPropSprite.setAlpha(0.6f);
							}
							of--;
						}
					}

					// getProps
					if (over) {
						if (!back && of < graphics.size()) {
							fish.setX(graphics.get(of).x);
							fish.setY(graphics.get(of).y);
							of++;
						}

						if (of == graphics.size()) {
							back = true;
							if (isMusic)
								pickupSound.play();
							of--;
						}

						if (back && of >= 0) {
							fish.setX(graphics.get(of).x);
							fish.setY(graphics.get(of).y);
							if (of == 0)
								over = false;
							of--;
						}
					}

					if (bubbleFunction) {// The props moves with the player
						currentPropSprite.setX(fish.getX() + fish.getWidth()
								/ 3 + 5);
						currentPropSprite.setY(fish.getY() + fish.getHeight()
								/ 5 - 5);
					}

					if (explosionFunction && !isAttract && !currentPropsMove) {
						currentPropSprite.setX(fish.getX() + fish.getWidth()
								/ 3 + 5);
						currentPropSprite.setY(fish.getY() + fish.getHeight()
								/ 5 - 5);
					}

					// collision with enemy
					if (!isSafe) {
						for (BaseEnemy enemy : enemySpriteList) {
							boolean islight = false; // Attacked by bullet
							if (enemy.getType() == LIGHT)
								islight = true;
							if (fish.collidesWith(enemy)
									&& (islight || enemy.getX() > fish.getX())) {
								if (isMusic)
									hitSound.play();
								if (over) { // For touch collision
									if (enemy.getType() != LIGHT) // To record
																	// the
																	// number of
																	// eliminated
																	// fish
										skillCount[enemy.getType()]++;
								} else {
									// With the protection of bubble
									if (bubbleFunction) {
										bubbleFunction = false;
										runOnUpdateThread(new Runnable() {
											@Override
											public void run() {
												mMainScene
														.detachChild(currentPropSprite);
												Log.d("currentPropSprite",
														"currentPropSprite bubble collides and delete");
											}
										});
									} else {
										switch (enemy.getType()) {
										case COMMOM_FISH3: // Ice fish
											mMainScene
													.registerUpdateHandler(avoidAttrackHandler);
											if (fish.getSpeed() > 0) {
												fish.stop();// Use ice to stop
															// the fish
												iceSprite.setX((fish.getX() + fish
														.getWidth() / 6));
												iceSprite.setY(fish.getY() - 20);
												mMainScene
														.attachChild(iceSprite);
												iceSprite
														.registerUpdateHandler(new TimerHandler(
																RESETSPEEDTIME,
																true,
																new ITimerCallback() {
																	@Override
																	public void onTimePassed(
																			TimerHandler pTimerHandler) {
																		runOnUpdateThread(new Runnable() {
																			@Override
																			public void run() {
																				iceSprite
																						.clearUpdateHandlers();// For
																												// the
																												// next
																												// time
																				mMainScene
																						.detachChild(iceSprite);
																			}
																		});
																	}
																}));
												fish.registerUpdateHandler(resetSpeedHandler);//
											}
											break;
										case BLIND_FISH:
											mMainScene
													.registerUpdateHandler(avoidAttrackHandler);
											if (blindTempTime == 0) {
												final Sprite blindSprite = new Sprite(
														0,
														0,
														blindITextureRegion,
														getVertexBufferObjectManager());
												mMainScene
														.attachChild(blindSprite);
												blindSprite.setAlpha(0.95f);
												blindTempTime = 0;
												blindSprite
														.registerUpdateHandler(new TimerHandler(
																1f,
																true,
																new ITimerCallback() {
																	@Override
																	public void onTimePassed(
																			TimerHandler pTimerHandler) {
																		runOnUpdateThread(new Runnable() {
																			@Override
																			public void run() {
																				blindTempTime++;
																				mMainScene
																						.detachChild(blindSprite);
																				mMainScene
																						.attachChild(blindSprite);
																				if (blindTempTime > cloudTime) {
																					blindSprite
																							.clearUpdateHandlers();
																					mMainScene
																							.detachChild(blindSprite);
																					blindTempTime = 0;
																				}

																			}
																		});
																	}
																}));
											}
											break;
										case SLOW_FISH:
											mMainScene
													.registerUpdateHandler(avoidAttrackHandler);

											if (fish.getSpeed() > 1f) {
												fish.resetSpeed();
												runOnUpdateThread(new Runnable() {
													@Override
													public void run() {
														speedUpprite
																.clearUpdateHandlers();
														mMainScene
																.detachChild(speedUpprite);
													}
												});
											} else if (fish.getSpeed() < 1f) {
												break;
											} else {
												fish.slowDown();
												slowsprite
														.registerUpdateHandler(new IUpdateHandler() {

															@Override
															public void reset() {
															}

															@Override
															public void onUpdate(
																	float arg0) {
																slowsprite.setX(fish
																		.getX()
																		+ fish.getWidth()
																		/ 3);
																slowsprite
																		.setY(fish
																				.getY() - 30);
															}
														});

												mMainScene
														.attachChild(slowsprite);
												slowsprite
														.registerUpdateHandler(new TimerHandler(
																RESETSPEEDTIME,
																true,
																new ITimerCallback() {
																	@Override
																	public void onTimePassed(
																			TimerHandler pTimerHandler) {
																		runOnUpdateThread(new Runnable() {
																			@Override
																			public void run() {
																				slowsprite
																						.clearUpdateHandlers();
																				mMainScene
																						.detachChild(slowsprite);
																			}
																		});
																	}
																}));
												fish.registerUpdateHandler(resetSpeedHandler);
											}
											Log.d("collsion",
													"Slow_SPEED_SLOWDOWN"
															+ fish.getSpeed());
											break;
										case ATTACK_FISH:
										case COMMOM_FISH1:
										case COMMOM_FISH2:
										case LIGHT:
										case TRASHCAN:
										case BOSS_FISH:
											fish.setAlpha(0.7f);
											mMainScene
													.registerUpdateHandler(safeHandler);
											if (lifeList.size() > 0) {
												mMainScene.detachChild(lifeList
														.get(lifeList.size() - 1));
												lifeList.remove(lifeList.size() - 1);
												Log.d("enemy", "life="
														+ lifeList.size());
											}
											break;
										default:
											break;
										}
										isSafe = true;
									}
								}
								mMainScene.detachChild(enemy);
								enemySpriteList.remove(enemy);
								break;
							}
						}
					}

					// 道具的碰撞
					if (!isPropFunction) {// To avoid multicolision props
						if (fish.collidesWith(currentPropSprite)) {
							isPropFunction = true;
							switch (currentPropSprite.getType()) {
							case LIFE:
								if (lifeList.size() < 3) {
									final Sprite lifeCount = new Sprite(
											CAMERA_WIDTH - life.getWidth()
													* (lifeList.size() + 1)
													- (lifeList.size() + 1) * 5,
											10, life,
											getVertexBufferObjectManager());
									lifeList.add(lifeCount);
									runOnUpdateThread(new Runnable() {
										@Override
										public void run() {
											mMainScene.attachChild(lifeCount);
										}
									});
								}

								mMainScene.detachChild(currentPropSprite);
								PROPSSTAYTIME = 0;
								currentPropSprite
										.unregisterUpdateHandler(propTimerHandler);
								break;

							case BUBBLE:
								currentPropSprite.functionProp();
								currentPropSprite.setScale(2.2f);
								bubbleFunction = true;
								break;

							case STAR:
								isStar = true;
								currentPropSprite.functionProp();
								for (BaseEnemy enemy : enemySpriteList) {
									enemy.setSpeed(enemy.getSpeed() * 2);
								}
								((AutoParallaxBackground) mMainScene
										.getBackground())
										.setParallaxChangePerSecond(backgroundSpeed * 2);
								fish.setSpeed(fish.getSpeed() * 2);
								currentPropSprite.setAlpha(0);
								PROPSSTAYTIME = 0;
								break;

							case SPEEDUP:
								mMainScene.detachChild(currentPropSprite);
								PROPSSTAYTIME = 0;
								if (fish.getSpeed() == 0) {
									fish.resetSpeed();
									runOnUpdateThread(new Runnable() {
										@Override
										public void run() {
											iceSprite.clearUpdateHandlers();
											mMainScene.detachChild(iceSprite);
										}
									});
								} else if (fish.getSpeed() < 1f) {
									fish.resetSpeed();
									runOnUpdateThread(new Runnable() {
										@Override
										public void run() {
											slowsprite.clearUpdateHandlers();
											mMainScene.detachChild(slowsprite);
										}
									});
								} else {
									fish.speedUp();
									currentPropSprite
											.unregisterUpdateHandler(propTimerHandler);
									speedUpprite
											.animate(GameActivity.ANIMATION_FRAMELENGTH);
									speedUpprite
											.registerUpdateHandler(new IUpdateHandler() {

												@Override
												public void reset() {
												}

												@Override
												public void onUpdate(float arg0) {
													speedUpprite.setX(fish
															.getX() + 15);
													speedUpprite.setY(fish
															.getY()
															+ fish.getHeight()
															/ 3);
												}
											});

									mMainScene.attachChild(speedUpprite);
									speedUpprite
											.registerUpdateHandler(new TimerHandler(
													RESETSPEEDTIME, true,
													new ITimerCallback() {
														@Override
														public void onTimePassed(
																TimerHandler pTimerHandler) {
															runOnUpdateThread(new Runnable() {
																@Override
																public void run() {
																	speedUpprite
																			.clearUpdateHandlers();
																	mMainScene
																			.detachChild(speedUpprite);
																}
															});
														}
													}));

									fish.registerUpdateHandler(resetSpeedHandler);
								}
								Log.d("Collosion", "speedup" + fish.getSpeed());
								break;
							case LAUNCHER:
								currentPropSprite.functionProp();
								currentPropSprite.setScale(2.0f);
								currentPropSprite.setAlpha(0.6f);
								explosionFunction = true;
								break;
							default:
								break;
							}
						}
					}
				}
			}
		});

		this.mMainScene.setOnSceneTouchListener(new IOnSceneTouchListener() {
			@Override
			public boolean onSceneTouchEvent(Scene arg0, final TouchEvent event) {
				final float x = event.getX();
				final float y = event.getY();
				if ((PROPSSTAYTIME > 0 || explosionFunction)
						&& currentPropSprite != null) {
					switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
						over = false;
						isAttract = false;
						back = false;
						attackEnemy = null;
						graphics.clear();
						of = 0;
						touchBegin = false;
						if (fish.contains(x, y)) {// Touch the fish to move
							currentPropsMove = true;
							touchFish.setX(fish.getX());
							touchFish.setY(fish.getY());
							touchFish.setAlpha(0.8f);
							if (!explosionFunction) {
								Log.d("explosionFunction",
										"Is not explosionFunction");
								runOnUpdateThread(new Runnable() {
									@Override
									public void run() {
										mMainScene.attachChild(touchFish);
									}
								});
							} else {
								Log.d("explosionFunction",
										"Is explosionFunction");
								currentPropSprite.setScale(1.0f);
								currentPropSprite.setX(fish.getX()
										+ fish.getWidth());
								currentPropSprite.setY(fish.getY()
										+ fish.getHeight() / 2);
							}

							graphics.add(new PointF(fish.getX(), fish.getY()));
							mX = fish.getX();
							mY = fish.getY();
							touchBegin = true;
						} else {
							touchBegin = false;
						}
						break;
					case MotionEvent.ACTION_MOVE:
						if (touchBegin) {
							float dx = Math.abs(x - mX);
							float dy = Math.abs(y - mY);
							if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
								mX = x;
								mY = y;
								graphics.add(new PointF(x, y));
								if (!explosionFunction) { // Move the touchfish
									runOnUpdateThread(new Runnable() {
										@Override
										public void run() {
											touchFish.setX(x
													- touchFish.getWidth() / 2);
											touchFish.setY(y
													- touchFish.getHeight() / 2);
										}
									});
								} else {// For the explosion, move the
										// currentPropSprite
									runOnUpdateThread(new Runnable() {
										@Override
										public void run() {
											currentPropSprite.setX(x
													- currentPropSprite
															.getWidth() / 2);
											currentPropSprite.setY(y
													- currentPropSprite
															.getHeight() / 2);
										}
									});
								}
							}
							if (explosionFunction) {
								for (BaseEnemy enemy : enemySpriteList) {
									if (enemy.contains(x, y)) {
										currentPropSprite.setScale(2.0f);
										break;
									} else {
										currentPropSprite.setScale(1f);
									}
								}
							}
							if (currentPropSprite != null
									&& currentPropSprite.contains(x, y)
									&& !currentPropSprite.isFunction()) {
								touchFish.setScale(1.5f);
							} else {
								touchFish.setScale(1f);
							}
						}
						break;
					case MotionEvent.ACTION_UP:
						if (touchBegin) {
							if (currentPropSprite != null
									&& currentPropSprite.contains(x, y)
									&& !currentPropSprite.isFunction()) {
								over = true;
							}
							if (explosionFunction) {
								for (BaseEnemy enemy : enemySpriteList) {
									if (enemy.contains(x, y)) {
										isAttract = true;
										currentPropSprite.setScale(1.0f);
										attackEnemy = enemy;
										enemy.setTag(10);
										graphics.add(new PointF(attackEnemy
												.getX(), attackEnemy.getY()));
										if (isMusic)
											launcherSound.play();
										break;
									}
								}
							}
							runOnUpdateThread(new Runnable() {
								@Override
								public void run() {
									mMainScene.detachChild(touchFish);
								}
							});
						}
						currentPropsMove = false;
						break;
					}
				}
				return true;
			}
		});

		this.mMainScene.registerUpdateHandler(new TimerHandler(1f, true,
				new ITimerCallback() {
					@Override
					public void onTimePassed(TimerHandler pTimerHandler) {
						mTime++;
						Log.d("Life", "lifeSize = " + lifeList.size());
						if (mTime % 10 == 0 && backgroundSpeed < 35 && !isStar) {
							backgroundSpeed++;
							((AutoParallaxBackground) mMainScene
									.getBackground())
									.setParallaxChangePerSecond(backgroundSpeed);
						}

						if (mTime % 30 == 0 && LEVEL < 5)
							LEVEL++;

						if (mTime % PROPSGENERATETIME == 0) {
							createNewProps(getRandomPropType());
						}

					}
				}));
		return this.mMainScene;
	}

	/**
	 * Initialize the texture for the first scene: MainView
	 */
	public void mainViewInit() {// Load the resource for the mainView, the first
								// scene
		try {
			BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
			this.mBitmapButtonTextureAtlas = new BuildableBitmapTextureAtlas(
					this.getTextureManager(), 1024, 512);
			this.startButton1 = BitmapTextureAtlasTextureRegionFactory
					.createFromAsset(this.mBitmapButtonTextureAtlas, this,
							"startbutton1.png");
			this.startButton2 = BitmapTextureAtlasTextureRegionFactory
					.createFromAsset(this.mBitmapButtonTextureAtlas, this,
							"startbutton2.png");
			this.musicButton1 = BitmapTextureAtlasTextureRegionFactory
					.createFromAsset(this.mBitmapButtonTextureAtlas, this,
							"ui_button_music_1.png");
			this.musicButton2 = BitmapTextureAtlasTextureRegionFactory
					.createFromAsset(this.mBitmapButtonTextureAtlas, this,
							"ui_button_music_2.png");
			this.scoresButton1 = BitmapTextureAtlasTextureRegionFactory
					.createFromAsset(this.mBitmapButtonTextureAtlas, this,
							"scoresbutton1.png");
			this.topTenScores = BitmapTextureAtlasTextureRegionFactory
					.createFromAsset(this.mBitmapButtonTextureAtlas, this,
							"topTen.png");
			this.otherButton1 = BitmapTextureAtlasTextureRegionFactory
					.createFromAsset(this.mBitmapButtonTextureAtlas, this,
							"otherButton1.png");
			this.quitButton1 = BitmapTextureAtlasTextureRegionFactory
					.createFromAsset(this.mBitmapButtonTextureAtlas, this,
							"quit1.png");
			this.quitButton2 = BitmapTextureAtlasTextureRegionFactory
					.createFromAsset(this.mBitmapButtonTextureAtlas, this,
							"quit2.png");
			this.pauseButton1 = BitmapTextureAtlasTextureRegionFactory
					.createFromAsset(this.mBitmapButtonTextureAtlas, this,
							"pause_icon.png");

			this.backgroundTexture = new BitmapTexture(
					this.getTextureManager(), new IInputStreamOpener() {
						@Override
						public InputStream open() throws IOException {
							return getAssets().open("gfx/mainView.png");
						}
					});
			try {
				this.mBitmapButtonTextureAtlas // Use buildable to realize it
						.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(
								0, 0, 0));
				this.mBitmapButtonTextureAtlas.load();
			} catch (TextureAtlasBuilderException e) {
				e.printStackTrace();
			}

			this.backgroundTexture.load();
			this.mainBackground = TextureRegionFactory
					.extractFromTexture(this.backgroundTexture);
		} catch (IOException e) {
			Debug.e(e);
		}

	}

	/**
	 * Initialize the texture for the background during the game
	 */
	public void backgroudInit() {

		this.mAutoParallaxBackgroundTexture = new BitmapTextureAtlas(
				this.getTextureManager(), 4096, 2048);

		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		this.resetartButton1 = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(this.mAutoParallaxBackgroundTexture, this,
						"button.png", 1520, 1520);
		this.resetartButton2 = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(this.mAutoParallaxBackgroundTexture, this,
						"button2.png", 1520, 1720);

		this.mParallaxLayerBack = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(this.mAutoParallaxBackgroundTexture, this,
						"background.png", 0, 0);
		this.mParallaxLayerbootom = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(this.mAutoParallaxBackgroundTexture, this,
						"mainBackgroundBottom.png", 0, 500);
		this.blindITextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(this.mAutoParallaxBackgroundTexture, this,
						"mozi.png", 0, 700);
		this.gameOverTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(this.mAutoParallaxBackgroundTexture, this,
						"gameOver.png", 0, 1520);
		this.pauseBackground = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(this.mAutoParallaxBackgroundTexture, this,
						"pauseBackground.png", 900, 1500);

		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("sprite/");
		this.iceITextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(this.mAutoParallaxBackgroundTexture, this,
						"ice.png", 0, 1400);

		this.mAutoParallaxBackgroundTexture.load();

		iceSprite = new Sprite(0, 0, iceITextureRegion,
				getVertexBufferObjectManager());
		// For the helping interview
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		this.helpBitmapTextureAtlas = new BitmapTextureAtlas(
				this.getTextureManager(), 1024, 2048);
		this.helpMainViewITextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(this.helpBitmapTextureAtlas, this,
						"helpMainView.png", 0, 0);
		this.helpAttrackFishITextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(this.helpBitmapTextureAtlas, this,
						"helpAttrackFishView.png", 0, 490);
		this.helpFunctionFishITextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(this.helpBitmapTextureAtlas, this,
						"helpFunctionFishView.png", 0, 860);
		this.helpControlViewITextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(this.helpBitmapTextureAtlas, this,
						"helpControlView.png", 0, 1220);
		this.helpPropViewITextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(this.helpBitmapTextureAtlas, this,
						"helpPropView.png", 0, 1550);
		this.helpButton1_1 = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(this.helpBitmapTextureAtlas, this,
						"helpButton1_1.png", 720, 0);
		this.helpButton1_2 = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(this.helpBitmapTextureAtlas, this,
						"helpButton1_2.png", 720, 100);
		this.helpButton2_1 = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(this.helpBitmapTextureAtlas, this,
						"helpButton2_1.png", 720, 200);
		this.helpButton2_2 = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(this.helpBitmapTextureAtlas, this,
						"helpButton2_2.png", 720, 300);
		this.helpBackITextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(this.helpBitmapTextureAtlas, this,
						"helpBack.png", 720, 400);
		this.helpMainTextITextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(this.helpBitmapTextureAtlas, this,
						"helpMainText.png", 820, 600);
		this.helpBitmapTextureAtlas.load();
	}

	/**
	 * Initialize the fistSprite's texture
	 */
	public void fishSpriteInit() {
		this.point = 0;
		this.skillPoint = 0;
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("sprite/"); // assets/sprites
																			// content
		Enemy enemy1 = new Enemy(360, 60, 4, 1, 200, "enemyFish1.png", 480, 50,
				"sprite/", 1);
		Enemy enemy2 = new Enemy(446, 72, 4, 1, 200, "enemyFish2.png", 480,
				150, "sprite/", 1);
		Enemy enemy3 = new Enemy(450, 58, 4, 1, 200, "enemyFish3.png", 480,
				250, "sprite/", 1);
		Enemy enemy4 = new Enemy(586, 64, 4, 1, 200, "enemyFish4.png", 480,
				350, "sprite/", 1);
		Enemy enemy5 = new Enemy(495, 64, 4, 1, 200, "enemyFish5.png", 480,
				350, "sprite/", 1);
		Enemy enemy6 = new Enemy(460, 88, 4, 1, 200, "enemyFish6.png", 480,
				350, "sprite/", 1);
		Enemy enemy7 = new Enemy(549, 103, 4, 1, 200, "enemyFish7.png", 480,
				350, "sprite/", 1);
		Enemy enemy8 = new Enemy(115, 74, 2, 1, 200, "trashCan.png", 480, 350,
				"sprite/", 1);
		Enemy enemy9 = new Enemy(30, 36, 1, 2, 200, "shell.png", 480, 350,
				"sprite/", 1);
		enemyList.add(enemy1);
		enemyList.add(enemy2);
		enemyList.add(enemy4);
		enemyList.add(enemy7);
		enemyList.add(enemy8);
		enemyList.add(enemy3);
		enemyList.add(enemy5);
		enemyList.add(enemy6);
		enemyList.add(enemy9);
		// Put the enemy fish in the ArrayList for later use
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("sprite/");
		this.fishSpriteTextureAtlas = new BuildableBitmapTextureAtlas(
				this.getTextureManager(), 1024, 512);
		// Load mainFish
		this.mainFish = BitmapTextureAtlasTextureRegionFactory // TiledTextureRegion
																// type to make
																// an animation
				.createTiledFromAsset(this.fishSpriteTextureAtlas, this,
						"mainFish.png", 4, 1);
		this.slowTiledTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(this.fishSpriteTextureAtlas, this,
						"slow.png", 3, 1);
		this.speedUpTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(this.fishSpriteTextureAtlas, this,
						"speedUp.png", 1, 2);

		// Load Enemy
		for (int i = 0; i < enemyList.size(); i++) {
			TiledTextureRegion tiledTextureRegion = BitmapTextureAtlasTextureRegionFactory
					.createTiledFromAsset(this.fishSpriteTextureAtlas, this,
							this.enemyList.get(i).getIcon(), this.enemyList
									.get(i).getColumn(), this.enemyList.get(i)
									.getRow());
			this.enemyITextureRegionList.add(tiledTextureRegion);
		}
		this.enemyList.clear();
		try {
			this.fishSpriteTextureAtlas
					.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(
							0, 0, 1));
			this.fishSpriteTextureAtlas.load();
		} catch (TextureAtlasBuilderException e) {
		}
		// The animation for slowsprite and speedUpsprite is built.
		slowsprite = new AnimatedSprite(0, 0, slowTiledTextureRegion,
				getVertexBufferObjectManager());
		speedUpprite = new AnimatedSprite(0, 0, speedUpTextureRegion,
				getVertexBufferObjectManager());
		slowsprite.animate(GameActivity.ANIMATION_FRAMELENGTH);
		speedUpprite.animate(GameActivity.ANIMATION_FRAMELENGTH);

	}

	/**
	 * Initialize the fistSprite's texture
	 */
	public void propsInit() {

		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("sprite/");

		this.dynamicPropsTextureAtlas = new BuildableBitmapTextureAtlas(
				this.getTextureManager(), 512, 512);
		// Load dynamicProps
		this.paopao = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(this.dynamicPropsTextureAtlas, this,
						"pao.png", 2, 1);
		this.life = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(this.dynamicPropsTextureAtlas, this,
						"lifeCount.png", 2, 1);
		this.cloud = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(this.dynamicPropsTextureAtlas, this,
						"cloud_0.png", 2, 1);
		this.star = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(this.dynamicPropsTextureAtlas, this,
						"ghaixing.png", 4, 2);
		this.explosion = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(this.dynamicPropsTextureAtlas, this,
						"explosion.png", 3, 2);

		this.dynPropsITextureRegionList.add(this.paopao);
		this.dynPropsITextureRegionList.add(this.life);
		this.dynPropsITextureRegionList.add(this.star);
		this.dynPropsITextureRegionList.add(this.cloud);
		this.dynPropsITextureRegionList.add(this.explosion);

		try {
			this.dynamicPropsTextureAtlas
					.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(
							0, 0, 1));
			this.dynamicPropsTextureAtlas.load();
		} catch (TextureAtlasBuilderException e) {
		}
	}

	public void soundInit() {
		SoundFactory.setAssetBasePath("music/");
		try {
			this.clickSound = SoundFactory.createSoundFromAsset(
					this.mEngine.getSoundManager(), this, "clickSound.mp3");
			this.hitSound = SoundFactory.createSoundFromAsset(
					this.mEngine.getSoundManager(), this, "hitSound.OGG");
			this.launcherSound = SoundFactory.createSoundFromAsset(
					this.mEngine.getSoundManager(), this, "launcherSound.OGG");
			this.lightSound = SoundFactory.createSoundFromAsset(
					this.mEngine.getSoundManager(), this, "lightSound.OGG");
			this.loseSound = SoundFactory.createSoundFromAsset(
					this.mEngine.getSoundManager(), this, "loseSound.mp3");
			this.pickupSound = SoundFactory.createSoundFromAsset(
					this.mEngine.getSoundManager(), this, "pickupSound.OGG");
			this.scoreSound = SoundFactory.createSoundFromAsset(
					this.mEngine.getSoundManager(), this, "scoreSound.OGG");
			this.readySound = SoundFactory.createSoundFromAsset(
					this.mEngine.getSoundManager(), this, "ready.mp3");
			this.goSound = SoundFactory.createSoundFromAsset(
					this.mEngine.getSoundManager(), this, "go.mp3");

			// this.clickSound.setVolume(50);
			// this.hitSound.setVolume(50);
			// this.launcherSound.setVolume(50);
			// this.lightSound.setVolume(50);
			// this.loseSound.setVolume(50);
			// this.pickupSound.setVolume(50);
			// this.scoreSound.setVolume(50);

		} catch (final IOException e) {
			Debug.e("mGoodMusic Error", e);
		}

		MusicFactory.setAssetBasePath("music/");
		try {

			this.menuBgMusic = MusicFactory
					.createMusicFromAsset(this.mEngine.getMusicManager(), this,
							"menu_loop_music.OGG");
			this.menuBgMusic.setLooping(true);
			this.bgMusic = MusicFactory.createMusicFromAsset(
					this.mEngine.getMusicManager(), this, "bgmusic.ogg");
			this.bgMusic.setLooping(true);
			// this.menuBgMusic.setVolume(50);
		} catch (final IOException e) {
			Log.d("Music", "musicInitWrong");
		}
	}

	public void fontInit() {

		final ITexture pointFontTexture = new BitmapTextureAtlas(
				this.getTextureManager(), 256, 256, TextureOptions.BILINEAR);
		final ITexture fontTexture = new BitmapTextureAtlas(
				this.getTextureManager(), 256, 256, TextureOptions.BILINEAR);
		final ITexture numberTexture = new BitmapTextureAtlas(
				this.getTextureManager(), 256, 256, TextureOptions.BILINEAR);
		final ITexture englishTexture = new BitmapTextureAtlas(
				this.getTextureManager(), 256, 256, TextureOptions.BILINEAR);
		// Load font
		FontFactory.setAssetBasePath("font/");
		this.mFont = FontFactory.createFromAsset(this.getFontManager(),
				fontTexture, this.getAssets(), "Plok.ttf", 40, true,
				android.graphics.Color.WHITE);
		this.pointFont = FontFactory.createFromAsset(this.getFontManager(),
				pointFontTexture, this.getAssets(), "KingdomOfHearts.ttf",
				FONT_SIZE, true, android.graphics.Color.WHITE);
		this.numberFont = FontFactory.createFromAsset(this.getFontManager(),
				numberTexture, this.getAssets(), "HARNGTON.TTF",
				FONT_SIZE - 18, true, android.graphics.Color.BLACK);
		this.englishFont = FontFactory.createFromAsset(this.getFontManager(),
				englishTexture, this.getAssets(), "BRDWAYG.TTF",
				FONT_SIZE - 12, true, android.graphics.Color.WHITE);

		this.pointFont.load();
		this.mFont.load();
		this.numberFont.load();
		this.englishFont.load();
	}

	/**
	 * Load AutoParallaxBackground background and the hero with gravity
	 * sensitivity
	 */
	public void loadSence() {
		this.resetParams();
		// show the background, using AutoParallaxBackground class to realize
		// the parallax effect
		final AutoParallaxBackground autoParallaxBackground = new AutoParallaxBackground(
				0, 0, 0, 5);
		final VertexBufferObjectManager vertexBufferObjectManager = this
				.getVertexBufferObjectManager();
		// There are 2 backgrounds, the bottom and the background
		autoParallaxBackground.attachParallaxEntity(new ParallaxEntity(-3.0f,
				new Sprite(0, 0, this.mParallaxLayerBack,
						vertexBufferObjectManager)));

		autoParallaxBackground.attachParallaxEntity(new ParallaxEntity(-7.0f,
				new Sprite(0, 360, this.mParallaxLayerbootom,
						vertexBufferObjectManager)));
		mMainScene.setBackground(autoParallaxBackground);
		((AutoParallaxBackground) mMainScene.getBackground())
				.setParallaxChangePerSecond(backgroundSpeed); // Set the speed
		// show the main fish
		fish = new Player(20, 240, this.mainFish,
				this.getVertexBufferObjectManager(), this.getEngine(),
				(SensorManager) getSystemService(SENSOR_SERVICE));
		mMainScene.attachChild(fish);

		// show the life
		for (int i = 0; i < 3; i++) {
			Sprite lifeCount = new Sprite(CAMERA_WIDTH - life.getWidth()
					* (i + 1) - (i + 1) * 5, 10, life,
					getVertexBufferObjectManager());
			lifeList.add(lifeCount);
			mMainScene.attachChild(lifeCount);
		}

		// show score
		pointText = new Text(40, 20, this.pointFont, this.point + "", 12,
				vertexBufferObjectManager);
		this.mMainScene.attachChild(pointText);

		touchFish = new AnimatedSprite(0, 0, mainFish,
				getVertexBufferObjectManager());
	}

	private void saveMusicSharePrefences(boolean isMusic) {
		SharedPreferences uiState = getPreferences(0);
		SharedPreferences.Editor editor = uiState.edit();
		editor.putBoolean("music", isMusic);
		editor.commit();
		this.isMusic = isMusic;
	}

	private void loadSharePrefences() {
		SharedPreferences settings = getPreferences(Activity.MODE_PRIVATE);
		isMusic = settings.getBoolean("music", false);
	}

	private void ShowRecord(final Scene scene) {
		runOnUpdateThread(new Runnable() {

			@Override
			public void run() {
				cursor = score.getAllRecord();// Get the result set
				startManagingCursor(cursor);
				int textY = 0;
				while (cursor.moveToNext())// Iterate the
				{
					int scoreColumnIndex = cursor
							.getColumnIndex(Score.key_score);
					int s = cursor.getInt(scoreColumnIndex);
					int nameColumnIndex = cursor.getColumnIndex(Score.key_name);
					String name = cursor.getString(nameColumnIndex);
					int disColumnIndex = cursor
							.getColumnIndex(Score.key_distance);
					String distance = cursor.getString(disColumnIndex);

					int y = textY * 32 + 200;
					Text nameText = new Text(185, y, numberFont, name, 9,
							getVertexBufferObjectManager());
					Text scoreText = new Text(325, y, numberFont, "" + s, 10,
							getVertexBufferObjectManager());
					Text distanceText = new Text(460, y, numberFont, ""
							+ distance + "m", 7, getVertexBufferObjectManager());
					scene.attachChild(nameText);
					scene.attachChild(scoreText);
					scene.attachChild(distanceText);
					textY++;
				}
				cursor.close();
				score.closeDB();
			}
		});

	}

	private void addRecord(final String newName, final int newScore,
			final int distance) {
		runOnUpdateThread(new Runnable() {

			@Override
			public void run() {
				cursor = score.getAllRecord();
				startManagingCursor(cursor);

				if (cursor.getCount() <= 5) {
					score.createRecord(newName, newScore, distance);
				} else {
					while (cursor.moveToNext()) {
						int scoreColumnIndex = cursor
								.getColumnIndex(Score.key_score);
						int s = cursor.getInt(scoreColumnIndex);
						if (newScore > s) {
							break;
						}
					}
					cursor.moveToLast();
					int deleteRowIdindex = cursor.getColumnIndex(Score.key_id);
					score.deleteRecord(cursor.getInt(deleteRowIdindex));
					score.createRecord(newName, newScore, distance);
				}
				cursor.close();
				score.closeDB();
			}
		});

	}

	@Override
	protected Dialog onCreateDialog(final int pID) {
		switch (pID) {
		case DIALOG_SHOW_QUIT:
			return new AlertDialog.Builder(this)
					.setIcon(android.R.drawable.ic_dialog_info)
					.setCancelable(true)
					.setMessage("QUIT GAME?")
					.setPositiveButton("Quit",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									finish();
								}
							})
					.setNegativeButton("Cancel",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {

								}
							}).create();
		case DIALOG_SHOW_BACK:
			return new AlertDialog.Builder(this)
					.setIcon(android.R.drawable.ic_dialog_info)
					.setCancelable(true)
					.setMessage("Are you want to back to selectView?")
					.setPositiveButton("Back",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									gameOverScene.back();
									mMainScene.back();
									if (isMusic) {
										if (bgMusic.isPlaying())
											bgMusic.pause();
									}
									if (isMusic)
										menuBgMusic.play();
								}
							})
					.setNegativeButton("Cancel",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {

								}
							}).create();
		case DIALOG_SHOW_RESTART:
			return new AlertDialog.Builder(this)
					.setIcon(android.R.drawable.ic_dialog_info)
					.setCancelable(true)
					.setMessage("Restart?")
					.setPositiveButton("Yes",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									bgMusic.seekTo(0);
									gameOverScene.back();
									mMainScene.back();
									fish.resetSpeed();
									mainScene.setChildScene(creatMenuScene());
								}
							})
					.setNegativeButton("Cancel",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {

								}
							}).create();
		case DIALOG_SHOW_MENURESTART:
			return new AlertDialog.Builder(this)
					.setIcon(android.R.drawable.ic_dialog_info)
					.setCancelable(true)
					.setMessage("Restart?")
					.setPositiveButton("Yes",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									mMenuScene.back();
									mMainScene.back();
									mainScene.setChildScene(creatMenuScene());
								}
							})
					.setNegativeButton("Cancel",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {

								}
							}).create();
		case DIALOG_SHOW_MENUQUIT:
			return new AlertDialog.Builder(this)
					.setIcon(android.R.drawable.ic_dialog_info)
					.setCancelable(true)
					.setMessage("Quit?")
					.setPositiveButton("Quit",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									mMenuScene.back();
									mMainScene.back();
									if (isMusic) {
										menuBgMusic.play();
									}
									if (isMusic) {
										menuBgMusic.play();
										musicButton.setCurrentTileIndex(0);
									} else {
										menuBgMusic.pause();
										musicButton.setCurrentTileIndex(1);
									}
								}
							})
					.setNegativeButton("Cancel",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {

								}
							}).create();

		case DIALOG_SHOW_EDITTEXT:
			final EditText inputServer = new EditText(GameActivity.this);
			inputServer
					.setFilters(new InputFilter[] { new InputFilter.LengthFilter(
							9) });
			return new AlertDialog.Builder(this)
					.setIcon(android.R.drawable.ic_dialog_info)
					.setView(inputServer)
					.setCancelable(true)
					.setTitle("New Record!!!")
					.setMessage("Please inputer your name?")
					.setPositiveButton("OK",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									int tempScore = skillPoint + point;
									score = new Score(GameActivity.this);
									score.open();
									addRecord(inputServer.getText().toString(),
											tempScore, point / 10);
								}
							})
					.setNegativeButton("Cancel",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {

								}
							}).create();
		default:
			return super.onCreateDialog(pID);
		}
	}
}

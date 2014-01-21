package com.swimmingFish.Role;


import org.andengine.engine.Engine;
import org.andengine.engine.handler.physics.PhysicsHandler;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import com.view.swimmingFish.GameActivity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
/**
 * The class for realizing the behavior of the player
 */
public class Player extends AnimatedSprite {

	static private float speed = 1;//Max speed for the fish
	private final Engine engine;
	SensorManager sensorManager;
	Sensor sensor;
	SensorEventListener sensorEventListener;
	private float sensorEventY;
	final PhysicsHandler physicsHandler;

	public Player(float pX, float pY, ITiledTextureRegion pTiledTextureRegion,
			VertexBufferObjectManager pVertexBufferObjectManager,
			Engine engine, SensorManager sensorManager) {
		super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);
		this.engine = engine;
		this.sensorManager = sensorManager;
		physicsHandler = new PhysicsHandler(this);
		this.registerUpdateHandler(physicsHandler); //Using PhysicsHandler to move the fish
		this.animate(GameActivity.ANIMATION_FRAMELENGTH);

		// accelerometer sensor
		sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		sensorEventListener = new SensorEventListener() {
			@Override
			public void onSensorChanged(SensorEvent event) {
				if(GameActivity.mGameRunning){
					sensorEventY = event.values[1];//y axis
					// The move of the hero and the sensitivity to the boundary
					if(speed > 0){
						float tempSpeed = sensorEventY;
						if (tempSpeed > Player.speed)
							tempSpeed = Player.speed;
						else if (tempSpeed < -Player.speed)
							tempSpeed = -Player.speed;

						if (getY() <= 0 && sensorEventY < 0) 
							tempSpeed = 0;
						

						if (getY() >= (GameActivity.CAMERA_HEIGHT - getHeight())
								&& sensorEventY > 0)
							tempSpeed = 0;

						physicsHandler.setVelocityY(tempSpeed * 100);
					}
					else {
						physicsHandler.setVelocityY(0);
					}
				}
				
			}

			@Override
			public void onAccuracyChanged(Sensor arg0, int arg1) {
			}
		};

		sensorManager.registerListener(sensorEventListener, sensor,
				SensorManager.SENSOR_DELAY_GAME);
	}

	public Player getThis() {
		return this;
	}
	
	public void resetSpeed(){
		Player.speed = 1f;
	}
	
	public float getSpeed(){
		return Player.speed;
	}
	
	public void setSpeed(float speed){
		Player.speed = speed;
	}
	
	public void slowDown(){
		Player.speed = (float) (0.5 * Player.speed);
	}
	
	public void speedUp(){
		Player.speed = (float) (2f * Player.speed);
	}
	
	public void stop(){
		Player.speed = 0;
	}
	

}

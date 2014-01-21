package com.swimmingFish.Role;

import org.andengine.engine.Engine;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.view.swimmingFish.GameActivity;
/**
 * The class for realizing the behavior of different enemies
 */
public class BaseEnemy extends AnimatedSprite{

	private final Engine engine;
	static final int COMMOM_FISH1 = 0; // bullet fish
	static final int COMMOM_FISH2 = 1; // Normal attack fish
	static final int ATTACK_FISH = 2; // Auto shark
	static final int BOSS_FISH = 3;
	static final int TRASHCAN = 4;
	
	static final int COMMOM_FISH3 = 5; //Ice fish
	static final int SLOW_FISH = 6;
	static final int BLIND_FISH = 7;
	static final int LIGHT = 8;
	
	static final int ATTACK_DISTANCE = 90000;//attack distance square
	
	private float speed = 1;
	private int type;  //indicate type of fish
	private Boolean functionDone = false; 
	
	
	public BaseEnemy(float pX, float pY,
			ITiledTextureRegion pTiledTextureRegion,
			VertexBufferObjectManager pTiledSpriteVertexBufferObject,final Engine engine,final int type) {
		super(pX, pY, pTiledTextureRegion,
				pTiledSpriteVertexBufferObject);
		this.engine = engine;
		this.resetSpeed(type);
		this.setType(type);
		this.animate(GameActivity.ANIMATION_FRAMELENGTH);
		this.registerUpdateHandler(new IUpdateHandler() {
			float pX = 0,pY = 0;     //The position of the function
			
			@Override
			public void reset() {
			}		
			@Override
			public void onUpdate(float arg0) {
				if(!functionDone){
					pX = GameActivity.fish.getX();
					pY = GameActivity.fish.getY();
				}
				
				switch (type) {
				case COMMOM_FISH1:
				case LIGHT:
				case COMMOM_FISH2:
				case COMMOM_FISH3:	
				case BLIND_FISH:
				case SLOW_FISH:
				case BOSS_FISH:
				case TRASHCAN:
					setX(getX() - speed);
					break;
				case ATTACK_FISH:
					float distance = (getX() - pX) * (getX() - pX) + (getY() - pY) * (getY() - pY);
					if(functionDone || distance < ATTACK_DISTANCE){
						setX(getX() - speed);		
						if(distance > 200 )
						{
							if(pY > getY())
								setY(getY() + speed);
							else if(pY < getY()){
								setY(getY() - speed);
							}
						}
						functionDone = true;			
					}
					else
						setX(getX() - speed);	
					break;
				default:
					break;
				}			
			}
		});
	}

	
	public BaseEnemy getThis() {
		return this;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
	
	public void setSpeed(float speed) {
		this.speed = speed;
	}
	
	public float getSpeed() {
		return this.speed;
	}

	public void resetSpeed(int type){

		switch (type) {
		case COMMOM_FISH1:
		case COMMOM_FISH2:
		case COMMOM_FISH3:	
			speed = 1;
			break;
		case BLIND_FISH:
		case SLOW_FISH:
			speed = 0.8f;
			break;
		case BOSS_FISH:
			speed = 1.4f;
			break;
		case ATTACK_FISH:
			speed = 1.2f;
			break;
		case LIGHT:
			speed = 5f;
			break;
		default:
			break;
		}
		speed = (float) (speed + (GameActivity.LEVEL * 0.3));
	}

}

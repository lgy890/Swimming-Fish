package com.swimmingFish.Role;


import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.view.swimmingFish.GameActivity;
/**
 * The class for realizing the behavior of different props
 */

public class DynamicProps extends AnimatedSprite{


	private int type;
	
	static final int BUBBLE = 0; 
	static final int LIFE = 1;
	static final int STAR = 2; 
	static final int SPEEDUP = 3; 
	static final int LAUNCHER = 4;
	

	private boolean isFunction;
	private float stayTime;   //The time of the props to exist
	private float functionTime; //The valid function time for the props

	
	public DynamicProps(float pX, float pY,
			ITiledTextureRegion pTiledTextureRegion,
			VertexBufferObjectManager pVertexBufferObjectManager,final int type) {
		super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);
		this.type = type;
		this.isFunction = false;
		this.stayTime = 5f;
		if(type == BUBBLE)
			this.functionTime = 8f;
		if(type == LAUNCHER)
			this.functionTime = 8f;
		if(type == STAR)
			this.functionTime = 8f;
		
		this.animate(GameActivity.ANIMATION_FRAMELENGTH);
		
	}
	

	public void setType(int type) {
		this.type = type;
	}

	public boolean isFunction() {
		return isFunction;
	}


	public void functionProp() {
		this.isFunction = true;
	}


	public float getStayTime() {
		return stayTime;
	}

	public float getfunctionTime() {
		return functionTime;
	}

	public int getType() {
		return type;
	}


}

package com.swimmingFish.Role;
/**
 * Record different possible kinds of enemies to
 * initial the texture for each
 */
public class Enemy extends BaseEntity{
			
	private float speed = 1;
	private Boolean isDelete;
	private int type;  //The type of enemy
	float pX = 0,pY = 0;
	
	public Enemy(){
		speed = 1;
		}
		
	public Enemy(int weight ,int height, int clo, int row, int animate, String icon, float x, float y,String path, float speed){
		super(weight, height, clo, row, animate, icon, x, y,path);
		this.speed = speed;
		this.setIsDelete(false);
	}
	

	public Boolean getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Boolean isDelete) {
		this.isDelete = isDelete;
	}
	
	public void setType(int type){
		this.type = type;
	}
	
	public float getSpeed() {
		return speed;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}

	public int getType() {
		return type;
	}

}

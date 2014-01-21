package com.swimmingFish.Role;
/**
 * The super class for the Enemy class. At first we decided to do a general superclass for
 * all the props, enemies. But we didn't do that at last. 
 */
public class BaseEntity {
	
	float x;
	float y;
	String icon;	
	String path;
	int weight;
	int height;
	int column;
	int row;
	int animate;
	
	public BaseEntity(){}
	
	public BaseEntity(int weight ,int height, int clo, int row, int animate, String icon, float x, float y,String path){
		this.x = x;
		this.y = y;
		this.weight = weight;
		this.height = height;
		this.column = clo;
		this.row = row;
		this.animate = animate;
		this.icon = icon;
		this.path = path;
	}
	
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public int getAnimate() {
		return animate;
	}
	public void setAnimate(int animate) {
		this.animate = animate;
	}
	public int getColumn() {
		return column;
	}
	public void setColumn(int column) {
		this.column = column;
	}
	public int getRow() {
		return row;
	}
	public void setRow(int row) {
		this.row = row;
	}
	public float getX() {
		return x;
	}
	public void setX(float x) {
		this.x = x;
	}
	public float getY() {
		return y;
	}
	public void setY(float y) {
		this.y = y;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public int getWeight() {
		return weight;
	}
	public void setWeight(int weight) {
		this.weight = weight;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	
}

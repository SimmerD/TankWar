package com.idc.tankwar;
import java.awt.*;
/**
 * 墙类
 * 用于控制墙的产生、绘制
 * @author xxq
*/
public class Wall {

	/**墙位置的x坐标*/
	int x;
	/**墙位置的y坐标*/
	int y;
	/**墙的宽度*/
	public static final int WALL_WIDTH =  20;
	/**墙的高度*/
	public static final int WALL_HEIGHT = 300;
	TankClient tc;
	
	public Wall(int x, int y, TankClient tc) {
		this.x = x;
		this.y = y;
		this.tc = tc;
	}

	/**
	 * 用于绘制墙
	 * @param g 画笔
	 */
	public void drawWall(Graphics g) {
		Color c = g.getColor();
		g.setColor(Color.gray);
		g.fillRect(x, y, WALL_WIDTH, WALL_HEIGHT);
		g.setColor(c);
	}
	
	public Rectangle getRect() {
		return new Rectangle(x, y, WALL_WIDTH, WALL_HEIGHT );
	}
	
	
}

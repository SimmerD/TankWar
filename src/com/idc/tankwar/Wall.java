package com.idc.tankwar;
import java.awt.*;
/**
 * ǽ��
 * ���ڿ���ǽ�Ĳ���������
 * @author xxq
*/
public class Wall {

	/**ǽλ�õ�x����*/
	int x;
	/**ǽλ�õ�y����*/
	int y;
	/**ǽ�Ŀ��*/
	public static final int WALL_WIDTH =  20;
	/**ǽ�ĸ߶�*/
	public static final int WALL_HEIGHT = 300;
	TankClient tc;
	
	public Wall(int x, int y, TankClient tc) {
		this.x = x;
		this.y = y;
		this.tc = tc;
	}

	/**
	 * ���ڻ���ǽ
	 * @param g ����
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

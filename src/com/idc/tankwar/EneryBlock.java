package com.idc.tankwar;
import java.awt.*;
/**
 * 能量块类
 * 用于控制能量块的产生、绘制；
 * 主战坦克吃了该能量块之后能立刻恢复满血状态
 * @author xxq

 */
public class EneryBlock {

	/** 能量块位置的x坐标*/
	int x;
	/**能量块位置的y坐标*/
	int y;
	/** 能量块的宽度*/
	public static final int BLOOD_WIDTH = 15;
	/**能量块的高度*/
	public static final int BLOOD_HEIGHT = 15;
	//能量块是否有效
	private boolean live = true; 
	
	//用一系列不同的坐标点来表示能量块的位置，使能量块一直在这些点来回变动，用step来表示绘制到第几步
	int[][] pos = {
			{420, 300},{450, 300},{470, 250},{500, 300},{520, 330},
			{550, 350},{580, 300},{530, 350},{555, 325},{500,350},
						};
	int step = 0;
	
	/**
	 * 构造能量块的时候，使其位于pos中的第一个位置
	 */
	public EneryBlock() {
		x = pos[step][0];
		y = pos[step][1];
	}
	
	public boolean isLive() {
		return live;
	}
	
	public void setLive(boolean live) {
		this.live = live;
	}
	
	/**
	 * 用于绘制能量块
	 * @param g 画笔
	 * 若该能量块无效，则不绘制，直接返回。
	 * 在每次绘制之前，将x,y的值进行改变，即沿着pos中的坐标点逐个绘制能量块，
	 * 在当前绘制完成之后，将step加一，并判断step是否超出pos的长度，若超出，则表示这一轮绘制完毕，将step重新置为0，再从pos中的第一个位置开始绘制。
	 */
	public void drawEneryBlock(Graphics g) {
		if(!live) return;
		Color c = g.getColor();
		g.setColor(Color.MAGENTA);
		x = pos[step][0];
		y = pos[step][1];
		g.fillRect(x, y, BLOOD_WIDTH, BLOOD_HEIGHT);
		g.setColor(c);
		step ++;
		if(step >= pos.length) {
			step = 0;
		}
		
	}
	
	public Rectangle getRect() {
		return new Rectangle(x, y,BLOOD_WIDTH, BLOOD_HEIGHT);
		
	}
	
	
}

package com.idc.tankwar;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;


/**
 * TankClient类是坦克大战的主类,2.9版本加入了配置文件
 * @author xxq
 *
 */

public class TankClient extends Frame {
	
	/**指明主窗口的宽度 */
	public static final int FRAME_WIDTH = 800;
	/**指明主窗口的高度 */
	public static final int FRAME_HEIGHT = 600;
	
	//myTank是主战坦克，只有一辆
	Tank myTank = new Tank(750, 550, true, this, Direction.STOP);
	
	Wall w1 = new Wall(400, 100, this);
	Wall w2 = new Wall(100, 100, this);
	EneryBlock blood = new EneryBlock();
	Image backImage = null;
	
	/** emTanks是窗口中放置敌对坦克的容器 */
	List<Tank> emTanks = new ArrayList<Tank>();
	/** explodes是窗口中放置爆炸的容器 */
	List<Explode> explodes = new ArrayList<Explode>();
	/** bullets是窗口中放置所有子弹的容器 */
	List<Bullets> bullets = new ArrayList<Bullets>() ;
	
	private int initialTankCount = Integer.parseInt(PropertyManager.getProperty("initialTankCount"));
	private int reproducedTankCount = Integer.parseInt(PropertyManager.getProperty("reproducedTankCount"));
	
	public static void main(String[] args) {
		new TankClient().launchFrame();
	}
	
	/**
	 * 对窗口中的主战坦克、敌对坦克、子弹、爆炸、墙、能量块进行绘制
	 */
	public void paint(Graphics g) {
		g.drawString("Bullets count: "+bullets.size(), 10, 50);
		g.drawString("Explodes count: "+explodes.size(), 10, 70);
		g.drawString("Tanks count: "+emTanks.size(), 10, 90);
		g.drawString("Life count: "+myTank.getLife(), 10, 110);
		
		myTank.drawTank(g);
		myTank.hitTanks(emTanks);
		myTank.hitWall(w1);
		myTank.hitWall(w2);
		myTank.eatEnergyBlock(blood);
		w1.drawWall(g);
		w2.drawWall(g);
		blood.drawEneryBlock(g);
		
		if(emTanks.size() <= 0) {
			
			for(int i=0; i<reproducedTankCount; i++) {
				this.emTanks.add(new Tank(120+40*i, 50, false, this, Direction.D) );
			}
		}
		for(int i=0; i<emTanks.size(); i++) {
			Tank t = emTanks.get(i);
			t.hitWall(w1);
			t.hitWall(w2);
			t.hitTanks(emTanks);
			t.drawTank(g);
	  	}

		for(int i=0; i<explodes.size(); i++) {
			Explode e = explodes.get(i);
			e.drawExplode(g);
		}
		
		
		for(int i=0; i<bullets.size(); i++) {
			Bullets b = bullets.get(i);
			b.hitTanks(emTanks);
			b.hitTank(myTank);
			b.hitWall(w1);
			b.hitWall(w2);
			b.drawBullets(g);
			
		}

	}

	
	@Override
	//利用双缓冲解决图像闪烁的问题
	public void update(Graphics g) {
		if(backImage == null) {
			backImage = this.createImage(FRAME_WIDTH, FRAME_HEIGHT);
		}
		
		Graphics backGraphic = backImage.getGraphics();
		Color backColor = backGraphic.getColor();
		backGraphic.setColor(Color.GREEN);
		backGraphic.fillRect(0,0, FRAME_WIDTH, FRAME_HEIGHT);
		backGraphic.setColor(backColor);
		paint(backGraphic);
		g.drawImage(backImage, 0, 0, null);
	
	}
	
	/**
	 * 生成主窗口，并且对窗口增加窗口与键盘的监听类
	 * 在窗口生成之后，专门启动另外一个线程用于在窗口上进行图像的绘制
	 */
	public void launchFrame() {
		
		/*
		 *这种写法会导致每次调用getProperty()之前都需要将配置文件从硬盘load一次，效率非常低
		 Properties props = new Properties();
		try {
			props.load(this.getClass().getClassLoader().getResourceAsStream("config/tank.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		int initialTankCount = Integer.parseInt(props.getProperty("initialTankCount"));
		*/
		
		for(int i=0; i<initialTankCount; i++) {
			this.emTanks.add(new Tank(120+40*i, 50, false, this, Direction.D) );
		}
		
		setTitle("TankWar");
		setSize(FRAME_WIDTH, FRAME_HEIGHT);
		setResizable(false);
		this.addWindowListener(new WindowMonitor());
		this.addKeyListener(new KeyMonitor());
		this.setBackground(Color.GREEN);
		setVisible(true);
		new Thread(new PaintThread()).start();
//System.out.println( isResizable());
		
	}
	
	//专门监听主战坦克的所有键盘行为
	class KeyMonitor extends KeyAdapter {

		@Override
		public void keyReleased(KeyEvent e) {
			myTank.keyReleased(e);
		}

		@Override
		public void keyPressed(KeyEvent e) {
			myTank.keyPressed(e);
		}
		
		
		
	}
	
	
	class WindowMonitor extends WindowAdapter {
		public void windowClosing(WindowEvent e) {
			System.exit(0);
		}
	}
	
	/**
	 * PaintThread线程类专门用于在窗口上进行画面的绘制，通过控制重绘时间间隔来控制画面的绘制
	 * @author xxq
	 *
	 */
	class PaintThread implements Runnable {

		@Override
		public void run() {
			while(true) {
				repaint();
				try{
					Thread.sleep(25);
				}catch(InterruptedException ie) {
					ie.printStackTrace();
				}
				
			}
		}
		
	}

}

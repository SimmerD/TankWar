package com.idc.tankwar;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;


/**
 * TankClient����̹�˴�ս������,2.9�汾�����������ļ�
 * @author xxq
 *
 */

public class TankClient extends Frame {
	
	/**ָ�������ڵĿ�� */
	public static final int FRAME_WIDTH = 800;
	/**ָ�������ڵĸ߶� */
	public static final int FRAME_HEIGHT = 600;
	
	//myTank����ս̹�ˣ�ֻ��һ��
	Tank myTank = new Tank(750, 550, true, this, Direction.STOP);
	
	Wall w1 = new Wall(400, 100, this);
	Wall w2 = new Wall(100, 100, this);
	EneryBlock blood = new EneryBlock();
	Image backImage = null;
	
	/** emTanks�Ǵ����з��õж�̹�˵����� */
	List<Tank> emTanks = new ArrayList<Tank>();
	/** explodes�Ǵ����з��ñ�ը������ */
	List<Explode> explodes = new ArrayList<Explode>();
	/** bullets�Ǵ����з��������ӵ������� */
	List<Bullets> bullets = new ArrayList<Bullets>() ;
	
	private int initialTankCount = Integer.parseInt(PropertyManager.getProperty("initialTankCount"));
	private int reproducedTankCount = Integer.parseInt(PropertyManager.getProperty("reproducedTankCount"));
	
	public static void main(String[] args) {
		new TankClient().launchFrame();
	}
	
	/**
	 * �Դ����е���ս̹�ˡ��ж�̹�ˡ��ӵ�����ը��ǽ����������л���
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
	//����˫������ͼ����˸������
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
	 * ���������ڣ����ҶԴ������Ӵ�������̵ļ�����
	 * �ڴ�������֮��ר����������һ���߳������ڴ����Ͻ���ͼ��Ļ���
	 */
	public void launchFrame() {
		
		/*
		 *����д���ᵼ��ÿ�ε���getProperty()֮ǰ����Ҫ�������ļ���Ӳ��loadһ�Σ�Ч�ʷǳ���
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
	
	//ר�ż�����ս̹�˵����м�����Ϊ
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
	 * PaintThread�߳���ר�������ڴ����Ͻ��л���Ļ��ƣ�ͨ�������ػ�ʱ���������ƻ���Ļ���
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

package com.idc.tankwar;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Random;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 坦克类
 * 用于控制坦克的生成、绘制、移动、开火、撞墙、坦克之间的相互碰撞以及如何加血等行为
 * @author xxq
 *
 */
public class Tank {

	/** 坦克在x方向上每次移动的距离 */
	public static final int XSPEED = 5;
	/** 坦克在y方向上每次移动的距离 */
	public static final int YSPEED = 5;
	/**  坦克的宽度 */
	public static final int TANK_WIDTH = 30;
	/**坦克的高度 */
	public static final int TANK_HEIGHT = 30;
	
	/*
	 * 坦克所处位置的x坐标
	 * 坦克所处位置的y坐标
	 * 坦克在这次绘制之后的x坐标
	 * 坦克在这次绘制之后的y坐标
	 * 坦克的生命值
	 * 控制坦克上下左右四个方向的值
	 */
	private int x;
	private int y;
	private int oldX;
	private int oldY;
	
	private int life = 100;
	
	private boolean up = false;
	private boolean down = false;
	private boolean left = false;
	private boolean right = false;
	
	/*
	 * 表示坦克是主战坦克还是敌对坦克
	 * 表示坦克的生死
	 */
	private boolean good;
	private boolean tAlive = true;
	

	TankClient tc = null;
	private Direction dir = Direction.STOP;
	private Direction barrelDir = Direction.U;
	
	private static Random random = new Random();
	private int step =  random.nextInt(12) + 3;
	
	//血条是一个类，它是坦克的一个内部类，属于坦克的成员变量，跟随主战坦克出现
	private BloodBar bloodBar = new BloodBar();

	private static Toolkit tk = Toolkit.getDefaultToolkit();
	private static Image[] images =  null;
	private static Map<String, Image> tankImages = new HashMap<String, Image>();
	
		static {
			images = new Image[] {
					tk.getImage(Tank.class.getClassLoader().getResource("images/tankL.gif")),
					tk.getImage(Tank.class.getClassLoader().getResource("images/tankLU.gif")),
					tk.getImage(Tank.class.getClassLoader().getResource("images/tankU.gif")),
					tk.getImage(Tank.class.getClassLoader().getResource("images/tankRU.gif")),
					tk.getImage(Tank.class.getClassLoader().getResource("images/tankR.gif")),
					tk.getImage(Tank.class.getClassLoader().getResource("images/tankRD.gif")),
					tk.getImage(Tank.class.getClassLoader().getResource("images/tankD.gif")),
					tk.getImage(Tank.class.getClassLoader().getResource("images/tankLD.gif"))
			};
			
			tankImages.put("L", images[0]);
			tankImages.put("LU", images[1]);
			tankImages.put("U", images[2]);
			tankImages.put("RU", images[3]);
			tankImages.put("R", images[4]);
			tankImages.put("RD", images[5]);
			tankImages.put("D", images[6]);
			tankImages.put("LD", images[7]);
			
			
				};
	
	public int getLife() {
		return  life;
	}
	
	public void setLife(int life) {
		this.life = life;
	}
	
	public boolean isTAlive() {
		return tAlive;
	}

	public void setTAlive(boolean tAlive) {
		this.tAlive = tAlive;
	}
	
	public boolean isGood() {
		return good;
	}
	
	public Tank(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public Tank(int x, int y, TankClient tc) {
		this(x, y);
		this.tc = tc;
	}
	
	public Tank(int x, int y, boolean good, TankClient tc) {
		this(x, y, tc);	
		this.good = good;

	}
	
	public Tank(int x, int y, boolean good, TankClient tc, Direction dir) {
		this(x, y, good, tc);
		this.dir = dir;
		this.oldX = x;
		this.oldY = y;
	}
	
	/**
	 * 用于绘制坦克
	 * @param g 画笔
	 * 在绘制坦克之前需要判断坦克的生死，若坦克已被消灭，则无需绘制，并且如果是敌对坦克，还需将其从装置坦克的容器中删除
	 * 此次绘制完成之后，通过move()方法来得到下次移动的x,y坐标值
	 */
	public void drawTank(Graphics g) {
		//一个tank一旦已经die了就不画了
		if(!tAlive) {
			if(!good) {
				tc.emTanks.remove(this);
			}
			return;
		}

		//为主站坦克加上表示生命值的血条
		if(this.good) bloodBar.drawBloodBar(g);
		
		//barrelDir表示炮筒的方向，该方向应与坦克的移动方向保持一致
		switch(barrelDir) {
		case L:
			g.drawImage(tankImages.get("L"), x, y, null);
			break;
		case LU:
			g.drawImage(tankImages.get("LU"), x, y, null);
			break;
		case U:
			g.drawImage(tankImages.get("U"), x, y, null);
			break;
		case RU:
			g.drawImage(tankImages.get("RU"), x, y, null);
			break;
		case R:
			g.drawImage(tankImages.get("R"), x, y, null);
			break;
		case RD:
			g.drawImage(tankImages.get("RD"), x, y, null);
			break;
		case D:
			g.drawImage(tankImages.get("D"), x, y, null);
			break;
		case LD:
			g.drawImage(tankImages.get("LD"), x, y, null);
			break;
		}
		
		//在坦克的此次绘制完成之后，通过move()方法计算出坦克下次绘制的x, y
		move();
		
	}
	

	/**
	 * 每次按下按键的时候，就调用keyPressed()方法
	 * @param e 键盘事件
	 * 通过该方法，将上下左右四个值改为true，从而决定坦克下次移动的方向
	 */
	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();
		switch(keyCode) {
			case KeyEvent.VK_LEFT:
				left = true;
				break;
			case KeyEvent.VK_RIGHT:
				right = true;
				break;
			case KeyEvent.VK_UP:
				up = true;
				break;
			case KeyEvent.VK_DOWN:
				down = true;
				break;
			case KeyEvent.VK_F2:
				if(!this.tAlive) {
					this.tAlive = true;
				}
		}
		locateDirection();
	}
	/**
	 * 每次松开按键的时候，就调用keyReleased()方法
	 * @param e 键盘事件
	 * 通过该方法，将上下左右四个值改为false，从而决定坦克下次移动的方向，
	 * 当按下Ctrl键并且主战坦克还活着的时候，主战坦克开火进行攻击，
	 * 当按下空格键并且主战坦克还活着的时候，主战坦克进行超级活力攻击，可以朝八个方向开火
	 */
	
	public void keyReleased(KeyEvent e) {
		int keyCode = e.getKeyCode();
		if(keyCode == KeyEvent.VK_CONTROL && this.tAlive) {
			fire();
		}else if( keyCode == KeyEvent.VK_LEFT) {
			left = false;
		}else if(keyCode == KeyEvent.VK_RIGHT) {
			right = false;
		}else if(keyCode == KeyEvent.VK_UP) {
			up = false;
		}else if(keyCode == KeyEvent.VK_DOWN){
			down = false;
		}else if(keyCode == KeyEvent.VK_SPACE && this.tAlive) {
			superFire();
		}
		locateDirection();
	}
	
	/**
	 * 通过键盘响应之后的上下左右四个值来决定坦克的移动方向
	 */
	public void locateDirection() {
		
		if(left && !right && !up && !down) {
			dir = Direction.L;
		}else if(left && !right && up && !down) {
			dir = Direction.LU;
		}else if(!left && !right && up && !down) {
			dir = Direction.U;
		}else if(!left && right && up && !down) {
			dir = Direction.RU;
		}else if(!left && right &&! up && !down) {
			dir = Direction.R;
		}else if(!left && right && !up && down) {
			dir = Direction.RD;
		}else if(!left && !right && !up && down) {
			dir = Direction.D;
		}else if(left && !right && !up && down) {
			dir = Direction.LD;
		}else {
			dir = Direction.STOP;
		}
	}
	
	/**
	 * 使坦克的位置停留在上次绘制之后的地方，不进行改变
	 */
	private void keep(){
		x = oldX;
		y = oldY;
	}
	
	/**
	 * 通过坦克的移动方向来决定坦克在x方向和y方向上的坐标值，
	 * 在坦克要出主窗口边界的时候，予以阻止
	 */
	public void move(){
		oldX = x;
		oldY = y;
		switch(dir) {
			case L:
				x -= XSPEED;
				break;
			case LU:
				x -= XSPEED;
				y -= YSPEED;
				break;
			case U:
				y -= YSPEED;
				break;
			case RU:
				x += XSPEED;
				y -= YSPEED;
				break;
			case R:
				x += XSPEED;
				break;
			case RD:
				x += XSPEED;
				y += YSPEED;
				break;
			case D:
				y += YSPEED;
				break;
			case LD:
				x -= XSPEED;
				y += YSPEED;
				break;
			case STOP:
				break;
		}
		
		if(dir != Direction.STOP) {
			barrelDir = dir;
		}
		
		if(x < 0) x = 0;
		if(y < 30) y = 30;
		if(x + Tank.TANK_WIDTH > tc.FRAME_WIDTH) x = tc.FRAME_WIDTH - Tank.TANK_WIDTH;
		if(y + Tank.TANK_HEIGHT > tc.FRAME_HEIGHT) y = tc.FRAME_HEIGHT - Tank.TANK_HEIGHT;
		
		/*
		 * 对于敌对坦克，其初始的方向都是向下，但是为了使敌对坦克能随机在整个窗口上进行移动，我们设置通过随机数生成器设置一个随机数
		 * 每个敌对坦克在此次绘制结束之后就将该随机数减一，当减至0的时候，随机改变该坦克的方向，再让其朝该方向移动step次
		 */
		if(!good) {
			if(random.nextInt(50) > 48) this.fire();
			step --;
			if(step == 0){
				step = random.nextInt(12) + 3;
				Direction[] dirs = dir.values();
				int rd = random.nextInt(dirs.length);
				dir = dirs[rd];
			}
			
		}
	
		
	}
	
	/**
	 * 当坦克进行攻击的时候，需要产生子弹，
	 * 子弹的方向与此时炮筒的方向一致，
	 * 在产生一颗子弹之后，需要将其放入专门放置子弹的容器bullets中
	 */
	public void fire() {
 		int x = this.x + Tank.TANK_WIDTH/2 - Bullets.BULLET_WIDTH/2;
		int y = this.y + Tank.TANK_HEIGHT/2 - Bullets.BULLET_HEIGHT/2;
		Bullets bullet = new Bullets(x, y, barrelDir, tc, good);
		tc.bullets.add(bullet);
	}
	
	/**
	 * 让子弹按照给定的方向进行射击
	 * @param dir 子弹射击的方向
	 */
	public void fire(Direction dir) {
		int x = this.x + Tank.TANK_WIDTH/2 - Bullets.BULLET_WIDTH/2;
		int y = this.y + Tank.TANK_HEIGHT/2 - Bullets.BULLET_HEIGHT/2;
		Bullets bullet = new Bullets(x, y, dir, tc, good);
		tc.bullets.add(bullet);
	}
	
	/**
	 * 在八个方向上分别调用 fire(Direction dir)方法，使得主战坦克能在八个方向“同时”开火
	 */
	public void superFire() {
		Direction[] dirs = Direction.values();
		for(int i=0; i<dirs.length -1; i++) {
			fire(dirs[i]);
		}
	}
	
	/**
	 * 获得某个对象外面包裹的矩形
	 * @return 一个矩形对象
	 */
	public Rectangle getRect() {
		return new Rectangle(x, y,Tank.TANK_WIDTH, Tank.TANK_HEIGHT);
		
	}
	
	/**
	 * 坦克撞墙的方法
	 * @param wall 被坦克撞击的墙
	 * @return 如果坦克撞到墙，则返回true，否则返回false。
	 * 当该坦克是活的，并且该坦克与该wall撞击了，则将坦克的x, y置为上次的x , y，即相当于坦克在下次要撞墙之前，将其在原地重画
	 */
	public boolean hitWall(Wall wall) {

		if(this.tAlive && this.getRect().intersects(wall.getRect())) {
			keep();
			return true;
		}
		return false;
	}

	/**
	 * 因为坦克之后不能互相穿透，所以设置hitTanks()
	 * @param tanks 要被撞击的坦克集合
	 * 在绘制一辆坦克之前，先将其与其他所有坦克进行碰撞测试
	 * 若该坦克是活的，被测试的那辆坦克也是活的，且二者不是同一辆坦克且两辆坦克要碰撞上了，则将这两辆坦克的x ，y都置为上次的位置，即都在原地进行重绘
	 */
	public void hitTanks(List<Tank> tanks) {
		for(int i=0; i<tanks.size(); i++) {
			Tank t = tanks.get(i);
			if(this.tAlive &&t.tAlive && !t.equals(this)&&this.getRect().intersects(t.getRect())) {
				this.keep();
				t.keep();
			}
		}
	}
	
/**
 * 为主站坦克增加生命条，该生命条跟随坦克移动，所以其x,y值都与坦克自身的x, y值有关联，所以比较适合写成内部类
 * @author xxq
 *生命条的长度随主站坦克的生命值而改变
 */
	private class BloodBar {
		//生命条的绘制方法
		public void drawBloodBar(Graphics g) {
			Color c = g.getColor();
			g.setColor(Color.RED);
			g.drawRect(x, y-10, images[0].getWidth(null), 10);
			int bloodWidth =  images[0].getWidth(null) * life / 100;
			g.fillRect(x, y-10, bloodWidth, 10);
			g.setColor(c);
		}
	}
	
	/**
	 * 主站坦克可以吃能量块进行补血
	 * @param blood 表示被吃的能量块
	 * @return 当主站坦克成功吃到该能量块之后返回true，否则返回false。
	 * 一旦主战坦克吃了该能量块，生命值回复满血，并且能量块立刻消失
	 */
	public boolean eatEnergyBlock(EneryBlock blood) {
		if(this.good) {
			if(this.tAlive && blood.isLive() && this.getRect().intersects(blood.getRect())) {
				this.setLife(100);
				blood.setLive(false);
				return true;
			}
		}
		return false;
	}
	
		
	
	
}
	
	
	


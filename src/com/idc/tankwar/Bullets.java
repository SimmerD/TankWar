package com.idc.tankwar;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.awt.event.KeyEvent;
/**
 * 子弹类
 * 用于控制子弹的生成、绘制、移动、对坦克的打击以及对墙的打击等行为
 * @author xxq
 *
 */
public class Bullets {
	/*
	 * 子弹所处位置的x坐标
	 * 子弹所处位置的y坐标
	 */
	private int x = 0;
	private int y = 0 ;
	private Direction dir;
	private TankClient tc;
	
	/**子弹每次在x方向上的移动的距离*/
	public static final int XSPEED = 10;
	/**子弹每次在y方向上的移动的距离*/
	public static final int YSPEED = 10;
	/**子弹的宽度*/
	public static final int BULLET_WIDTH = 10;
	/**子弹的高度*/
	public static final int BULLET_HEIGHT = 10;
	
	/*
	 * 子弹是否存活
	 * 子弹是好是坏，子弹的好坏与坦克本身的好坏是一致的，加上这个属性是为了不让子弹攻击与其好坏属性相同的坦克，即不攻击自己人
	 */
	private boolean bAlive = true;
	private boolean good;
	
	private static Toolkit tk = Toolkit.getDefaultToolkit();
	private static Image[] images =  null;
	private static Map<String, Image> missileImages = new HashMap<String, Image>();
	
		static {
			images = new Image[] {
					tk.getImage(Bullets.class.getClassLoader().getResource("images/missileL.gif")),
					tk.getImage(Bullets.class.getClassLoader().getResource("images/missileLU.gif")),
					tk.getImage(Bullets.class.getClassLoader().getResource("images/missileU.gif")),
					tk.getImage(Bullets.class.getClassLoader().getResource("images/missileRU.gif")),
					tk.getImage(Bullets.class.getClassLoader().getResource("images/missileR.gif")),
					tk.getImage(Bullets.class.getClassLoader().getResource("images/missileRD.gif")),
					tk.getImage(Bullets.class.getClassLoader().getResource("images/missileD.gif")),
					tk.getImage(Bullets.class.getClassLoader().getResource("images/missileLD.gif"))
			};
			
			missileImages.put("L", images[0]);
			missileImages.put("LU", images[1]);
			missileImages.put("U", images[2]);
			missileImages.put("RU", images[3]);
			missileImages.put("R", images[4]);
			missileImages.put("RD", images[5]);
			missileImages.put("D", images[6]);
			missileImages.put("LD", images[7]);
			
			
				};

	public boolean isAlive() {
		return bAlive;
	}

	public boolean isGood() {
		return good;
	}
	
	public Bullets(int x, int y, Direction dir) {
		this.x = x;
		this.y = y;
		this.dir = dir;
		
	}
	
	public Bullets(int x, int y, Direction dir, TankClient tc) {
		this(x, y, dir);
		this.tc = tc;
	}
	
	public Bullets(int x, int y, Direction dir, TankClient tc, boolean good) {
		this(x, y, dir, tc);
		this.good = good;
	}

	/**
	 * 用于绘制子弹
	 * @param g 画笔
	 * 在绘制子弹前需要判断子弹的生死，若该子弹已死亡，则将其从放置子弹的容器中删除并直接返回，
	 * 此次绘制完成之后，通过move()方法来得到下次移动的x,y坐标值
	 */
	public void drawBullets(Graphics g) {
		if(!bAlive) {
			tc.bullets.remove(this);
			return;
		}
		
		switch(dir) {
		case L:
			g.drawImage(missileImages.get("L"), x, y, null);
			break;
		case LU:
			g.drawImage(missileImages.get("LU"), x, y, null);
			break;
		case U:
			g.drawImage(missileImages.get("U"), x, y, null);
			break;
		case RU:
			g.drawImage(missileImages.get("RU"), x, y, null);
			break;
		case R:
			g.drawImage(missileImages.get("R"), x, y, null);
			break;
		case RD:
			g.drawImage(missileImages.get("RD"), x, y, null);
			break;
		case D:
			g.drawImage(missileImages.get("D"), x, y, null);
			break;
		case LD:
			g.drawImage(missileImages.get("LD"), x, y, null);
			break;
		}
		move();
		
	}
	
	
	public void move(){
		//因为子弹不能在运动过程中停止，所以子弹没有stop状态，由于子弹的方向直接取决于炮筒的方向，所以炮筒也没有stop方向
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
		
		//若子弹飞出边界，则置为死亡状态
		if(x < 0 || y < 0 || x > TankClient.FRAME_WIDTH || y > TankClient.FRAME_HEIGHT) {
			bAlive = false;
		}
		
		
	}

	public Rectangle getRect() {
		return new Rectangle(x, y, Bullets.BULLET_WIDTH, Bullets.BULLET_HEIGHT);
		
	}
	
	//子弹击中坦克之后先将该tank置为die状态，同时该子弹本身也要跟着置为die状态
	/**
	 * 子弹击打坦克的方法
	 * @param tank 被该子弹击打的坦克
	 * @return 若该子弹打到该坦克则返回ture，否则返回false
	 * 当该子弹与该坦克都是活的，并且两者即将撞击，且该被攻击的坦克不是自己人的时候，攻击有效，产生一次爆炸，该爆炸会被加入放置爆炸的容器explodes中，
	 * 该爆炸产生的位置与子弹此时的位置一致，
	 * 在攻击有效的情况下，若被攻击的是主战坦克，则其生命值掉20，若生命值低于0，则主战坦克死亡，
	 * 在攻击有效的情况下，若被攻击的敌对坦克，则该敌对坦克直接死亡。
	 */
	public boolean hitTank(Tank tank) {
		if(this.bAlive && tank.isTAlive() && this.getRect().intersects(tank.getRect())&& this.isGood() != tank.isGood()) {
			if(tank.isGood()) {
				tank.setLife(tank.getLife() - 20 );
				if(tank.getLife() <= 0) {
					tank.setTAlive(false);
				}
			}else{
				tank.setTAlive(false);
			}
				bAlive = false;		
				Explode explode = new Explode(x, y, tc);
				tc.explodes.add(explode);
				return true;
			
		}
		return false;
	}
	
	/**
	 * 因为窗口中有多辆坦克，所以每一发子弹都要针对所有的坦克进行射击测试
	 * @param tanks 被子弹攻击的坦克集合
	 * @return 若击中了集合中的某一辆坦克，返回true，否则返回false
	 * 将集合中的每一辆坦克挨个取出，进行this.hitTank(Tank tank)测试
	 */
	public boolean hitTanks(List<Tank> tanks) {
		for(int i=0; i<tanks.size(); i++) {
			if(hitTank(tanks.get(i)) ) {
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * 子弹撞墙
	 * @param wall 被撞击的墙
	 * @return 若子弹撞击了墙，则返回true，否则返回false
	 * 当该子弹是活的，并且该子弹与墙即将撞击的时候，将该子弹置为死亡状态
	 */
	public boolean hitWall(Wall wall) {
		if(this.bAlive && this.getRect().intersects(wall.getRect())) {
			bAlive = false;
			return true;
		}
		return false;
	}
	
}

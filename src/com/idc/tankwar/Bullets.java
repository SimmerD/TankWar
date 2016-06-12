package com.idc.tankwar;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.awt.event.KeyEvent;
/**
 * �ӵ���
 * ���ڿ����ӵ������ɡ����ơ��ƶ�����̹�˵Ĵ���Լ���ǽ�Ĵ������Ϊ
 * @author xxq
 *
 */
public class Bullets {
	/*
	 * �ӵ�����λ�õ�x����
	 * �ӵ�����λ�õ�y����
	 */
	private int x = 0;
	private int y = 0 ;
	private Direction dir;
	private TankClient tc;
	
	/**�ӵ�ÿ����x�����ϵ��ƶ��ľ���*/
	public static final int XSPEED = 10;
	/**�ӵ�ÿ����y�����ϵ��ƶ��ľ���*/
	public static final int YSPEED = 10;
	/**�ӵ��Ŀ��*/
	public static final int BULLET_WIDTH = 10;
	/**�ӵ��ĸ߶�*/
	public static final int BULLET_HEIGHT = 10;
	
	/*
	 * �ӵ��Ƿ���
	 * �ӵ��Ǻ��ǻ����ӵ��ĺû���̹�˱���ĺû���һ�µģ��������������Ϊ�˲����ӵ���������û�������ͬ��̹�ˣ����������Լ���
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
	 * ���ڻ����ӵ�
	 * @param g ����
	 * �ڻ����ӵ�ǰ��Ҫ�ж��ӵ��������������ӵ�������������ӷ����ӵ���������ɾ����ֱ�ӷ��أ�
	 * �˴λ������֮��ͨ��move()�������õ��´��ƶ���x,y����ֵ
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
		//��Ϊ�ӵ��������˶�������ֹͣ�������ӵ�û��stop״̬�������ӵ��ķ���ֱ��ȡ������Ͳ�ķ���������ͲҲû��stop����
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
		
		//���ӵ��ɳ��߽磬����Ϊ����״̬
		if(x < 0 || y < 0 || x > TankClient.FRAME_WIDTH || y > TankClient.FRAME_HEIGHT) {
			bAlive = false;
		}
		
		
	}

	public Rectangle getRect() {
		return new Rectangle(x, y, Bullets.BULLET_WIDTH, Bullets.BULLET_HEIGHT);
		
	}
	
	//�ӵ�����̹��֮���Ƚ���tank��Ϊdie״̬��ͬʱ���ӵ�����ҲҪ������Ϊdie״̬
	/**
	 * �ӵ�����̹�˵ķ���
	 * @param tank �����ӵ������̹��
	 * @return �����ӵ��򵽸�̹���򷵻�ture�����򷵻�false
	 * �����ӵ����̹�˶��ǻ�ģ��������߼���ײ�����Ҹñ�������̹�˲����Լ��˵�ʱ�򣬹�����Ч������һ�α�ը���ñ�ը�ᱻ������ñ�ը������explodes�У�
	 * �ñ�ը������λ�����ӵ���ʱ��λ��һ�£�
	 * �ڹ�����Ч������£���������������ս̹�ˣ���������ֵ��20��������ֵ����0������ս̹��������
	 * �ڹ�����Ч������£����������ĵж�̹�ˣ���õж�̹��ֱ��������
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
	 * ��Ϊ�������ж���̹�ˣ�����ÿһ���ӵ���Ҫ������е�̹�˽����������
	 * @param tanks ���ӵ�������̹�˼���
	 * @return �������˼����е�ĳһ��̹�ˣ�����true�����򷵻�false
	 * �������е�ÿһ��̹�˰���ȡ��������this.hitTank(Tank tank)����
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
	 * �ӵ�ײǽ
	 * @param wall ��ײ����ǽ
	 * @return ���ӵ�ײ����ǽ���򷵻�true�����򷵻�false
	 * �����ӵ��ǻ�ģ����Ҹ��ӵ���ǽ����ײ����ʱ�򣬽����ӵ���Ϊ����״̬
	 */
	public boolean hitWall(Wall wall) {
		if(this.bAlive && this.getRect().intersects(wall.getRect())) {
			bAlive = false;
			return true;
		}
		return false;
	}
	
}

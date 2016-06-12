package com.idc.tankwar;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Random;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ̹����
 * ���ڿ���̹�˵����ɡ����ơ��ƶ�������ײǽ��̹��֮����໥��ײ�Լ���μ�Ѫ����Ϊ
 * @author xxq
 *
 */
public class Tank {

	/** ̹����x������ÿ���ƶ��ľ��� */
	public static final int XSPEED = 5;
	/** ̹����y������ÿ���ƶ��ľ��� */
	public static final int YSPEED = 5;
	/**  ̹�˵Ŀ�� */
	public static final int TANK_WIDTH = 30;
	/**̹�˵ĸ߶� */
	public static final int TANK_HEIGHT = 30;
	
	/*
	 * ̹������λ�õ�x����
	 * ̹������λ�õ�y����
	 * ̹������λ���֮���x����
	 * ̹������λ���֮���y����
	 * ̹�˵�����ֵ
	 * ����̹�����������ĸ������ֵ
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
	 * ��ʾ̹������ս̹�˻��ǵж�̹��
	 * ��ʾ̹�˵�����
	 */
	private boolean good;
	private boolean tAlive = true;
	

	TankClient tc = null;
	private Direction dir = Direction.STOP;
	private Direction barrelDir = Direction.U;
	
	private static Random random = new Random();
	private int step =  random.nextInt(12) + 3;
	
	//Ѫ����һ���࣬����̹�˵�һ���ڲ��࣬����̹�˵ĳ�Ա������������ս̹�˳���
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
	 * ���ڻ���̹��
	 * @param g ����
	 * �ڻ���̹��֮ǰ��Ҫ�ж�̹�˵���������̹���ѱ�������������ƣ���������ǵж�̹�ˣ����轫���װ��̹�˵�������ɾ��
	 * �˴λ������֮��ͨ��move()�������õ��´��ƶ���x,y����ֵ
	 */
	public void drawTank(Graphics g) {
		//һ��tankһ���Ѿ�die�˾Ͳ�����
		if(!tAlive) {
			if(!good) {
				tc.emTanks.remove(this);
			}
			return;
		}

		//Ϊ��վ̹�˼��ϱ�ʾ����ֵ��Ѫ��
		if(this.good) bloodBar.drawBloodBar(g);
		
		//barrelDir��ʾ��Ͳ�ķ��򣬸÷���Ӧ��̹�˵��ƶ����򱣳�һ��
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
		
		//��̹�˵Ĵ˴λ������֮��ͨ��move()���������̹���´λ��Ƶ�x, y
		move();
		
	}
	

	/**
	 * ÿ�ΰ��°�����ʱ�򣬾͵���keyPressed()����
	 * @param e �����¼�
	 * ͨ���÷����������������ĸ�ֵ��Ϊtrue���Ӷ�����̹���´��ƶ��ķ���
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
	 * ÿ���ɿ�������ʱ�򣬾͵���keyReleased()����
	 * @param e �����¼�
	 * ͨ���÷����������������ĸ�ֵ��Ϊfalse���Ӷ�����̹���´��ƶ��ķ���
	 * ������Ctrl��������ս̹�˻����ŵ�ʱ����ս̹�˿�����й�����
	 * �����¿ո��������ս̹�˻����ŵ�ʱ����ս̹�˽��г����������������Գ��˸����򿪻�
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
	 * ͨ��������Ӧ֮������������ĸ�ֵ������̹�˵��ƶ�����
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
	 * ʹ̹�˵�λ��ͣ�����ϴλ���֮��ĵط��������иı�
	 */
	private void keep(){
		x = oldX;
		y = oldY;
	}
	
	/**
	 * ͨ��̹�˵��ƶ�����������̹����x�����y�����ϵ�����ֵ��
	 * ��̹��Ҫ�������ڱ߽��ʱ��������ֹ
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
		 * ���ڵж�̹�ˣ����ʼ�ķ��������£�����Ϊ��ʹ�ж�̹������������������Ͻ����ƶ�����������ͨ�����������������һ�������
		 * ÿ���ж�̹���ڴ˴λ��ƽ���֮��ͽ����������һ��������0��ʱ������ı��̹�˵ķ��������䳯�÷����ƶ�step��
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
	 * ��̹�˽��й�����ʱ����Ҫ�����ӵ���
	 * �ӵ��ķ������ʱ��Ͳ�ķ���һ�£�
	 * �ڲ���һ���ӵ�֮����Ҫ�������ר�ŷ����ӵ�������bullets��
	 */
	public void fire() {
 		int x = this.x + Tank.TANK_WIDTH/2 - Bullets.BULLET_WIDTH/2;
		int y = this.y + Tank.TANK_HEIGHT/2 - Bullets.BULLET_HEIGHT/2;
		Bullets bullet = new Bullets(x, y, barrelDir, tc, good);
		tc.bullets.add(bullet);
	}
	
	/**
	 * ���ӵ����ո����ķ���������
	 * @param dir �ӵ�����ķ���
	 */
	public void fire(Direction dir) {
		int x = this.x + Tank.TANK_WIDTH/2 - Bullets.BULLET_WIDTH/2;
		int y = this.y + Tank.TANK_HEIGHT/2 - Bullets.BULLET_HEIGHT/2;
		Bullets bullet = new Bullets(x, y, dir, tc, good);
		tc.bullets.add(bullet);
	}
	
	/**
	 * �ڰ˸������Ϸֱ���� fire(Direction dir)������ʹ����ս̹�����ڰ˸�����ͬʱ������
	 */
	public void superFire() {
		Direction[] dirs = Direction.values();
		for(int i=0; i<dirs.length -1; i++) {
			fire(dirs[i]);
		}
	}
	
	/**
	 * ���ĳ��������������ľ���
	 * @return һ�����ζ���
	 */
	public Rectangle getRect() {
		return new Rectangle(x, y,Tank.TANK_WIDTH, Tank.TANK_HEIGHT);
		
	}
	
	/**
	 * ̹��ײǽ�ķ���
	 * @param wall ��̹��ײ����ǽ
	 * @return ���̹��ײ��ǽ���򷵻�true�����򷵻�false��
	 * ����̹���ǻ�ģ����Ҹ�̹�����wallײ���ˣ���̹�˵�x, y��Ϊ�ϴε�x , y�����൱��̹�����´�Ҫײǽ֮ǰ��������ԭ���ػ�
	 */
	public boolean hitWall(Wall wall) {

		if(this.tAlive && this.getRect().intersects(wall.getRect())) {
			keep();
			return true;
		}
		return false;
	}

	/**
	 * ��Ϊ̹��֮���ܻ��ഩ͸����������hitTanks()
	 * @param tanks Ҫ��ײ����̹�˼���
	 * �ڻ���һ��̹��֮ǰ���Ƚ�������������̹�˽�����ײ����
	 * ����̹���ǻ�ģ������Ե�����̹��Ҳ�ǻ�ģ��Ҷ��߲���ͬһ��̹��������̹��Ҫ��ײ���ˣ���������̹�˵�x ��y����Ϊ�ϴε�λ�ã�������ԭ�ؽ����ػ�
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
 * Ϊ��վ̹��������������������������̹���ƶ���������x,yֵ����̹�������x, yֵ�й��������ԱȽ��ʺ�д���ڲ���
 * @author xxq
 *�������ĳ�������վ̹�˵�����ֵ���ı�
 */
	private class BloodBar {
		//�������Ļ��Ʒ���
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
	 * ��վ̹�˿��Գ���������в�Ѫ
	 * @param blood ��ʾ���Ե�������
	 * @return ����վ̹�˳ɹ��Ե���������֮�󷵻�true�����򷵻�false��
	 * һ����ս̹�˳��˸������飬����ֵ�ظ���Ѫ������������������ʧ
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
	
	
	


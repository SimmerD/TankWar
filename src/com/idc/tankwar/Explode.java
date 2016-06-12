package com.idc.tankwar;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
/**
 * 爆炸类
 * 控制爆炸的产生，绘制
 * @author xxq
 */
public class Explode {
	
	/*
	 * 爆炸发生的位置的x坐标
	 * 爆炸发生的位置的y坐标
	 * 爆炸是否有效
	 */
	private int x;
	private int y;
	private boolean eLive = true;
	private TankClient tc;
	
	private static boolean initial = false;
	private static Toolkit tk = Toolkit.getDefaultToolkit();
	private static Image[] images = {
		
			tk.getImage(Explode.class.getClassLoader().getResource("images/0.gif")),
			tk.getImage(Explode.class.getClassLoader().getResource("images/1.gif")),
			tk.getImage(Explode.class.getClassLoader().getResource("images/2.gif")),
			tk.getImage(Explode.class.getClassLoader().getResource("images/3.gif")),
			tk.getImage(Explode.class.getClassLoader().getResource("images/4.gif")),
			tk.getImage(Explode.class.getClassLoader().getResource("images/5.gif")),
			tk.getImage(Explode.class.getClassLoader().getResource("images/6.gif")),
			tk.getImage(Explode.class.getClassLoader().getResource("images/7.gif")),
			tk.getImage(Explode.class.getClassLoader().getResource("images/8.gif")),
			tk.getImage(Explode.class.getClassLoader().getResource("images/9.gif")),
			tk.getImage(Explode.class.getClassLoader().getResource("images/10.gif"))
														};
	int step = 0;
	
	public Explode(int x, int y, TankClient tc) {
		this.x = x;
		this.y = y;
		this.tc = tc;
	}
	/**
	 * 用于绘制爆炸
	 * @param g 画笔
	 * 首先需要判断该爆炸是否有效，若无效，则将其从集合explodes中删除，并直接返回；
	 * 当最后一个圆也绘制完成的时候，将该爆炸置为无效，即爆炸绘制完成之后立刻消失
	 */
	public void drawExplode(Graphics g) {
		
		if(!eLive){
			tc.explodes.remove(this);
			return;
		}
		
		/*问题：在第一颗子弹撞击坦克导致爆炸的时候没有爆炸图片的产生
		 * 可能的原因1：一开始getImage()只是拿到了图片的虚代理，并没有真正的拿到数据，所以当drawImage()方法被第一次调用的时候，它还没有得到图片的数据。
		 * 可能的原因2：getImage()采用的异步IO，即在通过这个方法获得图片的过程中，程序并没有在此阻塞，而是继续往后执行，也就导致当调用drawImage()进行图片的绘制时，爆炸图片的数据还没有被读进内存
		 * 所以导致第一次爆炸发生时，没有显示爆炸图片。
		 * 总之，以上两种原因的导致结果的共同点就是在drawImage()第一次被调用的时候，爆炸图片真正的数据还没有被读入内存，所以显示不出来。
		 * 因此我们在调用drawImage()方法之前，先在一个其他的位置将 图片先“画”一遍，确保后面真正在窗口上画爆炸图片时，相关图片的数据已经被读入了内存
		 * 
		 */
		
		
		if(!initial) {
			for(int i=0; i<images.length; i++) {
				g.drawImage(images[i], -100, -100, null);
			}
			initial = true;
			
		}
		
		
		g.drawImage(images[step], x, y, null);
		step++;
		if(step >= images.length) {
			eLive = false;
		}
		
	}
	
}

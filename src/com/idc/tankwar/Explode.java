package com.idc.tankwar;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
/**
 * ��ը��
 * ���Ʊ�ը�Ĳ���������
 * @author xxq
 */
public class Explode {
	
	/*
	 * ��ը������λ�õ�x����
	 * ��ը������λ�õ�y����
	 * ��ը�Ƿ���Ч
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
	 * ���ڻ��Ʊ�ը
	 * @param g ����
	 * ������Ҫ�жϸñ�ը�Ƿ���Ч������Ч������Ӽ���explodes��ɾ������ֱ�ӷ��أ�
	 * �����һ��ԲҲ������ɵ�ʱ�򣬽��ñ�ը��Ϊ��Ч������ը�������֮��������ʧ
	 */
	public void drawExplode(Graphics g) {
		
		if(!eLive){
			tc.explodes.remove(this);
			return;
		}
		
		/*���⣺�ڵ�һ���ӵ�ײ��̹�˵��±�ը��ʱ��û�б�ըͼƬ�Ĳ���
		 * ���ܵ�ԭ��1��һ��ʼgetImage()ֻ���õ���ͼƬ���������û���������õ����ݣ����Ե�drawImage()��������һ�ε��õ�ʱ������û�еõ�ͼƬ�����ݡ�
		 * ���ܵ�ԭ��2��getImage()���õ��첽IO������ͨ������������ͼƬ�Ĺ����У�����û���ڴ����������Ǽ�������ִ�У�Ҳ�͵��µ�����drawImage()����ͼƬ�Ļ���ʱ����ըͼƬ�����ݻ�û�б������ڴ�
		 * ���Ե��µ�һ�α�ը����ʱ��û����ʾ��ըͼƬ��
		 * ��֮����������ԭ��ĵ��½���Ĺ�ͬ�������drawImage()��һ�α����õ�ʱ�򣬱�ըͼƬ���������ݻ�û�б������ڴ棬������ʾ��������
		 * ��������ڵ���drawImage()����֮ǰ������һ��������λ�ý� ͼƬ�ȡ�����һ�飬ȷ�����������ڴ����ϻ���ըͼƬʱ�����ͼƬ�������Ѿ����������ڴ�
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

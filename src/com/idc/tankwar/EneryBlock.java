package com.idc.tankwar;
import java.awt.*;
/**
 * ��������
 * ���ڿ���������Ĳ��������ƣ�
 * ��ս̹�˳��˸�������֮�������ָ̻���Ѫ״̬
 * @author xxq

 */
public class EneryBlock {

	/** ������λ�õ�x����*/
	int x;
	/**������λ�õ�y����*/
	int y;
	/** ������Ŀ��*/
	public static final int BLOOD_WIDTH = 15;
	/**������ĸ߶�*/
	public static final int BLOOD_HEIGHT = 15;
	//�������Ƿ���Ч
	private boolean live = true; 
	
	//��һϵ�в�ͬ�����������ʾ�������λ�ã�ʹ������һֱ����Щ�����ر䶯����step����ʾ���Ƶ��ڼ���
	int[][] pos = {
			{420, 300},{450, 300},{470, 250},{500, 300},{520, 330},
			{550, 350},{580, 300},{530, 350},{555, 325},{500,350},
						};
	int step = 0;
	
	/**
	 * �����������ʱ��ʹ��λ��pos�еĵ�һ��λ��
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
	 * ���ڻ���������
	 * @param g ����
	 * ������������Ч���򲻻��ƣ�ֱ�ӷ��ء�
	 * ��ÿ�λ���֮ǰ����x,y��ֵ���иı䣬������pos�е������������������飬
	 * �ڵ�ǰ�������֮�󣬽�step��һ�����ж�step�Ƿ񳬳�pos�ĳ��ȣ������������ʾ��һ�ֻ�����ϣ���step������Ϊ0���ٴ�pos�еĵ�һ��λ�ÿ�ʼ���ơ�
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

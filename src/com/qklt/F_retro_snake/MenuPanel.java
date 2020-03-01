package com.qklt.F_retro_snake;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;


public class MenuPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	static final int BACKGROUND_WIDTH = 605;
	static final int BACKGROUND_HEIGHT = 605;
	private static final int Frequency = 300;//刷新频率
	
	JButton[] jb = new JButton[]
			{new JButton("开始游戏"),new JButton("设置按键"),
			new JButton("排行榜"),new JButton("退出游戏")};
	
	public Boolean is_exit = false;//是否退出
	public int menuChoose = -1;
	
	
	private void initMenu()
	{
		this.setLayout(null);//菜单采用自定义布局
		for(int i = 0; i < 4; i ++)//初始化按钮
		{
			SetPanel.buttonInit(jb[i]);
			jb[i].setBounds(190 + 225, 300 + 80 * i, 200, 50);//位置 大小
			this.add(jb[i]);
		}
		
		jb[0].addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				is_exit = true;
				menuChoose = 0;
			}
		});//按钮1加入事件监听器
		
		jb[1].addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				is_exit = true;
				menuChoose = 1;
			}
		});//按钮2加入事件监听器
		
		jb[2].addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				is_exit=true;
				menuChoose=2;
			}
		});//按钮3加入事件监听器
		
		jb[3].addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				System.exit(-1);
			}
		});//按钮4加入事件监听器
		
	}
	
	
	public void startMenu()
	{
		initMenu();
		repaint();
		while (true)//无限循环
		{
			try
			{
				if(is_exit)//如果选择了操作则退出菜单的循环
					break;
				Thread.sleep(Frequency);//延时间隔
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();//出错处理仅为简单打印信息
			}
		}
	}
	
	@Override
	protected void paintComponent(Graphics g)
	{
		g.drawImage(Toolkit.getDefaultToolkit().getImage("贪吃蛇素材\\background_m.jpg")
				,0,0,625 + 450,645,this);
		g.setColor(Color.BLACK);
		g.setFont(new Font("Celestia Redux", Font.BOLD, 45));
		g.drawString("Retro Snake", 165 + 225, 240);
		g.setFont(new Font("楷体", Font.BOLD, 70));
		g.drawString("贪吃蛇大作战", 75 + 225, 180);
	}
	
	
}

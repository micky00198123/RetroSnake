package com.qklt.F_retro_snake;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;

public class RankPanel extends JPanel{

	private static final long serialVersionUID = 1L;
	
	private Snakes snake=new Snakes();
	private JButton jb=new JButton("返回菜单");
	private JButton jb1=new JButton("上传高分榜");
	private JButton jb2=new JButton("获取高分榜");
	private JButton jb3=new JButton("清空高分榜");
	
	public Boolean is_exit = false;
	
	public void startRank() {
		
		repaint();
		setJButton(jb);
		setJButton1(jb1);
		setJButton2(jb2);
		setJButton3(jb3);
		
		while(true) {
			try {
				Thread.sleep(300);
				if(is_exit==true) {
					break;
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	private void setJButton(JButton jb) {
	    SetPanel.buttonInit(jb);
		jb.setBounds(600, 400, 200, 50);//位置 大小
		this.add(jb);
		jb.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				is_exit = true;
			}
		});
	}
	
	private void setJButton1(JButton jb) {
		SetPanel.buttonInit(jb);
		jb.setBounds(600, 300, 200, 50);//位置 大小
		this.add(jb);
		jb.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				new Thread()//启动客户端
				{
					@Override
					public void run()
					{
						new Client().startClient();
					}
				}.start();
				
			}
		});
	}
	
	private void setJButton2(JButton jb) {
		SetPanel.buttonInit(jb);
		jb.setBounds(600, 200, 200, 50);//位置 大小
		this.add(jb);
		jb.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				new Thread()//启动服务器
				{
					@Override
					public void run()
					{
						new Server().startServer();
					}
				}.start();
			}
		});
	}
	
	private void setJButton3(JButton jb) {
		SetPanel.buttonInit(jb);
		jb.setBounds(600, 100, 200, 50);//位置 大小
		this.add(jb);
		jb.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				snake.list.clear();
				Snakes.saveScoreTofile(snake.list);
				repaint();
			}
		});
	}
 
	
	@Override
	public void paintComponent(Graphics g) {
		
		g.drawImage(Toolkit.getDefaultToolkit().getImage("贪吃蛇素材\\background_m.jpg")
			,0,0,625 + 450,645,this);
	
		Font font=new Font("黑体",Font.BOLD,42);
		g.setFont(font);
		String str1="top 10 :";
		g.drawString(str1,200, 50);
		
		Font font1=new Font("黑体",Font.PLAIN,26);
		g.setFont(font1);
		
		int len=10>snake.list.size()?snake.list.size():10;
		for(int i=0;i<len;i++) {
			String str="第"+Integer.toString(i+1)+"名: "+Integer.toString(snake.list.get(i));
			g.drawString(str, 300, 100+50*i);
		}
		for(int i=len;i<10;i++) {
			String str="第"+Integer.toString(i+1)+"名: "+Integer.toString(0);
			g.drawString(str, 300, 100+50*i);
		}
		
	}
	
	
	
}

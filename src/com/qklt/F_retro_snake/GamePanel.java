package com.qklt.F_retro_snake;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Toolkit;
import javax.swing.JPanel;

public class GamePanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private static final int BACKGROUND_WIDTH = 605;
	private static final int BACKGROUND_HEIGHT = 605;
	private static final int Frequency = 300;//刷新频率（毫秒）
	private Clock clock = new Clock();//计时器
	public Snakes snake = new Snakes();//所有蛇、食物及游戏操控均在此
	public int score;//游戏得分
	
	/*
	 * 地图范围是0-25单位(26*26范围)
	 * 墙的范围是 X:1/24 Y:1/24
	 * 蛇的移动范围实际只有X:2/23 Y:2/23(22*22范围)
	 */
	
	//开始游戏
	public void startGame()
	{
		//启动一个线程运行计时器
		new Thread()
		{
			@Override
			public void run()
			{
				while(true)
				{
    		       	try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
    		       	if(!snake.is_stop)
    		       	{
						clock.timing();
    		       	}
				}
			}
		}.start();
		
		//主线程运行游戏
		while (true)
		{
			if(!snake.is_stop)//如果未暂停
			{
				playGame();//游戏操控
				this.score = snake.score;//获取当前得分
			}
			if(snake.is_exit)//如果按esc退出
				break;
			
			repaint();
			
			try
			{
				Thread.sleep(Frequency);//延时间隔
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();//出错处理仅为简单打印信息
			}
		}
		
		/*
		 * 程序运行时循环顺序是
		 * 移动 -> 画图 -> 延时 -> 移动 ...
		 * 如果先画图再移动会因为延时操作而有延迟感
		 */
	}
	
	
	//游戏运行
	private void playGame()
	{
		try {
			snake.is_turn = false;
			snake.snake_move();//玩家移动
			snake.aisnake_move(snake.get_dir());//ai移动
			
			if(snake.isaidead || snake.isdead)//玩家或ai死亡
			{
				if(snake.isaidead)//如果ai死亡，得分+100
					score += 100;
				snake.list.add(score);
				snake.sortList();//高分榜排序
				Snakes.saveScoreTofile(snake.list);//写入数据
				snake.is_stop = true;
			}
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	
	@Override
	public void paintComponent(Graphics g)
	{
		//画计时器背景
		g.drawImage(Toolkit.getDefaultToolkit().getImage("贪吃蛇素材\\background_s.jpg")
				,BACKGROUND_WIDTH-30, -20, BACKGROUND_WIDTH, BACKGROUND_HEIGHT+55,this);
		//画游戏背景
		g.drawImage(Toolkit.getDefaultToolkit().getImage("贪吃蛇素材\\background_s.jpg")
				,-12,-19,643,660,this);
		
		snake.paintComponent(g);//画蛇、食物
		
		String hourStr=Integer.toString(clock.getHour());
    	String minuteStr=": "+String.format("%02d",clock.getMinute());
    	String secondStr=": "+String.format("%02d",clock.getSecond());
    	String  titleStr="游戏时间:"; 
    	String getScoreStr="得分:";
    	String scoreStr=Integer.toString(score);
    	
    	Font font=new Font("宋体",Font.BOLD,32);
        g.setFont(font);
    	g.setColor(Color.BLACK);
    	g.drawString(titleStr, BACKGROUND_WIDTH+50, 150);
    	g.drawString(hourStr, BACKGROUND_WIDTH+150, 200);
    	g.drawString(minuteStr, BACKGROUND_WIDTH+200,200);
    	g.drawString(secondStr, BACKGROUND_WIDTH+300, 200);

    	g.drawString(getScoreStr, BACKGROUND_WIDTH+50, 250);
    	g.drawString(scoreStr, BACKGROUND_WIDTH+150, 300);
    	
    	String str="游戏说明:";
    	String str1="通过键盘控制蛇的移动";
    	String str11="默认按键方向       上↑  下↓  左 ←  右→ ";
    	String str2="吃到食物身体会变长";
        String str3="撞到身体或墙壁游戏都会结束";
      
        Font font1=new Font("宋体",Font.PLAIN,16);
        g.setFont(font1);
        g.drawString(str, BACKGROUND_WIDTH+50, 450);
        g.drawString(str11, BACKGROUND_WIDTH+100, 500);
        g.drawString(str1, BACKGROUND_WIDTH+100,475);
        g.drawString(str2, BACKGROUND_WIDTH+100, 525);
        g.drawString(str3, BACKGROUND_WIDTH+100, 550);
    
    	g.dispose();
		
	}
	
	
	
	
	
}

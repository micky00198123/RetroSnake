package com.qklt.F_retro_snake;

import javax.swing.JFrame;


public class GameFrame {
	
	private static final int WINDOW_WIDTH = 625;
	private static final int WINDOW_HEIGHT = 660;
	private JFrame f = new JFrame("Retro Snaker");//新建一个窗口
	private GamePanel GP;//游戏面板
	private MenuPanel MP;//菜单面板
	private SetPanel SP;//设置面板
	private RankPanel RP;//排行榜前十 面板
	
	
	//初始化游戏框架(从此处启动游戏)
	public void init()
	{
		f.setSize(WINDOW_WIDTH+450, WINDOW_HEIGHT);//窗口大小
		f.setLocation(500, 100);//窗口位置
		f.setVisible(true);//窗口可见
		f.setResizable(false);//窗口大小不可调整
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//关闭窗口自动退出虚拟机
		startRetroSnake();
	}
	
	private void startRetroSnake()
	{
		MP = new MenuPanel();
		while(true)
		{
			MP.is_exit = false;
			f.add(MP);//加入菜单组件
			MP.startMenu();//开始运行菜单
			
			//退出菜单后
			f.remove(MP);//移除菜单组件
			f.requestFocus();//框架获取屏幕焦点
			switch(MP.menuChoose)
			{
			case 0:
				GP = new GamePanel();//新建游戏面板
				ListerSnakes LS = new ListerSnakes(GP.snake);//键盘控制
				
				f.add(GP);//加入游戏控制组件
				f.addKeyListener(LS);
				
				f.setVisible(true);//再次可见
				GP.startGame();//开始游戏
				
				//退出游戏后
				f.remove(GP);//移除游戏组件
				f.removeKeyListener(LS);//移除游戏键盘监听器
				GP = null;
				break;
			
			case 1:
				SP = new SetPanel();
				f.add(SP);
				f.setVisible(true);
				SP.startSet();
				
				f.remove(SP);
				SP = null;
				break;
				
			case 2:
				RP=new RankPanel();
				f.add(RP);
				f.setVisible(true);
				RP.startRank();
				
				f.remove(RP);
				RP=null;
				break;
			}
		}
		
		
	}
	
	
}

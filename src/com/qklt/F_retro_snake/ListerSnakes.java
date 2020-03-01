package com.qklt.F_retro_snake;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashMap;

public class ListerSnakes extends KeyAdapter{
	
	public Snakes snake;
	private HashMap<String,Integer> keycode = new HashMap<String,Integer>();//操作键
	  
	public ListerSnakes(Snakes snake) {
		this.snake = snake;
		keycode = SetPanel.readData(keycode);//读取保存的操作按键
	}
      
	@Override
	public void keyPressed(KeyEvent e)
	{
		int key = e.getKeyCode();//键值码
		if (snake.is_turn)//禁止一轮循环连续转向
			return;

		if (key == keycode.get("KEY_UP")) {
			if (snake.dir == 1)
				return;
			else
				snake.dir = 0;
			snake.is_turn = true;
		}

		if (key == keycode.get("KEY_DOWN")) {
			if (snake.dir == 0)
				return;
			else
				snake.dir = 1;
			snake.is_turn = true;
		}

		if (key == keycode.get("KEY_LEFT")) {
			if (snake.dir == 3)
				return;
			else
				snake.dir = 2;
			snake.is_turn = true;
		}

		if (key == keycode.get("KEY_RIGHT")) {
			if (snake.dir == 2)
				return;
			else
				snake.dir = 3;
			snake.is_turn = true;
		}

		if (key == KeyEvent.VK_SPACE)//暂停
		{
			if (this.snake.is_stop == false)
				this.snake.is_stop = true;
			else
				this.snake.is_stop = false;
			return;
		}

		if (key == KeyEvent.VK_ESCAPE)//是否退出
			this.snake.is_exit = true;
	}
}

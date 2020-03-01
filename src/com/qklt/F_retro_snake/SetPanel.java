package com.qklt.F_retro_snake;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import javax.swing.JButton;
import javax.swing.JPanel;

public class SetPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private HashMap<String,Integer> keycode = new HashMap<String,Integer>();//操作键
	private ReSetKey RSK = new ReSetKey();//用于重设键的键盘监听器
	private int setKey = -1;//要重设的键
	
	private JButton[] jb = new JButton[]
			{new JButton("设置上键"),new JButton("设置下键"),new JButton("设置左键"),
			 new JButton("设置右键"),new JButton("设置完成"),new JButton("恢复默认"),
			 new JButton("返回菜单")};
	
	public Boolean is_exit = false;
	
	private void initSet()
	{
		this.setLayout(null);
		for(int i = 0; i < 7; i ++)
		{
			buttonInit(jb[i]);
			this.add(jb[i]);
			jb[i].setBounds(190 + 225, 100 + i * 70, 200, 50);//位置 大小
		}
		for(int i = 0; i < 4; i ++)
			jb[i].addKeyListener(RSK);
			
		jb[0].addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				setKey = 0;
				jb[4].setForeground(Color.BLACK);
			}
		});
		
		jb[1].addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				setKey = 1;
				jb[4].setForeground(Color.BLACK);
			}
		});
		
		jb[2].addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				setKey = 2;
				jb[4].setForeground(Color.BLACK);
			}
		});
		
		jb[3].addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				setKey = 3;
				jb[4].setForeground(Color.BLACK);
			}
		});
		
		jb[4].setForeground(Color.GRAY);
		jb[4].addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				writeData(keycode);//写入键位
				setKey = -1;
				jb[4].setForeground(Color.GRAY);
			}
		});
		
		jb[5].addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				setKey = -1;
				keycode = restoreDefaultKey(keycode);
				writeData(keycode);
				jb[4].setForeground(Color.GRAY);
			}
		});
		
		jb[6].addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				is_exit = true;
			}
		});
		
	}
	
	public static void buttonInit(JButton jb)//初始化按钮
	{
		jb.setBorder(null);//边框透明
		jb.setContentAreaFilled(false);//按键透明
		jb.setFont(new Font("楷体",Font.BOLD, 30));//字体
		jb.setForeground(Color.BLACK);//字体颜色
	}
	
	//开始设置
	public void startSet()
	{
		initSet();
		repaint();
		keycode = readData(keycode);//获取键位
		while (true)//无限循环
		{
			try
			{
				if(is_exit)//是否返回菜单
					break;
				Thread.sleep(300);//延时间隔
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();//出错处理仅为简单打印信息
			}
		}
	}
	
	//恢复默认按键
	public static HashMap<String,Integer> restoreDefaultKey(HashMap<String,Integer> kc)
	{
		kc.put("KEY_LEFT", 37);
		kc.put("KEY_UP", 38);
		kc.put("KEY_RIGHT", 39);
		kc.put("KEY_DOWN", 40);
		return kc;
	}
	
	//下面读写键位的方法为静态以供其他类调用
	public static HashMap<String,Integer> readData(HashMap<String,Integer> kc)
	{
		FileReader reader = null;
		BufferedReader br = null;
		String line;
		try 
		{
			//注意按需修改文件存储位置
			reader = new FileReader("贪吃蛇素材\\key.txt");
			br = new BufferedReader(reader);//按行读取
			line = br.readLine();
			kc.put("KEY_LEFT", Integer.parseInt(line));
			line = br.readLine();
			kc.put("KEY_UP", Integer.parseInt(line));
			line = br.readLine();
			kc.put("KEY_RIGHT", Integer.parseInt(line));
			line = br.readLine();
			kc.put("KEY_DOWN", Integer.parseInt(line));
			return kc;
		}
		catch (Exception e)
		{
			System.out.println("键位读取错误！");
			kc = restoreDefaultKey(kc);//若按键读取错误则恢复默认
			writeData(kc);
			return kc;
		}
		finally
		{
			try//如果打开文件失败，这里会由于开头的null而出现空指针异常并卡死，如果是读取文件内容失败则没有问题
			{
				if(reader != null)
					reader.close();
				if(br != null)
					br.close();
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	public static void writeData(HashMap<String,Integer> keycode)
	{
		FileWriter writer = null;
		BufferedWriter bw = null;
		File file = null;
		try 
		{
			file = new File("贪吃蛇素材\\key.txt");
			if(!file.exists())
				file.getParentFile().mkdirs();//如果文件夹不存在，先创建文件夹
			writer = new FileWriter(file.getAbsolutePath());
			bw = new BufferedWriter(writer);
			
			bw.write(keycode.get("KEY_LEFT") + "\r\n");
			bw.write(keycode.get("KEY_UP") + "\r\n");
			bw.write(keycode.get("KEY_RIGHT") + "\r\n");
			bw.write(keycode.get("KEY_DOWN") + "\r\n");
			bw.flush();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if(writer != null)
					writer.close();
				if(bw != null)
					bw.close();
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}


	public class ReSetKey extends KeyAdapter {
		@Override
		public void keyReleased(KeyEvent e)
		{
			int key = e.getKeyCode();
			switch (setKey)
			{
			case 0:
				keycode.put("KEY_UP", key);
				break;
			case 1:
				keycode.put("KEY_DOWN", key);
				break;
			case 2:
				keycode.put("KEY_LEFT", key);
				break;
			case 3:
				keycode.put("KEY_RIGHT", key);
				break;
			}
		}
	}
	
	@Override
	public void paintComponent(Graphics g)
	{
		g.drawImage(Toolkit.getDefaultToolkit().getImage("贪吃蛇素材\\background_m.jpg")
				,0,0,625 + 450,645,this);
	}
	
	
}

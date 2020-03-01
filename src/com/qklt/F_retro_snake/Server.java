package com.qklt.F_retro_snake;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {

	public void startServer()
	{
		ServerSocket serS = null;
		Socket S = null;
		BufferedReader br = null;
		String str = null;
		ArrayList<Integer> list = new ArrayList<Integer>();
		try
		{
			serS = new ServerSocket(23333);
			S = serS.accept();//若服务器未连接客户端，线程在此阻塞
			System.out.println("服务器已连接!");
			br = new BufferedReader(new InputStreamReader(S.getInputStream()));
			
			while(true)
			{
				str = br.readLine();//按行读取
				if(Integer.parseInt(str) < 0)//默认高分榜无负数，若接收到小于零的数字则退出
					break;
				list.add(Integer.parseInt(str));
			}
			Snakes.saveScoreTofile(list);//写入数据
			System.out.println("数据接收完成!");
			
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try {
				if(serS != null)
					serS.close();
				if(S != null)
					S.close();
				if(br != null)
					br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		
		
		
	}
	
	
	
	
	
	
}

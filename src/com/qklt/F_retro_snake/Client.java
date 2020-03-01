package com.qklt.F_retro_snake;

import java.io.IOException;
import java.io.PrintStream;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;

//客户端，上传数据
public class Client{

	public void startClient()
	{
		Socket S = null;
		PrintStream ps = null;
		Scanner sc = null;
		ArrayList<Integer> list = new ArrayList<Integer>();//高分数据存放位置
		try 
		{
			list = Snakes.loadScoreFromFile(list);
			S = new Socket("127.0.0.1",23333);//注意调整ip地址
			ps = new PrintStream(S.getOutputStream());
			
			for(int i = 0; i < list.size(); i ++)
				ps.println(list.get(i));
			ps.println(-233);//结束标志，服务器接收到小于零的数字会自动退出
			System.out.println("数据上传完成!");
			
		} catch(ConnectException e){
			System.out.println("服务器连接失败！");//若客户端长时间未连接
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally
		{
			try {
				if(S != null)
					S.close();
				if(ps != null)
					ps.close();
				if(sc != null)
					sc.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
	
	
}

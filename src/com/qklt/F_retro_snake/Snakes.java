package com.qklt.F_retro_snake;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

//定义一个node类,用与搜索
class node{ 
	int x;
	int y;
	int step;
	node(int x,int y,int step){
		this.x=x;
		this.y=y;
		this.step=step;
	}
}

public class Snakes extends JPanel{
	
	private static final long serialVersionUID = 4742295662632292440L;
	//图片信息
	private ImageIcon body=new ImageIcon("贪吃蛇素材\\body.png");   //1
	private ImageIcon food=new ImageIcon("贪吃蛇素材\\food.png");   //2
	private ImageIcon up=new ImageIcon("贪吃蛇素材\\up.png");       //3
	private ImageIcon down=new ImageIcon("贪吃蛇素材\\down.png");   //4
	private ImageIcon left=new ImageIcon("贪吃蛇素材\\left.png");   //5 
	private ImageIcon right=new ImageIcon("贪吃蛇素材\\right.png"); //6
	// 窗口定义规格大小
	private static final int WIDTH = 23;
	private static final int HEIGHT = 23;
	// 地图
	private int[][] map = new int[WIDTH][HEIGHT];
	// 食物位置
	private int foodx, foody;
	// 蛇最大长度
	private final int SNAKE_MAX_LENGTH = 400;
	// 用于数组标记
	private final int info = 500;
	// 蛇方向移动
	private int[] dx = { -1, 1, 0, 0 }; // 上下左右
	private int[] dy = { 0, 0, -1, 1 };
	// ai蛇身和头位置数组
	private int[] aisnakex = new int[SNAKE_MAX_LENGTH];
	private int[] aisnakey = new int[SNAKE_MAX_LENGTH];
	// 人工蛇身和头位置数组
	private int[] snakex = new int[SNAKE_MAX_LENGTH];
	private int[] snakey = new int[SNAKE_MAX_LENGTH];
	// 人工蛇长度
	private int len;
	// 人工蛇得分
	public int score;
	// 历史最高分
	public int maxScore;
	// 历史记录
	public ArrayList<Integer> list = new ArrayList<Integer>();// 分数排行榜，只有当游戏结束时才会记录分数lastScore
	// ai蛇长度
	private int ailen;
	// 人工蛇移动方向
	public int dir;
	// 人工蛇是否死亡
	public boolean isdead;
	// ai蛇是否死亡
	public boolean isaidead;
	// 用来存路径ai蛇
	private int[][] step_h2f = new int[WIDTH][HEIGHT]; // 头到食物
	private int[][] step_h2t = new int[WIDTH][HEIGHT]; // 头到尾
	private int[][] step_f2t = new int[WIDTH][HEIGHT]; // 食物到尾
	// 用于随机产生食物位置
	private Random r = new Random();
	    
	/*
	 * 按空格为暂停、按esc为退出游戏回到主菜单
     * 蛇死亡时自动暂停游戏并弹出game over
     * 需要手动按esc返回菜单开始下一轮游戏
     */
	// 是否暂停
	public Boolean is_stop = false;
	// 是否结束游戏
	public Boolean is_exit = false;
	// 是否转向
	public Boolean is_turn = false;
	    
	    
	// 构造器初始化人工蛇、ai蛇和地图
	public Snakes() {
		len = 3;
		score = 0;
		ailen = 3;
		dir = 3;
		isdead = false;
		isaidead = false;
		maxScore = 0;
		list = loadScoreFromFile(this.list);
		if (list.size() != 0)
			maxScore = list.get(0);

		for (int i = 0; i < WIDTH; i++) {
			for (int j = 0; j < HEIGHT; j++) {
				map[i][j] = 0;
			}
		}
		
		// 人工蛇数组:头存储在下标为0的位置(有利于吃到食物后立马身体增长)
		// ai蛇数组：头存储在下标为ailen-1的位置
		snakex[0] = 3;
		snakey[0] = 3;
		snakex[1] = 3;
		snakey[1] = 2;
		snakex[2] = 3;
		snakey[2] = 1;
		aisnakex[0] = 3;
		aisnakey[0] = 18;
		aisnakex[1] = 3;
		aisnakey[1] = 17;
		aisnakex[2] = 3;
		aisnakey[2] = 16;
		map[3][1] = 1;
		map[3][2] = 1;
		map[3][3] = 6;
		map[3][18] = 1;
		map[3][17] = 1;
		map[3][16] = 5;
		while (map[foodx = r.nextInt(20) % 20][foody = r.nextInt(20) % 20] != 0)
			;//这里无操作的循环用于随机生成食物位置并防止生成时产生碰撞
		map[foodx][foody] = 2;
	}
	
	// 画蛇和食物
	@Override
	public void paintComponent(Graphics g) {
		int n = 25;//用于调整显示位置
		// 画人工蛇
		// 由于坐标系的x,y与数组的x,y不一致,刚好相反,使用y数组表示x轴,x数组表示y轴
		for (int i = 1; i < len; i++)
			body.paintIcon(this, g, snakey[i] * 25 + n, snakex[i] * 25 + n);
		if (map[snakex[0]][snakey[0]] == 6)
			right.paintIcon(this, g, snakey[0] * 25 + n, snakex[0] * 25 + n);
		else if (map[snakex[0]][snakey[0]] == 5)
			left.paintIcon(this, g, snakey[0] * 25 + n, snakex[0] * 25 + n);
		else if (map[snakex[0]][snakey[0]] == 4)
			down.paintIcon(this, g, snakey[0] * 25 + n, snakex[0] * 25 + n);
		else if (map[snakex[0]][snakey[0]] == 3)
			up.paintIcon(this, g, snakey[0] * 25 + n, snakex[0] * 25 + n);
		// 画ai蛇
		for (int i = 0; i < ailen - 1; i++) {
			body.paintIcon(this, g, aisnakey[i] * 25 + n, aisnakex[i] * 25 + n);
		}
		if (map[aisnakex[ailen - 1]][aisnakey[ailen - 1]] == 6)
			right.paintIcon(this, g, aisnakey[ailen - 1] * 25 + n,
					aisnakex[ailen - 1] * 25 + n);
		else if (map[aisnakex[ailen - 1]][aisnakey[ailen - 1]] == 5)
			left.paintIcon(this, g, aisnakey[ailen - 1] * 25 + n,
					aisnakex[ailen - 1] * 25 + n);
		else if (map[aisnakex[ailen - 1]][aisnakey[ailen - 1]] == 4)
			down.paintIcon(this, g, aisnakey[ailen - 1] * 25 + n,
					aisnakex[ailen - 1] * 25 + n);
		else if (map[aisnakex[ailen - 1]][aisnakey[ailen - 1]] == 3)
			up.paintIcon(this, g, aisnakey[ailen - 1] * 25 + n,
					aisnakex[ailen - 1] * 25 + n);
		// 画食物
		food.paintIcon(this, g, foody * 25 + n, foodx * 25 + n);
		
		//游戏是否结束
		if(isdead)
		{
			 g.setColor(Color.WHITE);
			 g.setFont(new Font("宋体",Font.BOLD,30));
		     g.drawString("游戏失败",230,300);
		}  
        //ai蛇死亡
        if(isaidead)
        {
        	 g.setColor(Color.WHITE);
			 g.setFont(new Font("宋体",Font.BOLD,30));
		     g.drawString("游戏胜利",250,250);
		}
    }
	
	
	// 该位置是否为食物
	private boolean is_food(int x, int y) {
		return foodx == x && foody == y;
	}

	// 该位置是否为空
	private boolean is_space(int x, int y) {
		return map[x][y] == 0;
	}
	
	
	/**
	 * 人工蛇设计
	 */
	public void snake_move() {
		// 移动身体
		map[snakex[len - 1]][snakey[len - 1]] = 0;
		map[snakex[0]][snakey[0]] = 1;
		// 用下标len存尾巴坐标,如果吃到食物,有利于蛇身增长而没有延迟.
		for (int i = len; i > 0; i--) {
			snakex[i] = snakex[i - 1];
			snakey[i] = snakey[i - 1];
		}
		int x = snakex[0] + dx[dir];
		int y = snakey[0] + dy[dir]; // 【0为上，1为下，2为左，3为右】
		// 处理数组越界的情况
		if (x < 0 || x >= WIDTH || y < 0 || y >= HEIGHT) {
			isdead = true;
			return;
		}
		// 判断蛇头是否撞到ai蛇or蛇自身(通过如果不是食物和空格)
		if (!is_space(x, y) && !is_food(x, y)) {
			isdead = true;
			return;
		} else {
			snakex[0] = x;
			snakey[0] = y;
		}
		if (dir == 0)
			map[snakex[0]][snakey[0]] = 3;
		if (dir == 1)
			map[snakex[0]][snakey[0]] = 4;
		if (dir == 2)
			map[snakex[0]][snakey[0]] = 5;
		if (dir == 3)
			map[snakex[0]][snakey[0]] = 6;
		// 判断是否吃到食物
		if (is_food(snakex[0], snakey[0])) {
			len++;
			score++;
			while (map[foodx = r.nextInt(20) % 20][foody = r.nextInt(20) % 20] != 0)
				;
			map[foodx][foody] = 2;
		}
	}
    	    
	/**
	 * AI蛇移动设计
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	// 蛇向dir方向移动 【0为上，1为下，2为左，3为右】
	public int aisnake_move(int aidir) {
		int x = aisnakex[ailen - 1] + dx[aidir];
		int y = aisnakey[ailen - 1] + dy[aidir];
		// ai蛇出界
		if (x < 0 || y < 0 || x >= WIDTH || y >= HEIGHT)
			return 1;
		// 吃到食物
		if (map[x][y] == 2) {
			int hdir = map[aisnakex[ailen - 1]][aisnakey[ailen - 1]];
			map[aisnakex[ailen - 1]][aisnakey[ailen - 1]] = 1; // 头部为身体
			map[x][y] = hdir;
			aisnakex[ailen] = x;
			aisnakey[ailen] = y;
			ailen++;
			// 放食物
			while (map[foodx = r.nextInt(20) % 20][foody = r.nextInt(20) % 20] != 0)
				;
			map[foodx][foody] = 2;
			return 0;
		}
		// ai蛇撞到人工蛇了
		if (map[x][y] != 0)
			return 1;
		// ai蛇四周是否有路
		int done = 0;
		for (int i = 0; i < 4; i++) {
			if (x + dx[i] < 0 || x + dx[i] >= WIDTH || y + dy[i] < 0
					|| y + dy[i] >= HEIGHT) {
				done++;
				continue;
			}
			if (map[x + dx[i]][y + dy[i]] != 0
					&& map[x + dx[i]][y + dy[i]] != 2)
				done++;
		}
		if (done == 4) {
			isaidead = true;
			return 1;
		}
		// 移动身体
		map[aisnakex[0]][aisnakey[0]] = 0;
		for (int i = 0; i < ailen - 1; i++) {
			aisnakex[i] = aisnakex[i + 1];
			aisnakey[i] = aisnakey[i + 1];
		}
		map[aisnakex[ailen - 1]][aisnakey[ailen - 1]] = 1;
		aisnakex[ailen - 1] += dx[aidir];
		aisnakey[ailen - 1] += dy[aidir];
		if (aidir == 0)
			map[aisnakex[ailen - 1]][aisnakey[ailen - 1]] = 3;
		else if (aidir == 1)
			map[aisnakex[ailen - 1]][aisnakey[ailen - 1]] = 4;
		else if (aidir == 2)
			map[aisnakex[ailen - 1]][aisnakey[ailen - 1]] = 5;
		else if (aidir == 3)
			map[aisnakex[ailen - 1]][aisnakey[ailen - 1]] = 6;
		return 0;
	}
	
	// 获取移动方向
	public int get_dir() throws InterruptedException {
		bfs(foodx, foody, aisnakex[ailen - 1], aisnakey[ailen - 1], step_h2f); // 食物到头
		bfs(aisnakex[0], aisnakey[0], foodx, foody, step_f2t); // 尾到食物
		bfs(aisnakex[0], aisnakey[0], aisnakex[ailen - 1], aisnakey[ailen - 1],
				step_h2t); // 尾到头
		if (step_h2t[aisnakex[ailen - 1]][aisnakey[ailen - 1]] == info)
			return get_food(); // 如果头到尾巴没有路则找食物 ,如果有路它就会小于info
		else {
			if (ailen > 250)
				return find_farthest_way(); // 当长度大于250，采取保守路线追尾巴走，
			int minb = 3;
			int[] b = { info, info, info, info };
			for (int i = 0; i < 4; i++) {
				int tx = aisnakex[ailen - 1] + dx[i];
				int ty = aisnakey[ailen - 1] + dy[i];
				if (tx < 0 || ty < 0 || tx >= WIDTH || ty >= HEIGHT)
					continue;
				if (is_space(tx, ty) && step_h2t[tx][ty] < info)
					b[i] = step_h2f[tx][ty]; // 如果能找到去尾巴的路，那么就去吃食物
				if (is_food(tx, ty) && step_f2t[tx][ty] < info
						&& step_f2t[tx][ty] > 1)
					b[i] = 0; // 如果它是食物，且食物到尾巴有路，就去吃食物
				if (b[i] < b[minb])
					minb = i;
			}
			if (b[minb] == info)
				return find_farthest_way(); // 找不到安全的吃食物路径，先跟尾巴走
			else
				return minb; // 已找到最短路径
		}
	}
	
	private int find_farthest_way() {
		int maxb = 3;
		int[] b = { -1, -1, -1, -1 };
		for (int i = 0; i < 4; i++) {
			int tx = aisnakex[ailen - 1] + dx[i];
			int ty = aisnakey[ailen - 1] + dy[i];
			if (tx < 0 || ty < 0 || tx >= WIDTH || ty >= HEIGHT)
				continue;
			if (is_space(tx, ty))
				b[i] = step_h2t[tx][ty];
			if ((is_food(tx, ty) || step_h2f[tx][ty] < info)
					&& step_f2t[tx][ty] < info)
				b[i] = step_f2t[tx][ty];
			if (b[i] > b[maxb])
				maxb = i;
		}
		return maxb;
	}
	    
	private int get_food() {
		int maxb = 3;
		int[] b = { -1, -1, -1, -1 };
		for (int i = 0; i < 4; i++) {
			int tx = aisnakex[ailen - 1] + dx[i];
			int ty = aisnakey[ailen - 1] + dy[i];
			if (tx < 0 || ty < 0 || tx >= WIDTH || ty >= HEIGHT)
				continue;
			if (is_food(tx, ty) && ailen == SNAKE_MAX_LENGTH - 1)
				return i;
			if (is_space(tx, ty)
					|| (is_food(tx, ty) && step_f2t[tx][ty] < info))
				b[i] = step_h2f[tx][ty]; // 这一步为空or它为食物且食物到尾巴有路，因为头跟着尾巴走肯定不会死
			if (b[i] > b[maxb] && b[i] < info)
				maxb = i; // 选择最大的走,最短路径
		}
		return maxb;
	}
	    
	private void bfs(int sx, int sy, int ex, int ey, int[][] step)
			throws InterruptedException {
		int done = 0;
		// 标记数组,用来标记(虚拟蛇是否走过)
		int[][] vit = new int[WIDTH][HEIGHT];
		for (int i = 0; i < WIDTH; i++) {
			for (int j = 0; j < HEIGHT; j++) {
				step[i][j] = info;
				vit[i][j] = 0;
			}
		}
		vit[sx][sy] = 1;
		// 定义一个队列设置大小为SNAKE_MAX_LENDTH
		BlockingQueue<node> que = new ArrayBlockingQueue<node>(SNAKE_MAX_LENGTH);
		que.put(new node(sx, sy, 0));
		step[sx][sy] = 0;
		while (que.size() != 0) {
			node exa = que.poll(); // take()也可以
			for (int i = 0; i < 4; i++) {
				int tx = exa.x + dx[i];
				int ty = exa.y + dy[i];
				if (tx >= 0 && ty >= 0 && tx < WIDTH && ty < HEIGHT
						&& vit[tx][ty] == 0 && is_space(tx, ty)) {
					vit[tx][ty] = 1;
					int stp = exa.step + 1;
					step[tx][ty] = stp;
					que.put(new node(tx, ty, stp));
				}
				if (tx == ex && ty == ey)
					done++; // 一个方向能到达就加一
				if (done == 4)
					return; // 如果四个方向都能到达
			}
		}
		for (int i = 0; i < 4; i++) {
			int tx = ex + dx[i];
			int ty = ey + dy[i];
			if (tx < 0 || ty < 0 || tx >= WIDTH || ty >= HEIGHT)
				continue;
			if (step[tx][ty] < (step[ex][ey] - 1)) {
				step[ex][ey] = step[tx][ty] + 1; // 得到目的地四周,最小的步数到达目的地的位置，并且将目的地标记步数更新。这样可以尽快到达目的地
			}
		}
	}
	    
	
	// 分数由高至低排序
	public boolean sortList() {
		if (list.size() != 0) {
			Collections.sort(list);
			Collections.reverse(list);
			maxScore = list.get(0);
			return true;
		}
		return false;
	}
	
	// 载入高分榜
	public static ArrayList<Integer> loadScoreFromFile(ArrayList<Integer> list)
	{
		FileReader reader = null;
		BufferedReader br = null;
		String line;
		try
		{
			// 注意按需修改文件存储位置
			reader = new FileReader("贪吃蛇素材\\snakescore.txt");
			br = new BufferedReader(reader);// 按行读取
			while ((line = br.readLine()) != null)
				list.add(Integer.parseInt(line));// 读一行list就加入一条记录（默认每行只有一条记录）
			return list;
		} catch (Exception e) {
			System.out.println("高分榜读取错误！");
			saveScoreTofile(list);// 读取错误说明本地储存有误，重新存储一遍
			return list;
		} finally {
			try// 如果打开文件失败，这里会由于开头的null而出现空指针异常并卡死，如果是读取文件内容失败则没有问题
			{
				if (reader != null)
					reader.close();
				if (br != null)
					br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	// 保存高分榜
	public static void saveScoreTofile(ArrayList<Integer> list) {
		FileWriter writer = null;
		BufferedWriter bw = null;
		File file = null;
		try {
			file = new File("贪吃蛇素材\\snakescore.txt");
			if (!file.exists())
				file.getParentFile().mkdirs();// 如果文件夹不存在，先创建文件夹
			writer = new FileWriter(file.getAbsolutePath());// 获取file的绝对路径
			bw = new BufferedWriter(writer);// bw是缓存输出流
			for (int i = 0; i < list.size(); i++)// 先把要输出的数据全部缓存进bw
				bw.write(list.get(i) + "\r\n");// \r\n是换行符
			bw.flush();// 将bw中缓存的数据一次性输出到文件中
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (writer != null)
					writer.close();
				if (bw != null)
					bw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
		
	    
}

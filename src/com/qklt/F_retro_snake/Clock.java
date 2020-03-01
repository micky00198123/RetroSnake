package com.qklt.F_retro_snake;

public class Clock {
	
		private int second=0;
		private int minute=0;
		private int hour=0;
		
	    //进位
		private static final int limitSecond = 60;
		private static final int limitMinute = 60;
	    private boolean addM = false;//是否加分钟
	    private boolean addH = false;//是否加小时

	    
	    //对外的计时方法
	    public void timing()
	    {
	    	increaseSecond();
			increaseMinute();
			increaseHour();
	    }

	    
	    public int getSecond() {
			return second;
		}

		public int getMinute() {
			return minute;
		}

		public int getHour() {
			return hour;
		}

		private void increaseSecond() {
	    	second++;
	    	if(second==limitSecond) {
	    		second=0;
	    		addM = true;
	    	}
	    }

	    private void increaseMinute() {
	    	if(addM) {
	    		minute++;
	    		addM = false;
	    	}
	    	if(minute==limitMinute) {
	    		minute = 0;
	    		addH = true;
	    	}
	    }

	    private void increaseHour() {
	    	if(addH) {
	    		hour++;
	    		addH = false;
	    	}
	    }

	    
}

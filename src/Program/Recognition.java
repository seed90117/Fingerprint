package Program;

import IO.DrawPanel;
import Value.data;
import Value.value;

public class Recognition {

	static int[] SiteD8x = {-1,0,1,1,1,0,-1,-1};
	static int[] SiteD8y = {1,1,1,0,-1,-1,-1,0};
	
	public static void recogntition()
	{
		for(int x=1;x<value.imgwidth-1;x++)
		{
			for(int y=1;y<value.imghight-1;y++)
			{
				if(Main.finger[x][y].r==0)
				{
					if(IsEnd(x,y))
					{
						color(x,y,"r");
					}
					else if(IsFork(x,y))
					{
						color(x,y,"g");
					}
					else
					{
						ImageProcessing.draw(x, y);
					}
				}
			}
		}
		getsingular(-1);
		getsingular(1);
	}
	
	//*****提取端點*****//
	public static boolean IsEnd(int x,int y)
	{
		int sum = 0;
		for(int i=0;i<8;i++)
		{
			sum += Math.abs(Main.finger[x+SiteD8x[(i+1)%8]][y+SiteD8y[(i+1)%8]].r-Main.finger[x+SiteD8x[i]][y+SiteD8y[i]].r);
		}
		if(sum == 255*2)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	//*****提取叉點*****//
	public static boolean IsFork(int x,int y)
	{
		int sum = 0;
		for(int i=0;i<8;i++)
		{
			sum += Math.abs(Main.finger[x+SiteD8x[(i+1)%8]][y+SiteD8y[(i+1)%8]].r-Main.finger[x+SiteD8x[i]][y+SiteD8y[i]].r);
		}
		if(sum == 255*6)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	//*****尋找奇異點*****//
	public static void getsingular(int flag)
	{
		int[][] D3 = {{-1,-1},{-1,0},{-1,1},{0,1},{1,1},{1,0},{1,-1},{0,-1}};
		int[][] D5 = {{-2,-1},{-2,0},{-2,1},{-1,2},{0,2},{1,2},{2,1},{2,0},{2,-1},{1,-2},{0,-2},{-1,-2}};
		int[][] D7 = {{-3,-3}, {-3,-2}, {-3,-1}, {-3,0}, {-3,1}, {-3,2}, {-3,3}, {-2,3}, {-1,3}, {0,3}, {1,3}, {2,3}, 
				{3,3}, {3,2}, {3,1}, {3,0},{3,-1}, {3,-2}, {3,-3}, {2,-3}, {1,-3}, {0,-3}, {-1,-3}, {-2,-3}};
		boolean fg = false;
		int sum = 0,sum2 =0,num = 0;
		int a1,a2,d,v=0;
		double dis = 0;
		data[] singarr = new data[30];
		for(int i=0;i<singarr.length;i++)
		{
			singarr[i] = new data();
			singarr[i].x = 0;
			singarr[i].y = 0;
		}
		for(int x=3;x<value.imgwidth-3;x++)
		{
			for(int y=3;y<value.imghight-3;y++)
			{
				if(Main.finger[x][y].r == 255)
				{
					continue;
				}
				for(int i=0;i<24;i++)
				{
					if(Main.finger[x+D7[i][0]][y+D7[i][1]].ycbcr == 255)
					{
						fg = true;
						break;
					}
				}
				if(fg)
				{
					continue;
				}
				for(int i=0;i<8;i++)
				{
					a1 = (int) ImageProcessing.angle[x+D3[i][0]][y+D3[i][1]]/24;
					a2 = (int) ImageProcessing.angle[x+D3[(i+1)%8][0]][y+D3[(i+1)%8][1]]/24;
					d = orichange(a1,a2,flag);
					if(Math.abs(d) > 5)
					{
						break;
					}
					sum += d;
				}
				for(int i=0;i<12;i++)
				{
					a1 = (int) ImageProcessing.angle[x+D5[i][0]][y+D5[i][1]]/24;
					a2 = (int) ImageProcessing.angle[x+D5[(i+1)%12][0]][y+D5[(i+1)%12][1]]/24;
					d = orichange(a1,a2,flag);
					if(Math.abs(d) > 5)
					{
						break;
					}
					sum2 += d;
				}
				if(flag == -1)
				{
					v = -10;
				}
				else if(flag == 1)
				{
					v = 10;
				}
				if(sum2 == v && sum == v)
				{
					boolean bf = false;
					for(int i=0;i<num;i++)
					{
						dis = Math.sqrt((double)(((x-singarr[i].x)*(x-singarr[i].x))+((y-singarr[i].y)*(y-singarr[i].y))));
						if(dis < 4)
						{
							singarr[i].x = (singarr[i].x+x)/2.0;
							singarr[i].y = (singarr[i].y+y)/2.0;
							bf = true;
							break;
						}
					}
					if(!bf)
					{
						singarr[num].x = x;
						singarr[num].y = y;
						num++;
						if(num >= singarr.length)
						{
							break;
						}
					}
				}
			}
		}
		for(int i=0;i<num;i++)
		{
			color((int)singarr[i].x,(int)singarr[i].y,"b");
		}
	}
	
	//*****計算方向場變化******//
	public static int orichange(int a1,int a2,int type)
	{
		int d = a2-a1;
		if(type == 0)
		{
			if(d<0)
			{
				d += 10;
			}
		}
		if(type == 1)
		{
			if(d>0)
			{
				d -= 10;
			}
		}
		return d;
	}
	
	public static void color(int x,int y,String c)
	{
		if(c == "r")
		{
			DrawPanel.drawcolor(x,y,255,0,0,3);
		}
		if(c == "g")
		{
			DrawPanel.drawcolor(x,y,0,255,0,3);
		}
		if(c == "b")
		{
			DrawPanel.drawcolor(x,y,0,0,255,3);
		}
	}
}

package Program;

import IO.DrawPanel;
import IO.LoadFile;
import Value.rgbdata;
import Value.temp;
import Value.value;

public class ImageProcessing {

	public static int[][] Gx;
	public static int[][] Gy;
	public static int[][] G;
	public static int[][] g;
	public static long[][] angle;
	public static double[] kernel;
	static temp plast;
	static temp pnow;
	static temp pnext;
	static int windowsize = 25;//�ƾڪ���
	static int[][] point;
	static int[][][]  g_DDSite = {
			{{-3, 0},{-2, 0},{-1, 0},{0, 0},{1, 0},{2, 0},{3, 0}},
			{{-3,-1},{-2,-1},{-1, 0},{0, 0},{1, 0},{2, 1},{3, 1}},
			{{-3,-2},{-2,-1},{-1,-1},{0, 0},{1, 1},{2, 1},{3, 2}},
			{{-3,-3},{-2,-2},{-1,-1},{0, 0},{1, 1},{2, 2},{3, 3}},
			{{-2,-3},{-1,-2},{-1,-1},{0, 0},{1, 1},{1, 2},{2, 3}},
			{{-1,-3},{-1,-2},{0,-1},{0, 0},{0, 1},{1, 2},{1, 3}},
			{{0,-3},{0,-2},{0,-1},{0, 0},{0, 1},{0, 2},{0, 3}},
			{{-1, 3},{-1, 2},{0, 1},{0, 0},{0,-1},{1,-2},{1,-3}},
			{{-2, 3},{-1, 2},{-1, 1},{0, 0},{1,-1},{1,-2},{2,-3}},
			{{-3, 3},{-2, 2},{-1, 1},{0, 0},{1,-1},{2,-2},{3,-3}},
			{{-3, 2},{-2, 1},{-1, 1},{0, 0},{1,-1},{2,-1},{3,-2}},
			{{-3, 1},{-2, 1},{-1, 0},{0, 0},{1, 0},{2,-1},{3,-1}}};
	
	
	//*****�Ƕ���*****//OK
	public static void gray()
	{
		for(int x=0; x<value.imgwidth;x++)
		{
			for(int y=0; y<value.imghight;y++)
			{
				Main.finger[x][y] = new rgbdata();
				Main.finger[x][y].r = (LoadFile.image.getRGB(x, y)& 0xFF0000) >> 16;
				Main.finger[x][y].g = (LoadFile.image.getRGB(x, y)& 0xFF00) >> 8;
				Main.finger[x][y].b = (LoadFile.image.getRGB(x, y)& 0xFF);
				Main.finger[x][y].ycbcr = (int)((0.299*Main.finger[x][y].r)+(0.587*Main.finger[x][y].g)+(0.114*Main.finger[x][y].b));
				rgbset(x,y,Main.finger[x][y].ycbcr);
				draw(x,y);
			}
		}
	}
	//*****�Ϲ���*****//
	public static void getorientmap()
	{
		int vx,vy,lvx,lvy;
		double fang;
		long ang;
		angle = new long[value.imgwidth][value.imghight];
		G = new int[value.imgwidth][value.imghight];
		for(int x=0;x<value.imgwidth;x++)
		{
			for(int y=0;y<value.imghight;y++)
			{
				vx = vy = lvx = lvy = 0;
				if(x == 0 || x == value.imgwidth-1 || y == 0 || y == value.imghight-1)
				{
					G[x][y] = 0;
				}
				else
				{
					vx = Main.finger[x+1][y-1].ycbcr+(2*Main.finger[x+1][y].ycbcr)+Main.finger[x+1][y+1].ycbcr
							-Main.finger[x-1][y-1].ycbcr-(2*Main.finger[x-1][y].ycbcr)-Main.finger[x-1][y+1].ycbcr;
					vy = Main.finger[x-1][y+1].ycbcr+(2*Main.finger[x][y-1].ycbcr)+Main.finger[x+1][y+1].ycbcr
							-Main.finger[x-1][y-1].ycbcr-(2*Main.finger[x][y-1].ycbcr)-Main.finger[x+1][y-1].ycbcr;
					G[x][y] = Math.abs(vx)+Math.abs(vy);
					lvx += vx*vy*2;
					lvy += (vx*vx)-(vy*vy);
					
					fang = Math.atan2(lvy, lvx);
					if(fang<0)
					{
						fang += 2*value.PI;
					}
					fang = (fang*value.EPI*0.5)+0.5;
					ang = (long)fang;
					ang -= 135;
					if(ang <=0)
					{
						ang += 180;
					}
					ang = 180 - ang;
					angle[x][y] = ang;
				}
				if(G[x][y] > 255)
				{
					G[x][y] = 255;
				}
			}
		}
	}
	
	//*****��V���P��׭p��*****//
	public static void sobel()
	{
		Gx = new int[value.imgwidth][value.imghight];
		Gy = new int[value.imgwidth][value.imghight];
		G = new int[value.imgwidth][value.imghight];
		angle = new long[value.imgwidth][value.imghight];
		double ang = 0;
		for(int x=0; x<value.imgwidth;x++)
		{
			for(int y=0; y<value.imghight;y++)
			{
				long lvx=0,lvy=0;
				if(x==0 || y==0 || x==value.imgwidth-1 || y==value.imghight-1)
				{
					G[x][y] = 0;
					Gx[x][y] = 0;
					Gy[x][y] = 0;
				}
				else
				{
					Gx[x][y] = (Main.finger[x+1][y-1].ycbcr+(2*Main.finger[x+1][y].ycbcr)+Main.finger[x+1][y+1].ycbcr)-(Main.finger[x-1][y-1].ycbcr+(2*Main.finger[x-1][y].ycbcr)+Main.finger[x-1][y+1].ycbcr);
					Gy[x][y] = (Main.finger[x+1][y+1].ycbcr+(2*Main.finger[x][y+1].ycbcr)+Main.finger[x-1][y+1].ycbcr)-(Main.finger[x-1][y-1].ycbcr+(2*Main.finger[x][y-1].ycbcr)+Main.finger[x+1][y-1].ycbcr);
					//G[x][y] = Math.abs(Gx[x][y])+Math.abs(Gy[x][y]);
					G[x][y] = (int)Math.abs(Math.sqrt(Math.pow(Gx[x][y], 2)+Math.pow(Gy[x][y], 2)));
				}
				lvx = Gx[x][y]*Gy[x][y]*2;
				lvy = (Gx[x][y]*Gx[x][y])-(Gy[x][y]*Gy[x][y]);
				ang = Math.atan2(lvy, lvx);
				if(ang<0)
				{
					ang += 2*value.PI;
				}
				ang = (ang*value.EPI*0.5)+0.5;
				angle[x][y] = (long)ang;
				angle[x][y] -= 135;
				if(angle[x][y] <= 0)
				{
					angle[x][y] += 180;
				}
				angle[x][y] = 180-angle[x][y];
				if(G[x][y] >255)
				{
					rgbset(x,y,0);
				}
				else
				{
					rgbset(x,y,255-G[x][y]);
				}
				
				draw(x,y);
			}
		}
		
	}
	
	//*****����*****//
	public static void segment()
	{
		smooth(2);
		for(int x=0;x<value.imgwidth;x++)
		{
			for(int y=0;y<value.imghight;y++)
			{
				//if(x == 0)
				if(x==0 || x==value.imgwidth-1 || y==0 || y==value.imghight-1)
				{
					rgbset(x,y,255);
				}
				else
				{
					if(G[x][y] <=50)
					{
						rgbset(x,y,255);
						G[x][y] = 0;
					}
					else
					{
						rgbset(x,y,0);
						G[x][y] = 255;
					}
				}
				draw(x,y);
			}
		}
	}
	
	//*****����*****//
	public static void equalize()
	{
		int[] count = new int[256];
		int[] map = new int[256];
		int tmp;
		for(int x=0;x<value.imgwidth;x++)
		{
			for(int y=0;y<value.imghight;y++)
			{
				count[Main.finger[x][y].ycbcr]++;
			}
		}
		for(int i=0;i<256;i++)
		{
			tmp = 0;
			for(int j=0;j<=i;j++)
			{
				tmp += count[j];
			}
			map[i] = tmp*255/value.imgwidth/value.imghight;
		}
		for(int x=0;x<value.imgwidth;x++)
		{
			for(int y=0;y<value.imghight;y++)
			{
				Main.finger[x][y].ycbcr = map[Main.finger[x][y].ycbcr];
				rgbset(x,y,Main.finger[x][y].ycbcr);
				draw(x,y);
			}
		}
		
	}
	
	//*****����*****//
	public static void gaussian()
	{
		int windownsize = 25;
		int half;
		double dotmul,sum;
		//kernel = new double[windownsize];
		double[][] tmp = new double[value.imgwidth][value.imghight];
		windownsize = makegauss();
		half = windownsize/2;
		for(int y=0;y<value.imghight;y++)
		{
			for(int x=0;x<value.imgwidth;x++)
			{
				dotmul = 0;
				sum = 0;
				for(int i=(-1)*half;i<=half;i++)
				{
					if(x+i>-1 && x+i<value.imgwidth)
					{
						dotmul += Main.finger[x][y].ycbcr*kernel[i+half];
						sum += kernel[i+half];
					}
				}
				tmp[x][y] = dotmul/sum;
			}
		}
		for(int x=0;x<value.imgwidth;x++)
		{
			for(int y=0;y<value.imghight;y++)
			{
				dotmul = 0;
				sum = 0;
				for(int i=(-1)*half;i<=half;i++)
				{
					if(y+i>-1 && y+i<value.imghight)
					{
						dotmul += tmp[x][y]*kernel[i+half];
						sum += kernel[i+half];
					}
				}
				Main.finger[x][y].ycbcr = (int)(dotmul/sum);
				rgbset(x,y,(int)(dotmul/sum));
				draw(x,y);
			}
		}
	}
	
	//*****����G�Ȥ�*****//
	public static void binary()
	{
		int d = 0;
		int sum = 0;
		// ���u��V�W��7���I���v��
		int[] Hw = new int[]{2, 2, 3, 4, 3, 2, 2};
		// ���u��V��������V�W��7���I���v��
		int[] Vw = new int[]{1, 1, 1, 1, 1, 1, 1};
		int hsum = 0;	// ���u��V�W��7���I���[�v�M
		int vsum = 0;	// ���u��V��������V�W��7���I���[�v�M
		int Hv = 0;		// ���u��V�W��7���I���[�v������
		int Vv = 0;		// ���u��V��������V�W��7���I���[�v������
		
		for(int x=0;x<value.imgwidth;x++)
		{
			for(int y=0;y<value.imghight;y++)
			{
				if(Main.finger[x][y].ycbcr <4)
				{
					rgbset(x,y,0);
				}
				d = DDIndex(x,y);
				sum = 0;
				hsum = 0;
				for(int i=0;i<7;i++)
				{
					if(x+g_DDSite[d][i][0] > -1 
							&& y+g_DDSite[d][i][1] > -1 
							&& x+g_DDSite[d][i][0] < value.imgwidth 
							&& y+g_DDSite[d][i][1] < value.imghight)
					{
						sum += Hw[i]*Main.finger[x+g_DDSite[d][i][0]][y+g_DDSite[d][i][1]].ycbcr;
						hsum += Hw[i];
					}
				}
				if(hsum != 0)
				{
					Hv = sum/hsum;
				}
				else
				{
					Hv = 255;
				}
				
				d = (d+6)%12;
				sum = 0;
				vsum = 0;
				for(int i=0;i<7;i++)
				{
					if(x+g_DDSite[d][i][0] > -1 
							&& y+g_DDSite[d][i][1] > -1 
							&& x+g_DDSite[d][i][0] < value.imgwidth 
							&& y+g_DDSite[d][i][1] < value.imghight)
					{
						sum += Vw[i]*Main.finger[x+g_DDSite[d][i][0]][y+g_DDSite[d][i][1]].ycbcr;
						vsum += Vw[i];
					}
				}
				if(vsum != 0)
				{
					Vv = sum/vsum;
				}
				else
				{
					Vv = 255;
				}
				
				if(Hv < Vv)
				{
					rgbset(x,y,0);
				}
				else
				{
					rgbset(x,y,255);
				}
				draw(x,y);
			}
		}
	}
	
	
	public static void binaryclean()//ok
	{
		boolean f = true;
		int n = 0;
		while(f && n <8)
		{
			f =false;
			n++;
			for(int x=1;x<value.imgwidth-1;x++)
			{
				for(int y=1;y<value.imghight-1;y++)
				{
					if(Main.finger[x][y].r == 0)
					{
						if(thinning1(x,y,0) < 2)
						{
							Main.finger[x][y].r = 255-Main.finger[x][y].r;
							Main.finger[x][y].g = 255-Main.finger[x][y].g;
							Main.finger[x][y].b = 255-Main.finger[x][y].b;
							Main.finger[x][y].ycbcr = (int)((0.299*Main.finger[x][y].r)+(0.587*Main.finger[x][y].g)+(0.114*Main.finger[x][y].b));
							f = true;
						}
					}
					draw(x,y);
				}
			}
		}
	}
	
	//*****�ӯ���*****//
	public static void thinning()//OK
	{
		int[] mark = new int[]{
				0,0,1,1,0,0,1,1,             1,1,0,1,1,1,0,1,
				1,1,0,0,1,1,1,1,             0,0,0,0,0,0,0,1,
				0,0,1,1,0,0,1,1,             1,1,0,1,1,1,0,1,
				1,1,0,0,1,1,1,1,             0,0,0,0,0,0,0,1,
				1,1,0,0,1,1,0,0,             0,0,0,0,0,0,0,0,
				0,0,0,0,0,0,0,0,             0,0,0,0,0,0,0,0,
				1,1,0,0,1,1,0,0,             1,1,0,1,1,1,0,1,
				0,0,0,0,0,0,0,0,             0,0,0,0,0,0,0,0,
				0,0,1,1,0,0,1,1,             1,1,0,1,1,1,0,1,
				1,1,0,0,1,1,1,1,             0,0,0,0,0,0,0,1,
				0,0,1,1,0,0,1,1,             1,1,0,1,1,1,0,1,
				1,1,0,0,1,1,1,1,             0,0,0,0,0,0,0,0,
				1,1,0,0,1,1,0,0,             0,0,0,0,0,0,0,0,
				1,1,0,0,1,1,1,1,             0,0,0,0,0,0,0,0,
				1,1,0,0,1,1,0,0,             1,1,0,1,1,1,0,0,
				1,1,0,0,1,1,1,0,             1,1,0,0,1,0,0,0};
		
		int num;
		int n,ne,e,se,s,sw,w,nw;
		boolean f = true;
		while(f)
		{
			f = false;
			//*****������V�Ӥ�*****//
			for(int y=1;y<value.imghight-1;y++)
			{
				for(int x=1;x<value.imgwidth-1;x++)
				{
					if(Main.finger[x][y].r == 0 && x+1 != value.imgwidth-1)
					{
						if(Main.finger[x-1][y].r == 255 || Main.finger[x+1][y].r == 255)
						{
							nw = Main.finger[x-1][y-1].r;//���W
							n = Main.finger[x][y-1].r;//�W
							ne = Main.finger[x+1][y-1].r;//�k�W
							e = Main.finger[x+1][y].r;//�k
							se = Main.finger[x+1][y+1].r;//�k�U
							s = Main.finger[x][y+1].r;//�U
							sw = Main.finger[x-1][y+1].r;//���U
							w = Main.finger[x-1][y].r;//��
							num = (nw/255)+(n/255*2)+(ne/255*4)+(w/255*8)+(e/255*16)+(sw/255*32)+(s/255*64)+(se/255*128);
							if(mark[num] == 1)
							{
								rgbset(x,y,255);
								f = true;
								x++;
							}
						}
					}
				}
			}	
		}
		
		f = true;
		while(f)
		{
			f = false;
			//*****������V�Ӥ�*****//
			for(int x=1;x<value.imgwidth-1;x++)
			{
				for(int y=1;y<value.imghight-1;y++)
				{
					if(Main.finger[x][y].r == 0 && y+1 != value.imghight-1)
					{
						if(Main.finger[x][y-1].r == 255 || Main.finger[x][y+1].r == 255)
						{
							nw = Main.finger[x-1][y-1].r;//���W
							n = Main.finger[x][y-1].r;//�W
							ne = Main.finger[x+1][y-1].r;//�k�W
							e = Main.finger[x+1][y].r;//�k
							se = Main.finger[x+1][y+1].r;//�k�U
							s = Main.finger[x][y+1].r;//�U
							sw = Main.finger[x-1][y+1].r;//���U
							w = Main.finger[x-1][y].r;//��
							num = (nw/255)+(n/255*2)+(ne/255*4)+(w/255*8)+(e/255*16)+(sw/255*32)+(s/255*64)+(se/255*128);
							if(mark[num] == 1)
							{
								rgbset(x,y,255);
								f = true;
								y++;
							}
						}
					}
				}
			}
		}
		thinClear();
		for(int x=0;x<value.imgwidth;x++)
		{
			for(int y=0;y<value.imghight;y++)
			{
				draw(x,y);
			}
		}	
	}
	
	//*****�Ӥƫ�B�z*****//
	public static void thinClear()
	{
		int[] SiteD8x = {-1,0,1,1,1,0,-1,-1};
		int[] SiteD8y = {1,1,1,0,-1,-1,-1,0};
		int n,num=0;
		int len = 24;
		temp[] line = new temp[25];
		temp[] tmp = new temp[8];
		plast = new temp();
		pnow = new temp();
		pnext = new temp();
		for(int x=1;x<value.imgwidth-1;x++)
		{
			for(int y=1;y<value.imghight-1;y++)
			{
				if(Main.finger[x][y].ycbcr == 0)
				{
					pnow.x = x;
					pnow.y = y;
					line[0] = new temp();
					line[0] = pnow;
					
					n=0;
					for(int i=0;i<8;i++)
					{
						pnext.x = x+SiteD8x[i];
						pnext.y = y+SiteD8y[i];
						if(Main.finger[pnext.x][pnext.y].ycbcr == 0)
						{
							tmp[n] = new temp();
							tmp[n] = pnext;
							n++;
						}
					}
					if(n == 0)
					{
						rgbset(pnow.x,pnow.y,255);
					}
					else if(n == 1)
					{
						num = 0;
						plast = pnow;
						pnow = tmp[0];
						
						for(int i=0;i<len;i++)
						{
							if(Recognition.IsFork(pnow.x,pnow.y))
							{
								break;
							}
							num++;
							line[num] = pnow;
							
							if(getnext() == 0)
							{
								plast = pnow;
								pnow = pnext;
							}
							else
							{
								break;
							}
						}
						if(num<len)
						{
							for(int i=0;i<=num;i++)
							{
								rgbset(line[i].x,line[i].y,255);
							}
						}
					}
				}
			}
		}
	}
	
	//*****���Ƥ�*****//
	public static void smooth(int d)
	{
		for(int y=1;y<value.imghight-1;y++)
		{
			for(int x=1;x<value.imgwidth-1;x++)
			{
				int sum=0,num=0;
				for(int i=-1;i<=1;i+=d)
				{
					for(int j=-1;j<=1;j+=d)
					{
						sum += Main.finger[x+i][y+j].ycbcr;
						num++;
					}
				}
				Main.finger[x][y].ycbcr = sum/num;
				rgbset(x,y,sum/num);
				draw(x,y);
				
			}
		}
	}
	
	//*****����W�j*****//
	public static void orientEnhance()
	{
		double[][] tmp = new double[value.imgwidth][value.imghight];
		int d = 0;
		int sum = 0;
		// ���u��V�W�i�業���o�i�������o�i��
		int[] Hw = {1, 1, 1, 1, 1, 1, 1};
		// ���u��V��������V�W�i��U���o�i���U���o�i��
		int[] Vw = {-3, -1, 3, 9, 3, -1, -3};
		int hsum = 0;
		int vsum = 0;
		
		for(int x=0; x<value.imgwidth;x++)
		{
			for(int y=0; y<value.imghight;y++)
			{
				d = DDIndex(x,y);
				sum = 0;
				hsum = 0;
				for(int i=0;i<7;i++)
				{
					if(x+g_DDSite[d][i][0] > -1 
							&& y+g_DDSite[d][i][1] > -1 
							&& x+g_DDSite[d][i][0] < value.imgwidth 
							&& y+g_DDSite[d][i][1] < value.imghight)
					{
						sum += Hw[i]*Main.finger[x+g_DDSite[d][i][0]][y+g_DDSite[d][i][1]].ycbcr;
						hsum += Hw[i];
					}
				}
				if(hsum != 0)
				{
					tmp[x][y] = sum/hsum;
					//rgbset(x,y,sum/hsum);
				}
				else
				{
					tmp[x][y] = 255;
				}
			}
		}
		
		for(int x=0; x<value.imgwidth;x++)
		{
			for(int y=0; y<value.imghight;y++)
			{
				d = (DDIndex(x,y)+6)%12;
				sum =0;
				vsum = 0;
				for(int i=0;i<7;i++)
				{
					if(x+g_DDSite[d][i][0] > -1 
							&& y+g_DDSite[d][i][1] > -1 
							&& x+g_DDSite[d][i][0] < value.imgwidth 
							&& y+g_DDSite[d][i][1] < value.imghight)
					{
						sum += Vw[i]*tmp[x+g_DDSite[d][i][0]][y+g_DDSite[d][i][1]];
						vsum += Vw[i];
					}
				}
				if(vsum > 0)
				{
					sum = sum/vsum;
					if(sum > 255)
					{
						rgbset(x,y,255);
					}
					else if(sum < 0)
					{
						rgbset(x,y,0);
					}
					else
					{
						rgbset(x,y,sum);
					}
				}
				else
				{
					rgbset(x,y,255);
				}
				draw(x,y);
			}
		}
	}
	
	//*****����*****//
	public static void dilation()
	{
		int[][] tmp = new int[value.imgwidth][value.imghight];
		for(int x=0;x<value.imgwidth;x++)
		{
			for(int y=0;y<value.imghight;y++)
			{
				if(x-1>-1 && x+1<value.imgwidth && y-1>-1 && y+1<value.imghight)
				{
					if(Main.finger[x][y].r == 0)
					{
						tmp[x][y] = 0;//�ۤv
						tmp[x-1][y-1] = 0;//���W
						tmp[x+1][y-1] = 0;//�k�W
						tmp[x-1][y+1] = 0;//���U
						tmp[x+1][y+1] = 0;//�k�U
					}
					else
					{
						tmp[x][y] = Main.finger[x][y].r;
					}
				}
				else
				{
					tmp[x][y] = 255;
				}
			}
		}
		for(int x=0;x<value.imgwidth;x++)
		{
			for(int y=0;y<value.imghight;y++)
			{
				rgbset(x,y,tmp[x][y]);
				draw(x,y);
			}
		}
	}
	
	//*****�I�k*****//
	public static void erosion()
	{
		int[][] tmp = new int[value.imgwidth][value.imghight];
		for(int x=0;x<value.imgwidth;x++)
		{
			for(int y=0;y<value.imghight;y++)
			{
				if(x-1>-1 && y-1>-1 && x+1<value.imgwidth && y+1<value.imghight)
				{
					if(thinning1(x,y,3) >= 4)
					{
						tmp[x][y] = 0;//����
						tmp[x][y-1] = 255;//�W
						tmp[x][y+1] = 255;//�U
						tmp[x-1][y] = 255;//��
						tmp[x+1][y] = 255;//�k
					}
					else
					{
						tmp[x][y] = Main.finger[x][y].r;
					}
				}
				else
				{
					tmp[x][y] = 255;
				}
			}
		}
		for(int x=0;x<value.imgwidth;x++)
		{
			for(int y=0;y<value.imghight;y++)
			{
				rgbset(x,y,tmp[x][y]);
				draw(x,y);
			}
		}
	}
	
	public static int DDIndex(int x,int y)
	{
		if(angle[x][y] >= 173 || angle[x][y] < 8)
		{
			return 0;
		}
		else
		{
			return (int)(((angle[x][y]-8)/15)+1);
		}
	}
	
	
	public static void draw(int x,int y)
	{
		DrawPanel.drawcolor(x, y, Main.finger[x][y].r, Main.finger[x][y].g, Main.finger[x][y].b,1);
	}
	
	public static int getnext()
	{
		int[] SiteD8x = {-1,0,1,1,1,0,-1,-1};
		int[] SiteD8y = {1,1,1,0,-1,-1,-1,0};
		int n=0;
		temp tmp = new temp();
		temp[] t = new temp[8];
		for(int i=0;i<8;i++)
		{
			tmp.x = pnow.x+SiteD8x[i];
			tmp.y = pnow.y+SiteD8y[i];
			if(Main.finger[tmp.x][tmp.y].ycbcr == 0 && tmp != plast)
			{
				t[n] = new temp();
				t[n] = tmp;
				n++;
			}
		}
		if(n == 1)
		{
			pnext = t[0];
			return 0;
		}
		else
		{
			pnext.x = 0;
			pnext.y = 0;
			return 1;
		}
	}
	
	public static int makegauss()
	{
		int sigma = 1;
		int ws = (int)(1 + (2*Math.ceil(3*sigma)));
		int center = ws/2;
		double val = 0;
		double sum = 0;
		double dis = 0;
		kernel = new double[ws];
		for(int i=0;i<ws;i++)
		{
			dis = (double)(i-center);
			val = Math.exp((-1/2)*dis*dis/(sigma*sigma))/(Math.sqrt(2*value.PI)*sigma);
			kernel[i] = val;
			sum += val;
		}
		for(int i=0;i<ws;i++)
		{
			kernel[i] /= sum;
		}
		return ws;
	}
	
	public static int thinning1(int x,int y,int type)
	{
		int tmp = 0;
		if(type == 0)
		{
			if(Main.finger[x-1][y-1].r == 0)
				tmp++;
			if(Main.finger[x][y-1].r == 0)
				tmp++;
			if(Main.finger[x+1][y-1].r == 0)
				tmp++;
			if(Main.finger[x-1][y].r == 0)
				tmp++;
			if(Main.finger[x+1][y].r == 0)
				tmp++;
			if(Main.finger[x-1][y+1].r == 0)
				tmp++;
			if(Main.finger[x][y+1].r == 0)
				tmp++;
			if(Main.finger[x+1][y+1].r == 0)
				tmp++;
		}
		if(type == 1)
		{
			if(Math.abs(Main.finger[x][y-1].r - Main.finger[x+1][y-1].r) == 255)//P2��P3
				tmp++;
			if(Math.abs(Main.finger[x+1][y-1].r - Main.finger[x+1][y].r) == 255)//P3��P4
				tmp++;
			if(Math.abs(Main.finger[x+1][y].r - Main.finger[x+1][y+1].r) == 255)//P4��P5
				tmp++;
			if(Math.abs(Main.finger[x+1][y+1].r - Main.finger[x][y+1].r) == 255)//P5��P6
				tmp++;
			if(Math.abs(Main.finger[x][y+1].r - Main.finger[x-1][y+1].r) == 255)//P6��P7
				tmp++;
			if(Math.abs(Main.finger[x-1][y+1].r - Main.finger[x-1][y].r) == 255)//P7��P8
				tmp++;
			if(Math.abs(Main.finger[x-1][y].r - Main.finger[x-1][y-1].r) == 255)//P8��P9
				tmp++;
			if(Math.abs(Main.finger[x-1][y-1].r - Main.finger[x][y-1].r) == 255)//P9��P2
				tmp++;
		}
		if(type == 2)
		{
			if(Main.finger[x][y].r == 0)
			{
				tmp = 1;
			}
			else
			{
				tmp = 0;
			}
		}
		if(type == 3)
		{
			if(Main.finger[x][y-1].r == 0)
			{
				tmp++;
			}
			if(Main.finger[x][y+1].r == 0)
			{
				tmp++;
			}
			if(Main.finger[x-1][y].r == 0)
			{
				tmp++;
			}
			if(Main.finger[x+1][y].r == 0)
			{
				tmp++;
			}
		}
		if(type == 4)
		{
			if(Main.finger[x-1][y-1].r == 0)
			{
				tmp++;
			}
			if(Main.finger[x-1][y+1].r == 0)
			{
				tmp++;
			}
			if(Main.finger[x-1][y+1].r == 0)
			{
				tmp++;
			}
			if(Main.finger[x+1][y+1].r == 0)
			{
				tmp++;
			}
		}
		return tmp;
	}
	
	public static void rgbset(int x,int y,int c)
	{
		Main.finger[x][y].r = c;
		Main.finger[x][y].g = c;
		Main.finger[x][y].b = c;
		Main.finger[x][y].ycbcr = (int)((0.299*Main.finger[x][y].r)+(0.587*Main.finger[x][y].g)+(0.114*Main.finger[x][y].b));
	}
	
	public static void b()
	{
		for(int x=0; x<value.imgwidth;x++)
		{
			for(int y=0; y<value.imghight;y++)
			{
				if(Main.finger[x][y].ycbcr < 128)
				{
					rgbset(x,y,0);
				}
				else
				{
					rgbset(x,y,255);
				}
				Main.finger[x][y].ycbcr = (int)((0.299*Main.finger[x][y].r)+(0.587*Main.finger[x][y].g)+(0.114*Main.finger[x][y].b));//�G�׭�Y(YCbCr)
				draw(x,y);
			}
		}
	}
}

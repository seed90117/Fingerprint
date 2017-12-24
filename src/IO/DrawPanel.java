package IO;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import InterFace.View;

public class DrawPanel {

static Graphics gp;
	
	public static void drawoldpanel(int type)
	{
		if(type == 0)
		{
			gp = View.print1.getGraphics();
			View.b = true;
		}
		else
		{
			gp = View.print2.getGraphics();
			View.c = true;
		}
		Graphics2D g2d = (Graphics2D)gp;
		if(type == 0)
		{
			g2d.drawImage(LoadFile.image, 0, 0, null);
		}
		else
		{
			g2d.drawImage(LoadFile.image2, 0, 0, null);
		}
	}
	
	public static void drawcolor(int x, int y, int r, int g, int b,int s)
	{
		gp = View.print2.getGraphics();
		Color color = new Color(r,g,b);
		gp.setColor(color);
		gp.fillRect(x, y, s, s);
	}
}

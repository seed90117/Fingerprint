package IO;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;

import InterFace.View;
import Value.value;

public class LoadFile {

	public static BufferedImage image;
	public static BufferedImage image2;
	
	public static void loadfile(int type)
	{
		String tmp;
		
		//*****�w�]�ɮ׶}�Ҧ�m*****//
		View.open.setCurrentDirectory(new java.io.File("D:\\Visual C++�����Ҧ��ѧO�t�Υ���\\���������w"));
		
		//*****�ɮ׶}�ҵ���*****//
		View.open.setDialogTitle("����ɮ�"); //�]�wDialog Title�W��
		
		//*****�P�_�O�_���U�T�w*****//
		if(View.open.showDialog(View.print2, "�T�w") == JFileChooser.APPROVE_OPTION)
		{
			//*****���o����ɮ�*****//
			tmp = View.open.getSelectedFile().getPath();
			
			//*****�Ȧs�ɮ�*****//
			File file = new File(tmp);
			
			//*****�ɮ�Ū��*****//
			try
			{
				if(type ==0)
				{
					image = ImageIO.read(file);
				}
				else
				{
					image2 = ImageIO.read(file);
				}
			}
			catch (FileNotFoundException ex)
			{
				Logger.getLogger(LoadFile.class.getName()).log(Level.SEVERE, null , ex);
			}
			catch(IOException ex)
			{
				Logger.getLogger(LoadFile.class.getName()).log(Level.SEVERE, null , ex);
			}
			value.imgwidth = image.getWidth();
			value.imghight = image.getHeight();
			DrawPanel.drawoldpanel(type);
		}
	}
}

package Program;

import IO.LoadFile;
import Value.rgbdata;

public class Main {

	public static rgbdata[][] finger;
	static int[][] cutarray;
	public static void main()
	{
		finger = new rgbdata[LoadFile.image.getWidth()][LoadFile.image.getHeight()];
		cutarray = new int[LoadFile.image.getWidth()][LoadFile.image.getHeight()];
		
		ImageProcessing.gray();//�Ƕ���
		ImageProcessing.sobel();//��V���P��׭p��
		ImageProcessing.segment();//����
		ImageProcessing.smooth(1);//���Ƥ�
		ImageProcessing.orientEnhance();//����W�j
		//ImageProcessing.b();//�G�Ȥ�
		ImageProcessing.binary();//����G�Ȥ�
		//ImageProcessing.binaryclean();//�G�Ȥ����I�M��
		//ImageProcessing.thinning();//�ӯ���
		//ImageProcessing.thin();
	}
	
	
	
}

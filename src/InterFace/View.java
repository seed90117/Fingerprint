package InterFace;

import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;

import IO.LoadFile;
import Program.ImageProcessing;
import Program.Main;
import Program.Recognition;
import Value.rgbdata;

public class View extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static boolean b = false;
	public static boolean c = false;
	
	//*****�ŧi�ɭ�*****//
	Container cp = this.getContentPane();
	
	//*****�ŧi����*****//
	JButton loadfile1 = new JButton("Load File");
	JButton loadfile2 = new JButton("Load File");
	JButton start = new JButton("Starts");
	JButton gray = new JButton("�Ƕ���");
	JButton sobel = new JButton("��פ�V���p��");
	JButton segment = new JButton("����");
	JButton equalize = new JButton("����");
	JButton gaussian = new JButton("����");
	JButton smooth = new JButton("����");
	JButton orientenhance = new JButton("����W�j");
	JButton binary = new JButton("����G�Ȥ�");
	JButton binaryclear = new JButton("�G�Ȥƥh���T");
	JButton thinning = new JButton("�ӯ���");
	public static JPanel print1 = new JPanel();
	public static JPanel print2 = new JPanel();
	public static JFileChooser open = new JFileChooser();
	
	View()
	{
		//*****�]�w���*****//
		this.setSize(1100, 600);
		this.setLayout(null);
		this.setTitle("Grid");
		
		//*****�]�w�����m*****//
		print1.setBounds(20, 20, 400, 400);
		loadfile1.setBounds(120, 450, 200, 30);
		print2.setBounds(450, 20, 400, 400);
		loadfile2.setBounds(550, 450, 200, 30);
		start.setBounds(330, 500, 200, 30);
		gray.setBounds(900, 20, 150, 30);
		sobel.setBounds(900, 60, 150, 30);
		segment.setBounds(900, 100, 150, 30);
		equalize.setBounds(900, 140, 150, 30);
		gaussian.setBounds(900, 180, 150, 30);
		smooth.setBounds(900, 220, 150, 30);
		orientenhance.setBounds(900, 260, 150, 30);
		binary.setBounds(900, 300, 150, 30);
		binaryclear.setBounds(900, 340, 150, 30);
		thinning.setBounds(900, 380, 150, 30);

		//*****�]�wJPanel���*****//
		print1.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		print2.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		
		//*****�s�W����ܤ���*****//
		cp.add(loadfile1);
		cp.add(loadfile2);
		cp.add(start);
		cp.add(print1);
		cp.add(print2);
		cp.add(gray);
		cp.add(sobel);
		cp.add(segment);
		cp.add(equalize);
		cp.add(gaussian);
		cp.add(smooth);
		cp.add(orientenhance);
		cp.add(binary);
		cp.add(binaryclear);
		cp.add(thinning);
		
		//*****�]�w����ݩ�*****//
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		
		//*****���J�ɮ׫��s*****//
		loadfile1.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				LoadFile.loadfile(0);
			}});
		
		//*****JPanel�W�e�I���s*****//
		loadfile2.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				//LoadFile.loadfile(1);
				//ImageProcessing.b();
				Recognition.recogntition();
			}});
		
		//*****������s*****//
		start.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if(b)
				{
					//Main.main();
					ImageProcessing.dilation();
					ImageProcessing.erosion();
				}
			}});
		
		gray.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if(b)
				{
					Main.finger = new rgbdata[LoadFile.image.getWidth()][LoadFile.image.getHeight()];
					ImageProcessing.gray();
				}
			}});
		
		sobel.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if(b)
				{
					//ImageProcessing.sobel();
					ImageProcessing.getorientmap();
				}
			}});
		
		segment.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if(b)
				{
					ImageProcessing.segment();
				}
			}});
		
		equalize.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if(b)
				{
					ImageProcessing.equalize();
				}
			}});
		
		gaussian.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if(b)
				{
					ImageProcessing.gaussian();
				}
			}});
		
		smooth.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if(b)
				{
					ImageProcessing.smooth(1);
				}
			}});
		
		orientenhance.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if(b)
				{
					ImageProcessing.orientEnhance();
				}
			}});
		
		binary.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if(b)
				{
					ImageProcessing.binary();
					//ImageProcessing.b();
				}
			}});
		
		binaryclear.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if(b)
				{
					ImageProcessing.binaryclean();
				}
			}});
		
		thinning.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if(b)
				{
					ImageProcessing.thinning();
					//ImageProcessing.dilation();
					//ImageProcessing.erosion();
				}
			}});
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new View();
	}

}

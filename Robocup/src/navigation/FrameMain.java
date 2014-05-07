/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package navigation;

 import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;

import comunication.CameraReader;

import data.Field;
 
 
 public class FrameMain /*extends JFrame implements ActionListener*/ {
	 CameraReader cameraReader = new CameraReader();
	 Field field = new Field();
	 JPanel contentPane;
     BorderLayout borderLayout1 = new BorderLayout();
     JCheckBox virtualforce;
     JPanel commands = new JPanel();
     Set<Point> s = new HashSet<Point> ();
     JPanel pnlDraw = new JPanel() {
       
         public void paint(Graphics g) {          
             super.paint(g);
             g.setColor(Color.BLACK);
             s.add(new Point((int) r.x, (int) r.y));

             for (int i = 0; i < field.getOppenent().length; i++) {
            	 System.out.println((field.getOppenent()[i].getX() + " " +  field.getOppenent()[i].DIAMETER/2) + " " + 
 						(field.getOppenent()[i].getY() + " " + field.getOppenent()[i].DIAMETER/2) + " " + 
 						field.getOppenent()[i].DIAMETER + " " + (int) field.getOppenent()[i].DIAMETER);
            	 g.fillArc((int)(field.getOppenent()[i].getX() - field.getOppenent()[i].DIAMETER/2), 
						(int)(field.getOppenent()[i].getY() - field.getOppenent()[i].DIAMETER/2), 
						(int) field.getOppenent()[i].DIAMETER, (int) field.getOppenent()[i].DIAMETER, 0, 360);				
             }
             g.setColor(Color.BLUE);
             for(Point p : s) {
               g.fillArc((int)(p.x - r.diam/4), (int)(p.y - r.diam/4), (int)r.diam/2, (int)r.diam/2, 0, 360);
             }
             
             g.setColor(Color.RED);
             g.fillArc((int)(r.x - r.diam/2), (int)(r.y - r.diam/2), (int)r.diam, (int)r.diam, 0, 360);
             g.drawLine((int)r.x, (int)r.y, (int)(r.x+r.vx), (int)(r.y+r.vy));
         }
     };
 

     
     RobotAlg r;;
     Thread t = new Thread();

     public FrameMain() {
    	 while(true){
	    	 cameraReader.read(field);
	    	 
	    	 r = new RobotAlg(field.me[0], field.getOppenent(), field.getBall(),  0.1, 40, 4000, 15);
	   	 }
    	 //Paint
    	 /*try {
             setDefaultCloseOperation(EXIT_ON_CLOSE);
             jbInit();
         } catch (Exception exception) {
             exception.printStackTrace();
         }*/
 
     }
 
     /**
      * Component initialization.
      *
      * @throws java.lang.Exception
      */
     /*private void jbInit() throws Exception {
         contentPane = (JPanel) getContentPane();
         contentPane.setLayout(borderLayout1);
         setSize(new Dimension(900, 700));
         setTitle("Fuzzy Roboter");
         pnlDraw.addMouseListener(new FrameMain_pnlDraw_mouseAdapter(this));
         JButton bnew = new JButton("New");
         bnew.setActionCommand("new");
         bnew.addActionListener(this);
         commands.add(bnew);
         JButton bstart = new JButton("Start");
         bstart.setActionCommand("start");
         bstart.addActionListener(this);
         commands.add(bstart);
         JButton bpause = new JButton("Pause");
         bpause.setActionCommand("pause");
         bpause.addActionListener(this);        
         commands.add(bpause);
         JButton bresume = new JButton("Resume");
         bresume.setActionCommand("resume");
         bresume.addActionListener(this);        
         commands.add(bresume);        
         virtualforce = new JCheckBox("Virtual Force");
         virtualforce.setActionCommand("vf");
         virtualforce.addActionListener(this);
         commands.add(virtualforce);
         contentPane.add(pnlDraw, java.awt.BorderLayout.CENTER);
         contentPane.add(commands, java.awt.BorderLayout.NORTH);
     }*/
 
     public void pnlDraw_mouseReleased(MouseEvent e) {
       //obstacles.add(new Obstacle(e.getPoint()));
         pnlDraw.repaint();
     }
 
   public void actionPerformed(ActionEvent e) {
     if (e.getActionCommand().equals("start")) {      
           t = new Thread(new Runnable() {
               public void run() {
                   while ((r.x > 1) && (r.y > 1)) {
                       r.updatePosition();
                       pnlDraw.repaint();
                       try {
                           Thread.sleep(100);
                       } catch (InterruptedException ex) {
                           System.out.println(ex);
                       }
                   }
               }
           });  
           t.start();
     }
     else if (e.getActionCommand().equals("new")){
       r.x = 0; r.y = 0;
           r = new RobotAlg(field.me[0], field.getOppenent(), field.getBall(),  0.1, 40, 4000, 15);
           s = new HashSet<Point> ();
         t = new Thread();  
         r.virtualforce = virtualforce.isSelected();
         pnlDraw.repaint();        
     } else if (e.getActionCommand().equals("pause")){
       t.suspend();
     } else if (e.getActionCommand().equals("resume")){
       t.resume();
     }
     else if (e.getActionCommand().equals("vf")) {
       System.out.println("VIRTUAL FORCE!!");
       r.virtualforce = !r.virtualforce;
     }
   }
 
 }
 
 
 class FrameMain_pnlDraw_mouseAdapter extends MouseAdapter {
     private FrameMain adaptee;
     FrameMain_pnlDraw_mouseAdapter(FrameMain adaptee) {
         this.adaptee = adaptee;
     }
 
     public void mouseReleased(MouseEvent e) {
         adaptee.pnlDraw_mouseReleased(e);
     }
 }

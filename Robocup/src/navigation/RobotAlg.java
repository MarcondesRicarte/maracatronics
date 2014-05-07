/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package navigation;

import java.util.Random;

import simulator.Conection;

import comunication.CameraReader;

import data.Ball;
import data.Field;
import data.Robot;
 
public class RobotAlg {
	
	 Robot robot;
     double x, y;
     double vx, vy;
     double dt;
     double m;
     double fMax;
     Robot obstacles[];
     public double diam;
     Ball target;
     boolean virtualforce = false;
     
     public void goToBall(CameraReader cameraReader, Field field, Robot robot, Ball ball){
    	 Conection conection = new Conection();
    	 while(RobotMath.euclidianDistance(robot, ball) > 150){
    		 robot.setVelx(1);
    		 robot.setKickspeedz(10);
    		 conection.send(robot);
    		 cameraReader.read(field);
    	 }
    	 
    	 while(RobotMath.euclidianDistance(robot, ball) > 150){
    		 robot.setVelx(0);
    		 robot.setKickspeedx(10);
    		 conection.send(robot);
    		 cameraReader.read(field);
    	 }
     }

     public RobotAlg() {
     }
     
     public RobotAlg(Robot robot, Robot obstacles[], Ball ball, double dt, double m, double fMax, double diam) {
         this.diam = diam;
         this.fMax = fMax;
         this.m = m;
         this.dt = dt;
         vx = vy = 0;
         this.x = robot.getX();
         this.y = robot.getY();
         this.robot = robot;
         this.obstacles = obstacles;
         this.target = ball;
     }
 
     public void updatePosition() {
         double dirX = 0, dirY = 0;
         double minS = 200;
         
         for (int i = 0; i < this.obstacles.length; i++) {
        	 if(this.obstacles[i] != null && this.obstacles[i].getX() != 0.0 && this.obstacles[i].getY() != 0.0){
	             double distSq = RobotMath.distanceSq(this.robot, this.obstacles[i]);
	             if (distSq < 1){
	                 Math.sin(1);
	             }
	             double dx = this.obstacles[i].CHARGE * (this.obstacles[i].getX() - x) / distSq;
	             double dy = this.obstacles[i].CHARGE * (this.obstacles[i].getY() - y) / distSq;
	             dirX += dx;
	             dirY += dy;
        	 }
         }
 
         double norm = Math.sqrt(dirX*dirX+dirY*dirY);
         if(norm != 0.0){
        	 dirX = dirX / norm;
        	 dirY = dirY / norm;
         }
  
 
         for (int i = 0; i < this.obstacles.length; i++) {
        	 if(this.obstacles[i] != null && this.obstacles[i].getX() != 0.0 && this.obstacles[i].getY() != 0.0){
	             if(!range(this.obstacles[i], 1200)){ 
	            	 continue;
	             }
	             double distSq = RobotMath.distanceSq(this.robot, this.obstacles[i]);
	             double dx = (this.obstacles[i].getX() - x);
	             double dy = (this.obstacles[i].getY() - y);
	             //add normal noise to simulate the sonar effect
	             dx = addNoise(dx, 0, 1);
	             dy = addNoise(dy, 0 ,1);
	             double safety = distSq / ((dx * dirX+dy*dirY));
	             if ((safety > 0) &&(safety < minS))
	                 minS = safety;
        	 }
         }
         if (minS < 5) {
             double oc = this.target.CHARGE;
             this.target.CHARGE *= minS/5;
             //System.out.println(oc +" DOWN TO "+ this.target.CHARGE);
         }
 
         if (minS > 100) {
             double oc = this.target.CHARGE;
             this.target.CHARGE *= minS/100;
             //System.out.println(oc +" UP TO "+ this.target.CHARGE);
         }
 
 
         double vtNorm = minS/2;
         double vtx = vtNorm * dirX;
         double vty = vtNorm * dirY;
         double fx = m * (vtx - vx) / dt;
         double fy = m * (vty - vy) / dt;
         double fNorm =  Math.sqrt(fx * fx + fy * fy);
         if (fNorm > fMax ) {
             fx *=  fMax / fNorm;
             fy *=  fMax / fNorm;
         }
         vx += (fx * dt) / m;
         vy += (fy * dt) / m;
         //virtual force component        
         if((this.target.CHARGE < 1000) && (x > 25) && (y > 25)) {
           System.out.println("Virtual Force");
           this.target.CHARGE *= minS/100;
           
             vx = vx + 5;
         }
         //x += vx * dt;
         //y += vy * dt;
         if(this.robot != null){
        	 this.robot.setX(this.robot.getX() + (vx * dt));
        	 this.robot.setY(this.robot.getY() + (vy * dt));
        	 System.out.println("Robo X: " + this.robot.getX() + "Robo Y: " + this.robot.getY());
        	 System.out.println("Ball X: " + this.target.getX() + "Robo Y: " + this.target.getY());
         }
     }
     
     boolean range(Robot obstacle, double range) {
        double dist = RobotMath.distanceSq(this.robot, obstacle);
        if(dist < range){
          return true;
        } else { 
          return false;
        }
     }
     
     double addNoise(double x, double mean, double stddev) {
       Random r = new Random();
       double noise = stddev*r.nextGaussian() + mean;
       return x + noise;
     }
}


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package navigation;

/**
 *
 * @author Pinho
 */
import java.awt.Point;

public class Obstacle {
   static double DIAM = 10;
   static double CHARGE = -1;
   static double MASS = 10;
     double diam;
     double mass;
     Point p;
     double charge;
     public Obstacle(Point p, double charge, double diam) {
         this.diam = diam;
         this.p = p;
         this.charge = charge;
     }
     public Obstacle(Point p) {
       this.diam = DIAM;
       this.charge = CHARGE;
       this.mass = MASS;
       this.p = p;
     }
     /*public double distanceSq(Robot r) {
         double d = distance(r);
         return d * d;
     }
     public double distance(Robot r) {
         double d = p.distance(r.x, r.y) - (diam + r.diam) / 2;
         return d > 0?  d: 0.0000001;
     }*/
}

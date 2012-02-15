/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package computer.simulator.test;

import computer.simulator.Line;
import computer.simulator.Coordinates;
import computer.simulator.Direction;
import computer.simulator.LineTools;

/**
 *
 * @author Evgeniya Sotirova
 */
public class LineToolsTest {
    
    /*
     * testFunction prints "No errors. The functions are correct." if all tests pass.
     * If there is an error, it prints the test at which it was found.
     */
    public void testFunction(){
        boolean noErrors = true;
        
        Coordinates A = new Coordinates(0,0);
        Coordinates B = new Coordinates(1,0);
        Coordinates C = new Coordinates(2,0);
        Coordinates D = new Coordinates(1.5,0.5);
        Coordinates E = new Coordinates(1,0.5);
        Coordinates F = new Coordinates(0.5,0.5);
        Coordinates G = new Coordinates(2,1);
        Coordinates H = new Coordinates(1,1);
        Coordinates I = new Coordinates(0,1);
        
        Line k = new Line(A,B);
        Line l = new Line(A,I);
        Line m = new Line(G,C);
        Line n = new Line(I,G);
        Line o = new Line(F,E);
        Line p = new Line(A,F);
        Line q = new Line(H,D);
        Line r = new Line(B,D);
        Line s = new Line(G,D);
        
        //Testing intersectionOfLines...
        if(LineTools.intersectionOfLines(k,n).getX()!=Double.POSITIVE_INFINITY || LineTools.intersectionOfLines(k,n).getY()!=Double.POSITIVE_INFINITY){
            System.out.println("Error in intersectionOfLines: Wrong answer when intersecting parallel horizontal lines.");
            noErrors = false;
        }
        if(LineTools.intersectionOfLines(l,m).getX()!=Double.POSITIVE_INFINITY || LineTools.intersectionOfLines(l,m).getY()!=Double.POSITIVE_INFINITY){
            System.out.println("Error in intersectionOfLines: Wrong answer when intersecting parallel vertical lines.");
            noErrors = false;
        }
        if(LineTools.intersectionOfLines(p,r).getX()!=Double.POSITIVE_INFINITY || LineTools.intersectionOfLines(p,r).getY()!=Double.POSITIVE_INFINITY){
            System.out.println("Error in intersectionOfLines: Wrong answer when intersecting parallel lines with gradient 1.");
            noErrors = false;
        }
        if(LineTools.intersectionOfLines(l,k).getX()!=0 || LineTools.intersectionOfLines(l,k).getY()!=0){
            System.out.println(LineTools.intersectionOfLines(l,k).getX() +" "+ LineTools.intersectionOfLines(k,n).getY());
            System.out.println("Error in intersectionOfLines: Wrong answer when intersecting non-parallel lines in the origin.");
            noErrors = false;
        }
        if(LineTools.intersectionOfLines(p,q).getX()!=1 || LineTools.intersectionOfLines(p,q).getY()!=1){
            System.out.println("Error in intersectionOfLines: Wrong answer when intersecting non-parallel lines in the point (1,1).");
            noErrors = false;
        }
        
        //Testing symmetricalPoint...
        if(LineTools.symmetricalPoint(B, l).getX()!=-1 || LineTools.symmetricalPoint(B, l).getY()!=0){
            System.out.println("Error in symmetricalPoint: Does not find the summetrical point of (1,0) wrt line <(0,0),(0,1)>");
            noErrors = false;
        }
        if(LineTools.symmetricalPoint(B, o).getX()!=1 || LineTools.symmetricalPoint(B, o).getY()!=1){
            System.out.println("Error in symmetricalPoint: Does not find the summetrical point of (1,0) wrt line <(0.5,0.5),(1,0.5)>");
            noErrors = false;
        }
        if(LineTools.symmetricalPoint(H, o).getX()!=1 || LineTools.symmetricalPoint(H, o).getY()!=0){
            System.out.println("Error in symmetricalPoint: Does not find the summetrical point of (1,1) wrt line <(0.5,0.5),(1,0.5)>");
            noErrors = false;
        }
        if(LineTools.symmetricalPoint(E, r).getX()!=1.5 || LineTools.symmetricalPoint(E, r).getY()!=0){
            System.out.println("Error in symmetricalPoint: Does not find the summetrical point of (1,0.5) wrt line <(1,0),(1.5,0.5)>");
            noErrors = false;
        }
        if(LineTools.symmetricalPoint(H, n).getX()!=1 || LineTools.symmetricalPoint(H, n).getY()!=1){
            System.out.println("Error in symmetricalPoint: Does not find the summetrical point of (1,1) wrt line <(0,1),(2,1)>");
            noErrors = false;
        }
        if(LineTools.symmetricalPoint(LineTools.symmetricalPoint(F, q),q).getX()!=0.5 || LineTools.symmetricalPoint(LineTools.symmetricalPoint(F, q),q).getY()!=0.5){
            System.out.println("Error in symmetricalPoint: When applying the function twice to a point, the result is not the same point.");
            noErrors = false;
        }

        //Testing distanceFromPointToLine...
        if(LineTools.distanceFromPointToLine(F, l)!=0.5){
            System.out.println("Error in distanceFromPointToLine: Does not calculate the distance from (0.5,0.5) to <(0,0),(0,1)>");
            noErrors = false;
        }
        if(LineTools.distanceFromPointToLine(D, k)!=0.5){
            System.out.println("Error in distanceFromPointToLine: Does not calculate the distance from (1.5,0.5) to <(0,0),(1,0)>");
            noErrors = false;
        }
        if(Math.abs(LineTools.distanceFromPointToLine(E, q)-Math.sqrt(0.135))>0.05){
            System.out.println("Error in distanceFromPointToLine: Does not calculate the distance from (1,0.5) to <(1,1),(1.5,0.5)>");
            noErrors = false;
        }
        
        //Testing angleBetweenLineAndDirection...
        Direction dir = new Direction(0);
        
        dir.setDirection(-Math.PI);
        if(LineTools.angleBetweenLineAndDirection(l, dir)!=Math.PI/2){
            System.out.println("Error in angleBetweenLineAndDirection: Test 01");
            noErrors = false;
        }
        if(LineTools.angleBetweenLineAndDirection(m, dir)!=-Math.PI/2){
            System.out.println("Error in angleBetweenLineAndDirection: Test 02");
            noErrors = false;
        }
        dir.setDirection(-3*Math.PI/4);
        if(LineTools.angleBetweenLineAndDirection(l, dir)!=3*Math.PI/4){
            System.out.println("Error in angleBetweenLineAndDirection: Test 03");
            noErrors = false;
        }
        if(LineTools.angleBetweenLineAndDirection(m, dir)!=-Math.PI/4){
            System.out.println("Error in angleBetweenLineAndDirection: Test 04");
            noErrors = false;
        }
        dir.setDirection(-Math.PI/2);
        if(LineTools.angleBetweenLineAndDirection(l, dir)!=Math.PI && LineTools.angleBetweenLineAndDirection(l, dir)!=-Math.PI){
            System.out.println("Error in angleBetweenLineAndDirection: Test 05");
            noErrors = false;
        }
        if(LineTools.angleBetweenLineAndDirection(m, dir)!=0){
            System.out.println("Error in angleBetweenLineAndDirection: Test 06");
            noErrors = false;
        }
        dir.setDirection(-Math.PI/4);
        if(LineTools.angleBetweenLineAndDirection(l, dir)!=-3*Math.PI/4){
            System.out.println("Error in angleBetweenLineAndDirection: Test 07");
            noErrors = false;
        }
        if(LineTools.angleBetweenLineAndDirection(m, dir)!=Math.PI/4){
            System.out.println("Error in angleBetweenLineAndDirection: Test 08");
            noErrors = false;
        }
        dir.setDirection(0);
        if(LineTools.angleBetweenLineAndDirection(l, dir)!=-Math.PI/2){
            System.out.println("Error in angleBetweenLineAndDirection: Test 09");
            noErrors = false;
        }
        if(LineTools.angleBetweenLineAndDirection(m, dir)!=Math.PI/2){
            System.out.println("Error in angleBetweenLineAndDirection: Test 10");
            noErrors = false;
        }
        dir.setDirection(Math.PI/4);
        if(LineTools.angleBetweenLineAndDirection(l, dir)!=-Math.PI/4){
            System.out.println("Error in angleBetweenLineAndDirection: Test 11");
            noErrors = false;
        }
        if(LineTools.angleBetweenLineAndDirection(m, dir)!=3*Math.PI/4){
            System.out.println("Error in angleBetweenLineAndDirection: Test 12");
            noErrors = false;
        }
        dir.setDirection(Math.PI/2);
        if(LineTools.angleBetweenLineAndDirection(l, dir)!=0){
            System.out.println("Error in angleBetweenLineAndDirection: Test 13");
            noErrors = false;
        }
        if(LineTools.angleBetweenLineAndDirection(m, dir)!=Math.PI && LineTools.angleBetweenLineAndDirection(m, dir)!=-Math.PI){
            System.out.println("Error in angleBetweenLineAndDirection: Test 14");
            noErrors = false;
        }
        dir.setDirection(3*Math.PI/4);
        if(LineTools.angleBetweenLineAndDirection(l, dir)!=Math.PI/4){
            System.out.println("Error in angleBetweenLineAndDirection: Test 15");
            noErrors = false;
        }
        if(LineTools.angleBetweenLineAndDirection(m, dir)!=-3*Math.PI/4){
            System.out.println("Error in angleBetweenLineAndDirection: Test 16");
            noErrors = false;
        }
        dir.setDirection(Math.PI);
        if(LineTools.angleBetweenLineAndDirection(l, dir)!=Math.PI/2){
            System.out.println("Error in angleBetweenLineAndDirection: Test 17");
            noErrors = false;
        }
        if(LineTools.angleBetweenLineAndDirection(m, dir)!=-Math.PI/2){
            System.out.println("Error in angleBetweenLineAndDirection: Test 18");
            noErrors = false;
        }
        
        //Testing sideOfLine...
        if(LineTools.sideOfLine(A, l)!=0){
            System.out.println("Error in sideOfLine: Test 01");
            noErrors = false;
        }
        if(LineTools.sideOfLine(F, l)!=-1){
            System.out.println("Error in sideOfLine: Test 02");
            noErrors = false;
        }
        if(LineTools.sideOfLine(F, m)!=-1){
            System.out.println("Error in sideOfLine: Test 03");
            noErrors = false;
        }
        if(LineTools.sideOfLine(B, o)!=-1){
            System.out.println("Error in sideOfLine: Test 04");
            noErrors = false;
        }
        if(LineTools.sideOfLine(H, o)!=1){
            System.out.println("Error in sideOfLine: Test 05");
            noErrors = false;
        }
        if(LineTools.sideOfLine(H, s)!=-1){
            System.out.println("Error in sideOfLine: Test 06");
            noErrors = false;
        }
        if(LineTools.sideOfLine(C, s)!=1){
            System.out.println("Error in sideOfLine: Test 07");
            noErrors = false;
        }
        
        //Testing formRectagleAroundPoint...
        dir.setDirection(-Math.PI);
        if(LineTools.formRectagleAroundPoint(F,dir,0.5,0.5)[0].getX()==0.25 && LineTools.formRectagleAroundPoint(F,dir,0.5,0.5)[0].getY()==0.25
                && LineTools.formRectagleAroundPoint(F,dir,0.5,0.5)[1].getX()==0.75 && LineTools.formRectagleAroundPoint(F,dir,0.5,0.5)[1].getY()==0.25
                && LineTools.formRectagleAroundPoint(F,dir,0.5,0.5)[2].getX()==0.75 && LineTools.formRectagleAroundPoint(F,dir,0.5,0.5)[2].getY()==0.75
                && LineTools.formRectagleAroundPoint(F,dir,0.5,0.5)[0].getX()==0.25 && LineTools.formRectagleAroundPoint(F,dir,0.5,0.5)[0].getY()==0.75){
            System.out.println("Error in formRectagleAroundPoint: Test 01");
            noErrors = false;
        }
        dir.setDirection(-Math.PI/4);
        if(LineTools.formRectagleAroundPoint(E,dir,0.5,0.5)[0].getX()==1 && LineTools.formRectagleAroundPoint(E,dir,0.5,0.5)[0].getY()==(0.5-(0.5/Math.sqrt(2)))
                && LineTools.formRectagleAroundPoint(E,dir,0.5,0.5)[1].getX()==(1+(0.5/Math.sqrt(2))) && LineTools.formRectagleAroundPoint(E,dir,0.5,0.5)[1].getY()==0.5
                && LineTools.formRectagleAroundPoint(E,dir,0.5,0.5)[2].getX()==1 && LineTools.formRectagleAroundPoint(E,dir,0.5,0.5)[2].getY()==(0.5+(0.5/Math.sqrt(2)))
                && LineTools.formRectagleAroundPoint(E,dir,0.5,0.5)[0].getX()==(1-(0.5/Math.sqrt(2))) && LineTools.formRectagleAroundPoint(E,dir,0.5,0.5)[0].getY()==0.5){
            System.out.println("Error in formRectagleAroundPoint: Test 02");
            noErrors = false;
        }
        
        //Testing lineIntersectingLines...
        Line[] lines = new Line[3];
        lines[0] = l;
        lines[1] = p;
        lines[2] = k;
        if(LineTools.lineIntersectingLines(o, lines)!=-1){
            System.out.println("Error in lineIntersectingLines: Test 01");
            noErrors = false;
        }
        lines[2] = m;
        if(LineTools.lineIntersectingLines(o, lines)!=2){
            System.out.println("Error in lineIntersectingLines: Test 02");
            noErrors = false;
        }
        lines[1] = r;
        if(LineTools.lineIntersectingLines(o, lines)!=1){
            System.out.println("Error in lineIntersectingLines: Test 03");
            noErrors = false;
        }
        lines[0] = k;
        lines[1] = o;
        lines[2] = n;
        if(LineTools.lineIntersectingLines(l, lines)!=2){
            System.out.println("Error in lineIntersectingLines: Test 04");
            noErrors = false;
        }
        
        //testing ballOnTheTable...
        dir.setDirection(Math.PI/2);
        Direction dir2 = new Direction(0);
        if(LineTools.ballOnTheTable(o, F, dir2, 0.5, 0.5, D, dir, 0.5, 0.5) != 9){
            System.out.println("Error in ballOnTheTable: Test 01");
            noErrors = false;
        }
        if(LineTools.ballOnTheTable(s, F, dir2, 0.5, 0.5, F, dir2, 0.5, 0.5) != 0){
            System.out.println("Error in ballOnTheTable: Test 02");
            noErrors = false;
        }        
        if(LineTools.ballOnTheTable(p, D, dir2, 0.5, 0.5, D, dir2, 0.5, 0.5) != 2){
            System.out.println(LineTools.ballOnTheTable(p, D, dir2, 0.5, 0.5, D, dir2, 0.5, 0.5));
            System.out.println("Error in ballOnTheTable: Test 03");
            noErrors = false;
        }        
                 
        if(noErrors) System.out.println("No errors. The functions are correct.");
    }
}

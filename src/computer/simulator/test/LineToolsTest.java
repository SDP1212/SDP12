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
 * NOTE: LineToolsTest does not test the function formLinesAroundPoint.
 * @author Evgeniya Sotirova
 */
public class LineToolsTest {
    public static void test_all(){
        test_isOnLineAndInRange();
        test_intersectionOfLines();
        test_symmetricalPoint();
        test_distanceFromPointToLine();
        test_angleBetweenLineAndDirection();
        test_sideOfLine();
        test_formRectagleAroundPoint();
        test_lineIntersectingLines();
        test_ballOnTheTable();
    }
    public static void test_isOnLineAndInRange(){
        boolean noErrors = true;
        
        Coordinates A = new Coordinates(0,0);
        Coordinates B = new Coordinates(1,0);
        Coordinates C = new Coordinates(0,1);
        Coordinates D = new Coordinates(2,0);
        Coordinates E = new Coordinates(1,1);
        Coordinates F = new Coordinates(0,2);
        Coordinates G = new Coordinates(0,-2);
        Coordinates H = new Coordinates(-1,0);
        
        Line k = new Line(A,B);
        Line l = new Line(B,A);
        Line m = new Line(A,B,0,1);
        Line n = new Line(B,A,0,1);
        Line o = new Line(A,C);
        Line p = new Line(C,A);
        Line q = new Line(A,C,0,1);
        Line r = new Line(C,A,0,1);
        
        if(k.isOnLineAndInRange(A)){
            System.out.println("Error in isOnLineAndInRange: Test 01");
            noErrors = false;
        }
        if(!k.isOnLineAndInRange(B)){
            System.out.println("Error in isOnLineAndInRange: Test 02");
            noErrors = false;
        }
        if(!k.isOnLineAndInRange(D)){
            System.out.println("Error in isOnLineAndInRange: Test 03");
            noErrors = false;
        }
        if(k.isOnLineAndInRange(E)){
            System.out.println("Error in isOnLineAndInRange: Test 04");
            noErrors = false;
        }
        if(!l.isOnLineAndInRange(A)){
            System.out.println("Error in isOnLineAndInRange: Test 05");
            noErrors = false;
        }
        if(l.isOnLineAndInRange(B)){
            System.out.println("Error in isOnLineAndInRange: Test 06");
            noErrors = false;
        }
        if(!l.isOnLineAndInRange(H)){
            System.out.println("Error in isOnLineAndInRange: Test 07");
            noErrors = false;
        }
        if(l.isOnLineAndInRange(G)){
            System.out.println("Error in isOnLineAndInRange: Test 08");
            noErrors = false;
        }
        if(!m.isOnLineAndInRange(A)){
            System.out.println("Error in isOnLineAndInRange: Test 09");
            noErrors = false;
        }
        if(!m.isOnLineAndInRange(B)){
            System.out.println("Error in isOnLineAndInRange: Test 10");
            noErrors = false;
        }
        if(m.isOnLineAndInRange(D)){
            System.out.println("Error in isOnLineAndInRange: Test 11");
            noErrors = false;
        }
        if(m.isOnLineAndInRange(E)){
            System.out.println("Error in isOnLineAndInRange: Test 12");
            noErrors = false;
        }
        if(!n.isOnLineAndInRange(A)){
            System.out.println("Error in isOnLineAndInRange: Test 13");
            noErrors = false;
        }
        if(!n.isOnLineAndInRange(B)){
            System.out.println("Error in isOnLineAndInRange: Test 14");
            noErrors = false;
        }
        if(n.isOnLineAndInRange(H)){
            System.out.println("Error in isOnLineAndInRange: Test 15");
            noErrors = false;
        }
        if(n.isOnLineAndInRange(C)){
            System.out.println("Error in isOnLineAndInRange: Test 16");
            noErrors = false;
        }
        if(o.isOnLineAndInRange(A)){
            System.out.println("Error in isOnLineAndInRange: Test 17");
            noErrors = false;
        }
        if(!o.isOnLineAndInRange(C)){
            System.out.println("Error in isOnLineAndInRange: Test 18");
            noErrors = false;
        }
        if(!o.isOnLineAndInRange(F)){
            System.out.println("Error in isOnLineAndInRange: Test 19");
            noErrors = false;
        }
        if(o.isOnLineAndInRange(D)){
            System.out.println("Error in isOnLineAndInRange: Test 20");
            noErrors = false;
        }
        if(!p.isOnLineAndInRange(A)){
            System.out.println("Error in isOnLineAndInRange: Test 21");
            noErrors = false;
        }
        if(p.isOnLineAndInRange(C)){
            System.out.println("Error in isOnLineAndInRange: Test 22");
            noErrors = false;
        }
        if(!p.isOnLineAndInRange(G)){
            System.out.println("Error in isOnLineAndInRange: Test 23");
            noErrors = false;
        }
        if(p.isOnLineAndInRange(E)){
            System.out.println("Error in isOnLineAndInRange: Test 24");
            noErrors = false;
        }
        if(!q.isOnLineAndInRange(A)){
            System.out.println("Error in isOnLineAndInRange: Test 25");
            noErrors = false;
        }
        if(!q.isOnLineAndInRange(C)){
            System.out.println("Error in isOnLineAndInRange: Test 26");
            noErrors = false;
        }
        if(q.isOnLineAndInRange(F)){
            System.out.println("Error in isOnLineAndInRange: Test 27");
            noErrors = false;
        }
        if(q.isOnLineAndInRange(H)){
            System.out.println("Error in isOnLineAndInRange: Test 28");
            noErrors = false;
        }
        if(!r.isOnLineAndInRange(A)){
            System.out.println("Error in isOnLineAndInRange: Test 29");
            noErrors = false;
        }
        if(!r.isOnLineAndInRange(C)){
            System.out.println("Error in isOnLineAndInRange: Test 30");
            noErrors = false;
        }
        if(r.isOnLineAndInRange(G)){
            System.out.println("Error in isOnLineAndInRange: Test 31");
            noErrors = false;
        }
        if(r.isOnLineAndInRange(D)){
            System.out.println("Error in isOnLineAndInRange: Test 32");
            noErrors = false;
        }
        
        if(noErrors) System.out.println("isOnLineAndInRange: No errors.");
    }
    public static void test_intersectionOfLines(){
        boolean noErrors = true;
        
        Coordinates A = new Coordinates(0,0);
        Coordinates B = new Coordinates(0,2);
        Coordinates C = new Coordinates(1,2);
        Coordinates D = new Coordinates(2,2);
        Coordinates E = new Coordinates(1,1);
        Coordinates F = new Coordinates(2,1);
        Coordinates G = new Coordinates(3,0);
        Coordinates H = new Coordinates(3,2);
        
        Line k = new Line(A,B);
        Line l = new Line(H,G);
        Line m = new Line(C,F);
        Line n = new Line(E,D);
        Line o = new Line(C,F,1,2);
        Line p = new Line(E,D,1,2);
        Line q = new Line(H,G,0,2);
        
        if(LineTools.intersectionOfLines(k,l) != null){
            System.out.println("Error in intersectionOfLines: Test 01.");
            noErrors = false;
        }
        if(LineTools.intersectionOfLines(k,m) != null){
            System.out.println("Error in intersectionOfLines: Test 02.");
            noErrors = false;
        }
        if(LineTools.intersectionOfLines(l,m).getX()!=3 && LineTools.intersectionOfLines(l,m).getY()!=0 ){
            System.out.println("Error in intersectionOfLines: Test 03.");
            noErrors = false;
        }
        if(LineTools.intersectionOfLines(o,p).getX()!=1.5 && LineTools.intersectionOfLines(m,n).getY()!=1.5){
            System.out.println("Error in intersectionOfLines: Test 04.");
            noErrors = false;
        }
        if(LineTools.intersectionOfLines(m,n) != null){
            System.out.println("Error in intersectionOfLines: Test 05.");
            noErrors = false;
        }
        if(LineTools.intersectionOfLines(q,n) != null){
            System.out.println("Error in intersectionOfLines: Test 06.");
            noErrors = false;
        }
        
        if(noErrors) System.out.println("intersectionOfLines: No errors.");
    }
    public static void test_symmetricalPoint(){
        boolean noErrors = true;
        
        Coordinates A = new Coordinates(0,0);
        Coordinates B = new Coordinates(1,0);
        Coordinates D = new Coordinates(1.5,0.5);
        Coordinates E = new Coordinates(1,0.5);
        Coordinates F = new Coordinates(0.5,0.5);
        Coordinates G = new Coordinates(2,1);
        Coordinates H = new Coordinates(1,1);
        Coordinates I = new Coordinates(0,1);
        
        Line l = new Line(A,I);
        Line n = new Line(I,G);
        Line o = new Line(F,E);
        Line q = new Line(H,D);
        Line r = new Line(B,D);
        
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
        
        if(noErrors) System.out.println("symmetricalPoint: No errors.");
        
    }
    public static void test_distanceFromPointToLine(){
        boolean noErrors = true;
        
        Coordinates A = new Coordinates(0,0);
        Coordinates B = new Coordinates(1,0);
        Coordinates D = new Coordinates(1.5,0.5);
        Coordinates E = new Coordinates(1,0.5);
        Coordinates F = new Coordinates(0.5,0.5);
        Coordinates H = new Coordinates(1,1);
        Coordinates I = new Coordinates(0,1);
        
        Line k = new Line(A,B);
        Line l = new Line(A,I);
        Line q = new Line(H,D);
        
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
                
        if(noErrors) System.out.println("distanceFromPointToLine: No errors.");
        
    }
    public static void test_angleBetweenLineAndDirection(){
        boolean noErrors = true;
        
        Coordinates A = new Coordinates(0,0);
        Coordinates C = new Coordinates(2,2);
        Coordinates G = new Coordinates(2,0);
        Coordinates I = new Coordinates(0,1);
        
        Line l = new Line(A,I);
        Line m = new Line(C,G);
        
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
                
        if(noErrors) System.out.println("angleBetweenLineAndDirection: No errors.");
        
    }
    public static void test_sideOfLine(){
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
        
        Line l = new Line(A,I);
        Line m = new Line(G,C);
        Line o = new Line(F,E);
        Line s = new Line(G,D);
                
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
        
        if(noErrors) System.out.println("sideOfLine: No errors.");
        
    }
    public static void test_formRectagleAroundPoint(){
        boolean noErrors = true;
        
        Coordinates E = new Coordinates(1,0.5);
        Coordinates F = new Coordinates(0.5,0.5);
        
        Direction dir = new Direction(-Math.PI);
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
        
        if(noErrors) System.out.println("formRectagleAroundPoint: No errors.");
    }
    public static void test_lineIntersectingLines(){
        boolean noErrors = true;
        
        Coordinates A = new Coordinates(0,0);
        Coordinates B = new Coordinates(3,0);
        Coordinates C = new Coordinates(1,1);
        Coordinates D = new Coordinates(2,1);
        Coordinates E = new Coordinates(3,1);
        Coordinates F = new Coordinates(5,1);
        Coordinates G = new Coordinates(3,2);
        Coordinates H = new Coordinates(4,2);
        Coordinates I = new Coordinates(1,3);
        Coordinates J = new Coordinates(1,4);
        Coordinates K = new Coordinates(3,4);
        Coordinates L = new Coordinates(4,5);
        
        Line[] lines = new Line[4];
        lines[0] = new Line(B,D,2,3);
        lines[1] = new Line(I,E,1,3);
        lines[2] = new Line(G,J,1,3);
        lines[3] = new Line(H,F,4,5);
        
        Line m = new Line(A,C);
        Line n = new Line(L,K);
        
        if(LineTools.lineIntersectingLines(lines[0], lines) != null){
            System.out.println("Error in lineIntersectingLines: Test 01");
            noErrors = false;
        }
        if(LineTools.lineIntersectingLines(m, lines).getGradient() != lines[1].getGradient() || LineTools.lineIntersectingLines(m, lines).getOffset() != lines[1].getOffset()){
            System.out.println("Error in lineIntersectingLines: Test 02");
            noErrors = false;
        }
        if(LineTools.lineIntersectingLines(n, lines).getGradient() != lines[2].getGradient() || LineTools.lineIntersectingLines(n, lines).getOffset() != lines[2].getOffset()){
            System.out.println("Error in lineIntersectingLines: Test 03");
            noErrors = false;
        }
        if(noErrors) System.out.println("lineIntersectingLines: No errors.");
    }
    public static void test_ballOnTheTable(){
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
        Coordinates J = new Coordinates(0,2);
        
        Line k = new Line(A,B);
        Line l = new Line(A,I);
        Line m = new Line(G,C);
        Line n = new Line(I,G);
        Line o = new Line(F,E);
        Line p = new Line(A,F);
        Line q = new Line(H,D);
        Line r = new Line(B,D);
        Line s = new Line(G,D);
        Line t = new Line(A,I,0,1);
        
        Direction dir = new Direction(Math.PI/2);
        Direction dir2 = new Direction(0);
        
        if(LineTools.ballOnTheTable(o, F, dir2, 0.5, 0.5, D, dir, 0.5, 0.5).getGradient() != LineTools.formLinesAroundPoint(D, dir, 0.5, 0.5)[1].getGradient()
                || LineTools.ballOnTheTable(o, F, dir2, 0.5, 0.5, D, dir, 0.5, 0.5).getOffset() != LineTools.formLinesAroundPoint(D, dir, 0.5, 0.5)[1].getOffset()){
            System.out.println("Error in ballOnTheTable: Test 01");
            noErrors = false;
        }
        if(LineTools.ballOnTheTable(s, F, dir2, 0.5, 0.5, F, dir2, 0.5, 0.5).getGradient() != 0
                || LineTools.ballOnTheTable(s, F, dir2, 0.5, 0.5, F, dir2, 0.5, 0.5).getOffset() != 0){
            System.out.println("Error in ballOnTheTable: Test 02");
            noErrors = false;
        }        
        if(LineTools.ballOnTheTable(p, D, dir2, 0.5, 0.5, D, dir2, 0.5, 0.5).getGradient() != 0
                || LineTools.ballOnTheTable(p, D, dir2, 0.5, 0.5, D, dir2, 0.5, 0.5).getOffset() != 1){
            System.out.println(LineTools.ballOnTheTable(p, D, dir2, 0.5, 0.5, D, dir2, 0.5, 0.5));
            System.out.println("Error in ballOnTheTable: Test 03");
            noErrors = false;
        }        
                 
        if(noErrors) System.out.println("ballOnTheTable: No errors.");
    }
}

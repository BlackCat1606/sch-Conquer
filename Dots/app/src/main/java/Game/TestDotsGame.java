package Game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/**
 * For Testing
 * Created by JiaHao on 10/2/15.
 */
public class TestDotsGame {
    public static void main(String[] args) {


        // Initialize

        DotsBoard board = new DotsBoard(5);
//        System.out.println(board);
        board.printWithIndex();
        DotsLogic logic = new DotsLogic(board);

//        Scanner scanner = new Scanner(System.in);
//        while (true) {
//            System.out.println("Enter x");
//            String xString = scanner.nextLine();
//            System.out.println("Enter y");
//            String yString = scanner.nextLine();
//
//            Point startPoint = new Point(Integer.parseInt(xString), Integer.parseInt(yString));
//
//            System.out.println("Enter x");
//            xString = scanner.nextLine();
//            System.out.println("Enter y");
//            yString = scanner.nextLine();
//
//            Point endPoint = new Point(Integer.parseInt(xString), Integer.parseInt(yString));
//
//            System.out.println(logic.checkAdjacency(startPoint, endPoint));
//
//        }


        // Test code
        Point point1 = new Point(0, 1);
        Point point2 = new Point(1,1);
        Point point3 = new Point(1,2);
        Point point4 = new Point(1,3);
        Point point5 = new Point(0,3);

        ArrayList<Point> pointList = new ArrayList<Point>();
        pointList.add(point1);
        pointList.add(point2);
        pointList.add(point3);
        pointList.add(point4);
        pointList.add(point5);


//        System.out.println(board.getElement(startPoint));
//        System.out.println(board.getElement(endPoint));
//        System.out.println(logic.checkAdjacency(startPoint, endPoint));
        logic.moveCompleted(pointList);
        System.out.println(board.toString());


    }
}

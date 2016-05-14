package quadtree;

import java.util.ArrayList;
import java.util.Random;

public class Quadtree {

    private Node grid;
    private static int maxdeep = 0;

    public int getMaxdeep() {
        return maxdeep;
    }

    public Node getGrid() {
        return grid;
    }
    
    public void setGrid(Node grid) {
        this.grid = grid;
    }
    
    public Quadtree(){
        
    }

    public Quadtree(int size, int numberOfPoints) {
        grid = new Node(new Point(0, 0), size, size);
        grid.setDeepness(0);
        Random rnd = new Random();
        for (int i = 0; i < numberOfPoints; i++) {
            addPoint(new Point(rnd.nextInt(size), rnd.nextInt(size)));
        }
    }

    public void addPoint(Point p) {
        int newDeep = grid.addPoint(p);
        maxdeep = Math.max(maxdeep, newDeep);
    }

    public ArrayList<Point> getClosests(int x, int y) {
        Node node = getNodeWherePointIs(x, y);
        ArrayList<Point> result = new ArrayList<>();
        Point point = new Point(x, y);
        if (node.getNodes().isEmpty()) {
            result.addAll(node.getPointsBut(point));
        } else {
            result = getDescendantPoints(node, point);
        }
        return result;
    }
    
    public Node getNodeWherePointIs(int x, int y) {
        return Quadtree.this.getNodeWherePointIs(new Point(x, y));
    }

    public Node getNodeWherePointIs(Point p) {
        return getNodeWherePointIs(grid, p);
    }

    public Node getNodeWherePointIs(Node node, Point point) {
        Node result = null;
        if (node.getNodes().isEmpty() && node.contains(point)) {
            result = node;
        } else if (node.contains(point)) {
                int isIn = pointIsInHowMuchChildNodes(point, node);
            if (isIn >= 2) {
                result = node;
            } else {
                for (Node childNode : node.getNodes()) {
                    Node tmp = getNodeWherePointIs(childNode, point);
                    if (tmp != null) {
                        result = tmp;
                        break;
                    }
                }
            }
        }

        return result;
    }
    
    public ArrayList<Point> getDescendantPoints(Node node, Point point) {
        ArrayList<Point> result = new ArrayList<>();
        if (point == null) {
            result.addAll(node.getPoints());
        } else {
            result.addAll(node.getPointsBut(point));
        }
        for (Node childNode : node.getNodes()) {
            result.addAll(getDescendantPoints(childNode, point));
        }
        return result;
    }
    
    private int pointIsInHowMuchChildNodes(Point point, Node node) {
        int isIn = 0;
        for (Node childNode : node.getNodes()) {
            if (childNode.contains(point)) {
                isIn++;
            }
        }
        return isIn;
    }

    

}

package Graph;

import java.awt.Point;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;

public class Node {
    //protected List<Edge> edges;
    
    public static int NODE_RADIUS = 20;
    
    protected int _id;
    protected Node current_neighbour;
    protected int height;
    
    
    // for GUI representation
    protected Shape s;
    protected int x, y;
    
    public Node(int i, int j) {
        this.x = i;
        this.y = j;
        this.s = new Ellipse2D.Double(i - (NODE_RADIUS/2), j - (NODE_RADIUS/2), NODE_RADIUS, NODE_RADIUS);        
    }

    public void set_id(int id){
        _id = id;
    }
    
    public int get_id(){
        return _id;
    }
    
    public void set_current_neighbour(Node n){
        current_neighbour = n;
    }
    
    public Node get_current_neighbour(){
        return current_neighbour;
    }
    
    public void set_height(int h){
        height = h;
    }
    
    public int get_height(){
        return height;
    }
    
    public Point get_position() {
        return new Point(this.x, this.y);
    }
    
    public void set_shape(Shape sh) {
        this.s = sh;
    }
    
    public Shape get_shape() {
        return this.s;
    }
}

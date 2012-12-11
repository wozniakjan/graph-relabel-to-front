package Graph;

import java.awt.Polygon;
import java.awt.Shape;
import java.awt.geom.Line2D;

public class Edge {
    private Node _n1;
    private Node _n2;
    private int _capacity;
    private int _flow;
    private int _id;
    
    private Shape s;
    
    public void set_id(int id){
        _id = id;
    }
    public int get_id(){
        return _id;
    }
    
    public Edge(Node from, Node to, int capacity){
        _n1 = from;
        _n2 = to;
        _capacity = capacity;
        _flow = 0;
        //this.s = new Line2D.Double(from.x, from.y, to.x, to.y);
        this.s = new Polygon(new int[] {from.x - 1, from.x + 1, to.x + 1, to.x - 1}, new int[] {from.y, from.y, to.y, to.y}, 4);
    }
    
    public Node from(){
        return _n1;
    }
    
    public Node to(){
        return _n2;
    }
    
    public int get_capacity(){
        return _capacity;
    }
    
    public void set_capacity(int c){
        _capacity = c;
    }
    
    public int get_flow(){
        return _flow;
    }
    
    public void set_flow(int flow){
        _flow = flow;
    }
    
    public int get_residual(){
        return _capacity - _flow;
    }
    
    public void set_residual(int r){
        _flow = _capacity - r;
    }
    
    public Shape get_shape() {
        return this.s;
    }
    
    public void delete(){ }
}

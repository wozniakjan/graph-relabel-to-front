package Graph;

public class Edge {
    private Node _n1;
    private Node _n2;
    private int _capacity;
    public Edge(Node from, Node to, int capacity){
        _n1 = from;
        _n2 = to;
        _capacity = capacity;
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
    
    public void delete(){ }
}

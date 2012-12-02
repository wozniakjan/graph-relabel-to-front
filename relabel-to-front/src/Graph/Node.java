package Graph;

public class Node {
    //protected List<Edge> edges;
    
    protected int _id;
    protected Node current_neighbour;
    protected int height;
    protected int excess;
    
    public Node(){
        //edges = new ArrayList();
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
}

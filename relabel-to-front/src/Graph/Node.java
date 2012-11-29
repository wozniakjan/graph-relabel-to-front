package Graph;
import java.util.*;

public class Node {
    private List<Edge> edges;
    private int _id;
    
    public Node(){
        edges = new ArrayList();
    }
    
    public void set_id(int id){
        _id = id;
    }
    
    public int get_id(){
        return _id;
    }
    
    public void add_edge(Edge e){
        edges.add(e);
    }
    
    public void remove_edge(int to_node_id){
        for(Edge e : edges){
            if(e.to().get_id() == to_node_id){
                edges.remove(e);
                return;
            }
        }
    }
    
    public List<Edge> get_edges(){
        return edges;
    }
    
    public Edge has_edge(int to_node_id){
        for(Edge e : edges){
            if(e.to().get_id() == to_node_id){
                return e;
            }
        }
        return null;
    }
    
    
    public void delete(){
        for(Edge e : edges){
            e.delete();
        }
    }
}

package Graph;

import java.util.*;

public class Graph {
    protected Map<Integer,Node> nodes;
    protected int node_id;
    
    protected Map<Integer,Edge> edges;
    protected int edge_id;
    
    boolean debug = true;
    
    public Graph(){
        nodes = new HashMap<>();
        node_id = 0;
        
        edges = new HashMap<>();
        edge_id = 0;
    }
    
    public Node add_node(Node n){
        node_id++;
        n.set_id(node_id);
        nodes.put(node_id, n);
        return n;
    }
    public void remove_node(int id){
        //nodes.get(id).delete();
        for(Edge e : edges.values()){
            if(e.from().get_id()==id || e.to().get_id()==id){
                edges.remove(e.get_id());
            }
        }
        nodes.remove(id);
    }
    public void add_edge(Edge e){
        e.set_id(edge_id);
        //e.from().add_edge(e);
        edges.put(edge_id, e);
        edge_id++;
    }
    public void remove_edge(int id_n1, int id_n2){
        //nodes.get(id_n1).remove_edge(id_n2);
        for(Edge e : edges.values()){
            if(e.from().get_id() == id_n1 && e.to().get_id() == id_n2){
                edges.remove(e.get_id());
            }
        }
    }
    public Edge get_edge(Node u, Node v){
        for(Edge e : edges.values()){
            if(e.from() == u && e.to() == v){
                return e;
            }
        }
        return null;
    }
    public Edge get_edge(int id_u, int id_v){
        for(Edge e : edges.values()){
            if(e.from().get_id() == id_u && e.to().get_id() == id_v){
                return e;
            }
        }
        return null;
    }
    public void print_as_matrix(){
        for(Node line : nodes.values()){
            for(Node column : nodes.values()){
                Edge e = null;
                for(Edge f : edges.values()){
                    if(f.from() == line && f.to() == column){
                        e = f;
                        break;
                    }
                }
                if(e == null){
                    System.out.print("0/0 ");
                }
                else {
                    System.out.print(e.get_capacity()+"/"+e.get_flow()+ " ");
                }
            }
            System.out.print("\n");
        }
    }
    public void print(){
        for(Node n : nodes.values()){
            System.out.print("node " + n.get_id() + ":");
            for(Edge e : edges.values()){
                if(e.from() == n){
                    System.out.print(e.to().get_id() + "("+ e.get_capacity()+"/"+e.get_flow() +"), ");
                }
            }
            System.out.print("\n");
        }
    }
    public void debug_msg(String s){
        if(debug){
            System.out.println(s);
        }
    }
}

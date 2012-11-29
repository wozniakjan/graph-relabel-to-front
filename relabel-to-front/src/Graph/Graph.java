package Graph;

import java.util.*;

public class Graph {
    private Map<Integer,Node> nodes;
    private int node_id;
    
    public Graph(){
        nodes = new HashMap<>();
        node_id = 0;
    }
    
    public Node add_node(Node n){
        node_id++;
        n.set_id(node_id);
        nodes.put(node_id, n);
        return n;
    }
    public void remove_node(int id){
        nodes.get(id).delete();
        nodes.remove(id);
    }
    public void add_edge(Edge e){
        e.from().add_edge(e);
    }
    public void remove_edge(int id_n1, int id_n2){
        nodes.get(id_n1).remove_edge(id_n2);
    }
    public void print_as_matrix(){
        for(Node line : nodes.values()){
            for(Node column : nodes.values()){
                Edge e = line.has_edge(column.get_id());
                if(e == null){
                    System.out.print("X ");
                }
                else {
                    System.out.print(e.get_capacity()+ " ");
                }
            }
            System.out.print("\n");
        }
    }
    public void print(){
        for(Node n : nodes.values()){
            System.out.print("node " + n.get_id() + ":");
            for(Edge e : n.get_edges()){
                System.out.print(e.to().get_id() + "("+ e.get_capacity() +"), ");
            }
            System.out.print("\n");
        }
    }
}

import Graph.*;

public class Main {
    public static void main(String[] args) {
        Graph g = new Graph();
        Node n1 = g.add_node(new Node());
        Node n2 = g.add_node(new Node());
        Node n3 = g.add_node(new Node());
        
        g.add_edge(new Edge(n1,n2,6));
        g.print();
        g.print_as_matrix();
    }
}

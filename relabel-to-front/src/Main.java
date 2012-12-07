import Graph.*;

public class Main {
    public static void main(String[] args) {
        RelabelToFrontGraph g = new RelabelToFrontGraph();
        Node n1 = g.add_node(new Node(10, 10));
        Node n2 = g.add_node(new Node(20, 20));
        Node n3 = g.add_node(new Node(30, 30));
        Node n4 = g.add_node(new Node(40, 40));
        
        g.add_edge(new Edge(n1,n2,6));
        g.add_edge(new Edge(n1,n3,4));
        g.add_edge(new Edge(n2,n3,2));
        g.add_edge(new Edge(n2,n4,3));
        g.add_edge(new Edge(n3,n4,8));
        
        //g.print();
        //g.print_as_matrix();
        System.out.println("maximum_flow:" + g.do_the_trick(n1, n4));
    }
}

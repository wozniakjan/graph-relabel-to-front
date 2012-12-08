/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Graph;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Test implements GraphListener{
    RelabelToFrontGraph g;
    BufferedReader in;

    public void yield(){
        try{
            in.readLine();               
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void change_flow(Node u, Node v, int value){
        System.out.println("flow " + u.get_id() + "->" + v.get_id() + "=" + value);
    }
    public void change_height(Node u, int value){
        System.out.println("height " + u.get_id() + "=" + value);
    }
    public void print_message(String message){
        System.out.println(message);
    }
    public void print_total_flow(int flow){
        System.out.println("total maximum flow is " + flow);
    }

    public Test(){
        g = new RelabelToFrontGraph();
        g.set_listener(this);
        
        in=new BufferedReader(new InputStreamReader(System.in));

        Node n1 = g.add_node(new Node(10, 10));
        Node n2 = g.add_node(new Node(20, 20));
        Node n3 = g.add_node(new Node(30, 30));
        Node n4 = g.add_node(new Node(40, 40));

        g.add_edge(new Edge(n1,n2,6));
        g.add_edge(new Edge(n1,n3,4));
        g.add_edge(new Edge(n2,n3,2));
        g.add_edge(new Edge(n2,n4,3));
        g.add_edge(new Edge(n3,n4,8));
        g.set_sink(n4);
        g.set_source(n1);

        g.run();
    }
}

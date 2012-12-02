package Graph;

import java.util.*;

public class RelabelToFrontGraph extends Graph {
    Node _source;
    Node _sink;
    
    int reversed_edge_id;
    
    Stack<Node> l;

    private void initialize(Node source, Node sink){
       // add_reversed_edges();
        _source = source;
        _sink = sink;
        l = new Stack<>();
        
        for(Node n : nodes.values()){
            n.set_height(0);
            //if(n != _source && n != _sink){
                l.add(n);
            //}
        }
        
        for(Edge e : edges.values()){
            e.set_flow(0);
            if(e.from()==_source){
                e.set_flow(e.get_capacity());
            }
        }
        _source.set_height(nodes.size());
    }
    
    /*protected void add_reversed_edges(){
    }*/
    
    
    private int total_flow(){
        int sum = 0;
        for(Edge e : edges.values()){
            if(e.from()==_source){
                sum += e.get_flow();
            }
        }
        return sum;
    }
    
    public int do_the_trick(Node source, Node sink){
        debug_msg("do_the_trick() -> initialize("+source.get_id()+","+sink.get_id()+")");
        initialize(source, sink);
        int old_height;
        
        debug_msg("do_the_trick() -> l.pop()");
        Node u=l.pop();
        
        while(u!=null){
            old_height = u.get_height();
            debug_msg("do_the_trick() -> discharge()"); 
            discharge(u);
            
            if(u.get_height()>old_height){
                debug_msg("do_the_trick() -> l.push()");
                l.push(u);
            }
            else{
                u=l.pop();
            }
        }
        
        return total_flow();
    }
    
    private void discharge(Node u){
        while(is_active(u)){
            for(Edge e : edges.values()){
                if(e.from()==u){
                    debug_msg("discharge() -> e == u");
                    Node v = e.to();
                    if(e.get_capacity()-e.get_flow()>0 && u.get_height()>v.get_height()){
                        debug_msg("discharge() -> edge is admissable");
                        push(u,v);
                    }
                }
            }
            relabel(u);
        }
    }
    
    private int excess(Node u){
        int in = 0;
        int out = 0;
        for(Edge e : edges.values()){
            if(e.from() == u){
                out += e.get_flow();
            }
            if(e.to() == u){
                in += e.get_flow();
            }
        }
        return in-out;
    }
    
    private void push(Node u, Node v){
        Edge e = get_edge(u,v);
        Edge reversed = get_edge(v,u);
        int send = Math.min(excess(u), e.get_residual());
        e.set_flow(e.get_flow()+send);
        reversed.set_flow(reversed.get_flow()-send);
        
    }
    
    private void relabel(Node u){
        int min_height = Integer.MAX_VALUE;
        for(Edge e : edges.values()){
            if(e.from() == u){
                Node v = e.to();
                if((e.get_capacity()-e.get_flow())>0 && u.get_height()>v.get_height()){
                    min_height = Math.min(min_height, v.get_height());
                    u.set_height(min_height+1);
                }
            }
        }
    }
    
    private boolean is_active(Node n){
        int in = 0;
        int out = 0;
        
        for(Edge e : edges.values()){
            if(e.from() == n){
                out+=e.get_flow();
            }
            if(e.to() == n){
                in+=e.get_flow();
            }
        }
        if(in>out){
            return true;
        }
        return false;
    }
}

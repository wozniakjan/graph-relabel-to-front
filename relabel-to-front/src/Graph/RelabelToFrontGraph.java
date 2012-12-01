package Graph;

import java.util.*;

public class RelabelToFrontGraph extends Graph {
    Node _source;
    Node _sink;
    
    Map<Integer, Integer> height;
    Map<Integer, Integer> excess;
    Map<Integer, Boolean> seen;
    
    private void initialize(Node source, Node sink){
        _source = source;
        _sink = sink;
    }
    
    public int count_max_flow(Node source, Node sink){
        initialize(source, sink);
        
        return 0;
    }
    
    private void discharge_node(Node u){
        
    }
    
    private void push(){
        
    }
}

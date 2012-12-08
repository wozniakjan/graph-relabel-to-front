package Graph;

import java.util.*;

public class RelabelToFrontGraph extends Graph implements Runnable {
    Node _source;
    int _source_index;
    Node _sink;
    int _sink_index;
    
    int n;
    int[][] F;
    int[][] C;
    int[] height;
    int[] excess;
    int[] seen;    
    int[] list;
    
    Node[] index;
    
    GraphListener listener;
    
    public void set_listener(GraphListener l){
        listener = l;
    }
    
    private void change_flow(int u, int v, int value, boolean yield, String message){
        F[u][v] = value;
        listener.change_flow(u, v, value);
        if(yield){
            listener.yield();
        }
        if(message!=""){
            listener.print_message(message);
        }
    }

    private void change_height(int u, int value, boolean yield, String message){
        height[u] = value;
        listener.change_height(u, value);
        if(yield){
            listener.yield();
        }
        if(message!=""){
            listener.print_message(message);
        }
    }
    
    private void initialize(Node source, Node sink){
        _source = source;
        _sink = sink;

        n = nodes.size();
        F = new int[n][n];
        C = new int[n][n];
        height = new int[n];
        excess = new int[n];
        seen = new int[n];
        list = new int[n-2];
        
        index = new Node[n];
        
        int i_u = 0, li = 0;
        for(Node u : nodes.values()){
            u.set_height(0);
            int i_v = 0;
            for(Node v : nodes.values()){
                change_flow(i_u, i_v, 0, false, "");
                Edge e = get_edge(u, v);
                if(e == null){
                    C[i_u][i_v] = 0;
                }
                else {
                    C[i_u][i_v] = e.get_capacity();
                }
                i_v++;
            }
            change_height(i_u, 0, false, "");
            excess[i_u]=0;
            seen[i_u]=0;
            index[i_u] = u;
            if(u!=_sink && u!=_source){
                list[li++]=i_u;
            }
            if(u==_sink){
                _sink_index = i_u;
            }
            if(u==_source){
                _source_index = i_u;
            }
            i_u++;
        }
        
        for(Edge e : edges.values()){
            e.set_flow(0);
            if(e.from()==_source){
                e.set_flow(e.get_capacity());
            }
        }
        _source.set_height(nodes.size());
    }
    
    public int total_flow(){
        int sum = 0;
        for(int i=0; i<n; i++){
            sum+=F[_source_index][i];
        }
        return sum;
    }
    
    public void shift_list(int si){
        int aux = list[si];
        
        for(int i = si; i>=1; i--){
            list[i] = list[i-1];
        }
        list[0] = aux;
    }
    
    public void set_sink(Node sink){
        _sink = sink;
    }
    
    public void set_source(Node source){
        _source = source;
    }
    
    public void run(){
        initialize(_source, _sink);
        change_height(_source_index, n, true, "");
        excess[_source_index] = Integer.MAX_VALUE;
        
        for(int v = 0; v<n; v++){
            push(_source_index, v);
        }
 
        int p = 0, u, old_height;
        while(p < list.length){
            u = list[p];
            old_height = height[u];
            discharge(u);
            if(height[u]>old_height){
                shift_list(p);
                p=0;
            }
            else {
                p+=1;
            }
        }
        
        listener.print_total_flow(total_flow());
    }
    
    private void discharge(int u){
       while(excess[u] > 0){
           if(seen[u] < n){
               int v = seen[u];
               if((C[u][v]-F[u][v])>0 && height[u]>height[v]){ //residual
                   push(u,v);
               }
               else {
                   seen[u] += 1;
               }
           }
           else{
               relabel(u);
               seen[u]=0;
           }
       }
    }
    
    private void push(int u, int v){
        int send = Math.min(excess[u], C[u][v] - F[u][v]);
        change_flow(u, v, F[u][v] + send, true, "");
        change_flow(v, u, F[v][u] - send, true, "");
        excess[u] -= send;
        excess[v] += send;
    }
    
    private void relabel(int u){
        int min_height = Integer.MAX_VALUE;
        for(int v = 0; v<n; v++){
            if((C[u][v] - F[u][v])>0){ //residual
                min_height = Math.min(min_height, height[v]);
                change_height(u, min_height+1, true, "");
            }
        }
    }
}

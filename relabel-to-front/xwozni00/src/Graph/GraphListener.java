/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Graph;

/**
 *
 * @author Jan
 */
public interface GraphListener {
    //Stays in this method until user requests another step
    public void yield();
    
    //Changes value of flow from Node U to Node V
    public void change_flow(Edge e, int value);
    
    //Changes height of Node U
    public void change_height(Node u, int value);
    
    //Prints lovely message for user
    public void print_message(String message);
    
    //Prints result value of total maximal flow in the graph
    public void print_total_flow(int flow);
}

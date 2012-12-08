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
    public void yield();
    public void change_flow(int u, int v, int value);
    public void change_height(int u, int value);
    public void print_message(String message);
    public void print_total_flow(int flow);
}

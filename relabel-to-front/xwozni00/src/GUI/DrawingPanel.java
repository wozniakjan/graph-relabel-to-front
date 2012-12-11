/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import Graph.*;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.util.Iterator;
import java.util.Map;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

/**
 *
 * @author Marek
 */
public class DrawingPanel extends javax.swing.JPanel implements ActionListener {

    private final int ARR_SIZE = 6;
    
    protected enum Mode { ADD_EDGE, EDIT, RUN }; 
    
    
    protected RelabelToFrontGraph gr;
    protected Thread gr_t;
    protected Mode state;
    protected JPopupMenu popMenu;
    protected JPopupMenu edgeMenu;
    protected Point clickedPoint;
    protected int clickedId;
    protected int sink;
    protected int source;
    protected GraphListener parent;
    protected Edge li_edge;
    protected Node li_node;
    
    
    /**
     * Creates new form DrawingPanel
     */
    public DrawingPanel(GraphListener gl) {
        this.gr = new RelabelToFrontGraph();
        this.parent = gl;
        this.gr.set_listener(gl);
        this.state = Mode.EDIT;
        this.source = -1;
        this.sink = -1;
        initComponents();
        this.initPopupMenu();
        
    }
    
    private void initPopupMenu() {
        this.popMenu = new JPopupMenu();
        JMenuItem menuItem = new JMenuItem("Add Node");
        menuItem.addActionListener(this);
        this.popMenu.add(menuItem);
        menuItem = new JMenuItem("Delete");
        menuItem.addActionListener(this);
        this.popMenu.add(menuItem);
        this.popMenu.addSeparator();
        menuItem = new JMenuItem("Start edge");
        menuItem.addActionListener(this);
        this.popMenu.add(menuItem);
        this.popMenu.addSeparator();
        menuItem = new JMenuItem("Set as source");
        menuItem.addActionListener(this);
        this.popMenu.add(menuItem);
        menuItem = new JMenuItem("Set as sink");
        menuItem.addActionListener(this);
        this.popMenu.add(menuItem);
        this.edgeMenu = new JPopupMenu();
        menuItem = new JMenuItem("Remove");
        menuItem.addActionListener(this);
        this.edgeMenu.add(menuItem);
        menuItem = new JMenuItem("Edit capacity");
        menuItem.addActionListener(this);
        this.edgeMenu.add(menuItem);
    }
    
    public void load_graph(Graph g) {
        this.gr = (RelabelToFrontGraph)g;
        this.gr.set_listener(this.parent);
        this.repaint();
    }
    
    public Graph get_graph() {
        return this.gr;
    }
    
    public int get_source_id() {
        return this.source;
    }
    
    public int get_sink_id() {
        return this.sink;
    }
    
    public void clear() {
        this.gr = new RelabelToFrontGraph();
        this.sink = -1;
        this.source = -1;
        this.state = Mode.EDIT;
        this.repaint();
    }
    
    public void highlight(Node n) {
        this.li_node = n;
        this.repaint();
    }
    
    public void highlight(Edge e) {
        this.li_edge = e; 
        this.repaint();
    }
    
    public void lightdown() {
        this.li_edge = null;
        this.li_node = null;
    }
    
    private void drawArrow(Graphics g1, int x1, int y1, int x2, int y2) {
        Graphics2D g = (Graphics2D) g1.create();
        double dx = x2 - x1, dy = y2 - y1;
        double angle = Math.atan2(dy, dx);
        int len = (int) Math.sqrt(dx*dx + dy*dy);
        AffineTransform at = AffineTransform.getTranslateInstance(x1, y1);
        at.concatenate(AffineTransform.getRotateInstance(angle));
        g.transform(at);

        // Draw horizontal arrow starting in (0, 0)
        g.drawLine(0, 0, len, 0);
        len = len/2;
        g.fillPolygon(new int[] {len, len-ARR_SIZE, len-ARR_SIZE, len},
                      new int[] {0, -ARR_SIZE, ARR_SIZE, 0}, 4);
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        Map.Entry entry;
        
        Iterator it = this.gr.get_edges_iter();
        while (it.hasNext()) {
            entry = (Map.Entry)it.next();
            Edge e = (Edge)entry.getValue();
            Point p1 = e.from().get_position();
            Point p2 = e.to().get_position();
            if (e.equals(this.li_edge)) {
                g2.setColor(Color.yellow);
                g2.draw(e.get_shape());
                g2.setColor(Color.black);
            }
            this.drawArrow(g, p1.x, p1.y, p2.x, p2.y);
            String e_msg = Integer.toString(e.get_flow()) + "/" + Integer.toString(e.get_capacity());
            if (e.equals(this.li_edge)) {
                g2.setColor(Color.red);
            }
            g2.drawChars(e_msg.toCharArray(), 0, e_msg.length(), p1.x + (p2.x - p1.x)/2 + 5, p1.y + (p2.y - p1.y)/2);
            if (e.equals(this.li_edge)) {
                g2.setColor(Color.black);
            }
        }

        it = this.gr.get_nodes_iter();
        while (it.hasNext()) {
            entry = (Map.Entry)it.next();
            Node n = (Node)entry.getValue();
            if (n.get_id() == this.sink) {
                g2.setColor(Color.red);
            } else if (n.get_id() == this.source){
                g2.setColor(Color.green);
            }
            if (n.equals(this.li_node)) {
                g2.setColor(Color.yellow);
            }
            g2.fill(n.get_shape());
            String label = "h:" + Integer.toString(n.get_height());
            g2.setColor(Color.white);
            g2.drawChars(Integer.toString(n.get_id()).toCharArray(), 0, Integer.toString(n.get_id()).length(),n.get_position().x - Node.NODE_RADIUS/5, n.get_position().y + Node.NODE_RADIUS/4);
            if (n.equals(this.li_node)) {
                g2.setColor(Color.red);
            } else {
                g2.setColor(Color.black);
            }
            g2.drawChars(label.toCharArray(), 0, label.length(), n.get_position().x + Node.NODE_RADIUS/2, n.get_position().y);
            g2.setColor(Color.black);
            if (n.get_id() == this.clickedId && this.state == Mode.ADD_EDGE) {
                g2.drawLine(n.get_position().x, n.get_position().y, this.clickedPoint.x, this.clickedPoint.y);
            }
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setBackground(new java.awt.Color(255, 255, 255));
        setPreferredSize(new java.awt.Dimension(508, 424));
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                formMouseClicked(evt);
            }
        });
        addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                formMouseMoved(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 508, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 424, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void formMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseClicked
        // TODO add your handling code here:
        Iterator it;
        Map.Entry entry;
        if (this.state == Mode.ADD_EDGE) {
            it = this.gr.get_nodes_iter();         
            while (it.hasNext()) {
                entry = (Map.Entry)it.next();
                Node n = (Node)entry.getValue();
                if (n.get_shape().contains(evt.getPoint())) {
                    int c;
                    try {
                        c = Integer.parseInt(JOptionPane.showInputDialog(this, "Capacity:", "Edge Addition", JOptionPane.QUESTION_MESSAGE));
                        //this.gr.get_edge(this.clickedId).set_capacity(c);
                        Edge e = new Edge(this.gr.get_node(this.clickedId), n, c);
                        this.gr.add_edge(e);
                    } catch (Exception exc) {
                        JOptionPane.showMessageDialog(this, exc.getMessage(), "Entered capacity is not a number!", JOptionPane.ERROR_MESSAGE);
                    }                    
                    break;
                }
            }
            this.state = Mode.EDIT;
            this.repaint();
        } else if (evt.getButton() == java.awt.event.MouseEvent.BUTTON3) {
            this.clickedPoint = evt.getPoint();
            it = this.gr.get_edges_iter();
            boolean con = false;
            
            while (it.hasNext()) {
                entry = (Map.Entry)it.next();
                Edge e = (Edge)entry.getValue();
                if (e.get_shape().contains(clickedPoint)) {
                    this.clickedId = e.get_id();
                    con = true;
                    break;
                }
            }
            
            if (con) {
                this.edgeMenu.show(this, evt.getX(), evt.getY());
                return;
            }
            
            it = this.gr.get_nodes_iter();         
            while (it.hasNext()) {
                entry = (Map.Entry)it.next();
                Node n = (Node)entry.getValue();
                if (n.get_shape().contains(clickedPoint)) {
                    this.clickedId = n.get_id();
                    con = true;
                    break;
                }
            }
            if (con) {
                this.popMenu.getComponent(0).setEnabled(false); // add node
                this.popMenu.getComponent(1).setEnabled(true); // delete
                this.popMenu.getComponent(3).setEnabled(true); // start edge
                this.popMenu.getComponent(5).setEnabled(true); // set as source
                this.popMenu.getComponent(6).setEnabled(true); // set as sink
            } else {
                this.popMenu.getComponent(0).setEnabled(true); // add node
                this.popMenu.getComponent(1).setEnabled(false); // delete
                this.popMenu.getComponent(3).setEnabled(false); // start edge
                this.popMenu.getComponent(5).setEnabled(false); // set as source
                this.popMenu.getComponent(6).setEnabled(false); // set as sink
            }
            this.popMenu.show(this, evt.getX(), evt.getY());
        }
    }//GEN-LAST:event_formMouseClicked

    private void formMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseMoved
        if (this.state == Mode.ADD_EDGE) {
            this.clickedPoint = evt.getPoint();
            this.repaint();
        }
    }//GEN-LAST:event_formMouseMoved

    @Override
    public void actionPerformed(ActionEvent e) {
        JMenuItem action = (JMenuItem)e.getSource();
        switch (action.getText()) {
            case "Add Node":
                //int h = Integer.parseInt(JOptionPane.showInputDialog(this, "Height:", "Node Addition", JOptionPane.QUESTION_MESSAGE));
                Node n = new Node(this.clickedPoint.x, this.clickedPoint.y);
                //n.set_height(h);
                this.gr.add_node(n);
                this.repaint();
                break;
            /*case "Edit Height":
                int n_h = Integer.parseInt(JOptionPane.showInputDialog(this, "Height:", (Object)this.gr.get_node(this.clickedId).get_height()));
                this.gr.get_node(this.clickedId).set_height(n_h);
                this.repaint();
                break;*/
            case "Start edge":
                this.state = Mode.ADD_EDGE;
                break;
            case "Delete":
                this.gr.remove_node(this.clickedId);
                this.repaint();
                break;
            case "Set as source":
                this.source = this.clickedId;
                this.gr.set_source(this.gr.get_node(source));
                this.repaint();
                break;
            case "Set as sink":
                this.sink = this.clickedId;
                this.gr.set_sink(this.gr.get_node(sink));
                this.repaint();
                break;
            case "Remove":
                this.gr.remove_edge(this.clickedId);
                this.repaint();
                break;
            case "Edit capacity":
                int c;
                try {
                    c = Integer.parseInt(JOptionPane.showInputDialog(this, "Capacity:", (Object)this.gr.get_edge(this.clickedId).get_capacity()));
                    this.gr.get_edge(this.clickedId).set_capacity(c);
                } catch (Exception exc) {
                    JOptionPane.showMessageDialog(this, exc.getMessage(), "Entered capacity is not a number!", JOptionPane.ERROR_MESSAGE);
                }
                this.repaint();
                break;
        }
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}

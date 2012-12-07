package Graph;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

public class Graph {
    protected Map<Integer,Node> nodes;
    protected int node_id;
    
    protected Map<Integer,Edge> edges;
    protected int edge_id;
    
    protected enum ReadState { NODE, EDGE, NONE, DATA, EXPECT_X, EXPECT_Y, EXPECT_CAP, EXPECT_HEIGHT };
    
    boolean debug = true;
    
    public Graph(){
        nodes = new HashMap<>();
        node_id = 0;
        
        edges = new HashMap<>();
        edge_id = 0;
    }
    
    public Node add_node(Node n){
        node_id++;
        n.set_id(node_id);
        nodes.put(node_id, n);
        return n;
    }
    
    public void remove_node(int id){
        //nodes.get(id).delete();
        for(Edge e : edges.values()){
            if(e.from().get_id()==id || e.to().get_id()==id){
                edges.remove(e.get_id());
            }
        }
        nodes.remove(id);
    }
    
    public Node get_node(int id) {
        return this.nodes.get(id);
    }
    
    public Iterator get_nodes_iter() {
        return this.nodes.entrySet().iterator();
    }
    
    public int get_size() {
        return this.nodes.size();
    }
    
    public void add_edge(Edge e){
        e.set_id(edge_id);
        //e.from().add_edge(e);
        edges.put(edge_id, e);
        edge_id++;
    }
    public void remove_edge(int id_n1, int id_n2){
        //nodes.get(id_n1).remove_edge(id_n2);
        for(Edge e : edges.values()){
            if(e.from().get_id() == id_n1 && e.to().get_id() == id_n2){
                edges.remove(e.get_id());
            }
        }
    }
    
    public void remove_edge(int id) {
        edges.remove(id);
    }
    
    public Edge get_edge(Node u, Node v){
        for(Edge e : edges.values()){
            if(e.from() == u && e.to() == v){
                return e;
            }
        }
        return null;
    }

    public Edge get_edge(int id_u, int id_v){
        for(Edge e : edges.values()){
            if(e.from().get_id() == id_u && e.to().get_id() == id_v){
                return e;
            }
        }
        return null;
    }
    
    public Edge get_edge(int id) {
        return edges.get(id);
    }
    
    public Iterator get_edges_iter() {
        return this.edges.entrySet().iterator();
    }
    
    public boolean load_from_XML(File input) {
        ReadState state = ReadState.NONE;
        ReadState previous = ReadState.NONE;
        int n_id = 0, n_x = 0, n_y = 0, n_h = 0;
        ArrayList<Integer> from = new ArrayList<>();
        ArrayList<Integer> to = new ArrayList<>();
        ArrayList<Integer> cap = new ArrayList<>();
        
        XMLInputFactory xin = XMLInputFactory.newInstance();        
        try {
            XMLEventReader xrdr = xin.createXMLEventReader(new FileReader(input));            
            while (xrdr.hasNext()) {
                XMLEvent event = xrdr.nextEvent();
                if (event.isStartElement()) {
                    StartElement start = (StartElement) event;
                    if (start.getName().getLocalPart().equalsIgnoreCase("node")) {
                        state = ReadState.NODE;
                        n_id = Integer.parseInt(start.getAttributeByName(new QName("id")).getValue());
                    } else if (start.getName().getLocalPart().equalsIgnoreCase("edge")) {
                        state = ReadState.EDGE;
                        from.add(Integer.parseInt(start.getAttributeByName(new QName("source")).getValue()));
                        to.add(Integer.parseInt(start.getAttributeByName(new QName("target")).getValue()));
                    } else if (start.getName().getLocalPart().equalsIgnoreCase("data") && state == ReadState.NODE) {
                        previous = ReadState.NODE;
                        switch (start.getAttributeByName(new QName("key")).getValue()) {
                            case "x":
                                state = ReadState.EXPECT_X;
                                break;
                            case "y":
                                state = ReadState.EXPECT_Y;
                                break;
                            case "height":
                                state = ReadState.EXPECT_HEIGHT;
                                break;
                        }
                    } else if (start.getName().getLocalPart().equalsIgnoreCase("data") && state == ReadState.EDGE) {
                        previous = ReadState.EDGE;
                        state = ReadState.EXPECT_CAP;
                    }
                } else if (event.isCharacters()) {
                    Characters ch = (Characters) event;
                    switch (state) {
                        case EXPECT_X:
                            n_x = Integer.parseInt(ch.getData());
                            break;
                        case EXPECT_Y:
                            n_y = Integer.parseInt(ch.getData());
                            break;
                        case EXPECT_HEIGHT:
                            n_h = Integer.parseInt(ch.getData());
                            break;
                        case EXPECT_CAP:
                            cap.add(Integer.parseInt(ch.getData()));
                            break;
                    }
                } else if (event.isEndElement()) {
                    if (state == ReadState.NODE) {
                        state = ReadState.NONE;
                        Node n = new Node(n_x, n_y);
                        n.set_id(n_id);
                        n.set_height(n_h);
                        this.add_node(n);
                    } else if (state == ReadState.EDGE) {
                        state = ReadState.NONE;
                    } else {
                        state = previous;
                    }
                }
            }
            // add edges
            for (int i =0; i < from.size(); i++) {
                this.add_edge(new Edge(this.get_node(from.get(i)), this.get_node(to.get(i)), cap.get(i)));
            }
        } catch (Exception ex) {
            Logger.getLogger(Graph.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }
    
    public boolean save_to_XML(File output) {
        XMLOutputFactory xout = XMLOutputFactory.newInstance();
        XMLStreamWriter xwrt;
        try {
            xwrt = xout.createXMLStreamWriter(new FileWriter(output));
            xwrt.writeStartDocument();
            xwrt.writeStartElement("graphml");
            xwrt.writeAttribute("xmlns", "http://graphml.graphdrawing.org/xmlns");
            xwrt.writeAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
            xwrt.writeAttribute("xsi:schemaLocation", "http://graphml.graphdrawing.org/xmlns http://graphml.graphdrawing.org/xmlns/1.0/graphml.xsd");
            xwrt.writeStartElement("key");
            xwrt.writeAttribute("id", "height");
            xwrt.writeAttribute("for", "node");
            xwrt.writeAttribute("attr.name", "height");
            xwrt.writeAttribute("attr.type", "integer");
            xwrt.writeStartElement("default");
            xwrt.writeCharacters("0");
            xwrt.writeEndElement();
            xwrt.writeEndElement();
            xwrt.writeStartElement("key");
            xwrt.writeAttribute("id", "x");
            xwrt.writeAttribute("for", "node");
            xwrt.writeAttribute("attr.name", "x_position");
            xwrt.writeAttribute("attr.type", "integer");
            xwrt.writeEndElement();
            xwrt.writeStartElement("key");
            xwrt.writeAttribute("id", "y");
            xwrt.writeAttribute("for", "node");
            xwrt.writeAttribute("attr.name", "y_position");
            xwrt.writeAttribute("attr.type", "integer");
            xwrt.writeEndElement();
            xwrt.writeStartElement("key");
            xwrt.writeAttribute("id", "capacity");
            xwrt.writeAttribute("for", "edge");
            xwrt.writeAttribute("attr.name", "capacity");
            xwrt.writeAttribute("attr.type", "integer");
            xwrt.writeEndElement();
            xwrt.writeStartElement("graph");
            xwrt.writeAttribute("id", "G");
            xwrt.writeAttribute("edgedefault", "directed");
            
            // write nodes into xml
            Iterator it = this.get_nodes_iter();
            Map.Entry entry;
            
            while (it.hasNext()) {
                entry = (Map.Entry)it.next();
                Node n = (Node)entry.getValue();
                xwrt.writeStartElement("node");
                xwrt.writeAttribute("id", Integer.toString(n.get_id()));
                
                xwrt.writeStartElement("data");
                xwrt.writeAttribute("key", "x");
                xwrt.writeCharacters(Integer.toString(n.get_position().x));
                xwrt.writeEndElement();
                
                xwrt.writeStartElement("data");
                xwrt.writeAttribute("key", "y");
                xwrt.writeCharacters(Integer.toString(n.get_position().y));
                xwrt.writeEndElement();
                
                xwrt.writeStartElement("data");
                xwrt.writeAttribute("key", "height");
                xwrt.writeCharacters(Integer.toString(n.get_height()));
                xwrt.writeEndElement();
                
                xwrt.writeEndElement();
            }
            
            // write edges into xml
            it = this.get_edges_iter();
            while (it.hasNext()) {
                entry = (Map.Entry)it.next();
                Edge e = (Edge)entry.getValue();
                
                xwrt.writeStartElement("edge");
                
                xwrt.writeAttribute("id", Integer.toString(e.get_id()));
                xwrt.writeAttribute("source", Integer.toString(e.from().get_id()));
                xwrt.writeAttribute("target", Integer.toString(e.to().get_id()));
                xwrt.writeStartElement("data");
                xwrt.writeAttribute("key", "capacity");
                xwrt.writeCharacters(Integer.toString(e.get_capacity()));
                xwrt.writeEndElement();
                
                xwrt.writeEndElement();
            }
            
            xwrt.writeEndElement();
            xwrt.writeEndElement();
            xwrt.writeEndDocument();
            xwrt.flush();
            xwrt.close();
        } catch (Exception ex) {
            Logger.getLogger(Graph.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }
    
    public void print_as_matrix(){
        for(Node line : nodes.values()){
            for(Node column : nodes.values()){
                Edge e = null;
                for(Edge f : edges.values()){
                    if(f.from() == line && f.to() == column){
                        e = f;
                        break;
                    }
                }
                if(e == null){
                    System.out.print("0/0 ");
                }
                else {
                    System.out.print(e.get_capacity()+"/"+e.get_flow()+ " ");
                }
            }
            System.out.print("\n");
        }
    }
    public void print(){
        for(Node n : nodes.values()){
            System.out.print("node " + n.get_id() + ":");
            for(Edge e : edges.values()){
                if(e.from() == n){
                    System.out.print(e.to().get_id() + "("+ e.get_capacity()+"/"+e.get_flow() +"), ");
                }
            }
            System.out.print("\n");
        }
    }
    public void debug_msg(String s){
        if(debug){
            System.out.println(s);
        }
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xmlska;

/**
 *
 * @author aleks
 */
public class Node {

    private java.util.Vector<Node> subNodes = new java.util.Vector<Node>();
    private java.util.HashMap<String, String> attributes = new java.util.HashMap<String, String>();
    private String name,  value;
    private Node parent;
    private boolean head;

    public Node() {
        head = false;
    }

    public boolean isHead() {
        return head;
    }

    public Node(boolean head) {
        this.head = head;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public void linkUp(Node parent) {
        this.parent = parent;
        parent.getSubNodes().add(this);
    }

    public void linkDown(Node child) {
        subNodes.add(child);
        child.setParent(this);
    }

    public String removeAttribute(Object key) {
        return attributes.remove(key);
    }

    public void addAttributes(java.util.HashMap<String, String> m) {
        if (m != null && m.size() > 0)
            attributes.putAll(m);
    }

    public String addAttribute(String key, String value) {
        return attributes.put(key, value);
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public Node getParent() {
        return parent;
    }

    public java.util.Vector<Node> getSubNodes() {
        return subNodes;
    }

    public boolean hasValue() {
        return value == null;
    }

    public String getValue() {
        return value;
    }

    public void setHead(boolean head) {
        this.head = head;
    }

    public String getAttributeValue(String name) {
        return attributes.get(name);
    }

    public java.util.Set<String> getAttributes() {
        return attributes.keySet();
    }

    public void setAttribute(String name, String value) {
        attributes.put(name, value);
    }

    @Override
    public String toString() {
        String ts = "Node name=\"" + name + "\" value=\"" + value + ((parent != null) ? "\" parent=\"" + parent.name : "") + "\" attributes=" + attributes + "\n";
        for (int i = 0; i < subNodes.size(); i++) {
            ts += subNodes.get(i);

        }
        return ts;
    }

    public java.util.Vector<Node> getChildren(String name) {
        java.util.Vector<Node> nodes = new java.util.Vector<Node>();
        for (int i = 0; i < subNodes.size(); i++) {
            if (subNodes.get(i).getName().equals(name))
                nodes.add(subNodes.get(i));
        }
        return nodes;
    }

    public boolean hasChild(String name) {
        return getChild(name) != null;
    }

    public Node getChild(String name) {
        for (int i = 0; i < subNodes.size(); i++) {
            if (subNodes.get(i).getName() != null && subNodes.get(i).getName().equals(name))
                return subNodes.get(i);
        }
        return null;
    }

    public boolean hasAttribute(String name) {
        return attributes.containsKey(name);
    }

    public static String convertToReadable(String in) {
        String out = new String(in);
        out = out.replaceAll("&amp;", "&");
        out = out.replaceAll("&lt;", "<");
        out = out.replaceAll("&gt;", ">");
        out = out.replaceAll("&apos;", "'");
        out = out.replaceAll("&quot;", "\"");
        return out;
    }

    public static String convertFromReadable(String in) {
        String out = new String(in);
        out = out.replaceAll("&", "&amp;");
        out = out.replaceAll("<", "&lt;");
        out = out.replaceAll(">", "&gt;");
        out = out.replaceAll("'", "&apos;");
        out = out.replaceAll("\"", "&quot;");
        return out;
    }
}

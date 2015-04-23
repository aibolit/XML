/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xmlska;

/**
 *
 * @author aleks
 */
public class Writer {

    public static void write(Node head, java.io.File file) throws java.io.IOException {
        if (!head.isHead()) {
            throw new java.lang.IllegalArgumentException("node not a head");
        }
        String xml = getXMLString(head);
        java.io.PrintWriter out = new java.io.PrintWriter(new java.io.BufferedWriter(new java.io.FileWriter(file)));
        out.print(xml);
        out.close();
    }

    public static String getXMLString(Node head) {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
        xml += process(head, 0);
        return xml;
    }

    private static String process(Node node, int t) {
        String ts = "";
        for (int i = 0; i < t; i++)
            ts += "\t";
        if (node instanceof CDATANode) {
            return node.toString() + "\n";
        } else if (node instanceof CommentNode){
            return node.toString() + "\n";
        }else {
            String xmlnode = "";
            xmlnode += ts + "<" + node.getName();
            for (String key : node.getAttributes()) {
                String val = node.getAttributeValue(key);
                xmlnode += " " + key + "=\"" + val + "\"";
            }
            if (node.getSubNodes().size() > 0) {
                xmlnode += ">\n";
                for (int i = 0; i < node.getSubNodes().size(); i++) {
                    Node n = node.getSubNodes().get(i);
                    xmlnode += process(n, t + 1);
                }
                xmlnode += ts + "</" + node.getName() + ">\n";
            } else {
                boolean valnull = node.getValue() == null || node.getValue().length() == 0;
                xmlnode += ((valnull) ? "/>\n" : ">") + ((valnull) ? "" : node.getValue() + "</" + node.getName() + ">\n");
            }
            return xmlnode;

        }
    }

}

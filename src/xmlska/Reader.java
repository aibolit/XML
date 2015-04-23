/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xmlska;

/**
 *
 * @author aleks
 */
public class Reader {

    public static Node Read(java.io.File f) throws java.io.IOException {
        String filepath = f.getAbsolutePath().substring(0, f.getAbsolutePath().lastIndexOf(java.io.File.separator) + 1);
        java.util.Stack<Node> nodeStack = new java.util.Stack<Node>();
        nodeStack.push(new Node(true));
        Node tree = nodeStack.peek();
        java.io.BufferedReader in = new java.io.BufferedReader(new java.io.FileReader(f));
        String line;
        StringBuilder sb = new StringBuilder();
        while ((line = in.readLine()) != null)
            sb.append(line).append("\n");
        String xml = sb.toString();
        int o = xml.indexOf("<?xml");
        int c = xml.indexOf("?>", o);
        if (o < 0 || c < 0)
            throw new java.lang.IllegalArgumentException("bad format xml");
        validateXMLtag(xml.substring(o + 5, c));
        c = process(nodeStack, c + 2, xml, filepath);
        while (c > 0) c = process(nodeStack, c, xml, filepath);
        if (c == -2)
            throw new java.lang.IllegalArgumentException("bad format xml");
        tree.getSubNodes().firstElement().setHead(true);
        return tree.getSubNodes().firstElement();
    }

    public static Node createXMLFromString(String xml) {
        java.util.Stack<Node> nodeStack = new java.util.Stack<Node>();
        nodeStack.push(new Node(true));
        Node tree = nodeStack.peek();
        int o = xml.indexOf("<?xml");
        int c = xml.indexOf("?>", o);
        if (o < 0 || c < 0)
            throw new java.lang.IllegalArgumentException("bad format xml");
        validateXMLtag(xml.substring(o + 5, c));
        c = process(nodeStack, c + 2, xml, "");
        while (c > 0) c = process(nodeStack, c, xml, "");
        if (c == -2)
            throw new java.lang.IllegalArgumentException("bad format xml");
        tree.getSubNodes().firstElement().setHead(true);
        return tree.getSubNodes().firstElement();
    }

    private static void validateXMLtag(String tag) {
        java.util.HashMap<String, String> nv = processAttributes(tag);
        try {
            if (!(nv.get("encoding").equals("UTF-8") && nv.get("version").equals("1.0")))
                throw new java.lang.IllegalArgumentException("bad format xml");
        } catch (NullPointerException e) {
            System.err.println("XML EMCODING WARNING!!!!!");
        }
    }

    private static int process(java.util.Stack<Node> nodeStack, int idx, String xml, String filepath) {
        int o = xml.indexOf("<", idx) + 1, c = xml.indexOf(">", o);
        if (o == 0) return -2;
        String line = xml.substring(o, c);
        if (line.substring(0, 1).equals("/")) {
            if (line.substring(1).equals(nodeStack.peek().getName())) {
                if (nodeStack.pop().isHead()) return -1;
            } else
                throw new java.lang.IllegalArgumentException("bad format xml-tag close \"" + nodeStack.peek().getName() + "\" \"" + line + "\"");
            if (nodeStack.peek().isHead()) return -1;
        } else if (line.length() >= 3 && line.substring(0, 3).equals("!--")) {
            if (!line.substring(line.length() - 2).equals("--"))
                line = xml.substring(o, c = xml.indexOf("-->", c));
            line = line.substring(3, line.length() - 2);
            int i = line.indexOf("#include");
            if (i >= 0) {
                try {
                    i = line.indexOf("\"", i);
                    int j = line.indexOf("\"", i + 1);
                    String fname = line.substring(i + 1, j);
                    java.io.File file = new java.io.File(fname);
                    file = (file.exists()) ? file : new java.io.File(filepath + fname);
                    java.io.BufferedReader in = new java.io.BufferedReader(new java.io.FileReader(file));
                    String line2, nml = "";
                    while ((line2 = in.readLine()) != null)
                        nml += line2;
                    int d = 0;
                    Node inclval = new Node();
                    inclval.setParent(nodeStack.peek());
                    nodeStack.peek().getSubNodes().add(inclval);
                    inclval.setName("#include");
                    inclval.setValue(filepath + fname);
                    while (d >= 0) d = process(nodeStack, d, nml, filepath);
                } catch (java.io.IOException ex) {
                    ex.printStackTrace();
                }
            } else {
                CommentNode top = new CommentNode();
                top.setData(line);
                nodeStack.peek().getSubNodes().add(top);
                top.setParent(nodeStack.peek());
                nodeStack.push(top);
                nodeStack.pop();
            }
        } else if (line.length() > 8 && line.substring(0, 8).equals("![CDATA[")) {//[[
            if (!line.substring(line.length() - 2).equals("]]"))
                line = xml.substring(o, c = xml.indexOf("]]>", c));
            line = line.substring(8, line.length() - 2);
            CDATANode top = new CDATANode();
            top.setData(line);
            nodeStack.peek().getSubNodes().add(top);
            top.setParent(nodeStack.peek());
            nodeStack.push(top);
            nodeStack.pop();
        } else {
            Node top = new Node();
            nodeStack.peek().getSubNodes().add(top);
            top.setParent(nodeStack.peek());
            nodeStack.push(top);
            int s = line.indexOf(" ");
            if (s < 0) {
                nodeStack.peek().setName(line);
                if (line.substring(line.length() - 1).equals("/"))
                    nodeStack.pop().setName(line.substring(0, line.length() - 1));
            } else {
                nodeStack.peek().setName(line.substring(0, s));
                java.util.HashMap<String, String> atts = processAttributes(line.substring(s));
                if (!atts.isEmpty()) nodeStack.peek().addAttributes(atts);
                if (line.substring(line.length() - 1).equals("/"))
                    nodeStack.pop();

            }
            if (!(new java.util.StringTokenizer(xml.substring(c + 1))).nextToken().substring(0, 1).equals("<")) {
                nodeStack.peek().setValue(xml.substring(c + 1, xml.indexOf("<", c + 1)));
                int l = xml.indexOf("<", c + 1) + 1;
                if (!xml.substring(l, l + 1).equals("/"))
                    throw new java.lang.IllegalArgumentException("bad format xml");
            }
        }
        return c + 1;
    }

    private static java.util.HashMap<String, String> processAttributes(String line) {
        if (line.substring(line.length() - 1).equals("/"))
            line = line.substring(0, line.length() - 1);
        java.util.HashMap<String, String> nv = new java.util.HashMap<String, String>();
        int i = 0;
        while (i < line.length()) {
            while (line.substring(i, i + 1).equals(" ")) {
                i++;
                if (i == line.length())
                    return nv;
            }
            int x = line.indexOf("=", i), j = x;
            while (line.substring(j - 1, j).equals(" ")) j--;
            String name = line.substring(i, j);
            i = x + 1;
            while (line.substring(i, i + 1).equals(" ")) i++;
            String qm = line.substring(i, i + 1);
            if (!(qm.equals("\"") || (qm.equals("\'"))))
                throw new java.lang.IllegalArgumentException("bad format xml");
            j = line.indexOf(qm, i + 1);
            if (j < 0)
                throw new java.lang.IllegalArgumentException("bad format xml");
            nv.put(name, line.substring(i + 1, j));
            i = j + 1;
        }
        return nv;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws java.io.IOException {
        xmlska.Node head = new xmlska.Node();
        head.setName("msg");
        xmlska.Node data = new xmlska.Node();
        data.linkUp(head);
        data.setName("data");
        data.addAttribute("to", "localhost");
        data.setValue("apple\npie");
        System.out.println(head);
        System.out.println("**********************************************************************************");
        System.out.println(xmlska.Node.convertToReadable(xmlska.Writer.getXMLString(head)));
        System.out.println("----------------------------------------------------------------------------------");
        System.out.println(xmlska.Reader.createXMLFromString(xmlska.Writer.getXMLString(head)));
    }
}

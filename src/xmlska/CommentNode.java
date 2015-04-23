/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xmlska;

/**
 *
 * @author sasa
 */
public class CommentNode extends Node {

    private String data;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "<!-- " + data + " -->";
    }
}

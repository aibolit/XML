/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package xmlska;

/**
 *
 * @author Sasa
 */
public class CDATANode extends Node {
    private String data;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString(){
        return "<![CDATA[" + data + "]]>";
    }
    
}

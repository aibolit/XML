/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Templates;

/**
 *
 * @author aleks
 */
public class tGroup implements Item {
    public enum Type{ORDERED_MULTIPLE,ORDERED_SINGLE,UNORDERED,UNORDERED_ONCE,SINGLE_TYPE};
    private java.util.ArrayList<Item> down;
    private int min,max;
    private Type type;
    private boolean mutable = true;
    private  tGroup( int min, int max, Type type, boolean mutable) {
        this.min = min;
        this.max = max;
        this.type = type;
        this.mutable = mutable;
    }
    
    
    
    public static tGroup newFreeGroup(){
        return new tGroup(-1,-1,Type.UNORDERED,true);
    }
    
    
}

/**
 * A HeapNode has an integer key.
 * 
 * @author Donald Chinn
 * @version April 25, 2016
 */
public class HeapNode implements Comparable {
    int key;            // the data
    
    /**
     * Constructor.
     * @param key   the key value
     */
    public HeapNode (int key) {
        this.key = key;
    }
    
    
    // Accessor methods
    public int getKey() {
        return key;
    }
    
    /**
     * Implements the compareTo method.
     * @param rhs the other HeapNode object.
     * @return 0 if two objects are equal;
     *     less than zero if this object is smaller;
     *     greater than zero if this object is larger.
     * @exception ClassCastException if rhs is not a HeapNode.
     */
    public int compareTo (Object rhs) {
        if (this.key < ((HeapNode) rhs).key) {
            return -1;
        } else if (this.key == ((HeapNode) rhs).key) {
            return 0;
        } else {
            return 1;
        }
    }
}
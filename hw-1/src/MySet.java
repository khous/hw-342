/***
 * TCSS342
 * @author Kyle Housley (kylehous@uw.edu)
 *
 * This class provides the basic functionality of a set.
 */
public class MySet {
    /***
     * The default size for the backing array.
     */
    public static int DEFAULT_SIZE = 32;

    /***
     * The user set default size for the backing array.
     */
    private int myDefaultSize;

    /***
     * The factor to expand the array by each time expansion is necessary.
     */
    public static int EXPANSION_FACTOR = 2;

    /***
     * The return value for when an item is not found.
     */
    private static int ITEM_NOT_FOUND = -1;

    /***
     * The maximum length that the backing array can reach. Set this value lower to test edge cases.
     */
    private static int MAX_SIZE = Integer.MAX_VALUE;

    /***
     * The current number of items in the collection. The value is incremented when adding, decremented when removing.
     */
    private int myCount = 0;

    /***
     * The last index an item was inserted. This index is incremented when adding items,
     * and not decremented when removing.
     */
    private long myLastInsertionIndex = 0;

    /***
     * The backing data structure for this class. All objects are stored in this array. This array is expanded when
     * a new addition would overflow the array. The array never contracts in order to minimize the expensive copy
     * operation.
     */
    private Object[] myArray;

    /***
     * Default constructor for my set. Sets the size of the backing array to MySet.DEFAULT_SIZE
     */
    public MySet () {
        this(DEFAULT_SIZE);
    }

    /***
     * Instantiate MySet, setting a specific size for the backing array
     * @param initialSize The initial size to set the array to
     */
    public MySet (int initialSize) {
        myDefaultSize = initialSize;//Maintain the default size at the size specified by the user
        myArray = new Object[initialSize];
    }

    /***
     * Insert an object into the set, ensuring uniqueness before adding.
     * @param objectToInsert The candidate object to be inserted.
     * @return True if the object was inserted, false if the object is already in the set.
     */
    public boolean insert (Object objectToInsert) {
        boolean objectAlreadyExists = isPresent(objectToInsert);//Check for object presence first to make this a set

        if (!objectAlreadyExists) {//Only insert if the object was not found
            //do the insertion
            if (myLastInsertionIndex == myArray.length) {//Check that we aren't going to overflow the array
                expandArray();//If we are, expand the array by EXPANSION_FACTOR
            }

            //If the last insertion index is MAX_SIZE, throw an exception so we don't blow up the array
            if (myLastInsertionIndex == MAX_SIZE)
                throw new IndexOutOfBoundsException("Collection is full");//2Bn elements is too much, use a different datastructure please

            myArray[(int) myLastInsertionIndex] = objectToInsert;//Everything worked, insert the object at the last index
            myLastInsertionIndex++;//keep track of where the last element was inserted
            myCount++;//increment the count
        }

        return !objectAlreadyExists;
    }

    /***
     * Expand the array when the limit of the initial array is reached. Use EXPANSION_FACTOR to determine the size of
     * the next array.
     */
    private void expandArray () {
        int newSize;

        try {//Try to multiply the length by the expansion factor, but make sure our multipl'cn doesn't overflow newSize
            newSize = Math.multiplyExact(myArray.length, EXPANSION_FACTOR);
        } catch (ArithmeticException e) {
            newSize = Integer.MAX_VALUE;//Set a hard ceiling at the limit of what an array can store
            //This will be reached long after the JVM with default parameters crashes
        }

        Object[] newArray = new Object[newSize];

        //Copy the set to the new array, collapsing nulls as we go.
        myLastInsertionIndex = copyArray(myArray, newArray);

        myArray = newArray;
    }

    /***
     * Perform an array copy to a new array. Only copy items which are not null and track the index of the last item
     * inserted.
     * @param source The source array to copy from.
     * @param destination The array to copy to.
     * @return The index of the last item inserted.
     */
    private int copyArray (Object[] source, Object[] destination) {
        int insertionPoint = 0;//Track the index of the last item inserted

        for (Object member : source) {
            if (member != null) {//Do not copy over nulls
                destination[insertionPoint] = member;
                insertionPoint++;//Increment the insertion point when copying a real value
            }
        }

        return insertionPoint;
    }

    /***
     * Remove an object from this set.
     * @param objectToRemove The candidate object to be removed.
     * @return True if the item was found and removed, false if the item was not found, and not removed.
     */
    public boolean remove (Object objectToRemove) {
        int indexOfObject = indexOf(objectToRemove);
        boolean objectFound = indexOfObject > ITEM_NOT_FOUND;

        if (objectFound) {
            myArray[indexOfObject] = null;//Set index to null
            myCount--;
            //Only collapse the array on expansion to avoid costly copy operations
        }

        return objectFound;
    }

    /***
     * Get the number of items in this set.
     * @return The number of items in the set.
     */
    public int size () {
        return myCount;
    }

    /***
     * Empty this set. Reinitialize the backing array to the default size.
     */
    public void makeEmpty () {
        myArray = new Object[myDefaultSize];
        myLastInsertionIndex = myCount = 0;//Set both values to zero
    }

    /***
     * Determine whether there are any items stored in this set.
     * @return True if the set is empty, false if there is at least one item in the set.
     */
    public boolean isEmpty () {
        return myCount == 0;
    }

    /***
     * Determine if the test object exists in this set.
     * @param testObject The object to look for in the set.
     * @return True if the object exists in the set, false if it is not found.
     */
    public boolean isPresent (Object testObject) {
        return indexOf(testObject) > ITEM_NOT_FOUND;
    }

    /***
     * Get the index of the test object in the backing array.
     * @param testObject The object to find.
     * @return The object's location in the backing array or -1.
     */
    private int indexOf (Object testObject) {
        int index = ITEM_NOT_FOUND;

        //TODO think about how to deal with sparsity
        for (int i = 0; i < myLastInsertionIndex; i++) {
            Object member = myArray[i];
            if (member != null && member.equals(testObject)) {
                index = i;
                break;//return ASAP
            }
        }

        return index;
    }
}

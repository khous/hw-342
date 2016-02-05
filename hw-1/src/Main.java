public class Main {
    public static void main(String[] args) {
        test();
    }

    /***
     * Test the API for MySet in a black box fashion
     */
    public static void test() {
        //Initialize test conditions
        int numberToInsert = 10000;

        MySet set = new MySet(128);

        for (int i = 0; i < numberToInsert; i++) {
            set.insert(i + "");//QOD string conversion
        }

        //Verify that test conditions are met
        assert set.size() == numberToInsert;

        //Test that we made a set and not a list. Inserting duplicates should have no effect.
        set.insert("0");
        assert set.size() == numberToInsert;

        //Insert a unique value and test that it is present
        set.insert("unique value");
        assert set.size() == numberToInsert + 1;

        //Test another known present value
        assert set.isPresent("42");

        //Test that removal of non existent element has no effect.
        assert !set.remove("-1");
        assert set.size() == numberToInsert + 1;
        assert !set.isPresent("-1");

        //Test that removal of known element decrements size by one
        set.remove("0");
        assert set.size() == numberToInsert;

        //Test that isEmpty works
        assert !set.isEmpty();

        //Empty the set
        set.makeEmpty();
        //Verify that it is empty
        assert set.isEmpty();
        assert set.size() == 0;

        //Verify that the set still works as intended
        assert set.insert("0");
        assert set.size() == 1;
        assert set.isPresent("0");

//      //This will break your computer, modifying the max size allows this condition to be tested
//        for (long i = numberToInsert; i < (long)Integer.MAX_VALUE + 1; i++)
//            set.insert(i + "");
//
//        assert set.size() == Integer.MAX_VALUE;
    }
}

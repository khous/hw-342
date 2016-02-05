public class Main {

    public static void main(String[] args) {
        testits();
    }

    public static void testits () {
        for (int i = 0; i < 2048; i++) {
            int expectedLength = Integer.toBinaryString(i).length();

            assert expectedLength == binaryLength(i);
        }
    }

    public static int binaryLength (int n) {
        int size = 1;

        if (n < 2) {
            return 1;
        }
//        n--;
        return size + binaryLength(n/2);
    }
}
/*******
 P(n): binaryLength(n) returns the length of the binary representation of the integer n when n >= 2 and 1 when 0 <= n < 2
 basis, n = 0: binaryLength(0), set n = 0, n is less than 2, return 1
 Inductive Hypothesis, n = k: binaryLength(n) returns the length of the binary representation of n when n >= 0
 Inductive Step, n = k + 1: binaryLength(n + 1), assume that n is not less than 2, therefore continue, return 1 + binaryLength
   of n + 1 / 2, which equals n/2 + 1/2. 1/2 will be dropped by integer division



*******/
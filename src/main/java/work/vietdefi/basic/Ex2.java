package work.vietdefi.basic;

public class Ex2 {

    /**
     * Determines if the given number is even.
     *
     * @param input the number to check
     * @return true if the input is even; false if the input is odd
     */

    public int sum(int input) {
        if (input <= 0){
            return 0;    // returns 0 if input is 0
        }
        int sum = 0;
        for (int i = 1; i <= input; i++) {
            sum += i;
        }
        return sum;
    }
}

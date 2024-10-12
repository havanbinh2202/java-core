package work.vietdefi.basic;

public class Ex3 {
    /**
     * Determines whether the given number is prime.
     *
     * @param n is the number to test
     * @return true if the input is prime; false if the input is not prime
 */
    public boolean checkIprime(int n) {
        if (n < 2) {
            return false;
        }
        for (int i = 2; i < n; ++i) {
            if (n % i == 0) {
                return false;
            }
        }
        return true;
    }
}

package work.vietdefi.basic;

public class Ex4 {
     /**
     * Calculate nth fibonacci number
     *
     * @param n: index of fibonacci number starting from 0
     * eg: F0 = 0, F1 = 1, F2 = 1, F3 = 2
     * @return nth fibonacci number
     */
     public int fibonacci(int n) {
         if (n <= 0) {
             return 0;
         } else if (n == 1) {
             return 1;
         }
         return fibonacci(n - 1) + fibonacci(n - 2);
     }
}


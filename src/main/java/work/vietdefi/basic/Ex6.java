package work.vietdefi.basic;

public class Ex6 {
    public int FactorialTest(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("Không thể tính giai thừa của số âm");
        }

        int factorial = 1;
        for (int i = 1; i <= n; i++) {
            factorial *= i;
        }
        return factorial;
    }
}

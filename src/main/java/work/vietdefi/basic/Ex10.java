package work.vietdefi.basic;

public class Ex10 {
    /**
     * Reverse a string
     *
     * @param n: input the number to check
     * @return true if input is a perfect number;   false if input is not a perfect number
     */
    public boolean CheckPerfectnumber(int n)
    {
        int s = 0;
        for (int i = 1; i < n; i++)
            if (n % i == 0)
                s+= i;

        if (s == n)
            return true;
        return false;
    }
}

package work.vietdefi.basic;

public class Ex2 {

    /**
     * Tính tổng các số từ 1 đến giá trị đầu vào.
     *
     * @param input số lớn nhất để tính tổng.
     * @return tổng từ 1 đến input.
     */
    public int sum(int input) {
        if (input <= 0){
            return 0;    // Trả về 0 nếu đầu vào là 0 hoặc âm
        }
        int sum = 0;
        for (int i = 1; i <= input; i++) {
            sum += i;
        }
        return sum;
    }
}

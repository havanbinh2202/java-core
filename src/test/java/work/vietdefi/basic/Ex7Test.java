package work.vietdefi.basic;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class Ex7Test {
    @Test
    @DisplayName("Test findMax with normal array")
    public void testFindMax() {
        Ex7 ex7 = new Ex7();
        // Trường hợp bình thường
        int[] numbers = {1, 2, 3, 4, 5};
        assertEquals(5, ex7.findMax(numbers));

        // Trường hợp mảng chứa số âm
        int[] negativeNumbers = {-10, -20, -30, -5, -1};
        assertEquals(-1, ex7.findMax(negativeNumbers));

        // Trường hợp mảng chứa một phần tử
        int[] singleNumber = {10};
        assertEquals(10, ex7.findMax(singleNumber));
    }

    @Test
    @DisplayName("Test findMax with empty array")
    public void testFindMaxWithEmptyArray() {
        Ex7 ex7 = new Ex7();
        // Trường hợp mảng rỗng, nên ném ngoại lệ IllegalArgumentException
        int[] emptyArray = {};
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            ex7.findMax(emptyArray);
        });
        assertEquals("The array cannot be null or empty.", exception.getMessage());
    }

    @Test
    @DisplayName("Test findMax with null array")
    public void testFindMaxWithNullArray() {
        Ex7 ex7 = new Ex7();
        // Trường hợp mảng null, nên ném ngoại lệ IllegalArgumentException
        int[] nullArray = null;
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            ex7.findMax(nullArray);
        });
        assertEquals("The array cannot be null or empty.", exception.getMessage());
    }
}

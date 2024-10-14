package work.vietdefi.basic;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class Ex9Test {
    @Test
    @DisplayName("test Bubble Sort")
    public void checkBubblesort() {
        Ex9 ex9 = new Ex9();

        int[] sortedArray = {1, 2, 3, 4, 5};
        int[] testArray1 = {1, 2, 3, 4, 5};
        ex9.bubbleSort(testArray1);
        assertArrayEquals(sortedArray, testArray1);


        int[] testArray2 = {5, 4, 3, 2, 1};
        ex9.bubbleSort(testArray2);
        assertArrayEquals(sortedArray, testArray2);


        int[] testArray3 = {3, 1, 4, 5, 2};
        ex9.bubbleSort(testArray3);
        assertArrayEquals(sortedArray, testArray3);

    }
}

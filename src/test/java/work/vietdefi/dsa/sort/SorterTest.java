package work.vietdefi.dsa.sort;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public class SorterTest {
    @Test
    @DisplayName("Test BubbleSort")
    public void testBubbleSort() {
        Sorter sorter = new BubbleSorter();
        int[] unsortedArray = {5, 2, 9, 1, 5, 6};
        int[] SortedArray = {1, 2, 5, 5, 6, 9};
        assertArrayEquals(SortedArray, sorter.sort(unsortedArray));
    }

    @Test
    @DisplayName("Test InsertSort")
    public void testInsertSort() {
        Sorter sorter = new InsertionSorter();
        int[] unsortedArray = {10, 2, 17, 1, 8, 11};
        int[] SortedArray = {1, 2, 8, 10, 11, 17};
        assertArrayEquals(SortedArray, sorter.sort(unsortedArray));
    }

    @Test
    @DisplayName("Test SelectionSort")
    public void testSelectionSort() {
        Sorter sorter = new SelectionSorter();
        int[] unsortedArray = {3, 7, 5, 1, 4, 15,11};
        int[] SortedArray = {1, 3, 4, 5, 7, 11, 15};
        assertArrayEquals(SortedArray, sorter.sort(unsortedArray));
    }
}

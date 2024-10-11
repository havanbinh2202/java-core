package work.vietdefi.basic;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

public class Ex2Test {

    @Test
    @DisplayName("Test sum with positive input")
    public void testSumPositive() {
        Ex2 ex2 = new Ex2();
        assertEquals(15, ex2.sum(5)); // 1 + 2 + 3 + 4 + 5 = 15
        assertEquals(55, ex2.sum(10)); // 1 + 2 + ... + 10 = 55
    }

    @Test
    @DisplayName("Test sum with input zero")
    public void testSumZero() {
        Ex2 ex2 = new Ex2();
        assertEquals(0, ex2.sum(0)); // Sum from 1 to 0 should be 0
    }

    @Test
    @DisplayName("Test sum with negative input")
    public void testSumNegative() {
        Ex2 ex2 = new Ex2();
        assertEquals(0, ex2.sum(-5)); // Sum from 1 to negative number should be 0
    }
}

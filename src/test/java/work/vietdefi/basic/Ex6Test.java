package work.vietdefi.basic;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class Ex6Test {
    @Test
    @DisplayName("check CalculateFactorial")
    public void testCalculateFactorial(){
        Ex6 ex6 = new Ex6();

        assertEquals(1,ex6.FactorialTest(1));
        assertEquals(2,ex6.FactorialTest(2));
        assertEquals(6,ex6.FactorialTest(3));
        assertEquals(120,ex6.FactorialTest(5));
    }
}

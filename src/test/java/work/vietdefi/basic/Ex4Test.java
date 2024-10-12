package work.vietdefi.basic;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class Ex4Test {
    @Test
    @DisplayName("Test check fibonacci number")
    public void testFibonacci(){
        Ex4 ex4 = new Ex4();
        assertEquals(5,ex4.fibonacci(5));
        assertEquals(55,ex4.fibonacci(10));
    }
}

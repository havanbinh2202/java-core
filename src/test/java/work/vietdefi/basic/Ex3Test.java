package work.vietdefi.basic;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class Ex3Test {

    @Test
    @DisplayName("Test Check is prime")
    public void testIprime(){
        Ex3 ex3 = new Ex3();
        // Test prime number case
        assertTrue(ex3.checkIprime(2));
        assertTrue(ex3.checkIprime(3));
        assertTrue(ex3.checkIprime(17));

        // Test for non-prime cases
        assertFalse(ex3.checkIprime(1));
        assertFalse(ex3.checkIprime(4));
        assertFalse(ex3.checkIprime(18));
    }
}

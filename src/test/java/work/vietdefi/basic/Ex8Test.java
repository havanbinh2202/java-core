package work.vietdefi.basic;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class Ex8Test {
    @Test
    @DisplayName("check symmetry string")
    public void testSymmetry() {
        Ex8 ex8 = new Ex8();

        assertTrue(ex8.isSymmetrical("abcddcba"),"The string 'abcddcba' should be symmetrical");

        assertFalse(ex8.isSymmetrical("hello"), "The string 'hello' should not be symmetrical");

        assertTrue(ex8.isSymmetrical(""), "Empty string should be symmetrical");
    }
}

package work.vietdefi.basic;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
public class Ex5Test {
    @Test
    @DisplayName("Test reverser")
    public void testReverse() {
        Ex5 ex5 = new Ex5();
        assertEquals("olleH", ex5.reverse("Hello"));
        assertEquals("4321", ex5.reverse("1234"));
        assertEquals("", ex5.reverse(""));
        assertEquals("a", ex5.reverse("a"));
    }
}

package work.vietdefi.basic;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class Ex10Test {
    @Test
    @DisplayName("Check")
    public void ischeck() {
        Ex10 ex10 = new Ex10();

        assertTrue(ex10.CheckPerfectnumber(6));


        assertTrue(ex10.CheckPerfectnumber(28));


        assertFalse(ex10.CheckPerfectnumber(12));


        assertFalse(ex10.CheckPerfectnumber(1));


        assertTrue(ex10.CheckPerfectnumber(496));
    }
}

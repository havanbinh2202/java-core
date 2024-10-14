package work.vietdefi.basic;

public class Ex8 {

    public boolean isSymmetrical(String input)  {
        for (int i = 0, j = input.length() - 1; i < j; i++, j--) {
            if (input.charAt(i) != input.charAt(j)) {
                return false;
            }
        }
        return true;
    }

}

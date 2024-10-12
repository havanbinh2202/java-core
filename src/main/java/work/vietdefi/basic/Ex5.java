package work.vietdefi.basic;

public class Ex5 {
    /**
     * Reverse a string
     *
     * @param str: the input string
     * @return the reversed string
     */
    public static String reverse(String str) {
        char[] charArray = str.toCharArray();
        int left = 0, right = charArray.length - 1;
        while (left < right) {
            char temp = charArray[left];
            charArray[left] = charArray[right];
            charArray[right] = temp;
            left++;
            right--;
        }return new String(charArray);
    }
}

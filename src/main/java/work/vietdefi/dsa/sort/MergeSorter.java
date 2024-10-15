package work.vietdefi.dsa.sort;

public class MergeSorter implements Sorter {
    /**
     * Sorts the given array of integers using the Merge Sort algorithm.
     *
     * @param values The array of integers to be sorted.
     * @return A new sorted array.
     */
    @Override
    public int[] sort(int[] values) {
        if (values.length < 2) {
            return values.clone(); // Trả về mảng đã cho nếu có ít hơn 2 phần tử
        }

        int mid = values.length / 2;
        int[] left = new int[mid];
        int[] right;

        if (values.length % 2 == 0) {
            right = new int[mid];
        } else {
            right = new int[mid + 1];
        }

        // Tách mảng thành hai nửa
        System.arraycopy(values, 0, left, 0, mid);
        System.arraycopy(values, mid, right, 0, right.length);

        // Đệ quy sắp xếp hai nửa
        left = sort(left);
        right = sort(right);

        // Gộp lại và trả về mảng đã sắp xếp
        return merge(left, right);
    }

    /**
     * Merges two sorted arrays into a single sorted array.
     *
     * @param left The left sorted array.
     * @param right The right sorted array.
     * @return A merged sorted array.
     */
    private int[] merge(int[] left, int[] right) {
        int[] merged = new int[left.length + right.length];
        int i = 0, j = 0, k = 0;

        // Gộp hai mảng đã sắp xếp
        while (i < left.length && j < right.length) {
            if (left[i] <= right[j]) {
                merged[k++] = left[i++];
            } else {
                merged[k++] = right[j++];
            }
        }

        // Sao chép phần còn lại của mảng trái
        while (i < left.length) {
            merged[k++] = left[i++];
        }

        // Sao chép phần còn lại của mảng phải
        while (j < right.length) {
            merged[k++] = right[j++];
        }

        return merged;
    }
}

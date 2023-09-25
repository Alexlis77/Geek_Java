public class Main {
    public static void main(String[] args) {
        int[] arr = new int[]{5, 2, 1, 3, 4, 0, 8, 6, 10, 7, 11, 14, 9, 16};
        RedBlackTree<Integer> tree = new RedBlackTree<>();
        for (int i : arr) {
            tree.add(i);
        }

        tree.print();

    }
}
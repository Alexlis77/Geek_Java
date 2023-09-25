import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RedBlackTree <V extends Comparable<V>> {
    Node root;
    public void add(V value) {
        if (root == null) {
            root = new Node();
            root.value = value;
            root.color = Color.Black;
            return;
        }
        addNode(root, value);
    }

    private boolean addNode(Node node, V value) {
        if (node.value.compareTo(value) == 0)
            return false;
        if (node.value.compareTo(value) > 0) {
            if (node.leftChild != null) {
                boolean result = addNode(node.leftChild, value);
                node.leftChild = rebalanced(node.leftChild);
                return result;
            } else {
                node.leftChild = new Node();
                node.leftChild.color = Color.Red;
                node.leftChild.value = value;
                return true;
            }
        } else {
            if (node.rightChild != null) {
                boolean result = addNode(node.rightChild, value);
                node.rightChild = rebalanced(node.rightChild);
                return result;
            } else {
                node.rightChild = new Node();
                node.rightChild.color = Color.Red;
                node.rightChild.value = value;
                return true;
            }
        }
    }

    private Node rebalanced(Node node) {
        Node result = node;
        boolean needRebalanced;
        do {
            needRebalanced = false;
            if (result.rightChild != null && result.rightChild.color == Color.Red
                    && (result.leftChild == null || result.leftChild.color == Color.Black)) {
                needRebalanced = true;
                result = rightSwap(result);
            }
            if (result.leftChild != null && result.leftChild.color == Color.Red
                    && result.leftChild.leftChild != null && result.leftChild.leftChild.color == Color.Red) {
                needRebalanced = true;
                result = leftSwap(result);
            }
            if (result.leftChild != null && result.leftChild.color == Color.Red
                    && result.rightChild != null && result.rightChild.color == Color.Red) {
                needRebalanced = true;
                colorSwap(result);
            }
        } while (needRebalanced);
        return result;
    }

    private void colorSwap(Node node) {
        node.rightChild.color = Color.Black;
        node.leftChild.color = Color.Black;
        node.color = Color.Red;
    }

    private Node leftSwap(Node node) {
        Node leftChild = node.leftChild;
        Node between = leftChild.rightChild;
        leftChild.rightChild = node;
        node.leftChild = between;
        leftChild.color = node.color;
        node.color = Color.Red;
        return leftChild;
    }

    private Node rightSwap(Node node) {
        Node rightChild = node.rightChild;
        Node between = rightChild.leftChild;
        rightChild.leftChild = node;
        node.rightChild = between;
        rightChild.color = node.color;
        node.color = Color.Red;
        return rightChild;
    }

    private class Node {
        V value;
        Node leftChild;
        Node rightChild;
        Color color;
    }

    private class PrintNode {
        Node node;
        String str;
        int depth;

        public PrintNode() {
            node = null;
            str = " ";
            depth = 0;
        }

        public PrintNode(Node node) {
            depth = 0;
            this.node = node;
            this.str = node.value.toString();
        }
    }

    public void print() {

        int maxDepth = maxDepth() + 3;
        int nodeCount = nodeCount(root, 0);
        int width = 50;
        int height = nodeCount * 5;
        List<List<PrintNode>> list = new ArrayList<>();
        for (int i = 0; i < height; i++)  {
            ArrayList<PrintNode> row = new ArrayList<>();
            for (int j = 0; j < width; j++) {
                row.add(new PrintNode());
            }
            list.add(row);
        }

        list.get(height / 2).set(0, new PrintNode(root));
        list.get(height / 2).get(0).depth = 0;

        for (int j = 0; j < width; j++)  {
            for (int i = 0; i < height; i++) {
                PrintNode currentNode = list.get(i).get(j);
                if (currentNode.node != null) {
                    currentNode.str = currentNode.node.value.toString();
                    if (currentNode.node.leftChild != null) {
                        int in = i + (maxDepth / (int) Math.pow(2, currentNode.depth));
                        int jn = j + 3;
                        printLines(list, i, j, in, jn);
                        list.get(in).get(jn).node = currentNode.node.leftChild;
                        list.get(in).get(jn).depth = list.get(i).get(j).depth + 1;

                    }
                    if (currentNode.node.rightChild != null) {
                        int in = i - (maxDepth / (int) Math.pow(2, currentNode.depth));
                        int jn = j + 3;
                        printLines(list, i, j, in, jn);
                        list.get(in).get(jn).node = currentNode.node.rightChild;
                        list.get(in).get(jn).depth = list.get(i).get(j).depth + 1;
                    }

                }
            }
        }
        for (int i = 0; i < height; i++){
            boolean flag = true;
            for (int j = 0; j < width; j++) {
                if (!Objects.equals(list.get(i).get(j).str, " ")) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                list.remove(i);
                i--;
                height--;
            }
        }

        for (var row : list) {
            for (var item : row) {
                System.out.print(item.str + " ");
            }
            System.out.println();
        }
    }

    private void printLines(List<List<PrintNode>> list, int i, int j, int i2, int j2) {
        if (i2 > i)
        {
            while (i < i2) {
                i++;
                list.get(i).get(j).str = "|";
            }
            list.get(i).get(j).str = "\\";
        } else {
            while (i > i2) {
                i--;
                list.get(i).get(j).str = "|";
            }
            list.get(i).get(j).str = "/";
        }
        while (j < j2) {
            j++;
            list.get(i).get(j).str = "-";
        }
    }

    public int maxDepth() {
        return maxDepth2(0, root);
    }

    private int maxDepth2(int depth, Node node) {
        depth++;
        int leftChild = depth;
        int rightChild = depth;
        if (node.leftChild != null)
            leftChild = maxDepth2(depth, node.leftChild);
        if (node.rightChild != null)
            rightChild = maxDepth2(depth, node.rightChild);
        return Math.max(leftChild, rightChild);
    }

    private int nodeCount(Node node, int count) {
        if (node != null) {
            count++;
            return count + nodeCount(node.leftChild, 0) + nodeCount(node.rightChild, 0);
        }
        return count;
    }

}

enum Color {Red, Black}
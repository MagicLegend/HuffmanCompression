package logic;

public class Node {
    Node left, right;
    double weight;
    String chararacter;

    /**
     * Initial constructor to create the bottom nodes (with characters).
     *
     * @param character The character that the node represents.
     * @param weight    The weight of the character (amount of times it can be found).
     */
    public Node(Character character, double weight) {
        this.chararacter = String.valueOf(character);
        this.weight = weight;
    }

    /**
     * Second constructor to add the nodes above. https://codingsec.net/2016/03/compress-data-using-java-code-huffmans-coding-algorithm/
     * Comparison which node should be the left and which should be the right should be done here.
     *
     * @param left  The first node the new node will represent.
     * @param right The second node the new node will represent.
     */
    public Node(Node left, Node right) {
        this.weight = left.weight + right.weight;
        this.chararacter = "" + left.chararacter + right.chararacter;

        //The left node should be the node with the lowest weight. This to prevent inconsistencies when rebuilding the tree.
        if (left.weight > right.weight) {
            this.left = left;
            this.right = right;
        } else {
            this.left = right;
            this.right = left;
        }
    }
}

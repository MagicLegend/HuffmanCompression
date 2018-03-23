package main.com.magiclegend.huffman.logic;

import java.util.Comparator;

public class Node implements Comparable<Node> {
    private Node left, right;

    private double weight;

    private String chararacter;
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
    /**
     * Empty constructor for rebuilding the tree structure.
     */
    public Node() {

    }

    public Node getLeft() {
        return left;
    }

    public void setLeft(Node left) {
        this.left = left;
    }

    public Node getRight() {
        return right;
    }

    public void setRight(Node right) {
        this.right = right;
    }

    public String getChararacter() {
        return chararacter;
    }

    public void setChararacter(String chararacter) {
        this.chararacter = chararacter;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    /**
     * Compares this object with the specified object for order.  Returns a
     * negative integer, zero, or a positive integer as this object is less
     * than, equal to, or greater than the specified object.
     * <p>
     * <p>The implementor must ensure <tt>sgn(x.compareTo(y)) ==
     * -sgn(y.compareTo(x))</tt> for all <tt>x</tt> and <tt>y</tt>.  (This
     * implies that <tt>x.compareTo(y)</tt> must throw an exception iff
     * <tt>y.compareTo(x)</tt> throws an exception.)
     * <p>
     * <p>The implementor must also ensure that the relation is transitive:
     * <tt>(x.compareTo(y)&gt;0 &amp;&amp; y.compareTo(z)&gt;0)</tt> implies
     * <tt>x.compareTo(z)&gt;0</tt>.
     * <p>
     * <p>Finally, the implementor must ensure that <tt>x.compareTo(y)==0</tt>
     * implies that <tt>sgn(x.compareTo(z)) == sgn(y.compareTo(z))</tt>, for
     * all <tt>z</tt>.
     * <p>
     * <p>It is strongly recommended, but <i>not</i> strictly required that
     * <tt>(x.compareTo(y)==0) == (x.equals(y))</tt>.  Generally speaking, any
     * class that implements the <tt>Comparable</tt> interface and violates
     * this condition should clearly indicate this fact.  The recommended
     * language is "Note: this class has a natural ordering that is
     * inconsistent with equals."
     * <p>
     * <p>In the foregoing description, the notation
     * <tt>sgn(</tt><i>expression</i><tt>)</tt> designates the mathematical
     * <i>signum</i> function, which is defined to return one of <tt>-1</tt>,
     * <tt>0</tt>, or <tt>1</tt> according to whether the value of
     * <i>expression</i> is negative, zero or positive.
     *
     * @param o the object to be compared.
     * @return a negative integer, zero, or a positive integer as this object
     * is less than, equal to, or greater than the specified object.
     * @throws NullPointerException if the specified object is null
     * @throws ClassCastException   if the specified object's type prevents it
     *                              from being compared to this object.
     */
    @Override
    public int compareTo(Node o) {
        return (this.weight < o.weight) ? -1 : 1;
    }
}

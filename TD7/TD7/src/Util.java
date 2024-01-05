import java.awt.*;
import java.awt.event.*;

/**
 * Class for representing nodes of a Red-Black binary tree
 * 
 * Each node stores its parent node in order to simplify node
 * insertion/rotations
 */
class Node {
    final static boolean RED = true;
    final static boolean BLACK = false;

    String element;
    Node left;
    Node right;
    boolean color;
    Node parent;

    Node(String data) {
        this.element = data; // only including data. not key
        this.left = null; // left subtree
        this.right = null; // right subtree
        this.color = RED;
        this.parent = null; // required at time of rechecking.
    }

    Node(String data, boolean color) {
        this.element = data; // only including data. not key
        this.left = null; // left subtree
        this.right = null; // right subtree
        this.color = color;
        this.parent = null; // required at time of rechecking.
    }

    static RBT copyArbre(Node a) {
        if (a == null)
            return null;
        else {
            RBT result = new RBT(a.color, copyArbre(a.left), a.element, copyArbre(a.right));
            result.lw = (result.left == null) ? 0 : result.left.lw + result.left.rw + DrawBinaryTree.d / 2;
            result.rw = (result.right == null) ? 0 : result.right.lw + result.right.rw + DrawBinaryTree.d / 2;
            return result;
        }

    }

}

/**
 * This class provides methods for the construction of a valid Reb-Black tree
 */
public class Util {
    public Node root;// root node

    public Util() {
        root = null;
    }

    // this function performs left rotation
    Node rotateLeft(Node node) {
        Node x = node.right;
        Node y = x.left;
        x.left = node;
        node.right = y;
        node.parent = x; // parent resetting is also important.
        if (y != null)
            y.parent = node;
        return (x);
    }

    // this function performs right rotation
    Node rotateRight(Node node) {
        Node x = node.left;
        Node y = x.right;
        x.right = node;
        node.left = y;
        node.parent = x;
        if (y != null)
            y.parent = node;
        return (x);
    }

    // these are some flags.
    // Respective rotations are performed during traceback.
    // rotations are done if flags are true.
    boolean ll = false;
    boolean rr = false;
    boolean lr = false;
    boolean rl = false;

    // helper function for insertion. Actually this function performs all tasks in
    // single pass only.
    Node insertHelp(Node root, String data) {
        // f is true when RED RED conflict is there.
        boolean f = false;

        // recursive calls to insert at proper position according to BST properties.
        if (root == null)
            return (new Node(data));
        else if (data.compareTo(root.element) < 0) {
            root.left = insertHelp(root.left, data);
            root.left.parent = root;
            if (root != this.root) {
                if (root.color == Node.RED && root.left.color == Node.RED)
                    f = true;
            }
        } else {
            root.right = insertHelp(root.right, data);
            root.right.parent = root;
            if (root != this.root) {
                if (root.color == Node.RED && root.right.color == Node.RED)
                    f = true;
            }
            // at the same time of insertion, we are also assigning parent nodes
            // also we are checking for RED RED conflicts
        }

        // now lets rotate.
        if (this.ll) // for left rotate.
        {
            root = rotateLeft(root);
            root.color = Node.BLACK;
            root.left.color = Node.RED;
            this.ll = false;
        } else if (this.rr) // for right rotate
        {
            root = rotateRight(root);
            root.color = Node.BLACK;
            root.right.color = Node.RED;
            this.rr = false;
        } else if (this.rl) // for right and then left
        {
            root.right = rotateRight(root.right);
            root.right.parent = root;
            root = rotateLeft(root);
            root.color = Node.BLACK;
            root.left.color = Node.RED;

            this.rl = false;
        } else if (this.lr) // for left and then right.
        {
            root.left = rotateLeft(root.left);
            root.left.parent = root;
            root = rotateRight(root);
            root.color = Node.BLACK;
            root.right.color = Node.RED;
            this.lr = false;
        }
        // when rotation and recolouring is done flags are reset.
        // Now lets take care of RED RED conflict
        if (f) {
            if (root.parent.right == root) // to check which child is the current node of its parent
            {
                if (root.parent.left == null || root.parent.left.color == Node.BLACK) // case when parent's sibling is
                                                                                      // black
                {// perform certaing rotation and recolouring. This will be done while
                 // backtracking. Hence setting up respective flags.
                    if (root.left != null && root.left.color == Node.RED)
                        this.rl = true;
                    else if (root.right != null && root.right.color == Node.RED)
                        this.ll = true;
                } else // case when parent's sibling is red
                {
                    root.parent.left.color = Node.BLACK;
                    root.color = Node.BLACK;
                    if (root.parent != this.root)
                        root.parent.color = Node.RED;
                }
            } else {
                if (root.parent.right == null || root.parent.right.color == Node.BLACK) {
                    if (root.left != null && root.left.color == Node.RED)
                        this.rr = true;
                    else if (root.right != null && root.right.color == Node.RED)
                        this.lr = true;
                } else {
                    root.parent.right.color = Node.BLACK;
                    root.color = Node.BLACK;
                    if (root.parent != this.root)
                        root.parent.color = Node.RED;
                }
            }
            f = false;
        }
        return (root);
    }

    // function to insert data into tree.
    public void insert(String data) {
        if (this.root == null) {
            this.root = new Node(data);
            this.root.color = Node.BLACK;
        } else
            this.root = insertHelp(this.root, data);
    }

    // helper function to print inorder traversal
    void inorderTraversalHelper(Node node) {
        if (node != null) {
            inorderTraversalHelper(node.left);
            System.out.print(" " + node.element);
            inorderTraversalHelper(node.right);
        }
    }

    // function to print inorder traversal
    public void inorderTraversal() {
        inorderTraversalHelper(this.root);
    }

    // helper function to print the tree.
    void printTreeHelper(Node root, int space) {
        int i;
        if (root != null) {
            space = space + 10;
            printTreeHelper(root.right, space);
            System.out.printf("\n");
            for (i = 10; i < space; i++) {
                System.out.printf(" ");
            }
            System.out.print(" " + root.element);
            System.out.print("\n");
            printTreeHelper(root.left, space);
        }
    }

    // function to print the tree.
    public void printTree() {
        printTreeHelper(this.root, 0);
    }

}

/**
 * This class contains methods for checking the validity of binary search trees
 */
class BST {

    static boolean isBST(RBT t) {
        if (t == null)
            return true;
        return isBST(t.left, t.element) && isBST(t.element, t.right);
    }

    static boolean isBST(RBT t, String sup) { // t < sup
        if (t == null)
            return true;
        return t.element.compareTo(sup) < 0 && isBST(t.left, t.element)
                && isBST(t.element, t.right, sup);
    }

    static boolean isBST(String inf, RBT t) { // inf < t
        if (t == null)
            return true;
        return t.element.compareTo(inf) > 0 && isBST(inf, t.left, t.element)
                && isBST(t.element, t.right);
    }

    static boolean isBST(String inf, RBT t, String sup) { // inf < t < sup
        if (t == null)
            return true;
        return t.element.compareTo(inf) > 0 && t.element.compareTo(sup) < 0
                && isBST(inf, t.left, t.element) && isBST(t.element, t.right, sup);
    }

}

/**
 * Class for drawing binary (red-black) trees
 * 
 * @author C. Durr (2008), L. Castelli Aleardi (2010, 2023)
 */
class DrawBinaryTree extends Canvas {
    /** Noeud racine d'un arbre binaire de recherche qu'on veut visualiser */
    RBT a;

    // parameters for the drawing frame
    static final int d = 5, rad = 1;

    static int compteur = 1;

    /*
     * Initialise la fenetre graphique
     */
    public DrawBinaryTree(RBT n) {
        this.a = n;
        preprocess(a);

        Frame f = new Frame("Arbre " + compteur++);
        f.setLocation(100 + 40 * compteur, 100 + 20 * compteur);
        f.add(this);
        f.setSize(600, 600);
        f.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        f.setVisible(true);
    }

    /**
     * Preprocess the nodes of the tree to set the drawing parameters (for
     * visualizing the layout of a binary tree)
     * 
     * @param a node of a Red-Black tree
     */
    static void preprocess(RBT a) {
        if (a == null)
            return;
        else {
            preprocess(a.left);
            preprocess(a.right);
            a.lw = (a.left == null) ? 0 : a.left.lw + a.left.rw + DrawBinaryTree.d / 2;
            a.rw = (a.right == null) ? 0 : a.right.lw + a.right.rw + DrawBinaryTree.d / 2;
        }
    }

    public void paint(Graphics g) {
        if (a != null) {
            int f = getWidth() / (a.lw + 3 * d + a.rw);
            int x = a.lw + d;
            int y = d;
            paint(g, x, y, Math.max(f, 1), true, a);
        }
    }

    /**
     * 
     * @param g
     * @param x
     * @param y
     * @param f
     * @param showl
     * @param node  noeud d'un arbre binaire
     */
    public void paint(Graphics g, int x, int y, int f, boolean showl, RBT node) {
        int xx, yy;
        if (node.left != null) {
            xx = x - d / 2 - node.left.rw;
            yy = y + d;
            g.drawLine(x * f, y * f, xx * f, yy * f);
            paint(g, xx, yy, f, showl, node.left);
        } else {
            xx = x - d / 2;
            yy = y + d;
            int h = 2 * rad * f;
            g.drawLine(x * f, y * f, x * f - (h / 4) + (h / 8), y * f + h);
            g.fillRect(x * f - (h / 4), y * f + h, h / 4, h / 4);
        }

        if (node.right != null) {
            xx = x + d / 2 + node.right.lw;
            yy = y + d;
            g.drawLine(x * f, y * f, xx * f, yy * f);
            paint(g, xx, yy, f, showl, node.right);
        } else {
            xx = x + d;
            yy = y + d;
            int h = 2 * rad * f;
            g.drawLine(x * f, y * f, x * f + (h / 4) + (h / 8), y * f + h);
            g.fillRect(x * f + (h / 4), y * f + h, h / 4, h / 4);
        }

        g.setColor(showl ? Color.white : Color.black);
        // g.fillOval((x-rad)*f, (y-rad)*f, 2*rad*f, 2*rad*f);
        g.fillOval((x - (int) (1.25 * rad)) * f, (y - rad) * f, (int) (2.5 * rad * f), 2 * rad * f);
        if (showl) {
            Color c = Color.black;
            if (node.color == RBT.RED)
                c = Color.red;
            g.setColor(c);

            String label = "" + node.element;
            int fontSize = 12;
            if (label.length() > 8)
                fontSize = 9;
            else if (label.length() > 6)
                fontSize = 10;
            g.setFont(new Font("Arial", Font.BOLD, fontSize));
            FontMetrics m = g.getFontMetrics();

            int xCorner = (x - (int) (1.25 * rad)) * f;
            int yCorner = (y - rad) * f;
            int width = (int) (2.5 * rad * f);
            int height = 2 * rad * f;
            g.drawOval(xCorner, yCorner, width, height);
            // g.drawOval((x-rad)*f, (y-rad)*f, 2*rad*f, 2*rad*f);

            int posX = (int) (1.05 * x * f) - (int) (label.length() * m.charWidth('0') * 1.2 / 2.);
            int posY = y * f + m.getAscent() / 2;
            g.drawString(label, posX, posY);
        }
        g.setColor(Color.black);
    }

}

import java.util.*;

/**
 * Claff for generating valid and invalid Red-Black trees (for testing)
 */
class TreeGenerator {

    public static String[] villes = new String[] { "Paris", "Marseille", "Lyon", "Toulouse", "Nice", "Nantes",
            "Montpellier", "Strasbourg", "Bordeaux", "Lille", "Rennes", "Reims", "Toulon", "Dijon", "Grenoble",
            "Angers" };

    /* A collection of small (binary search) trees that are not Red-Black trees */
    public static RBT[] invalidTinyTrees = getInvalidTinyTrees();

    /**
     * Generate strings corresponding to 'k' random numbers (ranging in [0...999])
     */
    public static RBT generateTreeStoringNumbers(int seed, int k) {
        int N = 1000;
        Util tree = new Util();

        Random rand = new Random(seed);

        boolean[] exist = new boolean[N];
        int count = 0;

        while (count < k && count < N) {
            int index = rand.nextInt(N);
            if (exist[index] == false) {
                String number = "";
                if (index < 10)
                    number = "00" + index;
                else if (index < 100)
                    number = "0" + index;
                else
                    number = "" + index;
                tree.insert(number);
                exist[index] = true;
                count++;
            }
        }

        RBT copy = Node.copyArbre(tree.root);
        return copy;

    }

    /**
     * Generate a R-B tree containing a random selection of the elements in 'data'
     * 
     * @param data
     * @param seed
     * @param k
     * @param withRepetitions if 'false' do not insert elements twice (in the tree)
     * @return
     */
    public static RBT getValidRedBlackTree(String[] data, int seed, int k, boolean withRepetitions) {
        Util tree = new Util();

        Random rand = new Random(seed);

        if (withRepetitions == true || k > data.length) {
            for (int i = 0; i < k; i++) {
                int index = rand.nextInt(data.length);
                tree.insert(data[index]);
            }
        } else {
            boolean[] exist = new boolean[data.length];
            int count = 0;

            while (count < k) {
                int index = rand.nextInt(data.length);
                if (exist[index] == false) {
                    tree.insert(data[index]);
                    exist[index] = true;
                    count++;
                }
            }
        }

        RBT copy = Node.copyArbre(tree.root);
        return copy;
    }

    public static RBT getValidRedBlackTree(String[] data) {
        Util tree = new Util();

        for (int i = 0; i < data.length; i++) {
            tree.insert(data[i]);
        }
        RBT copy = Node.copyArbre(tree.root);
        return copy;
    }

    public static RBT[] getInvalidTinyTrees() {
        RBT[] invalid = new RBT[15];
        invalid[0] = new RBT(RBT.RED, null, "Paris", null); // size 1: red root
        invalid[1] = new RBT(RBT.BLACK, new RBT(RBT.RED, new RBT(RBT.RED, null, "Dijon", null), "Lyon", null), "Paris",
                null);
        invalid[2] = new RBT(RBT.BLACK, new RBT(RBT.RED, null, "Lyon", new RBT(RBT.RED, null, "Nantes", null)), "Paris",
                null);
        invalid[3] = new RBT(RBT.BLACK, new RBT(RBT.RED, new RBT(RBT.RED, null, "Dijon", null), "Lyon",
                new RBT(RBT.BLACK, null, "Nantes", null)), "Paris", null);
        invalid[4] = new RBT(RBT.BLACK,
                new RBT(RBT.RED, new RBT(RBT.RED, null, "Dijon", null), "Lyon", new RBT(RBT.RED, null, "Nantes", null)),
                "Paris", null);
        invalid[5] = new RBT(RBT.BLACK, null, "Dijon", new RBT(RBT.RED, new RBT(RBT.RED, null, "Lyon", null), "Paris",
                new RBT(RBT.BLACK, null, "Rennes", null)));
        invalid[6] = new RBT(RBT.BLACK, null, "Dijon", new RBT(RBT.RED, new RBT(RBT.BLACK, null, "Lyon", null), "Paris",
                new RBT(RBT.RED, null, "Rennes", null)));
        invalid[7] = new RBT(RBT.BLACK, null, "Dijon", new RBT(RBT.RED, new RBT(RBT.RED, null, "Lyon", null), "Paris",
                new RBT(RBT.RED, null, "Rennes", null)));
        invalid[8] = new RBT(RBT.BLACK, new RBT(RBT.BLACK, new RBT(RBT.RED, null, "Angers", null), "Bordeaux", null),
                "Dijon", new RBT(RBT.RED, new RBT(RBT.RED, null, "Lyon", null), "Paris",
                        new RBT(RBT.RED, null, "Rennes", null)));
        invalid[9] = new RBT(RBT.BLACK, new RBT(RBT.BLACK, new RBT(RBT.RED, null, "Angers", null), "Bordeaux", null),
                "Dijon", new RBT(RBT.RED, new RBT(RBT.BLACK, null, "Lyon", null), "Paris",
                        new RBT(RBT.RED, null, "Rennes", null)));
        invalid[10] = new RBT(RBT.BLACK, new RBT(RBT.BLACK, new RBT(RBT.RED, null, "Angers", null), "Bordeaux", null),
                "Dijon", new RBT(RBT.BLACK, new RBT(RBT.BLACK, null, "Lyon", null), "Paris",
                        new RBT(RBT.RED, null, "Rennes", null)));
        invalid[11] = new RBT(RBT.BLACK, new RBT(RBT.BLACK, new RBT(RBT.BLACK, null, "Angers", null), "Bordeaux", null),
                "Dijon", new RBT(RBT.BLACK, new RBT(RBT.BLACK, null, "Lyon", null), "Paris",
                        new RBT(RBT.RED, null, "Rennes", null)));
        invalid[12] = new RBT(RBT.BLACK, new RBT(RBT.BLACK, new RBT(RBT.RED, null, "Angers", null), "Bordeaux", null),
                "Dijon", new RBT(RBT.BLACK, new RBT(RBT.RED, null, "Lyon", null), "Paris",
                        new RBT(RBT.BLACK, null, "Rennes", null)));
        invalid[13] = new RBT(RBT.BLACK, new RBT(RBT.BLACK, new RBT(RBT.BLACK, null, "Angers", null), "Bordeaux", null),
                "Dijon", new RBT(RBT.RED, new RBT(RBT.BLACK, null, "Lyon", null), "Paris",
                        new RBT(RBT.BLACK, null, "Rennes", null)));
        invalid[14] = new RBT(RBT.BLACK, new RBT(RBT.BLACK, new RBT(RBT.BLACK, null, "Angers", null), "Bordeaux", null),
                "Dijon", new RBT(RBT.RED, new RBT(RBT.BLACK, null, "Lyon", new RBT(RBT.RED, null, "Nantes", null)),
                        "Paris", new RBT(RBT.BLACK, null, "Rennes", null)));

        return invalid;
    }

    /**
     * Generate a BST containing 'k+1' random numbers, which is not a valid
     * Red-Black tree
     * The 'k' random numbers are ranging in [0...999]
     */
    public static RBT generateInvalidTreeStoringNumbers(int seed, int k) {
        int N = 1000;
        Util tree = new Util();

        Random rand = new Random(seed);

        boolean[] exist = new boolean[N];
        int count = 0;

        // generate a valid Red-Black tree
        while (count < k && count < N) {
            int index = rand.nextInt(N);
            if (exist[index] == false) {
                String number = "";
                if (index < 10)
                    number = "00" + index;
                else if (index < 100)
                    number = "0" + index;
                else
                    number = "" + index;
                tree.insert(number);
                exist[index] = true;
                count++;
            }
        }
        // generate a valid Red-Black tree
        while (count < k + 1) {
            int index = rand.nextInt(N);
            if (exist[index] == false) {
                String number = "";
                if (index < 10)
                    number = "00" + index;
                else if (index < 100)
                    number = "0" + index;
                else
                    number = "" + index;
                insert(tree, number, RBT.BLACK);
                exist[index] = true;
                count++;
            }
        }

        RBT copy = Node.copyArbre(tree.root);
        return copy;

    }

    public static Node insert(Node node, String data, boolean color) {
        if (node == null)
            return new Node(data, color);
        else if (data.compareTo(node.element) < 0) {
            node.left = insert(node.left, data, color);
            node.left.parent = node;
        } else {
            node.right = insert(node.right, data, color);
            node.right.parent = node;
        }

        return node;
    }

    /**
     * Insert an element in a binary search tree (wihtout performing rotations) with
     * a given (possibly non valid) node color.
     * The result is a binary search tree which may be unbalanced (possibly invalid
     * R-B tree) <br>
     * <br>
     * Remark: function useful for generating invalid R-B trees for testing
     */
    public static void insert(Util tree, String data, boolean color) {
        if (tree.root == null) {
            tree.root = new Node(data);
            tree.root.color = Node.BLACK;
        } else
            tree.root = insert(tree.root, data, color);
    }

}

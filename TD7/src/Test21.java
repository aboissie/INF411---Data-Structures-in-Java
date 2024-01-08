import java.util.ArrayList;

public class Test21 {
	public static void main(String[] args) {
		if (!Test21.class.desiredAssertionStatus()) {
			System.err.println("Vous devez activer l'option -ea de la JVM");
			System.err.println("To run the tests please activate the -ea option of the JVM");
			System.exit(1);
		}

		System.out.println("(Q2.1) Validité des noeuds rouges (validity for red nodes): Test de isRedValid()");

		int repeat = 10;
		RBT validTree;
		boolean valid;
		System.out.print("\tTeste arbres valides (testing valid trees)...");
		for (int i = 0; i < repeat; i++) { // teste des arbres valides de petite taille: n=8
			validTree = TreeGenerator.getValidRedBlackTree(TreeGenerator.villes, i, 8, false); // arbre valide
			if (BST.isBST(validTree) == false)
				throw new Error("Error: the tree is not a Binary Search tree");

			valid = RBT.isRedValid(validTree); // Check condition (2) for red nodes
			if (!valid) {
				new DrawBinaryTree(validTree); // draw the tree in a graphical frame
			}
			assert (valid) : "\nL'arbre devrait etre valide (la propriété (2) des noeuds rouges est respectée)\n" + 
					"\nThe tree should be valid (property (2) of red nodes is satisfied)";
		}
		int maxSize = 20;
		for (int i = 0; i < repeat; i++) { // teste des arbres valides de taille moyenne: n=20
			validTree = TreeGenerator.generateTreeStoringNumbers(i, maxSize); // arbre valide
			if (BST.isBST(validTree) == false)
				throw new Error("Error: the tree is not a Binary Search tree");

			valid = RBT.isRedValid(validTree);
			if (!valid) {
				new DrawBinaryTree(validTree); // draw the tree in a graphical frame
			}
			assert (valid) : "\nL'arbre devrait etre valide (la propriété (2) des noeuds rouges est respectée)" + 
					"\nThe tree should be valid (property (2) of red nodes is satisfied)";
		}
		maxSize = 500;
		for (int i = 0; i < repeat; i++) { // teste des arbres valides de plus grosses taille: n=500
			validTree = TreeGenerator.generateTreeStoringNumbers(i, maxSize); // arbre valide
			if (BST.isBST(validTree) == false)
				throw new Error("Error: the tree is not a Binary Search tree");

			valid = RBT.isRedValid(validTree);
			if (!valid) {
				new DrawBinaryTree(validTree); // draw the tree in a graphical frame
			}
			assert (valid) : "\nL'arbre devrait etre valide (la propriété (2) des noeuds rouges est respectée)" + 
					"\nThe tree should be valid (property (2) of red nodes is satisfied)";
		}
		System.out.println("\t\t\t [OK].");

		// Check for false negative: test invalid RB trees
		System.out.print("\tTeste arbres non valides (testing non valid trees)...");

		ArrayList<RBT> trees = new ArrayList<RBT>();
		trees.add(TreeGenerator.invalidTinyTrees[1]);
		trees.add(TreeGenerator.invalidTinyTrees[2]);
		trees.add(TreeGenerator.invalidTinyTrees[3]);
		trees.add(TreeGenerator.invalidTinyTrees[4]);
		trees.add(TreeGenerator.invalidTinyTrees[5]);
		trees.add(TreeGenerator.invalidTinyTrees[6]);
		trees.add(TreeGenerator.invalidTinyTrees[7]);
		trees.add(TreeGenerator.invalidTinyTrees[8]);
		trees.add(TreeGenerator.invalidTinyTrees[9]);

		for (RBT t : trees) {
			if (BST.isBST(t) == false)
				throw new Error("Error: the tree is not a Binary Search tree");

			valid = RBT.isRedValid(t); // check the property of red nodes
			// System.out.println("valid=" +valid);
			if (valid)
				new DrawBinaryTree(t); // draw the tree in a graphical frame
			assert (!valid)
					: "\nLe résultat du test devrait etre faux, car l'arbre ne respecte pas la propriété (2) des noeuds rouges" + 
					"\nThe result should be false, since condition (2) of red nodes is not satisfied)";
		}
		System.out.println("\t\t [OK].");

	}

}

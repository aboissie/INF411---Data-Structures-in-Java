import java.util.ArrayList;

public class Test23 {
	public static void main(String[] args) {
		if (!Test21.class.desiredAssertionStatus()) {
			System.err.println("Vous devez activer l'option -ea de la JVM");
			System.err.println("To run the tests please activate the -ea option of the JVM");
			System.exit(1);
		}

		System.out.println("(Q2.3) Test de isValid()");

		int repeat=10;
		RBT validTree;
		boolean valid;
		System.out.print("\tTeste arbres valides (testing valid trees)...");

		valid=RBT.isValid(null); // check the empty tree
		assert (valid): "\nL'arbre vide devrait etre valide (the tree should be valid)";

		for(int i=0;i<repeat;i++) { // teste des arbres valides de petite taille: n=8
			validTree=TreeGenerator.getValidRedBlackTree(TreeGenerator.villes, i, 8, false); // arbre valide
			if(BST.isBST(validTree)==false)
				throw new Error("Error: the tree is not a Binary Search tree");

			valid=RBT.isValid(validTree); // Check condition (3) for black nodes
			if(!valid) {
				new DrawBinaryTree(validTree); // draw the tree in a graphical frame
			}
			assert (valid): "\nL'arbre devrait etre valide (the tree should be valid)";
		}
		int maxSize=20;
		for(int i=0;i<repeat;i++) { // teste des arbres valides de taille moyenne: n=20
			validTree=TreeGenerator.generateTreeStoringNumbers(i, maxSize); // arbre valide
			if(BST.isBST(validTree)==false)
				throw new Error("Error: the tree is not a Binary Search tree");

			valid=RBT.isValid(validTree);
			if(!valid) {
				new DrawBinaryTree(validTree); // draw the tree in a graphical frame
			}
			assert (valid): "\nL'arbre devrait etre valide (the tree should be valid)";
		}
		maxSize=500;
		for(int i=0;i<repeat;i++) { // teste des arbres valides de plus grosses taille: n=500
			validTree=TreeGenerator.generateTreeStoringNumbers(i, maxSize); // arbre valide
			if(BST.isBST(validTree)==false)
				throw new Error("Error: the tree is not a Binary Search tree");

			valid=RBT.isValid(validTree);
			if(!valid) {
				new DrawBinaryTree(validTree); // draw the tree in a graphical frame
			}
			assert (valid): "\nL'arbre devrait etre valide (the tree should be valid)";
		}
		System.out.println("\t\t\t [OK].");

		// Check for false negative: test invalid RB trees
		System.out.print("\tTeste arbres non valides (testing invalid trees)...");

		valid=RBT.isValid(TreeGenerator.invalidTinyTrees[0]); 
		assert (!valid): "\nLa racine devrait etre un noeud noir\nThe root node should be black";

		// test with tiny non valid trees
		ArrayList<RBT> trees=new ArrayList<RBT>();
		trees.add(TreeGenerator.invalidTinyTrees[1]); // non valid
		trees.add(TreeGenerator.invalidTinyTrees[2]); // non valid
		trees.add(TreeGenerator.invalidTinyTrees[3]); // non valid
		trees.add(TreeGenerator.invalidTinyTrees[4]); // non valid
		trees.add(TreeGenerator.invalidTinyTrees[5]); // non valid
		trees.add(TreeGenerator.invalidTinyTrees[6]); // non valid
		trees.add(TreeGenerator.invalidTinyTrees[7]); // non valid
		trees.add(TreeGenerator.invalidTinyTrees[8]); // non valid
		trees.add(TreeGenerator.invalidTinyTrees[9]); // non valid
		trees.add(TreeGenerator.invalidTinyTrees[10]); // non valid
		trees.add(TreeGenerator.invalidTinyTrees[11]); // non valid
		trees.add(TreeGenerator.invalidTinyTrees[12]); // non valid
		trees.add(TreeGenerator.invalidTinyTrees[13]); // non valid
		trees.add(TreeGenerator.invalidTinyTrees[14]); // non valid

		// check for tiny trees
		for(RBT t: trees) {
			if(BST.isBST(t)==false)
				throw new Error("Error: the tree is not a Binary Search tree");

			valid=RBT.isValid(t); // check the property of red nodes
			if(valid) new DrawBinaryTree(t); // draw the tree in a graphical frame
			assert (!valid): "\nLe résultat du teste devrait etre faux\nThe result should be false";
		}
		
		maxSize=20; // size of the tree
		repeat=20; // check for 20 non valid Red-Black trees
		RBT t;
		for(int i=0;i<repeat;i++) { // teste des arbres valides de taille moyenne: n=20
			t=TreeGenerator.generateInvalidTreeStoringNumbers(i, maxSize); // arbre valide
			if(BST.isBST(t)==false)
				throw new Error("Error: the tree is not a Binary Search tree");

			valid=RBT.isValid(t); // check the property of red nodes
			if(valid) new DrawBinaryTree(t); // draw the tree in a graphical frame
			assert (!valid): "\nLe résultat du teste devrait etre faux\nThe result should be false";
		}

		System.out.println("\t\t [OK].");
	}
	
}

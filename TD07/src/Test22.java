import java.util.ArrayList;

public class Test22 {
	public static void main(String[] args) {
		if (!Test21.class.desiredAssertionStatus()) {
			System.err.println("Vous devez activer l'option -ea de la JVM");
			System.err.println("To run the tests please activate the -ea option of the JVM");
			System.exit(1);
		}

		System.out.println("(Q2.2) Validité des noeuds noirs (validity for black nodes): Test de isBlackValid()");

		int repeat=10;
		RBT validTree;
		boolean valid;
		System.out.print("\tTeste arbres valides (testing valid trees)...");
		for(int i=0;i<repeat;i++) { // teste des arbres valides de petite taille: n=8
			validTree=TreeGenerator.getValidRedBlackTree(TreeGenerator.villes, i, 8, false); // arbre valide
			if(BST.isBST(validTree)==false)
				throw new Error("Error: the tree is not a Binary Search tree");

			valid=RBT.isBlackValid(validTree); // Check condition (3) for black nodes
			if(!valid) {
				new DrawBinaryTree(validTree); // draw the tree in a graphical frame
			}
			assert (valid): "\nL'arbre devrait etre valide (la propriété (3) des noeuds noirs est respectée)" + 
			"\nThe tree should be valid (property (3) of black nodes is satisfied)";
		}
		int maxSize=20;
		for(int i=0;i<repeat;i++) { // teste des arbres valides de taille moyenne: n=20
			validTree=TreeGenerator.generateTreeStoringNumbers(i, maxSize); // arbre valide
			if(BST.isBST(validTree)==false)
				throw new Error("Error: the tree is not a Binary Search tree");

			valid=RBT.isBlackValid(validTree);
			if(!valid) {
				new DrawBinaryTree(validTree); // draw the tree in a graphical frame
			}
			assert (valid): "\nL'arbre devrait etre valide (la propriété (3) des noeuds noirs est respectée)" + 
			"\nThe tree should be valid (property (3) of black nodes is satisfied)";
		}
		maxSize=500;
		for(int i=0;i<repeat;i++) { // teste des arbres valides de plus grosses taille: n=500
			validTree=TreeGenerator.generateTreeStoringNumbers(i, maxSize); // arbre valide
			if(BST.isBST(validTree)==false)
				throw new Error("Error: the tree is not a Binary Search tree");

			valid=RBT.isBlackValid(validTree);
			if(!valid) {
				new DrawBinaryTree(validTree); // draw the tree in a graphical frame
			}
			assert (valid): "\nL'arbre devrait etre valide (la propriété (3) des noeuds noirs est respectée)"+
			"\nThe tree should be valid (property (3) of black nodes is satisfied)";
		}
		System.out.println("\t\t\t [OK].");

		// Check for false negative: test invalid RB trees
		System.out.print("\tTeste arbres non valides (testing invalid trees)...");

		// test with tiny non valid trees
		ArrayList<RBT> trees=new ArrayList<RBT>();
		trees.add(TreeGenerator.invalidTinyTrees[3]); // non valid for black nodes
		trees.add(TreeGenerator.invalidTinyTrees[5]); // non valid for black nodes
		trees.add(TreeGenerator.invalidTinyTrees[6]); // non valid for black nodes
		trees.add(TreeGenerator.invalidTinyTrees[8]); // non valid for black nodes
		trees.add(TreeGenerator.invalidTinyTrees[9]); // non valid for black nodes
		trees.add(TreeGenerator.invalidTinyTrees[10]); // non valid for black nodes
		trees.add(TreeGenerator.invalidTinyTrees[11]); // non valid for black nodes
		trees.add(TreeGenerator.invalidTinyTrees[12]); // non valid for black nodes
		trees.add(TreeGenerator.invalidTinyTrees[13]); // non valid for black nodes
		trees.add(TreeGenerator.invalidTinyTrees[14]); // non valid for black nodes

		// check for tiny trees
		for(RBT t: trees) {
			if(BST.isBST(t)==false)
				throw new Error("Error: the tree is not a Binary Search tree");

			valid=RBT.isBlackValid(t); // check the property of red nodes
			if(valid) new DrawBinaryTree(t); // draw the tree in a graphical frame
			assert (!valid): "\nLe résultat du test devrait etre faux, car l'arbre ne respecte pas la propriété (3) des noeuds noirs" + 
			"\nThe result should be false, since condition (3) for black nodes is not satisfied)";
		}
		
		maxSize=20; // size of the tree
		repeat=20; // check for 20 non valid Red-Black trees
		RBT t;
		for(int i=0;i<repeat;i++) { // teste des arbres valides de taille moyenne: n=20
			t=TreeGenerator.generateInvalidTreeStoringNumbers(i, maxSize); // arbre valide
			if(BST.isBST(t)==false)
				throw new Error("Error: the tree is not a Binary Search tree");

			valid=RBT.isBlackValid(t); // check the property of red nodes
			if(valid) new DrawBinaryTree(t); // draw the tree in a graphical frame
			assert (!valid): "\nLe résultat du test devrait etre faux, car l'arbre ne respecte pas la propriété (3) des noeuds noirs" + 
			"\nThe result should be false, since condition (3) for black nodes is not satisfied)";
		}

		System.out.println("\t\t [OK].");
	}
	
}

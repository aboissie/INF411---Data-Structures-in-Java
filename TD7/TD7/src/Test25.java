import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

public class Test25 {
	public static void main(String[] args) {
		if (!Test21.class.desiredAssertionStatus()) {
			System.err.println("Vous devez activer l'option -ea de la JVM");
			System.err.println("To run the tests please activate the -ea option of the JVM");
			System.exit(1);
		}

		System.out.println("(Q2.5) Validité: Test de ofList(LinkedList<String> l)");

		String[] villesTriees = new String[] {"Angers", "Bordeaux", "Dijon", "Grenoble", "Lyon", "Marseille", "Montpellier",
				"Nantes", "Nice", "Paris", "Reims", "Rennes", "Strasbourg", "Toulon" };

		checkTreeConstruction(villesTriees);
		checkTreeConstruction(generateRandomNumbers(0, 0.2));
	}

	public static void checkTreeConstruction(String[] data) {
		System.out.print("\tTeste construction de l'arbre à partir de liste triée ("+data.length+" éléments)...");
		RBT tree;
		LinkedList<String> liste=new LinkedList<String>();
		tree = RBT.ofList(liste);
		assert(tree==null) : "la liste ne contient aucun élément, l'arbre devrait donc etre vide (the tree should be empty, since the list is empty)";

		for (int k = 1; k <= data.length; k++) {
			liste = new LinkedList<String>();
			for (int i = 0; i < k; i++) {
				liste.add(data[i]);
			}
			int sizeList=liste.size();
			tree = RBT.ofList(liste);
			int treeSize=RBT.size(tree);

			if(sizeList!=treeSize)
				new DrawBinaryTree(tree);
			assert (sizeList==treeSize) : "l'arbre contient "+treeSize+" noeuds, au lieu de "+sizeList +"\nThe tree contains "+treeSize+" nodes, instead of "+sizeList;

			assert (BST.isBST(tree)==true) : "l'arbre n'est pas un arbre binaire de recherche valide (the tree is not a valid binary search tree)";
			assert (RBT.isValid(tree)==true) : "l'arbre n'est pas un arbre rouge noir valide, d'après votre méthode isValid\n" +
			"the tree is not a valid Red-Black tree, according to the result of your implementatio of isValid() method";
		}

		System.out.println("\t\t[OK].");
	}

	/** 
     * Generate strings corresponding to 'k' random numbers (ranging in [0...999])
    */
    public static String[] generateRandomNumbers(int seed, double p) {
        int N=1000;

		ArrayList<String> data=new ArrayList<String>();

        Random rand = new Random(seed);
        int count=0;
        
        while(count<N) {
			double r=rand.nextDouble();
            if(r<0.5) {
                String number="";
                if(count<10)
                    number="00"+count;
                else if (count<100)
                    number="0"+count;
                else
                    number=""+count;
                data.add(number);
                count++;
            }
        }
		String[] result=new String[data.size()];
		return data.toArray(result);

    }

}

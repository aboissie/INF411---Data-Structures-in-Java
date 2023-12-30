
public class TestInsert {
	
	public static int size(KDTree tree) {
		if (tree == null)
			return 0;
		return 1 + size(tree.left) + size(tree.right);
	}

	static double p [][] = {
			{ 0.0, 0.0, 0.0 },
			{ -1.0, 0.0, 0.0 },
			{ 1.0, 0.0, 0.0 },
			{ 0.0, 2.0, 0.0 },
			{ -1.0, -1.0, -1.0 }
	};

	static KDTree do_insert(KDTree kd, int i) {
		System.out.printf("kd = insert(%s, p%d = [%f, %f, %f]);\n",
				(kd == null ? "null" : "kd"),
				i, p[i][0], p[i][1], p[i][2]);
		int size_before = size(kd);
		kd = KDTree.insert(kd, p[i]);
		int size_after = size(kd);
		assert (size_after == size_before + 1):
			String.format("taille avant la derniere insertion = %d, apres = %d", size_before, size_after);
		return kd;
	}

	public static void main(String[] args) {

		if (!TestInsert.class.desiredAssertionStatus()) {
			System.err.println("Vous devez activer l'option -ea de la JVM");
			System.exit(1);
		}

		// test de KDtree.insert
		System.out.println("--Test de la methode insert");

		KDTree kd = do_insert(null, 0);
		assert (kd != null) : "...devrait renvoyer un nouveau KDTree contenant p a profondeur 0 mais a renvoye null";

		kd = do_insert(kd, 1);
		assert (kd.left != null) : "...devrait renvoyer un KDTree tel que kd.left!=null";
		assert (kd.left.point.equals(p[1])) : "kd devrait contenir le point p1 dans le sous-arbre gauche";
		
		kd = do_insert(kd, 2);
		assert (kd.right != null) : "kd.right devrait etre non null";
		assert (kd.right.point.equals(p[2])
				&& kd.right.depth == 1) : "kd.right devrait etre p2 a profondeur 1";
		
		kd = do_insert(kd, 4);
		assert (kd.left.left != null) : "kd.left.left devrait etre != null";
		assert (kd.left.left.point.equals(p[4])
				&& kd.left.left.depth == 2) : "kd.left.left.point devrait etre p4 a profondeur 2";

		kd = do_insert(kd, 3);
		kd = do_insert(kd, 4);
		assert (kd.right != null) : "kd.right devrait etre != null";
		assert (kd.right.point.equals(p[2])
				&& kd.right.depth == 1) : "kd.right devrait etre p2 a profondeur 1";
		assert (kd.right.right != null) : "kd.right.right devrait etre != null";
		assert (kd.right.right.point.equals(p[3])	&& kd.right.right.depth == 2) :
			"kd.right.right devrait etre p3 a profondeur 2,"
						+ " or on a kd.right.right.depth=" + kd.right.right.depth;

		System.out.println("[OK]");
	}

}

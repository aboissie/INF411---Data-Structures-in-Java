
public class Test24 {
	public static void main(String[] args) {
		if (!Test21.class.desiredAssertionStatus()) {
			System.err.println("Vous devez activer l'option -ea de la JVM");
			System.err.println("To run the tests please activate the -ea option of the JVM");
			System.exit(1);
		}

		System.out.print("(Q2.4) Validité: Test de estimateBlackHeight()...");

		int n, res;

		n=0;
		res=RBT.estimateBlackHeight(n);
		assert (res==0): "\nLa valeur pour n="+n+" devrait etre 0, au lieu de "+res;
		n=1;
		res=RBT.estimateBlackHeight(n);
		assert (res==1): "\nLa valeur pour n="+n+" devrait etre 1, au lieu de "+res;
		n=2;
		res=RBT.estimateBlackHeight(n);
		assert (res==1): "\nLa valeur pour n="+n+" devrait etre 1, au lieu de "+res;
		n=9;
		res=RBT.estimateBlackHeight(n);
		assert (res==3): "\nLa valeur pour n="+n+" devrait etre 3, au lieu de "+res;
		n=127;
		res=RBT.estimateBlackHeight(n);
		assert (res==7): "\nLa valeur pour n="+n+" devrait etre 7, au lieu de "+res;
		n=2048;
		res=RBT.estimateBlackHeight(n);
		assert (res==11): "\nLa valeur pour n="+n+" devrait etre 11, au lieu de "+res;
		n=4194310;
		res=RBT.estimateBlackHeight(n);
		assert (res==22): "\nLa valeur pour n="+n+" devrait etre 22, au lieu de "+res;

		System.out.println("\t\t [OK].");
	}
	
}


public class Test12 {
	public static void main(String[] args) {
		if (!Test21.class.desiredAssertionStatus()) {
			System.err.println("Vous devez activer l'option -ea de la JVM");
			System.err.println("To run the tests please activate the -ea option of the JVM");
			System.exit(1);
		}

		System.out.println("(Q1.2) Test method deleteDuplicates(BinaryImage[] a)");
		testDuplicates(ImageGenerator.generateRandomImages(0, 0.8, 20, 20, 10));
		testDuplicates(ImageGenerator.generateRandomImages(0, 0.8, 20, 30, 1000));
		testDuplicates(ImageGenerator.generateRandomImages(0, 0.3, 30, 20, 10000));
	}

	public static void testDuplicates(BinaryImage[] images) {
		System.out.print("\t Test method deleteDuplicates(): "+images.length+" images...");
		int k=images.length;

		BinaryImage[] duplicates=ImageGenerator.createDuplicatesDeterministic(images);
		BinaryImage[] res=BinaryImage.deleteDuplicates(duplicates);

		for(BinaryImage im:res) {
			assert(im!=null) : "image non définie (null pointer, image not defined)";
		}

		assert(res.length==k) : "le résultat est un tableau de taille "+res.length+" au lieu de "+images.length;

		res=BinaryImage.deleteDuplicates(duplicates);
		assert(res.length==k) : "deleteDuplicates(deleteDuplicates(t)) diffère de deleteDuplicates(t)";

		System.out.println("\t\t [OK].");
	}
}

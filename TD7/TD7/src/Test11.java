
public class Test11 {
	public static void main(String[] args) {
		if (!Test21.class.desiredAssertionStatus()) {
			System.err.println("Vous devez activer l'option -ea de la JVM");
			System.err.println("To run the tests please activate the -ea option of the JVM");
			System.exit(1);
		}

		System.out.println("(Q1.1) Test: equals()");
		testEquals(ImageGenerator.generateRandomImages(0, 0.8, 20, 20, 10));
		testEquals(ImageGenerator.generateRandomImages(0, 0.8, 20, 20, 1000));
	}

	public static void testEquals(BinaryImage[] images) {
		System.out.print("\t teste equals() sur "+images.length+" images...");

		BinaryImage im1=new BinaryImage(10, 5);
		BinaryImage im2=new BinaryImage(10, 2);
		BinaryImage im3=new BinaryImage(2, 2);
		assert(!im1.equals(im2)) : "les deux images diffèrent par leur taille";
		assert(!im3.equals(im2)) : "les deux images diffèrent par leur taille";

		for(BinaryImage im: images) {
			int n=im.pixels.length;
			int m=im.pixels.length;
			BinaryImage copy=ImageGenerator.copy(im);
			assert(im.equals(copy)) : "les deux images devraient coincider";

			int r=(int)(Math.random()*n);
			copy.inversePixel(r, 0);
			assert(im.equals(copy)==false) : "les deux images diffèrent d'un pixel";

			copy=new BinaryImage(n+1, m+1);
			assert(im.equals(copy)==false) : "les deux images diffèrent par leur taille";
		}
		System.out.println("\t\t [OK].");
	}
}

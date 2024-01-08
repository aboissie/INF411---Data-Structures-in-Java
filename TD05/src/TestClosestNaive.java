public class TestClosestNaive extends TestClosest {

	TestClosestNaive() {
		name = "closestNaive()";
	}

	double[] closest(KDTree tree, double[] a) {
		return KDTree.closestNaive(tree, a);
	}

	public static void main(String[] args) {
		TestClosest test = new TestClosestNaive();
		test.testClosest(10, 10);
		test.testClosest(100, 100);
		System.out.println("Le test suivant peut prendre une vingtaine de secondes");
		test.testClosest(20000, 1000);
	}

}

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class KDTree {
	int depth;
	double[] point;
	KDTree left;
	KDTree right;

	KDTree(double[] point, int depth) {
		this.point = point;
		this.depth = depth;
	}

	boolean compare(double[] a) {
		int r = depth % a.length;
		return (a[r] >= point[r])? true:false;
	}

	static KDTree insert(KDTree tree, double[] p, int depth) {
		if (tree == null) return new KDTree(p, depth);
		if (tree.compare(p)) tree.right = insert(tree.right, p, depth + 1);
		else tree.left = insert(tree.left, p, depth + 1);

		return tree;
	}

	static KDTree insert(KDTree tree, double[] p) {
		return KDTree.insert(tree, p, 0);
	}
	
	static double sqDist(double[] a, double[] b) {
		double cum = 0.0;
		for(int idx = 0; idx < a.length; idx++) cum += Math.pow((a[idx] - b[idx]), 2);
		return cum;
	}

	static double[] closestNaive(KDTree tree, double[] a, double[] champion) {
		if(tree == null) return champion;
		if(sqDist(a, champion) > sqDist(a, tree.point)) champion = tree.point;

		champion = closestNaive(tree.left, a, champion);
		return closestNaive(tree.right, a, champion);
	}


	static double[] closestNaive(KDTree tree, double[] a) {
		if (tree == null) return null;
		return closestNaive(tree, a, tree.point);
	}

	static double[] closest(KDTree tree, double[] a, double[] champion) {
		if (tree == null)
			return champion;

		// sert pour InteractiveClosest.
		InteractiveClosest.trace(tree.point, champion);
		
		throw(new Error("TODO"));
	}

	static double[] closest(KDTree tree, double[] a) {
		throw(new Error("TODO"));
	}

	static int size(KDTree tree) {
		throw(new Error("TODO"));
	}

	static void sum(KDTree tree, double[] acc) {
		throw(new Error("TODO"));
	}

	static double[] average(KDTree tree) {
		throw(new Error("TODO"));
	}


	static Vector<double[]> palette(KDTree tree, int maxpoints) {
		throw(new Error("TODO"));
	}

	public String pointToString() {
		StringBuffer sb = new StringBuffer();
		sb.append("[");
		if (this.point.length > 0)
			sb.append(this.point[0]);
		for (int i = 1; i < this.point.length; i++)
			sb.append("," + this.point[i]);
		sb.append("]");
		return sb.toString();
	}

}

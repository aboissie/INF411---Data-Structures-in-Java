import java.util.Arrays;

class Fenwick {
	Fenwick left;
	Fenwick right;
	final int lo;
	final int hi;
	int acc;

	// Question 2.1

	// Constructeur
	Fenwick(int lo, int hi) {		
		this.lo = lo;
		this.hi = hi;
		this.acc = 0;

		if(hi - lo > 1){
			int mid = (hi + lo) / 2;

			this.left = new Fenwick(lo, mid);
			this.right = new Fenwick(mid, hi);
		}
	}

	// Question 2.2

	// renvoie la feuille associée à l'intervalle [i,i+1[ si elle existe et null
	// sinon.
	Fenwick get(int i) {
		Fenwick f = this;
		
		while(f.hi - f.lo > 1){
			if(f.hi <= i | f.lo > i) return null;

			int mid = (f.hi + f.lo) / 2;

			if(i < mid) f = f.left;
			if(i >= mid) f = f.right;
		}

		return f;
	}

	// Question 2.3

	// augmente la valeur stockée dans la case d'indice i du tableau
	// et actualise l'arbre pour maintenir les propriétés d'un arbre de Fenwick.
	void inc(int i) {
		if(i < lo | i >= hi) return;
		acc += 1;
		if(left != null) left.inc(i);
		if(right != null) right.inc(i);
	}

	// Question 2.4

	// renvoie la somme des valeurs des cases d'indice < i
	int cumulative(int i) {
		if(i <= lo) return 0;
		if(i >= hi) return acc;

		int res = 0;

		if(left != null) res += left.cumulative(i);
		if(right !=	null) res += right.cumulative(i);

		return res;
	}

}

class CountInversions {

	// Question 3.1 :

	// implémentation naive, quadratique pour calculer le nombre d'inversions
	static int countInversionsNaive(int[] a) {
		int countInvs = 0;
		for(int i = 0; i < a.length; i++){
			for(int j = i + 1; j < a.length; j++) if(a[i] > a[j]) countInvs += 1;
		}

		return countInvs;
	}

	// Question 3.2 :

	// une implémentation moins naive, mais supposant une plage d'éléments pas trop
	// grande espace O(max-min)
	static int[] findMaxMin(int[] a){
		int min = Integer.MAX_VALUE;
		int max = Integer.MIN_VALUE;

		for(int t:a){
			min = (min <= t)? min:t;
			max = (max >= t)? max:t;
		}

		return new int[]{min, max};
	}
	static int[] reverseList(int[] a){
		int[] res = new int[a.length];
		for(int i = 0; i < a.length; i++) res[a.length - i - 1] = a[i];
		return res; 
	}

	static int countInversionsFen(int[] a) {
		int[] res = findMaxMin(a);
		
		Fenwick f = new Fenwick(res[0], res[1] + 1);
		int s = 0;

		for(int t:reverseList(a)){
			s += f.cumulative(t);
			f.inc(t);
		}

		return s;
	}

	// Question 3.3

	// une implémentation encore meilleure, sans rien supposer sur les éléments
	// cette fois

	static int countInversionsBest(int[] a) {
		int n = a.length;
		int[] copy = Arrays.copyOf(a, n);
		Arrays.sort(copy);
		int[] b = new int[n];
		for (int i = 0; i < n; i++)
			b[i] = Arrays.binarySearch(copy, a[i]);
		return countInversionsFen(b);
	}

}

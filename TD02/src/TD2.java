
/* TD2. Fruits et tables de hachage
 * Ce fichier contient 7 classes:
 * 		- Row représente une ligne de fruits,
 * 		- CountConfigurationsNaive compte naïvement les configurations stables,
 * 		- Quadruple manipule des quadruplets,
 * 		- HashTable construit une table de hachage,
 * 		- CountConfigurationsHashTable compte les configurations stables en utilisant notre table de hachage,
 * 		- Triple manipule des triplets,
 * 		- CountConfigurationsHashMap compte les configurations stables en utilisant la HashMap de java.
 */

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Vector;

class Row { // représente une ligne de fruits
	private final int[] fruits;
	
	// constructeur d'une ligne vide
	Row() {
		this.fruits = new int[0];
	}

	// constructeur à partir du champ fruits
	Row(int[] fruits) {
		this.fruits = fruits;
	}

	// méthode equals pour comparer la ligne à un objet o
	@Override
	public boolean equals(Object o) {
		// on commence par transformer l'objet o en un objet de la classe Row
		// ici on suppose que o sera toujours de la classe Row
		Row that = (Row) o;
		// on vérifie que les deux lignes ont la meme longueur
		if (this.fruits.length != that.fruits.length)
			return false;
		// on vérifie que les ièmes fruits des deux lignes coincident
		for (int i = 0; i < this.fruits.length; ++i) {
			if (this.fruits[i] != that.fruits[i])
				return false;
		}
		// on a alors bien l'égalité des lignes
		return true;
	}

	// code de hachage de la ligne
	@Override
	public int hashCode() {
		int hash = 0;
		for (int i = 0; i < fruits.length; ++i) {
			hash = 2 * hash + fruits[i];
		}
		return hash;
	}

	// chaîne de caracteres représentant la ligne
	@Override
	public String toString() {
		StringBuffer s = new StringBuffer();
		for (int i = 0; i < fruits.length; ++i)
			s.append(fruits[i]);
		return s.toString();
	}

	public int size() {
		return this.fruits.length;
	}

	// Question 1

	// renvoie une nouvelle ligne en ajoutant fruit à la fin de la ligne
	Row extendedWith(int fruit) {
		int[] new_fruits = new int[this.fruits.length + 1];
		for(int i = 0; i < this.fruits.length; i++) new_fruits[i] = this.fruits[i];
		new_fruits[this.fruits.length] = fruit;
		
		return new Row(new_fruits);
	}

	// renvoie la liste de toutes les lignes stables de largeur width
	static LinkedList<Row> allStableRows(int width) {
		LinkedList<Row> result = new LinkedList<Row>();
		if(width == 0) result.add(new Row());
		else{
			LinkedList<Row> res = allStableRows(width - 1);
			for(int i = 0; i < res.size(); i++){
				Row r = res.get(i);
				if(r.fruits.length < 2 || r.fruits[r.fruits.length -1] != r.fruits[r.fruits.length - 2]){
					result.add(r.extendedWith(0));
					result.add(r.extendedWith(1));
				}

				else result.add(r.extendedWith(1 - r.fruits[r.fruits.length - 1]));
			}
		}

		return result;
	}

	// vérifie si la ligne peut s'empiler avec les lignes r1 et r2
	// sans avoir trois fruits du même type adjacents
	boolean areStackable(Row r1, Row r2) {
		if(this.fruits.length != r1.fruits.length | this.fruits.length != r2.fruits.length) return false;
		for(int idx = 0; idx < r1.fruits.length; idx++){
			if(this.fruits[idx] == r1.fruits[idx] & r1.fruits[idx] == r2.fruits[idx]) return false;
		}
		return true;
	}
}

// COMPTAGE NAIF
class CountConfigurationsNaive { // comptage des configurations stables

	// Question 2

	// renvoie le nombre de grilles dont les premières lignes sont r1 et r2,
	// dont les lignes sont des lignes de rows et dont la hauteur est height
	static long count(Row r1, Row r2, LinkedList<Row> rows, int height) {
		if(height <= 1) return 0;
		if(height == 2) return 1;
		
		long res = 0;
		for(Row r:rows){
			if(r.areStackable(r1, r2)) res += count(r2, r, rows, height - 1);
		}
		return res;
	}

	// renvoie le nombre de grilles à n lignes et n colonnes
	static long count(int n) {
		LinkedList<Row> rows = Row.allStableRows(n);
		if (n <= 1) return rows.size();
		long res = 0;
		for(Row r1: rows)
			for(Row r2: rows)
				res += count(r1, r2, rows, n);

		return res;
    }
}

// CONSTRUCTION ET UTILISATION D'UNE TABLE DE HACHAGE

class Quadruple { // quadruplet (r1, r2, height, result)
	Row r1;
	Row r2;
	int height;
	long result;

	Quadruple(Row r1, Row r2, int height, long result) {
		this.r1 = r1;
		this.r2 = r2;
		this.height = height;
		this.result = result;
	}
}

class HashTable { // table de hachage
	final static int M = 50000;
	Vector<LinkedList<Quadruple>> buckets;

	// Question 3.1

	// constructeur
	HashTable() {
		this.buckets = new Vector<LinkedList<Quadruple>>(M);
		for(int i = 0; i < M; i ++){
			this.buckets.add(new LinkedList<Quadruple>());
		}
	}

	// Question 3.2

	// renvoie le code de hachage du triplet (r1, r2, height)
	static int hashCode(Row r1, Row r2, int height) {
		return 13 * r1.hashCode() * r1.hashCode() + 17 * r2.hashCode() * r2.hashCode() + height;
	}

	// renvoie le seau du triplet (r1, r2, height)
	int bucket(Row r1, Row r2, int height) {
		int mod = hashCode(r1, r2, height) % M;
		return (mod >= 0 ? mod : mod + M);
	}

	// Question 3.3

	// ajoute le quadruplet (r1, r2, height, result) dans le seau indiqué par la
	// methode bucket
	void add(Row r1, Row r2, int height, long result) {
		buckets.get(bucket(r1, r2, height)).add(new Quadruple(r1, r2, height, result));
	}

	// Question 3.4

	// recherche dans la table une entrée pour le triplet (r1, r2, height)
	Long find(Row r1, Row r2, int height) {
		for (Quadruple q : buckets.get(bucket(r1, r2, height))) {
			if (r1.equals(q.r1) && r2.equals(q.r2) && height == q.height)
				return Long.valueOf(q.result);
		}	
		return null;
	}

}

class CountConfigurationsHashTable { // comptage des configurations stables en utilisant notre table de hachage
	static HashTable memo = new HashTable();

	// Question 4

	// renvoie le nombre de grilles dont les premières lignes sont r1 et r2,
	// dont les lignes sont des lignes de rows et dont la hauteur est height
	// en utilisant notre table de hachage
	static long count(Row r1, Row r2, LinkedList<Row> rows, int height) {
		if(height <= 1) return 0;
		if(height == 2) return 1;
		if(memo.find(r1, r2, height) != null) return memo.find(r1, r2, height);

		long res = 0;
		for(Row r:rows){
			if(r.areStackable(r1, r2)) res += count(r2, r, rows, height - 1);
		}

		memo.add(r1, r2, height, res);
		return res;
	}

	// renvoie le nombre de grilles a n lignes et n colonnes
	static long count(int n) {
		LinkedList<Row> rows = Row.allStableRows(n);
		if (n <= 1) return rows.size();
		long res = 0;
		for(Row r1: rows)
			for(Row r2: rows)
				res += count(r1, r2, rows, n);

		return res;
	}
}

//UTILISATION DE HASHMAP

class Triple { // triplet (r1, r2, height)
	private Row r1, r2;
	private int height;

	Triple(Row r1, Row r2, int height){
		this.r1 = r1;
		this.r2 = r2;
		this.height = height;
	}

	@Override
	public boolean equals(Object o){
		Triple that = (Triple) o;
		return (that.height == this.height) & (that.r1.equals(this.r1)) && (that.r2.equals(this.r2));
	}

	@Override
	public int hashCode() {
		return 13 * r1.hashCode() * r1.hashCode() + 17 * r2.hashCode() * r2.hashCode() + height;
	}
};


class CountConfigurationsHashMap { // comptage des configurations stables en utilisant la HashMap de java
	static HashMap<Triple, Long> memo = new HashMap<Triple, Long>();

	// Question 5

	// renvoie le nombre de grilles dont les premières lignes sont r1 et r2,
	// dont les lignes sont des lignes de rows et dont la hauteur est height
	// en utilisant la HashMap de java
	static long count(Row r1, Row r2, LinkedList<Row> rows, int height) {
		if(height <= 1) return 0;
		if(height == 2) return 1;
		Triple t = new Triple(r1, r2, height);
		if(memo.get(t) != null) return memo.get(t);

		long res = 0;
		for(Row r:rows){
			if(r.areStackable(r1, r2)) res += count(r2, r, rows, height - 1);
		}

		memo.put(t, res);
		return res;
	}

	// renvoie le nombre de grilles à n lignes et n colonnes
	static long count(int n) {
		LinkedList<Row> rows = Row.allStableRows(n);
		if (n <= 1) return rows.size();
		long res = 0;
		for(Row r1: rows)
			for(Row r2: rows)
				res += count(r1, r2, rows, n);

		return res;
	}
}

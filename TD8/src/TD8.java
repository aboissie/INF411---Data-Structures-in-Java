/* TD8. Tri fusion en place et généricité 
 * Ce fichier contient 5 classes:
 * 	- Singly<E> : listes chaînées génériques,
 * 	- MergeSortString : algorithme merge-sort pour les listes (chaînées) de chaînes de caractères,
 * 	- Occurrence : comptage des mots d'un texte,
 *  - MergeSort : algorithme merge-sort générique (on remplace le type «String» par le type générique «E»),
 *  - Median : calcul de la médiane d'un ensemble de valeurs numériques
 */

/* Remarque : ne sont declarées «public» que les constructeurs, ainsi que les méthodes dont la 
 * visibilité ne peut pas être réduite, ici toString et compareTo.
 */

// SINGLY 
 
class Singly<E> {
	E element;
	Singly<E> next;

	// On choisit de représenter la liste vide par null, les deux constructeurs qui suivent ne
	// peuvent donc pas construire de liste vide.

	// Cree une liste a un element.
	
	public Singly(E element, Singly<E> next) {
		this.element = element;
		this.next = next;
	}

	// Crée une liste à partir d'un tableau non vide.
	
	public Singly(E[] data) {
		assert (data.length > 0) : "\nLe constructeur Singly(E[] data) ne peut pas être utilisé avec un tableau vide"
				+ "\ncar on ne peut pas construire une liste non vide sans données.";
		this.element = data[0];
		this.next = null;
		Singly<E> cursor = this;
		for (int i = 1; i < data.length; i++) {
			cursor.next = new Singly<E>(data[i], null);
			cursor = cursor.next;
		}
		;
	}

	// Copie physique d'une liste (pour les tests uniquement)
	
	static <E> Singly<E> copy(Singly<E> l) {
		if (l == null)
			return null;
		Singly<E> res = new Singly<E>(l.element, l.next);
		Singly<E> cursor = res;
		while (l.next != null) {
			l = l.next;
			cursor.next = new Singly<E>(l.element, l.next);
			cursor = cursor.next;
		}
		return res;
	}

	// Test l'égalite de deux chaînes.
	
	static <E> boolean areEqual(Singly<E> chain1, Singly<E> chain2) {
		while (chain1 != null && chain2 != null) {
			if (!chain1.element.equals(chain2.element))
				return false;
			chain1 = chain1.next;
			chain2 = chain2.next;
		}
		return chain1 == chain2;
	}
	
	// Crée une chaîne de caractères à partir d'une liste chaînée (nécessaire à l'affichage).
	
	public String toString() {
		Singly<E> cursor = this;
		String answer = "[ ";
		while (cursor != null) {
			answer = answer + (cursor.element).toString() + " ";
			cursor = cursor.next;
		}
		answer = answer + "]";
		return answer;
	}

	// Question 1
	// Longueur d'une chaîne. Implémentation itérative pour éviter les 
	// débordements de pile («stack overflow»).
	
	static<E> int length(Singly<E> l) {
		int res = 0;
		while(l != null){
			l = l.next;
			res++;
		}
		return res;
	}
	
	// Question 1
	// Coupe la seconde moitié de la chaîne passée en argument, 
	// la partie supprimée est renvoyée.
	// La méthode split modifie donc la liste passée en argument.
	
	static<E> Singly<E> split(Singly<E> l) {
		int n = length(l);
		if(n <= 1) return null;
		Singly<E> current = l;
		for(int i = 0; i < (n-1)/2; i++){
			current = current.next;
		}
		Singly<E> res = current.next;
		current.next = null;
		return res;
	}
}

/* MERGE_SORT_STRING */

class MergeSortString {

	// Question 2.2
	// Réalise la fusion des chaînes passées en argument, renvoie la chaîne obtenue.
	// Les deux chaînes passées en arguments sont détruites puisque l'opération 
	// se fait «en place».
	
	static Singly<String> merge(Singly<String> l1, Singly<String> l2) {
		if(l1 == null) return l2;
		if(l2 == null) return l1;

		String elem = (l1.element.compareTo(l2.element) < 0)? l1.element:l2.element;
		Singly<String> res = new Singly<String>(elem, MergeSortString.merge((l1.element.compareTo(l2.element) < 0)? l1.next:l1, (l1.element.compareTo(l2.element) < 0)? l2.next:l2));

		return res;
	}

	// Question 2.2
	// Trie (récursivement) la liste passée en argument en triant chacune de ses deux  
	// moitiés séparement avant de fusionner les deux moitiés triées.  
	// La liste passée en argument est détruite au cours de l'opération.
	
	static Singly<String> sort(Singly<String> l) {
		if (l == null || l.next == null) return l;
		return merge(sort(Singly.split(l)), sort(l));
	}

}

/* OCCURRENCE */

class Occurrence implements Comparable<Occurrence> {
	String word;
	int count;

	Occurrence(String word, int count) {
		this.word = word;
		this.count = count;
	}
	
	public String toString() {
		return word;
	}
	
	// Question 2.3
	// Renvoie une liste dont chaque maillon contient un mot présent 
	// dans la liste de mots passée en argument, avec sa multiplicité.   
	// La liste passée en argument peut être détruite.
	
	static Singly<Occurrence> count(Singly<String> l) {
		throw new Error("Méthode count(Singly<String> l) à remplir (Question 2.3)");
	}
	
	// Question 3.2
	// Méthode de comparaison nécessaire a l'utilisation de l'algorithme de tri
	
	public int compareTo(Occurrence that) {
		throw new Error("Méthode compareTo(Occurrence that) à remplir (Question 3.2)");
	}

	// Question 3.2
	// Identique à la méthode count(Singly<String> l) excepté que la liste renvoyée 
	// est triée par ordre décroissant de multiplicité.
	
	static Singly<Occurrence> sortedCount(Singly<String> l) {
		throw new Error("Méthode sortedCount(Singly<String> l) à remplir (Question 3.2)");
	}
}

/* MERGE_SORT */

// Version générique de MergeSortString
// On remplace le type «String» par le type générique «E» dans l'implémentation de MergeSort

class MergeSort {
	
	// Question 3.1
	// Identique à merge(Singly<String> l1, Singly<String> l2) avec «E» au lieu de «String»
	
	static<E extends Comparable<E>> Singly<E> merge(Singly<E> l1, Singly<E> l2) {
		throw new Error("Méthode merge(Singly<E> l1, Singly<E> l2) à remplir (Question 3.1)");
	}

	// Question 3.1
	// Identique à sort(Singly<String> l) avec «E» au lieu de «String»
	
	static<E extends Comparable<E>> Singly<E> sort(Singly<E> l) {
		throw new Error("Méthode sort(Singly<E> l) à remplir (Question 3.1)");
	}

}

/* MEDIAN */

class Median {

	// Question 3.3
	// Renvoie une médiane de l'ensemble de valeurs numériques passé en argument
	// sous la forme d'une liste chaînée.
	
	static Pair<Double> median (Singly<Double> data) {
		throw new Error("Méthode median (Singly<Double> data) à remplir (Question 3.3)");
	}
}

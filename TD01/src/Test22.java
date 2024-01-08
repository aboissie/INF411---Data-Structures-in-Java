import java.util.LinkedList;
import java.util.HashSet;
import java.util.Scanner;

public class Test22 {
	
	static void testOrderPreservation(int timeout) {
		for (int k = 1; k <= timeout; k++) {
			int N = 26;
			LinkedList<Integer> l1 = new LinkedList<Integer>();
			LinkedList<Integer> l2 = new LinkedList<Integer>();
			for (int j = 1; j <= N; j++) {
				l1.add(j);
				l2.add(j + N);
			}
			Deck d1 = new Deck(l1); // le Deck d1 contient 1, 2, ..., N
			Deck d2 = new Deck(l2); // le Deck d1 contient N+1, N+2, ...; 2N
			Deck d1copy = d1.copy();
			Deck d2copy = d2.copy();
			d1.riffleWith(d2); // d1 et d2 sont détruits, d1 contient le résultat du «riffle»
			Deck e1 = new Deck();
			Deck e2 = new Deck();
			// extraction du sous-paquet des cartes de valeur <= N,
			// et de celui des cartes de valeur > N.
			for (Integer card : d1.cards) {
				if (card <= N) {
					e1.cards.addLast(card);
				} else {
					e2.cards.addLast(card);
				}
			}
			assert (e1.equals(d1copy) && e2.equals(d2copy)) : "\nPour les paquets\nd1 = " + d1copy + "\net\nd2 = "
					+ d2copy + "\nle retour de la méthode d1.riffleWith(d2) ne devrait pas être\n" + d1
					+ "\ncar au moins un des paquets extraits\n" + e1 + "\net\n" + e2 + "\nn'est pas croissant (ou il manque des cartes).";
		}
	}
	
	private static int binomial(int a, int b) { 
		if(a>b || a<0) return 0;
		if(a==0 || a==b) return 1; 
		return binomial(a-1, b-1) + binomial(a, b-1);
	}
	
	static void testRandomness(int timeout) { 
		int N = 5;
		int nbOutputs = binomial(N,2*N);
		HashSet<Deck> r = new HashSet<Deck> ();
		int counter = 0; 
		r = new HashSet<Deck> ();
		while(r.size()<nbOutputs && counter <= timeout) {
			counter++;
			LinkedList<Integer> l1 = new LinkedList<Integer>();
			LinkedList<Integer> l2 = new LinkedList<Integer>(); 
			for(int j=1;j<=N;j++) {
				l1.add(j); 
				l2.add(j+N);
			} 
			Deck d1 = new Deck(l1); // le Deck d1 contient 1, 2, ..., N 
			Deck d2 = new Deck(l2); // le Deck d1 contient N+1, N+2, ...; 2N
			d1.riffleWith(d2); // il y a (N parmi 2N) résultats possibles (mais pas uniformément distribués) 
			r.add(d1); 
		}
		assert(r.size()==nbOutputs) : "\nAprès "+timeout+" essais de la méthode RiffleWith avec les paquets 1,2,3,4,5 et 6,7,8,9,10 "
				+ "l'un des "+nbOutputs+" résultats possibles n'est jamais apparu: c'est très improbable, \n"
		+"	votre implémentation de la méthode RiffleWith ne respecte sans doute pas la contrainte probabiliste.";
		System.out.println("[OK]"+" ("+counter+" essais pour couvrir les "+nbOutputs+" possibilités)");
	} 
	
	public static void main(String[] args) {

		// vérifie que les asserts sont activés
		if (!Test22.class.desiredAssertionStatus()) {
			System.err.println("Vous devez passer l'option -ea à la machine virtuelle Java.");
			System.err.println("Voir la section 'Activer assert' du préambule du TD.");
			System.exit(1);
		}

		// tests de la méthode riffleWith
		System.out.print("Test de la méthode riffleWith ... ");
		testOrderPreservation(10000);
		testRandomness(10000); 
	}
}

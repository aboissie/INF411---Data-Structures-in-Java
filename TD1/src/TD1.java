/* TD1. Bataille
 * Ce fichier contient deux classes :
 * 		- Deck représente un paquet de cartes,
 * 		- Battle représente un jeu de bataille.
 */

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

class Deck { // représente un paquet de cartes

	LinkedList<Integer> cards;

	// les méthodes toString, hashCode, equals, et copy sont utilisées pour 
  // l'affichage et les tests, vous ne devez pas les modifier.

	@Override
	public String toString() {
		return cards.toString();
	}

	@Override
	public int hashCode() {
		return 0;
	}
	
	@Override
	public boolean equals(Object o) {
		Deck d = (Deck) o;
		return cards.equals(d.cards);
	}

	Deck copy() {
		Deck d = new Deck();
		for (Integer card : this.cards)
			d.cards.addLast(card);
		return d;
	}

	// constructeur d'un paquet vide
	Deck() {
		cards = new LinkedList<Integer>();
	}

	// constructeur à partir du champ
	Deck(LinkedList<Integer> cards) {
		this.cards = cards;
	}

	// constructeur d'un paquet de cartes complet trié avec nbVals valeurs
	Deck(int nbVals) {
		cards = new LinkedList<Integer>();
		for (int j = 1; j <= nbVals; j++)
			for (int i = 0; i < 4; i++)
				cards.add(j);
	}

	// Question 1

	// prend une carte du paquet d pour la mettre à la fin du paquet courant
	int pick(Deck d) {
		if(d.cards.size() == 0) return -1;
		int card = d.cards.pop();
		this.cards.add(card);
		return card;
	}

	// prend toutes les cartes du paquet d pour les mettre à la fin du paquet courant
	void pickAll(Deck d) {
		while(d.cards.size() > 0){
			this.pick(d);
		}
	}

	// vérifie si le paquet courant est valide
	boolean isValid(int nbVals) {
		Map<Integer, Integer> m = new HashMap<Integer, Integer>();
		Integer val = 1;
		
		for(Integer c: this.cards){
			val = 1;
			if(m.containsKey(c)) val = m.get(c) + val;
			m.put(c, val);
		}
		
		for (Integer num : m.keySet()) {
            if (num > nbVals || num <= 0 || m.get(num) > 4) {
                return false;
            }
        }

        return true;
    }

	// Question 2.1

	// choisit une position pour la coupe
	int cut() {
		int s = 0;
		for(int i = 0; i < this.cards.size(); i++) s += (Math.random() > 0.5)? 1:0;
		return s;
	}

	// coupe le paquet courant en deux au niveau de la position donnée par cut()
	Deck split() {
		int cutidx = this.cut();
		LinkedList<Integer> res = new LinkedList<>(); 
		for(int i = 0; i < cutidx; i++) res.add(this.cards.pop());
		return new Deck(res);
	}

	// Question 2.2

	// mélange le paquet courant et le paquet d
	void riffleWith(Deck d) {
		Deck res = new Deck();
		while(d.cards.size() > 0 || this.cards.size() > 0){
			int a = this.cards.size();
			int b = d.cards.size();
			float ratio =  ((float) a) / ((float) a + b);
			res.pick((Math.random() < ratio)? this:d);
		}
		this.cards = res.cards;
	}

	// Question 2.3

	// mélange le paquet courant au moyen du riffle shuffle
	void riffleShuffle(int m) {
		for(int i = 0; i < m; i++) this.riffleWith(this.split());
	}
}

class Battle { // représente un jeu de bataille

	Deck player1;
	Deck player2;
	Deck trick;
	boolean turn;

	// constructeur d'une bataille sans cartes
	Battle() {
		player1 = new Deck();
		player2 = new Deck();
		trick = new Deck();
		turn = true;
	}
	
	// constructeur à partir des champs
	Battle(Deck player1, Deck player2, Deck trick) {
		this.player1 = player1;
		this.player2 = player2;
		this.trick = trick;
		this.turn = true;
	}

	// copie la bataille
	Battle copy() {
		Battle r = new Battle();
		r.player1 = this.player1.copy();
		r.player2 = this.player2.copy();
		r.trick = this.trick.copy();
		r.turn = this.turn;
		return r;
	}

	// chaîne de caractères représentant la bataille
	@Override
	public String toString() {
		return "Joueur 1 : " + player1.toString() + "\n" + "Joueur 2 : " + player2.toString() + "\nPli " + trick.toString();
	}

	// Question 3.1

	// constructeur d'une bataille avec un jeu de cartes de nbVals valeurs
	Battle(int nbVals) {
		this.trick = new Deck(nbVals);
		this.trick.riffleShuffle(7);
		this.player1 = new Deck();
		this.player2 = new Deck();
		this.turn = true;

		while(this.trick.cards.size() > 0){
			this.player1.pick(this.trick);
			this.player2.pick(this.trick);
		}
	}

	// Question 3.2

	// teste si la partie est terminée
	boolean isOver() {
		return this.player1.cards.size() == 0 || this.player2.cards.size() == 0;
	}

	// effectue un tour de jeu
	boolean oneRound() {
		if(this.player1.cards.size() == 0 || this.player2.cards.size() == 0) return false;
		Integer c1 = this.player1.cards.removeFirst();
		Integer c2 = this.player2.cards.removeFirst();
		
		if(this.turn){
			this.trick.cards.add(c1);
			this.trick.cards.add(c2);
		}

		else{
			this.trick.cards.add(c2);
			this.trick.cards.add(c1);
		}
		
		this.turn = !this.turn;
		
		if(c1 != c2){
			if(c1 > c2) this.player1.pickAll(this.trick);
			if(c1 < c2) this.player2.pickAll(this.trick);
			return true;
		}
		
		if(this.player1.cards.size() == 0 || this.player2.cards.size() == 0) return false;
		this.trick.pick(player1);
		this.trick.pick(player2);
		return this.oneRound();
	}

	// Question 3.3

	// renvoie le gagnant
	int winner() {
		if(this.player1.cards.size() == this.player2.cards.size()) return 0;
		return (this.player1.cards.size() > this.player2.cards.size())? 1:2;
	}

	// effectue une partie avec un nombre maximum de coups fixé
	int game(int turns) {
		for(int i = 0; i < turns; i++) this.oneRound();
		return this.winner();
	}

	// Question 4.1

	// effectue une partie sans limite de coups, mais avec détection des parties infinies
	int game(){
		Battle b1 = this.copy();
		this.oneRound();
		
		while(!this.isOver()){
			this.oneRound();
			if(this.isOver()) return this.winner();
			this.oneRound();
			if(this.isOver()) return this.winner();
			b1.oneRound();

			if(b1.toString().equals(this.toString())) return 3;
		}
		
		return this.winner();
	}

	// Question 4.2

	// effectue des statistiques sur le nombre de parties infinies
	static void stats(int nbVals, int nbGames) {
		HashMap<Integer, Integer> count = new HashMap<>();
		count.put(0, 0);
		count.put(1, 0);
		count.put(2, 0);
		count.put(3, 0);
		
		for(int i = 0; i < nbGames; i++){
			Integer val = (new Battle(nbVals)).game();
			count.put(val, count.get(val) + 1);
		}

		System.out.println("Victoires du joueur 1 : " + count.get(1));
		System.out.println("Victoires du joueur 2 : " + count.get(2));
		System.out.println("Egalités : " + count.get(0));
		System.out.println("Parties Infinies : " + count.get(3));
	}
}

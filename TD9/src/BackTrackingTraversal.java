import ocean.Coordinate;
import ocean.Direction;
import ocean.Ocean;
import ocean.Traversal;

/***********************************
 * Question 2 : parcours en largeur
 ***********************************/

/**
 * Une classe abstraite, qui étend {@link Traversal} avec le rebroussement
 * de chemin (Question 2.3)
 */
abstract class BackTrackingTraversal extends Traversal {
	/**
	 * Implémente le rebroussement de chemin
	 * 
	 * @param ocean   l'océan en train d'être exploré
	 * @param current le point de départ du rebroussement, c'est-à-dire la cellule
	 *                où l'exploration s'était arrêtée
	 */
	void backTrack(Ocean ocean, Coordinate current) {
		if (current == null) {
			return;
		}
	
		Direction dir = getDirection(ocean, current);
	
		if (dir != null) {
			ocean.setMark(current, path);
	
			Coordinate prev = current.moveTo(dir);
			backTrack(ocean, prev);
		}
	}
}

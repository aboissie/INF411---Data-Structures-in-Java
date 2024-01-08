import java.util.LinkedList;
import java.util.Vector;

/**
 * Une classe représentant un point en 2D à coordonnées entières p=(lw, rw). <br> <br>
 * Class for representing a 2D point with integer coordinates. <br> <br>
 * Utile pour dessiner les arbres binaires (rouges et noirs)
 */
abstract class IntegerPoint2D {

  /** x and y coordinates */
  int lw, rw;

}

/**
 * A class representing a (node of a) Red-Black binary search tree
 */
class RBT extends IntegerPoint2D {
  final static boolean RED = true;
  final static boolean BLACK = false;

  boolean color;
  RBT left, right;
  String element;
  int height;
  /**
   * Construit un noeud d'un arbre binaire Rouge-Noir. <br> <br>
   * Create a node of a Red-Black binary tree.
   * 
   * @param color   couleur du noeud (node color)
   * @param left    fils gauche (left child)
   * @param element élément stocké dans le noeud (element stored in the node)
   * @param right   fils droit (right child)
   */
  RBT(boolean color, RBT left, String element, RBT right) {
    this.color = color;
    this.left = left;
    this.element = element;
    this.right = right;
  }

  RBT(boolean color, RBT left, String element, RBT right, int height) {
    this.color = color;
    this.left = left;
    this.element = element;
    this.right = right;
    this.height = height;
  }

  // Question 2.1

  /**
   * Vérifie si l'arbre en entrée satisfait propriété (2) de la définition des arbres rouges et noirs. <br> <br>
   * Check whether the input tree does satisfy the property (2) of the definitions of a red-Black tree.
   * 
   * @param t noeud racine d'un arbre rouge noir (root node a of Red-Black tree)
   * @return true  if condition (2) is satisfied for all nodes of the input tree
   */
  static boolean isRedValid(RBT t) {
    if(t==null) return true;
    if(t.left==null && t.right==null){
      return true;
    }

    if(t.left==null){
      return (t.color==BLACK | t.color!=t.right.color) && RBT.isRedValid(t.right);
    }

    if(t.right==null){
      return (t.color==BLACK | t.color!=t.left.color) && RBT.isRedValid(t.left);
    }
  
    return (t.color==BLACK | (t.left.color==BLACK && t.right.color==BLACK)) && RBT.isRedValid(t.left) && RBT.isRedValid(t.right);
  }

  // Question 2.2

  /**
   * Vérifie la validité de la condition (3) de la définition des arbres rouges et noirs. <br> <br>
   * Chech the validity of the condition (3) of the definition of Red-Black binary trees.
   * 
   * @param t noeud racine (root node)
   * @return true   si pour tout noeud 'v' de l'arbre, les chemins qui de vont de
   *         'v' jusqu'aux feuilles contiennent tous le même nombre de sommets noirs. <br> <br>
   *         (if for each node v, the paths from v to the leaves do contain the same number of black nodes)
   */
  
  static int isBlackValidRBThelper(RBT t){
    if(t == null) return 0;
    if(RBT.isBlackValidRBThelper(t.left) != RBT.isBlackValidRBThelper(t.right)) return -1000;
    
    int s = Math.max(RBT.isBlackValidRBThelper(t.left), RBT.isBlackValidRBThelper(t.right));
    s += (t.color == BLACK)? 1:0;

    return s;
  }
  
  static boolean isBlackValid(RBT t) {
    if(RBT.isBlackValidRBThelper(t)<-1) return false;
    return true;
  }

  // Question 2.3

  /**
   * Vérifie qu'il s'agit d'un arbre rouge et noir valide (toutes les 3 conditions sont vérifiées). <br> <br>
   * Check whether the input tree is a valid Red-Black tree (all three conditions are verified).
   * 
   * @param t noeud racine (root node))
   * @return true   if the inpu tree is valid
   */
  static boolean isValid(RBT root) {
    if(root == null) return true;
    return RBT.isBlackValid(root) && RBT.isRedValid(root) && root.color==BLACK;
  }

  // Question 2.4

  /**
   * Estime la hauteur d'un arbre rouge et noir contenant n éléments.
   * Cette méthode renvoie l'entier non négatif h qui satisfait: 
   * <tt>
   * 2^h-1 <= n < 2^(h+1)-1
   * </tt> <br><br>
   * Estimate the height of a Red-Black tree containing n elements.
   * This method returns the non negative integer value h satisfying: 
   * <tt>
   * 2^h-1 <= n < 2^(h+1)-1
   * </tt> <br><br>
   * @param n nombre d'éléments à stocker dans l'arbre (number of elements stored
   *          in the tree)
   * @return l'hauteur de l'arbre (height )
   */
  static int estimateBlackHeight(int n) {
    assert n >= 0;
    int h = 0;
    while(Math.pow(2, h + 1) - 1 <= n) h += 1;
    return h;
  }

  // Question 2.5

  /**
   * Consruit un arbre Rouge et Noir à partir d'une liste triée. <br> <br>
   * Construct a Red-Black binary search tree starting from an input sorted list.
   * 
   * @param l  a sorted listed (elements are sorted in ascending order)
   */
  static RBT ofList(LinkedList<String> l) {
    // throw new Error("Méthode RBT ofList(LinkedList<String> l) à compléter (Question 2.5)");
    int h = estimateBlackHeight(l.size());
    return ofList(l, h, l.size());
  }

  static RBT ofList(LinkedList<String> l, int h, int n) {
    assert (1 << h) - 1 <= n && n < (1 << (h + 1));
    if (h == 0) {
      if (n == 0)
        return null;
      return new RBT(RED, null, l.removeFirst(), null);
    }
    int n1 = (n - 1) / 2;
    RBT left = ofList(l, h - 1, n1);
    String e = l.removeFirst();
    RBT right = ofList(l, h - 1, n - n1 - 1);
    return new RBT(BLACK, left, e, right);
  }
  
  /**
   * Affiche les noeuds de l'arbre binaire (ordre infixe). <br> <br>
   * Print the nodes of a binary tree (prefix order).
   * 
   * @param root racine d'un arbre binaire (root of a binary tree)
   */
  public static void print(RBT root) {
    if (root != null) {
      print(root.left);
      System.out.println(root.element);
      print(root.right);
    }
  }

  /**
   * Renvoie le nombre de noeuds d'un arbre binaire rouge et noir. <br> <br>
   * Return the number of nodes of a binary tree.
   */
  static int size(RBT node) {
    if (node == null)
      return 0;
    else
      return size(node.left) + 1 + size(node.right);
  }

}

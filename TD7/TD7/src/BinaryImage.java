import java.util.HashSet;
import java.util.Vector;

/**
 * Class for representing a binary image (as a 2D boolean array)
 */
class BinaryImage {
  public static final boolean WHITE = false;
  public static final boolean BLACK = true;

  /** 2D array representing the image pixels */
  final boolean[][] pixels;
  public int height;
  public int width;

  /**
   * Create an image of n rows and m columns
   * 
   * @param n
   * @param m
   */
  BinaryImage(int n, int m) {
    assert n >= 1 && m >= 1;
    this.pixels = new boolean[n][m];
    this.height = n;
    this.width = m;
  }

  /**
   * Set the pixel BLACK
   * 
   * @param i
   * @param j
   */
  void set(int i, int j) {
    if (i >= 0 && i < pixels.length && j >= 0 && j < pixels[0].length)
      this.pixels[i][j] = BLACK;
  }

  /**
   * Set the pixel WHITE
   * 
   * @param i
   * @param j
   */
  void clear(int i, int j) {
    if (i >= 0 && i < pixels.length && j >= 0 && j < pixels[0].length)
      this.pixels[i][j] = WHITE;
  }

  /**
   * Inverse the color of a pixel
   */
  public void inversePixel(int i, int j) {
    if (i >= 0 && i < pixels.length && j >= 0 && j < pixels[0].length)
      this.pixels[i][j] = !this.pixels[i][j];
  }

  @Override
  public int hashCode() {
    int s = 0;
    for(int i = 0; i < this.height; i++){
      for(int j = 0; j < this.width; j++){
          if(this.pixels[i][j]){
            int val = (this.pixels[i][j])? 1:0;
            s += (2 * (i + j) * val) + (Math.pow(13, i) * val) + (Math.pow(13, j) * val);
        }
      }
    }
    return s;
  }

  /**
   * Check equality between two images (pixel by pixel)
   */
  @Override
  public boolean equals(Object o) {
    BinaryImage that = (BinaryImage) o;
    if(that.height != this.height) return false;
    if(that.width != this.width) return false;
    
    for(int i = 0; i < this.height; i++){
      for(int j = 0; j < this.width; j++){
        if(this.pixels[i][j] != that.pixels[i][j]) return false;
      }
    }
    return true;
  }

  /**
   * Given a collection of binary images (stored in the input array), remove all
   * duplicates and return a new array storing the images (without duplicates)
   * 
   * @param a an array storing the input images
   * @return a new array storing all input images without repetitions
   */
  static BinaryImage[] deleteDuplicates(BinaryImage[] t) {
    Vector<BinaryImage> BinaryCopy = new Vector<>();

    for(int i = 0; i < t.length; i++){
      boolean keep = true;
      for(int j = i + 1; j < t.length; j++){
        if(t[i].equals(t[j])){
          keep = false;
          break;
        }
      }
      
      if(keep) BinaryCopy.add(t[i]);
    }
    BinaryImage[] res = new BinaryImage[BinaryCopy.size()];
    return BinaryCopy.toArray(res);
  }



}

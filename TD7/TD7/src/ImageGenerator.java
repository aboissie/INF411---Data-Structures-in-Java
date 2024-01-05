import java.util.*;

/**
 * Class for generating synthetic black/white images with random or regular
 * patterns
 * 
 */
class ImageGenerator {

    /**
     * Generate a collection of random binary images
     * 
     * @param seed seed for the random generator
     * @param p    density (a value in [0..1])
     * @param n    height
     * @param m    width
     * @param N    number of images to generate
     * @return
     */
    public static BinaryImage[] generateRandomImages(int seed, double p, int n, int m, int N) {
        Random rand = new Random(seed);

        BinaryImage[] res = new BinaryImage[N];

        double r;
        for (int k = 0; k < N; k++) {
            res[k] = new BinaryImage(n, m);
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < m; j++) {
                    r = rand.nextDouble();
                    if (r <= p)
                        res[k].set(i, j);
                }
            }
        }
        return res;
    }

    /**
     * Make a copy of a binary image
     */
    public static BinaryImage copy(BinaryImage im) {
        int n = im.pixels.length;
        int m = im.pixels[0].length;

        BinaryImage res = new BinaryImage(n, m);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                res.pixels[i][j] = im.pixels[i][j];
            }
        }

        return res;
    }

    /**
     * Make a copy of a binary image
     */
    public static BinaryImage[] createDuplicatesDeterministic(BinaryImage[] im) {
        int k = im.length;
        Random rand = new Random(0);

        BinaryImage[] res = new BinaryImage[2 * k];
        for (int i = 0; i < k; i++) {
            res[2 * i] = copy(im[i]);
            res[2 * i + 1] = im[i];
        }

        // swap pair of images at random
        for (int i = 0; i < k; i++) {
            int p = rand.nextInt(2 * k);
            int q = rand.nextInt(2 * k);
            if (p != q) {
                BinaryImage tmp = res[p];
                res[p] = res[q];
                res[q] = tmp;
            }
        }

        return res;
    }

}

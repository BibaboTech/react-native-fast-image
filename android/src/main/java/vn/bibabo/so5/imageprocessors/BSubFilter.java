package vn.bibabo.so5.imageprocessors;

/**
 * @author varun on 27/07/15.
 */
public interface BSubFilter {
    int[] apply(int[] inputPixels, int width, int height);
}

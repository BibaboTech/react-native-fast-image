package vn.bibabo.so5.imageprocessors;

import com.zomato.photofilters.imageprocessors.NativeImageProcessor;
import com.zomato.photofilters.imageprocessors.subfilters.ContrastSubFilter;


/**
 * @author varun
 * Class to add Contrast Subfilter
 */
public class BContrastSubFilter extends ContrastSubFilter implements BSubFilter {

    // The value is in fraction, value 1 has no effect
    protected float mContrast = 0;

    /**
     * Initialise contrast subfilter
     *
     * @param contrast The contrast value ranges in fraction, value 1 has no effect
     */
    public BContrastSubFilter(float contrast) {
        super(contrast);
        this.mContrast = contrast;
    }

    @Override
    public int[] apply(int[] inputPixels, int width, int height) {
        NativeImageProcessor.doContrast(inputPixels, mContrast, width, height);
        return inputPixels;
    }
}

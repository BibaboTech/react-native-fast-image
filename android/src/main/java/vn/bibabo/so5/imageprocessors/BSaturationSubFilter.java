package vn.bibabo.so5.imageprocessors;

import com.zomato.photofilters.imageprocessors.NativeImageProcessor;
import com.zomato.photofilters.imageprocessors.subfilters.SaturationSubfilter;


/**
 * @author varun on 28/07/15.
 */
public class BSaturationSubFilter extends SaturationSubfilter implements BSubFilter {

    protected float mLevel;

    public BSaturationSubFilter(float level) {
        super(level);
        mLevel = level;
    }

    @Override
    public int[] apply(int[] inputPixels, int width, int height) {
        NativeImageProcessor.doSaturation(inputPixels, mLevel, width, height);
        return inputPixels;
    }
}

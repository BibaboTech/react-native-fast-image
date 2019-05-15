package vn.bibabo.so5.imageprocessors;

import com.zomato.photofilters.imageprocessors.NativeImageProcessor;
import com.zomato.photofilters.imageprocessors.subfilters.BrightnessSubFilter;


/**
 * @author varun
 * subfilter used to tweak brightness of the Bitmap
 */
public class BBrightnessSubFilter extends BrightnessSubFilter implements BSubFilter {
    protected int mBrightness;
    /**
     * Takes Brightness of the image
     *
     * @param brightness Integer brightness value {value 0 has no effect}
     */
    public BBrightnessSubFilter(int brightness) {
        super(brightness);
        mBrightness = brightness;
    }

    @Override
    public int[] apply(int[] inputPixels, int width, int height) {
        NativeImageProcessor.doBrightness(inputPixels, mBrightness, width, height);
        return inputPixels;
    }
}

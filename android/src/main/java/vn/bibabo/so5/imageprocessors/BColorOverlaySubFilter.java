package vn.bibabo.so5.imageprocessors;

import android.graphics.Bitmap;

import com.zomato.photofilters.imageprocessors.NativeImageProcessor;
import com.zomato.photofilters.imageprocessors.subfilters.ColorOverlaySubFilter;


/**
 * @author varun
 * Subfilter used to overlay bitmap with the color defined
 */
public class BColorOverlaySubFilter extends ColorOverlaySubFilter implements BSubFilter {
    // the color overlay depth is between 0-255
    protected final int mColorOverlayDepth;

    // these values are between 0-1
    private final float mColorOverlayRed;
    private final float mColorOverlayGreen;
    private final float mColorOverlayBlue;

    /**
     * Initialize Color Overlay Subfilter
     *
     * @param depth Value ranging from 0-255 {Defining intensity of color overlay}
     * @param red   Red value between 0-1
     * @param green Green value between 0-1
     * @param blue  Blue value between 0-1
     */
    public BColorOverlaySubFilter(int depth, float red, float green, float blue) {
        super(depth, red, green, blue);
        this.mColorOverlayDepth = depth;
        this.mColorOverlayRed = red;
        this.mColorOverlayBlue = blue;
        this.mColorOverlayGreen = green;
    }

    @Override
    public int[] apply(int[] inputPixels, int width, int height) {
        NativeImageProcessor.doColorOverlay(inputPixels,
                mColorOverlayDepth, mColorOverlayRed, mColorOverlayGreen, mColorOverlayBlue, width, height);
        return inputPixels;
    }
}

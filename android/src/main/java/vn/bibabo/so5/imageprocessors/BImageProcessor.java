package vn.bibabo.so5.imageprocessors;

import com.zomato.photofilters.imageprocessors.NativeImageProcessor;

/**
 * @author Varun on 29/06/15.
 */
public final class BImageProcessor {
    private BImageProcessor() {
    }

    public static int[] applyCurves(int[] rgb, int[] red, int[] green, int[] blue, int[] pixels, int width, int height) {
        if (rgb != null) {
            pixels = NativeImageProcessor.applyRGBCurve(pixels, rgb, width, height);
        }

        if (!(red == null && green == null && blue == null)) {
            pixels = NativeImageProcessor.applyChannelCurves(pixels, red, green, blue, width, height);
        }
        return pixels;
    }
}

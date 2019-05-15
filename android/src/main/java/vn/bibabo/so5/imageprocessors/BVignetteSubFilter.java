package vn.bibabo.so5.imageprocessors;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.dylanvann.fastimage.R;
import com.zomato.photofilters.imageprocessors.subfilters.VignetteSubfilter;


/**
 * @author varun
 * Subfilter to add Vignette effect on an image
 */
public class BVignetteSubFilter extends VignetteSubfilter implements BSubFilter {

    protected Context mContext;

    // value of alpha is between 0-255
    protected int mAlpha = 0;

    /**
     * Initialise Vignette subfilter
     *
     * @param alpha value of alpha ranges from 0-255 (Intensity of Vignette effect)
     */
    public BVignetteSubFilter(Context context, int alpha) {
        super(context, alpha);
        this.mContext = context;
        this.mAlpha = alpha;
    }

    @Override
    public int[] apply(int[] inputPixels, int width, int height) {
        Bitmap vignette = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.vignette);

        vignette = Bitmap.createScaledBitmap(vignette, width, height, true);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setAlpha(mAlpha);

        Bitmap bmm = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bmm.setPixels(inputPixels, 0, width, 0, 0, width, height);
        Canvas comboImage = new Canvas(bmm);
        comboImage.drawBitmap(vignette, 0f, 0f, paint);
        bmm.getPixels(inputPixels, 0, width, 0, 0, width, height);
        return inputPixels;
    }
}

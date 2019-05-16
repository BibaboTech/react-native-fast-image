package com.dylanvann.fastimage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.load.model.GlideUrl;

import vn.bibabo.so5.imageprocessors.BFilter;
import vn.bibabo.so5.imageprocessors.BBrightnessSubFilter;
import vn.bibabo.so5.imageprocessors.BColorOverlaySubFilter;
import vn.bibabo.so5.imageprocessors.BContrastSubFilter;
import vn.bibabo.so5.imageprocessors.BSaturationSubFilter;
import vn.bibabo.so5.imageprocessors.BFilterPack;

import java.util.Hashtable;

import javax.annotation.Nullable;

class FastImageViewWithUrl extends ImageView {
    public GlideUrl glideUrl;
    private String mRequestedFilters;

    public FastImageViewWithUrl(Context context) {
        super(context);
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);
        this.applyFilters();
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        super.setImageDrawable(drawable);
        this.applyFilters();
    }

    @Override
    public void setImageIcon(Icon icon) {
        super.setImageIcon(icon);
    }

    @Override
    public void setImageResource(int resId) {
        super.setImageResource(resId);
    }

    public void setFilters(@Nullable  String filters) {
        mRequestedFilters = filters;
        this.applyFilters();
    }

    protected int[]  pixelBuffer = null;
    protected Bitmap originalImage = null;
    protected String loadUrl = "";

    private void applyFilters() {
        long beforeUsedMem = Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();

        if (mRequestedFilters == null || mRequestedFilters.length() == 0) {
            if (originalImage != null) {
                super.setImageDrawable(new BitmapDrawable(getResources(), originalImage));
            }
            return;
        }
        String url = this.glideUrl.toStringUrl();
        if (url == null || !url.startsWith("file://")) {
            return;
        }

        if (!url.equals(loadUrl)) {
            loadUrl = url;
            url = url.substring(7);
            originalImage = FastImageViewWithUrl.getSafeResizingBitmap(url);
            int width = originalImage.getWidth();
            int height = originalImage.getHeight();
            pixelBuffer = new int[width * height];
        }

        if (originalImage != null) {
            int width = originalImage.getWidth();
            int height = originalImage.getHeight();
            originalImage.getPixels(pixelBuffer, 0, width, 0, 0, width, height);

            this.ensureFilters(this.getContext());
            String[] filtersMap = mRequestedFilters.split(",");
            for (String flt : filtersMap) {
                BFilter filter = this.findFilter(flt);
                if (filter != null) {
                    filter.processFilter(pixelBuffer, width, height);
                }
            }

            Bitmap bmm = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            bmm.setPixels(pixelBuffer, 0, width, 0, 0, width, height);
            Drawable d = new BitmapDrawable(getResources(), bmm);
            super.setImageDrawable(d);

            long afterUsedMem = Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
            long diff = afterUsedMem - beforeUsedMem;
            //Log.e("ImageProcessor", "before: " + Math.round(beforeUsedMem / 1024) + " KB"
            //        + " after: " + Math.round(afterUsedMem / 1024) + " KB"
            //        + " used: " + Math.round(diff / 2014) + " KB");
        }
    }

    private BFilter findFilter(String filter) {
        String[] fltInfo = filter.split(":");
        BFilter filterObject = sFilters.get(fltInfo[0]);
        if (filterObject != null) {
            return filterObject;
        }

        if (fltInfo.length < 2) {
            return null;
        }

        int value = Integer.parseInt(fltInfo[1]);
        if (value >= 0) {
            if (fltInfo[0].equals("bright")) {
                filterObject = new BFilter();
                filterObject.addBSubFilter(new BBrightnessSubFilter(value));
                return filterObject;

            } else if (fltInfo[0].equals("contrast")) {
                float contrast = ((float)value) / 10;
                filterObject = new BFilter();
                filterObject.addBSubFilter(new BContrastSubFilter(contrast));
                return filterObject;

            } else if (fltInfo[0].equals("warmth")) {
                float red = (((float) value) / 100);
                float green = 0;
                float blue = 0;
                filterObject = new BFilter();
                filterObject.addBSubFilter(new BColorOverlaySubFilter(100, red, green, blue));
                return filterObject;

            } else if (fltInfo[0].equals("saturation")) {
                float saturation = ((float)value) / 10;
                filterObject = new BFilter();
                filterObject.addBSubFilter(new BSaturationSubFilter(saturation));
                return filterObject;
            }
        }

        return null;
    }

    private static Hashtable<String, BFilter> sFilters = null;
    private void ensureFilters(final Context context) {
        if (sFilters == null) {
            sFilters = new Hashtable<>();
            sFilters.put("Struck", BFilterPack.getAweStruckVibeFilter(context));
            sFilters.put("Clarendon", BFilterPack.getClarendon(context));
            sFilters.put("OldMan", BFilterPack.getOldManFilter(context));
            sFilters.put("Mars", BFilterPack.getMarsFilter(context));
            sFilters.put("Rise", BFilterPack.getRiseFilter(context));
            sFilters.put("April", BFilterPack.getAprilFilter(context));
            sFilters.put("Amazon", BFilterPack.getAmazonFilter(context));
            sFilters.put("Starlit", BFilterPack.getStarLitFilter(context));
            sFilters.put("Whisper", BFilterPack.getNightWhisperFilter(context));
            sFilters.put("Lime", BFilterPack.getLimeStutterFilter(context));
            sFilters.put("Haan", BFilterPack.getHaanFilter(context));
            sFilters.put("Bluemess", BFilterPack.getBlueMessFilter(context));
            sFilters.put("Adele", BFilterPack.getAdeleFilter(context));
            sFilters.put("Cruz", BFilterPack.getCruzFilter(context));
            sFilters.put("Metropolis", BFilterPack.getMetropolis(context));
            sFilters.put("Audrey", BFilterPack.getAudreyFilter(context));
        }
    }

    public static Bitmap getSafeResizingBitmap(String strImagePath) {

        BitmapFactory.Options options = getBitmapSize(strImagePath);

        if (options == null) {
            return null;
        }

        int widthSample = 1;
        int heightSample = 1;
        if (options.outWidth > 700) {
            widthSample = options.outWidth / 700;
        }
        if (options.outHeight > 700) {
            heightSample = options.outHeight / 700;
        }
        if (widthSample > 1 && heightSample > 1) {
            options.inSampleSize = (widthSample < heightSample)?widthSample:heightSample;
        }

        options.inJustDecodeBounds = false;
        options.inDither = false;
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        options.inPurgeable = true;

        return BitmapFactory.decodeFile(strImagePath, options);
    }

    public static BitmapFactory.Options getBitmapSize(String strImagePath) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(strImagePath, options);

        return options;
    }
}

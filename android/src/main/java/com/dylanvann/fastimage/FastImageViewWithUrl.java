package com.dylanvann.fastimage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.widget.ImageView;

import com.bumptech.glide.load.model.GlideUrl;

import com.zomato.photofilters.FilterPack;
import com.zomato.photofilters.imageprocessors.Filter;
import com.zomato.photofilters.imageprocessors.subfilters.BrightnessSubFilter;
import com.zomato.photofilters.imageprocessors.subfilters.ColorOverlaySubFilter;
import com.zomato.photofilters.imageprocessors.subfilters.ContrastSubFilter;
import com.zomato.photofilters.imageprocessors.subfilters.SaturationSubfilter;

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

    protected Bitmap originalImage = null;
    protected String loadUrl = "";

    private void applyFilters() {
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
            //originalImage = BitmapFactory.decodeFile(url, new BitmapFactory.Options());
            originalImage = FastImageViewWithUrl.getSafeResizingBitmap(url);
        }

        if (originalImage != null) {
            Bitmap target = originalImage.copy(originalImage.getConfig(), true);
            this.ensureFilters(this.getContext());
            String[] filtersMap = mRequestedFilters.split(",");
            for (String flt : filtersMap) {
                Filter filter = this.findFilter(flt);
                if (filter != null) {
                    target = filter.processFilter(target);
                }

            }
            Drawable d = new BitmapDrawable(getResources(), target);
            super.setImageDrawable(d);
        }
    }

    private Filter findFilter(String filter) {
        String[] fltInfo = filter.split(":");
        Filter filterObject = sFilters.get(fltInfo[0]);
        if (filterObject != null) {
            return filterObject;
        }

        if (fltInfo.length < 2) {
            return null;
        }

        int value = Integer.parseInt(fltInfo[1]);
        if (value >= 0) {
            if (fltInfo[0].equals("bright")) {
                filterObject = new Filter();
                filterObject.addSubFilter(new BrightnessSubFilter(value));
                return filterObject;

            } else if (fltInfo[0].equals("contrast")) {
                float contrast = ((float)value) / 10;
                filterObject = new Filter();
                filterObject.addSubFilter(new ContrastSubFilter(contrast));
                return filterObject;

            } else if (fltInfo[0].equals("warmth")) {
                float red = (((float) value) / 100);
                float green = 0;
                float blue = 0;
                filterObject = new Filter();
                filterObject.addSubFilter(new ColorOverlaySubFilter(100, red, green, blue));
                return filterObject;

            } else if (fltInfo[0].equals("saturation")) {
                float saturation = ((float)value) / 10;
                filterObject = new Filter();
                filterObject.addSubFilter(new SaturationSubfilter(saturation));
                return filterObject;
            }
        }

        return null;
    }

    private static Hashtable<String, Filter> sFilters = null;
    private void ensureFilters(final Context context) {
        if (sFilters == null) {
            sFilters = new Hashtable<>();
            sFilters.put("Struck", FilterPack.getAweStruckVibeFilter(context));
            sFilters.put("Clarendon", FilterPack.getClarendon(context));
            sFilters.put("OldMan", FilterPack.getOldManFilter(context));
            sFilters.put("Mars", FilterPack.getMarsFilter(context));
            sFilters.put("Rise", FilterPack.getRiseFilter(context));
            sFilters.put("April", FilterPack.getAprilFilter(context));
            sFilters.put("Amazon", FilterPack.getAmazonFilter(context));
            sFilters.put("Starlit", FilterPack.getStarLitFilter(context));
            sFilters.put("Whisper", FilterPack.getNightWhisperFilter(context));
            sFilters.put("Lime", FilterPack.getLimeStutterFilter(context));
            sFilters.put("Haan", FilterPack.getHaanFilter(context));
            sFilters.put("Bluemess", FilterPack.getBlueMessFilter(context));
            sFilters.put("Adele", FilterPack.getAdeleFilter(context));
            sFilters.put("Cruz", FilterPack.getCruzFilter(context));
            sFilters.put("Metropolis", FilterPack.getMetropolis(context));
            sFilters.put("Audrey", FilterPack.getAudreyFilter(context));
        }
    }

    public static Bitmap getSafeResizingBitmap(String strImagePath) {

        BitmapFactory.Options options = getBitmapSize(strImagePath);

        if (options == null) {
            return null;
        }

        int widthSample = 1;
        int heightSample = 1;
        if (options.outWidth > 2048) {
            widthSample = options.outWidth / 1024;
        }
        if (options.outHeight > 2048) {
            heightSample = options.outHeight / 1024;
        }
        if (widthSample > 1 && heightSample > 1) {
            options.inSampleSize = (widthSample < heightSample)?widthSample:heightSample;
        }

        options.inJustDecodeBounds = false;
        options.inDither = false;
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        options.inPurgeable = true;

        Bitmap photo = BitmapFactory.decodeFile(strImagePath, options);

        return photo;
    }

    public static BitmapFactory.Options getBitmapSize(String strImagePath) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(strImagePath, options);

        return options;
    }
}

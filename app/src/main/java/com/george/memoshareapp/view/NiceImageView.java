package com.george.memoshareapp.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

public class NiceImageView extends AppCompatImageView {

    private Paint paint;
    private BitmapShader shader;
    private Bitmap imageBitmap;

    public NiceImageView(Context context) {
        super(context);
        init();
    }

    public NiceImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public NiceImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (getDrawable() != null) {
            imageBitmap = getBitmapFromDrawable(getDrawable());
            if (imageBitmap != null) {
                shader = new BitmapShader(imageBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
                paint.setShader(shader);//

                float radius = Math.min(getWidth() / 2f, getHeight() / 2f);
                canvas.drawCircle(getWidth() / 2f, getHeight() / 2f, radius, paint);//canvas用来
            }
        } else {
            super.onDraw(canvas);
        }
    }

    private Bitmap getBitmapFromDrawable(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        } else if (drawable instanceof VectorDrawable || drawable instanceof VectorDrawableCompat) {
            Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
            return bitmap;
        } else {
            return null;
        }
    }
}

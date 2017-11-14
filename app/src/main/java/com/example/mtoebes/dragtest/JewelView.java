package com.example.mtoebes.dragtest;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.example.mtoebes.dragtest.R.id.image;

/**
 * TODO: document your custom view class.
 */
public class JewelView extends AppCompatTextView implements View.OnTouchListener  {
    private static final double BOX_SIZE_PCT = .20;
    private float dX, dY;
    private int pWidth, pHeight;

    public JewelView(Context context) {
        super(context);
        init();
    }

    public JewelView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setOnTouchListener(this);
        init();
    }

    public JewelView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        /*
         * Set the id so it is easy to find later (don't do this way if more than one copy of JewelView).
         * Id can either be a random unique int or defined in res/values/strings
        */
        this.setId(R.id.jewel_view);

        /*
         * Can also define the color in res/values/colors and call getResources().getColor(R.color.<name>)
         */
        this.setBackgroundColor(Color.argb(0x20,0,0,0));

        /*
         * Set up the touchListener so that events are handled
         */
        this.setOnTouchListener(this);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        pWidth = ((View) this.getParent()).getWidth();
        pHeight = ((View) this.getParent()).getHeight();

        int vWidth = (int) (pWidth * BOX_SIZE_PCT);
        int vHeight = (int) (pHeight * BOX_SIZE_PCT);
        if (this.getWidth() != vWidth || this.getHeight() != vHeight) {
            this.setLayoutParams(new FrameLayout.LayoutParams(vWidth, vHeight));
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // dX/dY are the offset from where the view's top left corner is to where it was touched
                dX = view.getX() - event.getRawX();
                dY = view.getY() - event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                // Clip the x/y position to say within the patent's boundaries
                final float newX = clipWidth(event.getRawX() + dX);
                final float newY = clipHeight(event.getRawY() + dY);

                // Set the text for debugging purposes
                this.setText(String.format(Locale.CANADA, "%.1f %.1f\n%d %d\n%d %d",
                        newX, newY, pWidth, pHeight, clipWidth(event.getRawX() + dX), clipHeight(event.getRawY() + dY)));
                Log.e("", String.format(Locale.CANADA, "%.1f %.1f",newX, newY));
                // animate the view to move to the new position
                view.animate()
                        .x(newX)
                        .y(newY)
                        .setDuration(0)
                        .start();

                break;
            default:
                return false; // Return false if we did not handle the event
        }
        return true; // Return true if we handled the event
    }

    private int clipWidth(float x) {
        return ensureRange((int) x, 0, pWidth -  this.getWidth());
    }

    private int clipHeight(float y) {
        return ensureRange((int) y, 0, pHeight -  this.getHeight());
    }

    private int ensureRange(int value, int min, int max) {
        return Math.min(Math.max(value, min), max);
    }

    public int readColor(Bitmap bitmap) {
        float red = 0;
        float green = 0;
        float blue = 0;
        int count = 0;
        for (int x = (int) getX(); x < (int) getX() + getWidth(); x++) {
            for (int y = (int) getY(); y < (int) getY() + getHeight(); y++) {
                int color = bitmap.getPixel(x,y);
                red += Color.red(color);
                green += Color.green(color);
                blue += Color.blue(color);
                count++;
            }
        }

        int avgRed = (int) (red/count);
        int avgGreen = (int) (green/count);
        int avgBlue = (int) (blue/count);
        int color = Color.argb(255, avgRed, avgGreen, avgBlue);
        this.setBackgroundColor(Color.argb(255,avgRed,avgGreen,avgBlue));
        return color;
    }
}

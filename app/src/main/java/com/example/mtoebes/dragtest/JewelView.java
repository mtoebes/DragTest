package com.example.mtoebes.dragtest;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * TODO: document your custom view class.
 */
public class JewelView extends AppCompatTextView implements View.OnTouchListener  {
    private static final int BOX_SIZE = 100;
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
        //TODO alternatively we could create a layout for the view and then inflate it rather than set its attributes here

        /*
         * Set the id so it is easy to find later (don't do this way if more than one copy of JewelView).
         * Id can either be a random unique int or defined in res/values/strings
        */
        this.setId(R.id.jewel_view);

        /*
         * Can also define the color in res/values/colors and call getResources().getColor(R.color.<name>)
         */
        this.setBackgroundColor(Color.argb(0x55,0,0,0));

        /*
         * If added programmatically, the size will default to 0x0
         */
        this.setLayoutParams(new FrameLayout.LayoutParams(BOX_SIZE, BOX_SIZE));

        /*
         * Set up the touchListener so that events are handled
         */
        this.setOnTouchListener(this);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        /*
         * Once the view has been added to the layout, we can get the dimensions of its parent
         */
        pWidth = ((View) this.getParent()).getWidth();
        pHeight = ((View) this.getParent()).getHeight();
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
                this.setText(String.format(Locale.CANADA, "%.1f %.1f",newX, newY));

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
}

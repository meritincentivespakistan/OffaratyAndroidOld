package com.riyadhbank.Custom;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class RBRegularTextView extends TextView {

    public RBRegularTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        init();
    }

    public RBRegularTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RBRegularTextView(Context context) {
        super(context);
        init();
    }

    private void init() {

        if (isInEditMode()) {
            // do something else, as getResources() is not valid.
        } else {
            // you can use getResources()
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(),
                    "RB-Regular.otf");
            setTypeface(tf);
        }

    }

}

package com.riyadhbank.Custom;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

public class RBLightEditText extends EditText {

	public RBLightEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		init();
	}

	public RBLightEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}


	public RBLightEditText(Context context) {
		super(context);
		init();
	}

	private void init() {

		if (isInEditMode()) {
			// do something else, as getResources() is not valid.
		} else {
			// you can use getResources()
			Typeface tf = Typeface.createFromAsset(getContext().getAssets(),
					"RB-Light.otf");
			setTypeface(tf);
		}

	}

}
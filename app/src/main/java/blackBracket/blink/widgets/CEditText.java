package blackBracket.blink.widgets;


import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.EditText;

import blackBracket.blink.R;


/**
 * Created by anish on 03-04-2017.
 */

public class CEditText extends EditText {

    public CEditText(Context context) {
        super(context);
        setFont(context, null);
    }

    public CEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFont(context, attrs);
    }

    public CEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setFont(context, attrs);
    }

    private void setFont(Context context, AttributeSet attrs) {

        String font = "";
        TypedArray typedArray = null;
        try {
            typedArray = context.obtainStyledAttributes(attrs, R.styleable.CEditText);
            font = typedArray.getString(R.styleable.CEditText_font);
        } catch (Exception e) {
            //  e.printStackTrace();
        } finally {
            if (typedArray != null) {
                typedArray.recycle();
            }
        }
        setTypeface(CVController.applyFont(context, font, isInEditMode()));
    }

}

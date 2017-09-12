package blackBracket.blink.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

import blackBracket.blink.R;


/**
 * Created by anish on 03-04-2017.
 */


public class CAnimTextView extends TextView {
    public CAnimTextView(Context context) {
        super(context);
        setFont(context, null);
    }

    public CAnimTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setFont(context, attrs);
    }

    public CAnimTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setFont(context, attrs);
    }

    private void setFont(Context context, AttributeSet attrs) {
        String font = "";
        TypedArray typedArray = null;
        try {
            typedArray = context.obtainStyledAttributes(attrs, R.styleable.CTextView);
            font = typedArray.getString(R.styleable.CTextView_font);
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



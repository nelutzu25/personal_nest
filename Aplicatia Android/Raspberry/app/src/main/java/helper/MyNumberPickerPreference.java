package helper;

/**
 * Created by Beleiu on 01.06.2015.
 */
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.beleiu.raspberry.R;



public class MyNumberPickerPreference extends DialogPreference {
    private static final int     MIN_VALUE           = 0;
    private static final int     MAX_VALUE           = 100;
    private static final boolean WRAP_SELECTOR_WHEEL = false;

    private int                  mSelectedValue;
    private final int            mMinValue;
    private final int            mMaxValue;
    private final boolean        mWrapSelectorWheel;
    private NumberPicker         mNumberPicker;

    public MyNumberPickerPreference(final Context context, final AttributeSet attrs) {
        super(context, attrs);

        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MyNumberPickerPreference);

        mMinValue = a.getInt(R.styleable.MyNumberPickerPreference_minValue, MyNumberPickerPreference.MIN_VALUE);
        mMaxValue = a.getInt(R.styleable.MyNumberPickerPreference_maxValue, MyNumberPickerPreference.MAX_VALUE);
        mWrapSelectorWheel = a.getBoolean(R.styleable.MyNumberPickerPreference_setWrapSelectorWheel, MyNumberPickerPreference.WRAP_SELECTOR_WHEEL);

        a.recycle();
    }

    @Override
    protected void onSetInitialValue(final boolean restoreValue, final Object defaultValue) {
        mSelectedValue = restoreValue ? this.getPersistedInt(0) : (Integer) defaultValue;
        this.updateSummary();
    }

    @Override
    protected Object onGetDefaultValue(final TypedArray a, final int index) {
        return a.getInteger(index, 0);
    }

    @Override
    protected void onPrepareDialogBuilder(final Builder builder) {
        super.onPrepareDialogBuilder(builder);

        mNumberPicker = new NumberPicker(this.getContext());
        mNumberPicker.setMinValue(mMinValue);
        mNumberPicker.setMaxValue(mMaxValue);
        mNumberPicker.setValue(mSelectedValue);
        mNumberPicker.setWrapSelectorWheel(mWrapSelectorWheel);
        mNumberPicker.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

        final LinearLayout linearLayout = new LinearLayout(this.getContext());
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        linearLayout.setGravity(Gravity.CENTER);
        linearLayout.addView(mNumberPicker);

        builder.setView(linearLayout);
    }

    @Override
    protected void onDialogClosed(final boolean positiveResult) {
        if (positiveResult && this.shouldPersist()) {
            mSelectedValue = mNumberPicker.getValue();
            this.persistInt(mSelectedValue);
            this.updateSummary();
        }
    }

    private void updateSummary() {
        this.setSummary(Integer.toString(mSelectedValue));
    }
}

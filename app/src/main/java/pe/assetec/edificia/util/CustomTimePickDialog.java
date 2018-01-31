package pe.assetec.edificia.util;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.graphics.drawable.ColorDrawable;
import android.icu.text.NumberFormat;
import android.os.Build;
import android.os.Bundle;
import android.util.AttributeSet;
import android.widget.NumberPicker;
import android.widget.TimePicker;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import pe.assetec.edificia.R;

/**
 * Created by frank on 18/01/18.
 */

public class CustomTimePickDialog extends TimePickerDialog {


    final OnTimeSetListener mCallback;
    TimePicker mTimePicker;
    final int increment;
    Context context;

    public CustomTimePickDialog(Context mcontext, int themeResId, OnTimeSetListener callBack, int hourOfDay, int minute,int increment, boolean is24HourView) {
        super(mcontext, themeResId, callBack, hourOfDay, minute/increment, is24HourView);

        this.mCallback = callBack;
        this.increment = increment;
        this.context = mcontext;
        fixSpinner(context, hourOfDay, minute, is24HourView);

    }

    private void fixSpinner(Context context, int hourOfDay, int minute, boolean is24HourView) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { // android:timePickerMode spinner and clock began in Lollipop
            try {
                // Get the theme's android:timePickerMode
                final int MODE_SPINNER = 2;
                Class<?> styleableClass = Class.forName("com.android.internal.R$styleable");
                Field timePickerStyleableField = styleableClass.getField("TimePicker");
                int[] timePickerStyleable = (int[]) timePickerStyleableField.get(null);
                final TypedArray a = context.obtainStyledAttributes(null, timePickerStyleable, android.R.attr.timePickerStyle, 0);
                Field timePickerModeStyleableField = styleableClass.getField("TimePicker_timePickerMode");
                int timePickerModeStyleable = timePickerModeStyleableField.getInt(null);
                final int mode = a.getInt(timePickerModeStyleable, MODE_SPINNER);
                a.recycle();
                if (mode == MODE_SPINNER) {
                    TimePicker timePicker = (TimePicker) findField(TimePickerDialog.class, TimePicker.class, "mTimePicker").get(this);
                    Class<?> delegateClass = Class.forName("android.widget.TimePicker$TimePickerDelegate");
                    Field delegateField = findField(TimePicker.class, delegateClass, "mDelegate");
                    Object delegate = delegateField.get(timePicker);
                    Class<?> spinnerDelegateClass;
                    if (Build.VERSION.SDK_INT != Build.VERSION_CODES.LOLLIPOP) {
                        spinnerDelegateClass = Class.forName("android.widget.TimePickerSpinnerDelegate");
                    } else {
                        // TimePickerSpinnerDelegate was initially misnamed TimePickerClockDelegate in API 21!
                        spinnerDelegateClass = Class.forName("android.widget.TimePickerClockDelegate");
                    }
                    // In 7.0 Nougat for some reason the timePickerMode is ignored and the delegate is TimePickerClockDelegate
                    if (delegate.getClass() != spinnerDelegateClass) {
                        delegateField.set(timePicker, null); // throw out the TimePickerClockDelegate!
                        timePicker.removeAllViews(); // remove the TimePickerClockDelegate views
                        Constructor spinnerDelegateConstructor = spinnerDelegateClass.getConstructor(TimePicker.class, Context.class, AttributeSet.class, int.class, int.class);
                        spinnerDelegateConstructor.setAccessible(true);
                        // Instantiate a TimePickerSpinnerDelegate
                        delegate = spinnerDelegateConstructor.newInstance(timePicker, context, null, android.R.attr.timePickerStyle, 0);
                        delegateField.set(timePicker, delegate); // set the TimePicker.mDelegate to the spinner delegate
                        // Set up the TimePicker again, with the TimePickerSpinnerDelegate
                        timePicker.setIs24HourView(is24HourView);
                        timePicker.setCurrentHour(hourOfDay);
                        timePicker.setCurrentMinute(minute);
                        timePicker.setOnTimeChangedListener(this);
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static Field findField(Class objectClass, Class fieldClass, String expectedName) {
        try {
            Field field = objectClass.getDeclaredField(expectedName);
            field.setAccessible(true);
            return field;
        } catch (NoSuchFieldException e) {} // ignore
        // search for it if it wasn't found under the expected ivar name
        for (Field searchField : objectClass.getDeclaredFields()) {
            if (searchField.getType() == fieldClass) {
                searchField.setAccessible(true);
                return searchField;
            }
        }
        return null;
    }


    @Override
    public void updateTime(int hourOfDay, int minuteOfHour) {
        mTimePicker.setCurrentHour(hourOfDay);
        mTimePicker.setCurrentMinute(minuteOfHour / increment);
    }


    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case BUTTON_POSITIVE:
                if (mCallback != null) {
                    mCallback.onTimeSet(mTimePicker, mTimePicker.getCurrentHour(),
                            mTimePicker.getCurrentMinute() * increment);
                }
                break;
            case BUTTON_NEGATIVE:
                cancel();
                break;
        }
    }

    @Override
    protected void onStop()
    {
        // override and do nothing
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        try
        {
            Class<?> rClass = Class.forName("com.android.internal.R$id");
            Field timePicker = rClass.getField("timePicker");
            this.mTimePicker = (TimePicker)findViewById(timePicker.getInt(null));

            Field m = rClass.getField("minute");
            Field h = rClass.getField("hour");

            NumberPicker mMinuteSpinner = (NumberPicker) mTimePicker.findViewById(m.getInt(null));
            NumberPicker mHourSpinner = (NumberPicker) mTimePicker.findViewById(h.getInt(null));

            Field hdividerField = mHourSpinner.getClass().getDeclaredField("mSelectionDivider");
            hdividerField.setAccessible(true);
            ColorDrawable hcolorDrawable = new ColorDrawable(context.getResources().getColor(R.color.colorAccent));
            hdividerField.set(mHourSpinner,hcolorDrawable);


            Field dividerField = mMinuteSpinner.getClass().getDeclaredField("mSelectionDivider");
            dividerField.setAccessible(true);
            ColorDrawable colorDrawable = new ColorDrawable(context.getResources().getColor(R.color.colorAccent));
            dividerField.set(mMinuteSpinner,colorDrawable);

            mMinuteSpinner.setMinValue(0);
            mMinuteSpinner.setMaxValue((60 / increment) - 1);
            List<String> displayedValues = new ArrayList<>();
            for(int i=0;i<60;i+=increment)
            {
                displayedValues.add(String.format("%02d", i));
            }

            mMinuteSpinner.setDisplayedValues(displayedValues.toArray(new String[displayedValues.size()]));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }



}

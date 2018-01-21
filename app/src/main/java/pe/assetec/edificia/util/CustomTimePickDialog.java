package pe.assetec.edificia.util;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.icu.text.NumberFormat;
import android.os.Bundle;
import android.widget.NumberPicker;
import android.widget.TimePicker;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by frank on 18/01/18.
 */

public class CustomTimePickDialog extends TimePickerDialog {


    final OnTimeSetListener mCallback;
    TimePicker mTimePicker;
    final int increment;

    public CustomTimePickDialog(Context context, int themeResId, OnTimeSetListener callBack, int hourOfDay, int minute,int increment, boolean is24HourView) {
        super(context, themeResId, callBack, hourOfDay, minute/increment, is24HourView);

        this.mCallback = callBack;
        this.increment = increment;


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

            NumberPicker mMinuteSpinner = (NumberPicker) mTimePicker.findViewById(m.getInt(null));
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

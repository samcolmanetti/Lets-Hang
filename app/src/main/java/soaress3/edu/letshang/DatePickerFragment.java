package soaress3.edu.letshang;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Calendar;

import butterknife.Bind;

public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    public String context;

    public void setContext(String context) {
        this.context = context;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current dateTextView as the default dateTextView in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        month = month + 1;
        if(context.equals("signup")) {
            ((SignupActivity) getActivity())._birthday.setText(((Integer) month).toString() + "/" + ((Integer) day).toString() + "/" + ((Integer) year).toString());
        }
        else if(context.equals("profile")) {
            android.support.v4.app.Fragment frag = ((MainActivity) getActivity()).getsFm().findFragmentByTag("Profile");
            ((TextView) frag.getView().findViewById(R.id.birthday)).setText(((Integer) month).toString() + "/" + ((Integer) day).toString() + "/" + ((Integer) year).toString());
        }
        else if(context.equals("event")) {
            android.support.v4.app.Fragment frag = ((MainActivity) getActivity()).getsFm().findFragmentByTag("Event");
            ((TextView) frag.getView().findViewById(R.id.event_date)).setText(((Integer) month).toString() + "/" + ((Integer) day).toString() + "/" + ((Integer) year).toString());
        }
    }
}
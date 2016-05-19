package soaress3.edu.letshang;

import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import soaress3.edu.letshang.model.Event;

public class CreateEventFragment extends Fragment implements View.OnClickListener{

    private static final String TAG = "CreateEventFragment";
    private ProgressDialog progressDialog;
    private Firebase fbRef;
    EditText eventNameEditText;
    TextView dateTextView;
    EditText addressEditText;
    RadioGroup eventPrivacyRadioGroup;
    EditText eventDescriptionEditText;
    Button createEventButton;
    Button pickDateButton;
    String uid;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_create_event, container, false);

        eventNameEditText = (EditText) view.findViewById(R.id.input_name_event);
        dateTextView = (TextView) view.findViewById(R.id.event_date);
        addressEditText = (EditText) view.findViewById(R.id.input_address_event);
        eventPrivacyRadioGroup = (RadioGroup) view.findViewById(R.id.rg_event_status);
        eventDescriptionEditText = (EditText) view.findViewById(R.id.input_description_event);
        createEventButton = (Button) view.findViewById(R.id.btn_create_event);
        pickDateButton = (Button) view.findViewById(R.id.pick_date);

        fbRef = new Firebase(Constants.FIREBASE_URL);
        uid = fbRef.getAuth().getUid();
        fbRef = new Firebase(Constants.FIREBASE_URL_EVENTS);

        pickDateButton.setOnClickListener(this);
        createEventButton.setOnClickListener(this);

        return view;
    }

    public void createEvent() {
        String nameText = eventNameEditText.getText().toString();
        String dateText = dateTextView.getText().toString();
        String descriptionText = eventDescriptionEditText.getText().toString();

        boolean isPublic = eventPrivacyRadioGroup.getCheckedRadioButtonId() == R.id.rb_public_event;

        if(!validate()) return;

        Event newEvent = new Event(nameText, dateText, null, null, isPublic, uid, descriptionText);
        fbRef.push().setValue(newEvent, new Firebase.CompletionListener() {
            @Override
            public void onComplete(FirebaseError error, Firebase ref) {
                if (error != null) {
                    Toast.makeText(getActivity(), "Error creating event", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Event created", Toast.LENGTH_SHORT).show();
                    clearForm();
                }
            }
        });
    }

    private void clearForm () {
        eventNameEditText.setText("");
        eventNameEditText.setError(null);

        dateTextView.setText("MM/DD/YYYY");
        addressEditText.setText("");
        addressEditText.setError(null);

        eventDescriptionEditText.setText("");
        eventDescriptionEditText.setError(null);

        eventPrivacyRadioGroup.clearCheck();
        eventPrivacyRadioGroup.check(R.id.rb_public_event);
    }

    public boolean validate() {
        boolean valid = true;

        String nameText = eventNameEditText.getText().toString();
        String dateText = dateTextView.getText().toString();
        String descriptionText = eventDescriptionEditText.getText().toString();

        if (nameText.isEmpty() || nameText.length() < 3) {
            eventNameEditText.setError("At least 3 characters");
            valid = false;
        } else {
            eventNameEditText.setError(null);
        }

        if (dateText.equals("MM/DD/YYYY")) {
            dateTextView.setError("Enter the date of the event");
            valid = false;
        } else {
            dateTextView.setError(null);
        }

        if (descriptionText.isEmpty() || descriptionText.length() < 3) {
            eventDescriptionEditText.setError("At least 3 characters");
            valid = false;
        } else {
            eventDescriptionEditText.setError(null);
        }

        return valid;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.pick_date){
            DialogFragment newFragment = new DatePickerFragment();
            ((DatePickerFragment) newFragment).setContext("event");
            newFragment.show(getActivity().getFragmentManager(), "datePicker");
        } else if (v.getId() == R.id.btn_create_event) {
            createEvent();
        }
    }
}

package soaress3.edu.letshang;

import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
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
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;

import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.model.LatLng;

import soaress3.edu.letshang.model.Event;

public class CreateEventFragment extends Fragment implements View.OnClickListener , PlaceSelectionListener{

    private static final String TAG = "CreateEventFragment";
    private PlaceAutocompleteFragment autocompleteFragment;
    private Firebase fbRef;
    private EditText eventNameEditText;
    private TextView dateTextView;
    private Place eventAddress;
    private RadioGroup eventPrivacyRadioGroup;
    private EditText eventDescriptionEditText;
    private Button createEventButton;
    private Button pickDateButton;
    private String uId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_create_event, container, false);

        eventNameEditText = (EditText) view.findViewById(R.id.input_name_event);
        dateTextView = (TextView) view.findViewById(R.id.event_date);
        eventPrivacyRadioGroup = (RadioGroup) view.findViewById(R.id.rg_event_status);
        eventDescriptionEditText = (EditText) view.findViewById(R.id.input_description_event);
        createEventButton = (Button) view.findViewById(R.id.btn_create_event);
        pickDateButton = (Button) view.findViewById(R.id.pick_date);

        fbRef = new Firebase(Constants.FIREBASE_URL);
        uId = fbRef.getAuth().getUid();
        fbRef = new Firebase(Constants.FIREBASE_URL_EVENTS);

        pickDateButton.setOnClickListener(this);
        createEventButton.setOnClickListener(this);

        autocompleteFragment = (PlaceAutocompleteFragment) getActivity().getFragmentManager()
                .findFragmentById(R.id.autocomplete_fragment);
        autocompleteFragment.setHint("Event Address");
        autocompleteFragment.setOnPlaceSelectedListener(this);

        return view;
    }

    public void createEvent() {
        String nameText = eventNameEditText.getText().toString();
        String dateText = dateTextView.getText().toString();
        String descriptionText = eventDescriptionEditText.getText().toString();

        boolean isPublic = eventPrivacyRadioGroup.getCheckedRadioButtonId() == R.id.rb_public_event;

        if(!validate()) return;
        LatLng address = eventAddress.getLatLng();

        Event newEvent = new Event(nameText, dateText, address.latitude, address.longitude, isPublic,
                uId, descriptionText);

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
        eventAddress = null;
        autocompleteFragment.setText("");

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

        if (eventAddress == null){
            Toast.makeText(getActivity(), "Address cannot be blank", Toast.LENGTH_SHORT).show();
            return false;
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

    @Override
    public void onPlaceSelected(Place place) {
        this.eventAddress = place;
    }

    @Override
    public void onError(Status status) {
        // error with places autocomplete
    }
}

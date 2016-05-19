package soaress3.edu.letshang;

import android.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import soaress3.edu.letshang.model.Event;
import soaress3.edu.letshang.model.Profile;

public class CreateEventFragment extends Fragment {

    private static final String TAG = "CreateEventFragment";
    private Firebase fbRef;
    EditText name;
    TextView date;
    EditText address;
    RadioButton public_event;
    EditText description;
    Button create_event;
    Button pick_date;
    String nameText;
    String dateText;
    String descriptionText;
    String uid;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_create_event, container, false);

        name = (EditText) view.findViewById(R.id.input_name_event);
        date = (TextView) view.findViewById(R.id.event_date);
        address = (EditText) view.findViewById(R.id.input_address_event);
        public_event = (RadioButton) view.findViewById(R.id.public_event);
        description = (EditText) view.findViewById(R.id.input_description_event);
        create_event = (Button) view.findViewById(R.id.btn_create_event);
        pick_date = (Button) view.findViewById(R.id.pick_date);

        fbRef = new Firebase(Constants.FIREBASE_URL);
        uid = fbRef.getAuth().getUid();
        fbRef = new Firebase(Constants.FIREBASE_URL_EVENTS);

        pick_date.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DialogFragment newFragment = new DatePickerFragment();
                ((DatePickerFragment) newFragment).setContext("event");
                newFragment.show(getActivity().getFragmentManager(), "datePicker");
            }
        });

        create_event.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                createEvent(v);
            }
        });

        return view;
    }

    public void createEvent(View v) {

        nameText = name.getText().toString();
        dateText = date.getText().toString();
        descriptionText = description.getText().toString();
        final boolean itIsPublic = public_event.isChecked();

        if(!validate()) {
            Toast.makeText(getActivity().getBaseContext(), "Something was wrong.", Toast.LENGTH_LONG).show();
            return;
        }

        fbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                /* If there is no user, make one */
                if (dataSnapshot.getValue() == null) {
                    Event newEvent = new Event(nameText, dateText, null, null, itIsPublic, uid, descriptionText);
                    fbRef.push().setValue(newEvent, new Firebase.CompletionListener() {
                        @Override
                        public void onComplete(FirebaseError error, Firebase ref) {
                            if (error != null) {
                                System.out.println("Data could not be saved. " + error.getMessage());
                            } else {
                                System.out.println("Data saved successfully.");
                            }
                        }
                    });
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.d(TAG, "Error occured: " + firebaseError.getMessage());
            }
        });
    }

    public boolean validate() {
        boolean valid = true;

        if (nameText.isEmpty() || nameText.length() < 3) {
            name.setError("At least 3 characters");
            valid = false;
        } else {
            name.setError(null);
        }

        if (dateText.equals("MM/DD/YYYY")) {
            date.setError("Enter the date of the event");
            valid = false;
        } else {
            date.setError(null);
        }

        if (descriptionText.isEmpty() || descriptionText.length() < 3) {
            description.setError("At least 3 characters");
            valid = false;
        } else {
            description.setError(null);
        }

        return valid;
    }
}

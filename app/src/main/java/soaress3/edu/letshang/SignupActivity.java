package soaress3.edu.letshang;

import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ServerValue;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;

import butterknife.Bind;
import butterknife.ButterKnife;
import soaress3.edu.letshang.model.Profile;

public class SignupActivity extends AppCompatActivity {
    private static final String TAG = "SignupActivity";
    private Firebase fbRef;

    @Bind(R.id.input_name)
    EditText _nameText;
    @Bind(R.id.input_email)
    EditText _emailText;
    @Bind(R.id.input_password)
    EditText _passwordText;
    @Bind(R.id.btn_signup)
    Button _signupButton;
    @Bind(R.id.link_login)
    TextView _loginLink;
    @Bind(R.id.birthday)
    TextView _birthday;
    @Bind(R.id.input_gender)
    Spinner _gender;
    String mName;
    String mEmail;
    String mPassword;
    String mBirthday;
    String mGender;
    boolean flag = true;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);
        Firebase.setAndroidContext(this);
        fbRef = new Firebase(Constants.FIREBASE_URL);

        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                finish();
            }
        });
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    public void signup() {
        Log.d(TAG, "Signup");

        mName = _nameText.getText().toString();
        mEmail = _emailText.getText().toString();
        mPassword = _passwordText.getText().toString();
        mBirthday = _birthday.getText().toString();
        mGender = _gender.getSelectedItem().toString();

        if (!validate()) {
            onSignupFailed();
            return;
        }

        _signupButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        Toast.makeText(SignupActivity.this, "Creating user", Toast.LENGTH_SHORT).show();
        fbRef.createUser(mEmail, mPassword, new Firebase.ValueResultHandler<Map<String, Object>>() {
            @Override
            public void onSuccess(Map<String, Object> result) {
                Toast.makeText(SignupActivity.this, "successfully signed up", Toast.LENGTH_SHORT).show();
                onSignupSuccess(result);
            }

            @Override
            public void onError(FirebaseError firebaseError) {
                onSignupFailed();
                progressDialog.dismiss();
            }
        });
    }


    public void onSignupSuccess(Map<String, Object> result) {
        _signupButton.setEnabled(true);
        setResult(RESULT_OK, null);
        Toast.makeText(SignupActivity.this, "Sign up successful", Toast.LENGTH_SHORT).show();

        String uid = (String) result.get("uid");
        createUserInFirebaseHelper(uid);

        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
    }

    /**
     * Creates a new user in Firebase from the Java POJO
     */
    private void createUserInFirebaseHelper(String uid) {

        final Firebase userLocation = new Firebase(Constants.FIREBASE_URL_USERS).child(uid);
        final CountDownLatch done = new CountDownLatch(1);
        /**
         * See if there is already a user (for example, if they already logged in with an associated
         * Google account.
         */
        userLocation.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                /* If there is no user, make one */
                if (dataSnapshot.getValue() == null) {
                    Profile newUser = new Profile(mName, mEmail, mBirthday, mGender, null);
                    userLocation.setValue(newUser, new Firebase.CompletionListener() {
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


    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _signupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        if (mName.isEmpty() || mName.length() < 3) {
            _nameText.setError("At least 3 characters");
            valid = false;
        } else {
            _nameText.setError(null);
        }

        if (mEmail.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(mEmail).matches()) {
            _emailText.setError("Enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (mPassword.isEmpty() || mPassword.length() < 4 || mPassword.length() > 10) {
            _passwordText.setError("Between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        if (mBirthday.equals("MM/DD/YYYY")) {
            _birthday.setError("Enter your birthday");
            valid = false;
        } else {
            _birthday.setError(null);
        }

        return valid;
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Signup Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://soaress3.edu.letshang/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Signup Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://soaress3.edu.letshang/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}
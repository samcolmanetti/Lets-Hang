package soaress3.edu.letshang;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

import soaress3.edu.letshang.model.Profile;

public class ChangeProfileFragment extends Fragment implements View.OnClickListener {

    private Firebase fbRef;
    private EditText _name;
    private TextView _birthday;
    private Spinner _gender;
    private Button _submit;
    private String previousName;
    private String previousBirthday;
    private String previousGender;
    private String nameText;
    private String birthdayText;
    private String genderText;
    private Button uploadImageButton;
    private Button takePictureButton;
    private ImageView imageView;

    static final int PICK_IMAGE_REQUEST = 1;
    static final int REQUEST_IMAGE_CAPTURE = 2;

    View view;
    Bitmap mBitmap;
    Bitmap previousBitmap = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_change_profile, container, false);

        _name = (EditText) view.findViewById(R.id.name);
        _birthday = (TextView) view.findViewById(R.id.birthday);
        _gender = (Spinner) view.findViewById(R.id.gender);
        _submit = (Button) view.findViewById(R.id.btn_submit);
        imageView = (ImageView) view.findViewById(R.id.imageView);
        _submit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                changeProfile(v);
            }
        });

        fbRef = new Firebase(Constants.FIREBASE_URL);
        String uid = fbRef.getAuth().getUid();
        fbRef = new Firebase(Constants.FIREBASE_URL_USERS).child(uid);

        fbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Profile profile = dataSnapshot.getValue(Profile.class);

                if (profile != null) {
                    // If there was data, take the TextViews and set the appropriate values.
                    _name.setText(profile.getName());
                    previousName = profile.getName();
                    _birthday.setText(profile.getBirthday());
                    previousBirthday = profile.getBirthday();
                    _gender.setSelection(getSpinnerIndex(_gender, profile.getGender()));
                    previousGender = profile.getGender();
                    if (profile.getPicture() !=  null) {
                        byte[] imageAsBytes = Base64.decode(profile.getPicture(), Base64.DEFAULT);
                        imageView.setImageBitmap(BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length));
                        previousBitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        uploadImageButton = (Button) view.findViewById(R.id.btn_upload);
        uploadImageButton.setOnClickListener(this);

        takePictureButton = (Button) view.findViewById(R.id.btn_camera);
        takePictureButton.setOnClickListener(this);

        return view;
    }

    private int getSpinnerIndex(Spinner spinner, String myString) {
        int index = 0;

        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)){
                index = i;
                break;
            }
        }
        return index;
    }

    public void changeProfile(View v) {
        nameText = _name.getText().toString();
        birthdayText = _birthday.getText().toString();
        genderText = _gender.getSelectedItem().toString();

        if (!nameText.equals(previousName) || !birthdayText.equals(previousBirthday) || !genderText.equals(previousGender)) {
            if(!validate()) {
                Toast.makeText(getActivity().getBaseContext(), "Change failed: eventNameEditText is not valid", Toast.LENGTH_LONG).show();
                return;
            }

            HashMap<String, Object> updatedProperties = new HashMap<String, Object>();
            updatedProperties.put("eventNameEditText", nameText);
            updatedProperties.put("birthday", birthdayText);
            updatedProperties.put("gender", genderText);

            if(mBitmap != null && !mBitmap.equals(previousBitmap)) {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                mBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                mBitmap.recycle();
                byte[] byteArray = stream.toByteArray();
                String imageFile = Base64.encodeToString(byteArray, Base64.DEFAULT);
                updatedProperties.put("picture", imageFile);
            }

            fbRef.updateChildren(updatedProperties);
            Toast.makeText(getActivity().getBaseContext(), "Profile changed!", Toast.LENGTH_LONG).show();
        }
    }

    public boolean validate() {
        boolean valid = true;

        if (nameText.isEmpty() || nameText.length() < 3) {
            _name.setError("At least 3 characters");
            valid = false;
        } else {
            _name.setError(null);
        }

        return valid;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == getActivity().RESULT_OK &&
                data != null && data.getData() != null) {

            Uri uri = data.getData();

            RetrieveImageTask rit = new RetrieveImageTask(this);
            rit.execute(uri);
        }

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == getActivity().RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            setBitmap(imageBitmap);
        }
    }

    public void setBitmap(Bitmap bitmap) {
        imageView.setImageBitmap(bitmap);
        mBitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id){
            case R.id.btn_upload:
                Intent intent = new Intent();
                // Show only images, no videos or anything else
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                // Always show the chooser (if there are multiple options available)
                startActivityForResult(Intent.createChooser(intent, "Select Picture"),
                        PICK_IMAGE_REQUEST);
                break;
            case R.id.btn_camera:
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
                break;
        }
    }
}

package soaress3.edu.letshang;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.IOException;
import java.lang.ref.WeakReference;

/**
 * Created by luizr on 18/05/2016.
 */
public class RetrieveImageTask extends AsyncTask<Uri, Void, Bitmap> {

    private WeakReference<ChangeProfileFragment> changeProfileFragmentWeakReference;

    public RetrieveImageTask(ChangeProfileFragment cpf) {
        changeProfileFragmentWeakReference = new WeakReference<ChangeProfileFragment>(cpf);
    }

    @Override
    protected Bitmap doInBackground(Uri[] params) {
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(changeProfileFragmentWeakReference.get()
                    .getActivity().getContentResolver(), params[0]);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        ChangeProfileFragment changeProfileFragment = changeProfileFragmentWeakReference.get();
        if(changeProfileFragment == null) {
            return;
        }
        changeProfileFragment.setBitmap(bitmap);
        changeProfileFragment = null;
    }
}

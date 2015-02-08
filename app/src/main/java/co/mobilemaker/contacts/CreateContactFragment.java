package co.mobilemaker.contacts;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import java.io.File;
import java.sql.SQLException;

/**
 * A placeholder fragment containing a simple view.
 */
public class CreateContactFragment extends Fragment {

    private final static String LOG_TAG = CreateContactFragment.class.getSimpleName();
    private final static Integer REQUEST_CODE = 1;
    final static String NAME = "NAME";
    final static String NICKNAME = "NICKNAME";
    final static String IMAGE = "IMAGE";
    EditText mEditTextFirstName;
    EditText mEditTextLastName;
    EditText mEditTextNickname;
    ImageButton mImageButton;
    Uri mImageUri;
    DataBaseHelper mDBHelper = null;
    public String mImageFileName = "image.jpg";



    public CreateContactFragment() {
    }

    public DataBaseHelper getDBHelper(){
        if(mDBHelper == null){
            mDBHelper = OpenHelperManager.getHelper(getActivity(), DataBaseHelper.class);
        }
        return mDBHelper;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_create_contact, container, false);
        wireUp(rootView);
        prepareDoneButton(rootView);
        return rootView;
    }

    private void prepareDoneButton(View rootView) {
        Button button = (Button)rootView.findViewById(R.id.button_done);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity activity = getActivity();
                String name = mEditTextFirstName.getText().toString() + " " +
                        mEditTextLastName.getText().toString();
                saveContact(name);
                createIntent(activity, name);
                activity.finish();
            }
        });
    }

    private void saveContact(String name) {
        Contact contact = new Contact();
        contact.setName(name);
        contact.setNickname(mEditTextNickname.getText().toString());
        contact.setImage(mImageUri.toString());
        try {
            Dao<Contact,Integer> dao = getDBHelper().getDocumentDao();
            dao.create(contact);
        }   catch(SQLException e){
            Log.e(LOG_TAG, "Failed to create DAO", e);

        }
    }

    private void createIntent(Activity activity, String name) {
        Intent intent = new Intent();
        intent.putExtra(NAME,name);
        intent.putExtra(NICKNAME,mEditTextNickname.getText().toString());
        intent.putExtra(IMAGE,mImageUri.toString());
        activity.setResult(Activity.RESULT_OK, intent);
    }

    private void wireUp(View rootView) {
        mEditTextFirstName = (EditText)rootView.findViewById(R.id.edit_text_first_name);
        mEditTextLastName = (EditText)rootView.findViewById(R.id.edit_text_last_name);
        mEditTextNickname = (EditText)rootView.findViewById(R.id.edit_text_nickname);
        mImageButton = (ImageButton)rootView.findViewById(R.id.image_button_place_holder);
        prepareImageButton();
    }

    private void prepareImageButton() {
        mImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT,getImageFileUri(mImageFileName));
                startActivityForResult(intent, REQUEST_CODE);
            }
        });
    }

    public Uri getImageFileUri(String fileName){
        File mediaStorageDir = new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), LOG_TAG);
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            Log.d(LOG_TAG, "failed to create directory");
        }
        return Uri.fromFile(new File(mediaStorageDir.getPath() + File.separator + fileName));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == getActivity().RESULT_OK && requestCode == REQUEST_CODE) {
            mImageUri = getImageFileUri(mImageFileName);
            Bitmap bitmap = BitmapFactory.decodeFile(mImageUri.getPath());
            mImageButton.setImageBitmap(bitmap);
        }
    }

    @Override
    public void onDestroy() {
        if(mDBHelper != null){
            OpenHelperManager.releaseHelper();
            mDBHelper = null;
        }
        super.onDestroy();
    }
}

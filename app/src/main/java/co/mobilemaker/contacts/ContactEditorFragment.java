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

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;

import java.io.File;
import java.sql.SQLException;

/**
 * A placeholder fragment containing a simple view.
 */
public class ContactEditorFragment extends Fragment {

    private final static String LOG_TAG = ContactEditorFragment.class.getSimpleName();
    private final static Integer REQUEST_CODE = 1;
    private final static String IMAGE_FILENAME = "image.jpg";
    final static Integer RESULT_DELETE = 3;
    final static String NAME = "NAME";
    final static String NICKNAME = "NICKNAME";
    final static String IMAGE = "IMAGE";
    EditText mEditTextFirstName;
    EditText mEditTextLastName;
    EditText mEditTextNickname;
    ImageButton mImageButton;
    Button mButtonDone;
    Button mButtonDelete;
    Uri mImageUri;
    DataBaseHelper mDBHelper = null;




    public ContactEditorFragment() {
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
        View rootView = inflater.inflate(R.layout.fragment_contact_editor, container, false);
        wireUp(rootView);
        int action = (int)getActivity().getIntent().getExtras().get(ContactListFragment.ACTION);
        if(action == ContactListFragment.REQUEST_CODE_CREATE)
            mButtonDelete.setVisibility(View.GONE);
        else if(action == ContactListFragment.REQUEST_CODE_EDIT)
            mButtonDone.setText("UPDATE");
        prepareDoneButton(rootView);
        prepareDeleteButton(rootView);
        return rootView;
    }

    private void prepareDeleteButton(View rootView) {
        mButtonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity activity = getActivity();
                activity.setResult(RESULT_DELETE,new Intent());
                activity.finish();
            }
        });
    }

    private void prepareDoneButton(View rootView) {
        mButtonDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity activity = getActivity();
                String name = mEditTextFirstName.getText().toString() + " " +
                        mEditTextLastName.getText().toString();
                createIntent(activity, name);
                activity.finish();
            }
        });
    }


    private void createIntent(Activity activity, String name) {
        Intent intent = new Intent();
        intent.putExtra(NAME,name);
        intent.putExtra(NICKNAME,mEditTextNickname.getText().toString());
        if(mImageUri != null)
            intent.putExtra(IMAGE,mImageUri.toString());
        else
            intent.putExtra(IMAGE, "");
        activity.setResult(Activity.RESULT_OK, intent);
    }

    private void wireUp(View rootView) {
        mEditTextFirstName = (EditText)rootView.findViewById(R.id.edit_text_first_name);
        mEditTextLastName = (EditText)rootView.findViewById(R.id.edit_text_last_name);
        mEditTextNickname = (EditText)rootView.findViewById(R.id.edit_text_nickname);
        mButtonDone = (Button)rootView.findViewById(R.id.button_done);
        mButtonDelete = (Button)rootView.findViewById(R.id.button_delete);
        mImageButton = (ImageButton)rootView.findViewById(R.id.image_button_place_holder);
        prepareImageButton();
    }

    private void prepareImageButton() {
        mImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT,getImageFileUri(IMAGE_FILENAME));
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
        if (resultCode == getActivity().RESULT_OK){
            if(requestCode == REQUEST_CODE) {
                mImageUri = getImageFileUri(IMAGE_FILENAME);
                Bitmap bitmap = BitmapFactory.decodeFile(mImageUri.getPath());
                mImageButton.setImageBitmap(bitmap);
            }
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

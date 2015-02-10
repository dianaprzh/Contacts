package co.mobilemaker.contacts;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class ContactListFragment extends ListFragment {

    final static Integer REQUEST_CODE_CREATE = 0;
    final static Integer REQUEST_CODE_EDIT = 2;
    final static String ACTION = "ACTION";
    ArrayAdapter<Contact> mAdapter;
    Contact mContact = null;
    DataBaseHelper mDBHelper;
    int mPosition;


    public ContactListFragment() {
    }

    public DataBaseHelper getDBHelper(){
        if(mDBHelper == null){
            mDBHelper = OpenHelperManager.getHelper(getActivity(), DataBaseHelper.class);
        }
        return mDBHelper;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        List<Contact> contacts;
        try {
            contacts = getDBHelper().getDocumentDao().queryForAll();
        } catch (SQLException e) {
            contacts = new ArrayList<>();
            e.printStackTrace();
        }
        mAdapter = new ContactAdapter(getActivity(),mDBHelper,contacts);
        setListAdapter(mAdapter);
        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(getActivity(), ContactEditorActivity.class);
                    intent.putExtra(ACTION,REQUEST_CODE_EDIT);
                    mPosition = position;
                    startActivityForResult(intent, REQUEST_CODE_EDIT);
                    mAdapter.remove((Contact)parent.getItemAtPosition(position));
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_fragment_contact_list, menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Boolean handled = false;
        switch (item.getItemId()){
            case R.id.action_add_contact:
                startCreateContact();
                handled = true;
                break;
        }
        if(!handled){
            handled = super.onOptionsItemSelected(item);
        }
        return handled;
    }

    private void startCreateContact() {
        Intent intent = new Intent(getActivity(), ContactEditorActivity.class);
        intent.putExtra(ACTION,REQUEST_CODE_CREATE);
        startActivityForResult(intent, REQUEST_CODE_CREATE);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == getActivity().RESULT_OK){
            if(requestCode == REQUEST_CODE_CREATE){
                getContactData(data);
                mAdapter.add(mContact);
            }else if(requestCode == REQUEST_CODE_EDIT){
                getContactData(data);
                mAdapter.insert(mContact, mPosition);
            }
        }
    }

    private void getContactData(Intent data) {
        String name = (String) data.getExtras().get(ContactEditorFragment.NAME);
        String nickname = (String)data.getExtras().get(ContactEditorFragment.NICKNAME);
        String image = (String)data.getExtras().get(ContactEditorFragment.IMAGE);
        mContact = new Contact();
        mContact.setName(name);
        mContact.setNickname(nickname);
        mContact.setImage(image);
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


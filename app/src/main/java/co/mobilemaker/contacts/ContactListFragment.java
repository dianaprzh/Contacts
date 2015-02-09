package co.mobilemaker.contacts;


import android.content.Intent;
import android.graphics.Bitmap;
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

import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class ContactListFragment extends ListFragment {

    final static Integer REQUEST_CODE = 0;
    ArrayAdapter<Contact> mAdapter;
    Contact mContact = null;


    public ContactListFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        List<Contact> contacts = new ArrayList<>();
        mAdapter = new ContactAdapter(getActivity(),contacts);
        setListAdapter(mAdapter);
        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Contact contact = (Contact) parent.getItemAtPosition(position);
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
        Intent intent = new Intent(getActivity(), CreateContactActivity.class);
        startActivityForResult(intent, REQUEST_CODE);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE){
            if(resultCode == getActivity().RESULT_OK){
                String name = (String) data.getExtras().get(CreateContactFragment.NAME);
                String nickname = (String)data.getExtras().get(CreateContactFragment.NICKNAME);
                String image = (String)data.getExtras().get(CreateContactFragment.IMAGE);
                mContact = new Contact();
                mContact.setName(name);
                mContact.setNickname(nickname);
                mContact.setImage(image);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(mContact!=null) {
            mAdapter.add(mContact);
            mContact = null;
        }
    }
}


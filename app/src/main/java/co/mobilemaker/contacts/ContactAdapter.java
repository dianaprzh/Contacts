package co.mobilemaker.contacts;

import android.content.Context;
import android.media.Image;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by diany_000 on 2/7/2015.
 */
public class ContactAdapter extends ArrayAdapter<Contact> {

    Context mContext;
    List<Contact> mContact;
    DataBaseHelper mDBHelper;

    public ContactAdapter(Context context, DataBaseHelper dbHelper, List<Contact> contact){
        super(context, R.layout.list_item_contact_list, contact);
        mContext = context;
        mDBHelper = dbHelper;
        mContact = contact;
    }

    @Override
    public void add(Contact contact) {
        super.add(contact);
        try {
            Dao<Contact, Integer> contactDao = mDBHelper.getDocumentDao();
            contactDao.create(contact);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void insert(Contact contact, int index) {
        super.insert(contact, index);
        try {
            Dao<Contact, Integer> contactDao = mDBHelper.getDocumentDao();
            contactDao.create(contact);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void remove(Contact contact) {
        super.remove(contact);
        try {
            Dao<Contact, Integer> contactDao = mDBHelper.getDocumentDao();
            contactDao.delete(contact);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView;
        rowView = reuseOrGenerateRowView(convertView, parent);
        displayContentInView(position, rowView);
        return rowView;
    }

    private View reuseOrGenerateRowView(View convertView, ViewGroup parent) {
        View rowView;
        if (convertView != null) {
            rowView = convertView;
        } else {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.list_item_contact_list, parent, false);
        }
        return rowView;
    }

    private void displayContentInView(int position, View rowView) {
        if (rowView != null) {
            TextView textViewName = (TextView)rowView.findViewById(R.id.text_view_name);
            textViewName.setText(mContact.get(position).getName());
            TextView textViewNickname =(TextView)rowView.findViewById(R.id.text_view_nickname);
            textViewNickname.setText(mContact.get(position).getNickname());
            ImageView imageView = (ImageView)rowView.findViewById(R.id.image_view_contact);
            if(mContact.get(position).getImage() != null && !mContact.get(position).getImage().isEmpty())
                imageView.setImageURI(Uri.parse(mContact.get(position).getImage()));
        }
    }
}

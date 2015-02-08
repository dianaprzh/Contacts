package co.mobilemaker.contacts;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;


/**
 * Created by diany_000 on 2/7/2015.
 */
public class Contact {

    public final static String ID = "ID";
    public final static String NAME = "NAME";
    public final static String NICKNAME = "NICKNAME";
    public final static String IMAGE = "IMAGE";

    @DatabaseField(generatedId = true, columnName = ID) private int _id;
    @DatabaseField(columnName = NAME)private String mName;
    @DatabaseField(columnName = NICKNAME)private String mNickname;
    @DatabaseField(columnName = IMAGE)private String mImageUri;


    public String getName() {
        return mName;
    }

    public String getNickname() {
        return mNickname;
    }

    public String getImage() {
        return mImageUri;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public void setNickname(String mNickname) {
        this.mNickname = mNickname;
    }

    public void setImage(String mImage) {
        this.mImageUri = mImage;
    }

}

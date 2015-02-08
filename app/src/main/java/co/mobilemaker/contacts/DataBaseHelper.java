package co.mobilemaker.contacts;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

/**
 * Created by diany_000 on 2/7/2015.
 */
public class DataBaseHelper extends OrmLiteSqliteOpenHelper {

    private final static String LOG_TAG = DataBaseHelper.class.getSimpleName();
    private final static String DATABASE_NAME = "contacts.db";
    private final static int DATABASE_VERSION = 1;

    private Dao<Contact, Integer> mContactDao = null;

    public DataBaseHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try{
            Log.i(LOG_TAG, "Creating database");
            TableUtils.createTable(connectionSource, Contact.class);
        }catch (SQLException e){
            Log.e(LOG_TAG, "Error creating database", e);
            throw new RuntimeException(e);
        }
    }


    public Dao<Contact,Integer> getDocumentDao() throws SQLException {
        if(mContactDao == null){
            mContactDao = getDao(Contact.class);
        }
        return mContactDao;
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {

    }
}
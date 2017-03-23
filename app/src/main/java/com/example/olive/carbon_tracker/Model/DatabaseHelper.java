package com.example.olive.carbon_tracker.Model;
////for reading database---mData.sqlite

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
/**
 * Copy and create a database for existing sqlite file which is located at external memeory, and this
 * sqlite file contains all vehicle info which can be used when user try to add car;
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    private SQLiteDatabase myDataBase;
    private final Context myContext;
    public static String DB_PATH = "/data/data/com.example.olive.carbon_tracker/databases/";
    public static String DB_NAME = "CarInfo.sqlite";


    public DatabaseHelper(Context context){
        super(context,DB_NAME,null,1);
        this.myContext = context;
        boolean DBexist = checkDataBase();
        if (DBexist){
            //openDataBase();
        }else {
            System.out.println("DataBase does not exist");
            createDataBase();
        }

    }


    private void copyDataBase() {
        try{
            InputStream myInput = myContext.getAssets().open(DB_NAME);
            String outFileName = DB_PATH + DB_NAME;
            OutputStream myOutPut = new FileOutputStream(outFileName);
            byte[] buffer = new byte[1024];
            int length;
            while((length = myInput.read(buffer))>0){
                myOutPut.write(buffer,0,length);
            }
            myOutPut.flush();
            myOutPut.close();
            myInput.close();
        }catch (Exception e){

        }
    }
    @Override
    public synchronized void close(){
        if(myDataBase != null){
            myDataBase.close();
        }
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }



    public void openDataBase() throws SQLException{
        String myPath = DB_PATH+DB_NAME;
        myDataBase = SQLiteDatabase.openDatabase(myPath,null,SQLiteDatabase.OPEN_READWRITE);
//        return myDataBase;
    }

    private boolean checkDataBase() {
        SQLiteDatabase checkDB = null;
        try{
            String myPath = DB_PATH+DB_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath,null, SQLiteDatabase.OPEN_READONLY);
        }catch (SQLiteException e){

        }

        if (checkDB != null){
            checkDB.close();
        }
        return checkDB !=null ? true:false;
    }

    public void createDataBase() {
        try{
            boolean DBexist = checkDataBase();
            if (DBexist){

            }else{
                this.getReadableDatabase();
                copyDataBase();
            }
        }catch (Exception e){

        }
    }
}

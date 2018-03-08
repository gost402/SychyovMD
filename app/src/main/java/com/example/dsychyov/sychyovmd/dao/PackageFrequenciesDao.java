package com.example.dsychyov.sychyovmd.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.support.v4.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class PackageFrequenciesDao {
    private Connection connection;

    interface TableInfo {
        String TABLE_NAME = "package_frequencies";

        interface Columns extends BaseColumns {
            String FIELD_FREQUENCY = "frequency";
            String FIELD_PACKAGE_NAME = "package_name";
        }

        String CREATE_TABLE_SCRIPT =
                "CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
                        "(" +
                        Columns.FIELD_FREQUENCY+ " NUMBER, " +
                        Columns.FIELD_PACKAGE_NAME + " TEXT" +
                        ")";

        String DROP_TABLE_SCRIPT =
                "DROP TABLE IF EXISTS " + TABLE_NAME;

    }

    private static class Connection extends SQLiteOpenHelper {
        static final int VERSION = 1;
        static final String DB_NAME = "sample.db";

        Connection(Context context) {
            super(context, DB_NAME, null, VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(TableInfo.CREATE_TABLE_SCRIPT);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(TableInfo.DROP_TABLE_SCRIPT);
            onCreate(db);
        }

        @Override
        public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            onUpgrade(db, oldVersion, newVersion);
        }
    }


    public PackageFrequenciesDao(Context context) {
        connection = new Connection(context);
    }

    public List<Pair<String, Integer>> getFrequencies() {
        List<Pair<String, Integer>> packageFrequencies = new ArrayList<>();

        SQLiteDatabase db = connection.getWritableDatabase();
        Cursor cursor = db.query(TableInfo.TABLE_NAME,
                new String[]{ TableInfo.Columns.FIELD_PACKAGE_NAME, TableInfo.Columns.FIELD_FREQUENCY },
                null, null, null, null, null);


        cursor.moveToFirst();

        while (cursor.moveToNext()) {
            String packageName = cursor.getString(cursor.getColumnIndex(TableInfo.Columns.FIELD_PACKAGE_NAME));
            Integer frequency = cursor.getInt(cursor.getColumnIndex(TableInfo.Columns.FIELD_FREQUENCY));
            packageFrequencies.add(new Pair<>(packageName, frequency));
        }

        cursor.close();

        return packageFrequencies;
    }

    public void insertOrUpdate(String packageName, int frequency) {
        if(update(packageName, frequency)) {
            return;
        }

        insert(packageName, frequency);
    }

    public void delete(String packageName) {
        SQLiteDatabase db = connection.getWritableDatabase();
        db.delete(TableInfo.TABLE_NAME, TableInfo.Columns.FIELD_PACKAGE_NAME + " = ?", new String[] { packageName });
    }

    private void insert(String packageName, int frequency) {
        SQLiteDatabase db = connection.getWritableDatabase();
        db.insert(TableInfo.TABLE_NAME, null, preparePackageNameAndFrequency(packageName, frequency));
    }

    private boolean update(String packageName, int frequency) {
        boolean updated = false;

        SQLiteDatabase db = connection.getWritableDatabase();
        int updatedRows = db.update(
                TableInfo.TABLE_NAME,
                preparePackageNameAndFrequency(packageName, frequency),
                TableInfo.Columns.FIELD_PACKAGE_NAME + " = ?",
                new String[]{packageName});
        updated = updatedRows > 0;

        return updated;
    }

    private ContentValues preparePackageNameAndFrequency(String packageName, int frequency) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(TableInfo.Columns.FIELD_PACKAGE_NAME, packageName);
        contentValues.put(TableInfo.Columns.FIELD_FREQUENCY, frequency);

        return contentValues;
    }
}
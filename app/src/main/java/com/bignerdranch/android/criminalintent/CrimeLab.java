package com.bignerdranch.android.criminalintent;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.bignerdranch.android.criminalintent.database.CrimeBaseHelper;
import com.bignerdranch.android.criminalintent.database.CrimeCursorWrapper;
import com.bignerdranch.android.criminalintent.database.CrimeDbSchema;
import com.bignerdranch.android.criminalintent.database.CrimeDbSchema.CrimeTable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by liubin on 2017/2/8.
 *  单例实现
 */

public class CrimeLab {

    private static CrimeLab sCrimeLab;


    // 创建数据库时添加的

    private Context mContext;
    private SQLiteDatabase mDatabase;


    public static CrimeLab get(Context context) {
        if (sCrimeLab == null) {
            sCrimeLab = new CrimeLab(context);
        }

        return sCrimeLab;
    }
    //私有构造方法，防止通过 new CrimeLab()去实例化
    private CrimeLab(Context context) {

        mContext = context.getApplicationContext();

        /**getWritableDatabase() 打开数据库，如果不存在，就创建crimeBase.db数据库文件
         * 如果首次打开，就调用onCreate，然后保存最新版本
         * 如果有高版本，就调用onUpgrade
         */
        mDatabase = new CrimeBaseHelper(mContext).getWritableDatabase();
        //mCrimes = new ArrayList<>();

//        for (int i = 0; i < 100; i++) {
//
//            Crime crime = new Crime();
//            crime.setTitle("Crime #" + i);
//            crime.setSolved(i % 2 == 0);
//            mCrimes.add(crime);
//       }
    }

    //添加数据
    public void addCrime(Crime c) {
        //mCrimes.add(c);

        ContentValues values = getContentValues(c);

        mDatabase.insert(CrimeTable.NAME, null, values);
    }


    public List<Crime> getCrimes() {
        //return mCrimes;
//        return new ArrayList<>();

        List<Crime> crimes = new ArrayList<>();
        CrimeCursorWrapper cursor = queryCrimes(null,null);

        try{

            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {

                crimes.add(cursor.getCrime());
                cursor.moveToNext();
            }
        }finally {
            cursor.close();
        }
        return crimes;
    }

    public Crime getCrime(UUID id) {
//        for (Crime crime : mCrimes) {
//
//            if (crime.getId().equals(id)) {
//                return crime;
//            }
//        }

        CrimeCursorWrapper cursor = queryCrimes(CrimeTable.Cols.UUID + " = ?",
                new String[]{id.toString()});

        try{
            if (cursor.getCount() == 0) {

                return null;
            }

            cursor.moveToFirst();

            return cursor.getCrime();
        }finally {
            cursor.close();
        }
    }

    //更新数据
    public void updateCrime(Crime crime) {

        String uuidString = crime.getId().toString();

        ContentValues values = getContentValues(crime);

        mDatabase.update(CrimeTable.NAME, values, CrimeTable.Cols.UUID
        + "= ?", new String[]{uuidString});
    }



    private static ContentValues getContentValues(Crime crime) {

        ContentValues values = new ContentValues();

        values.put(CrimeTable.Cols.UUID, crime.getId().toString());
        values.put(CrimeTable.Cols.TITLE, crime.getTitle());
        values.put(CrimeTable.Cols.DATE, crime.getDate().getTime());
        values.put(CrimeTable.Cols.SOLVED, crime.isSolved() ? 1 : 0);
        values.put(CrimeTable.Cols.SUSPECT, crime.getSuspect());

        return values;
    }


    //查询
    private CrimeCursorWrapper queryCrimes(String whereClause, String[] whereArgs) {

        Cursor cursor = mDatabase.query(CrimeTable.NAME, null, whereClause,whereArgs,null,null,null);
        return new CrimeCursorWrapper(cursor);
    }


}

package com.gmail.a93ak.andrei19.finance30.model.dbhelpers;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import com.gmail.a93ak.andrei19.finance30.model.base.DBHelper;
import com.gmail.a93ak.andrei19.finance30.model.base.DBHelperInterface;
import com.gmail.a93ak.andrei19.finance30.model.pojos.IncomeCategory;

import java.util.ArrayList;
import java.util.List;

public class DBHelperCategoryIncome implements DBHelperInterface<IncomeCategory> {


    private DBHelper dbHelper;

    private static DBHelperCategoryIncome instance;

    public static DBHelperCategoryIncome getInstance(DBHelper dbHelper) {
        if (instance == null)
            instance = new DBHelperCategoryIncome(dbHelper);
        return instance;
    }

    private DBHelperCategoryIncome(DBHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    @Override
    public long add(IncomeCategory incomeCategory) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBHelper.INCOME_CATEGORY_KEY_NAME, incomeCategory.getName());
        values.put(DBHelper.INCOME_CATEGORY_KEY_PARENT_ID, incomeCategory.getParent_id());
        long id;
        try {
            db.beginTransaction();
            id = db.insert(DBHelper.TABLE_INCOME_CATEGORIES, null, values);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        return id;
    }

    @Override
    public IncomeCategory get(long id) {
        String selectQuery = "SELECT * FROM " + DBHelper.TABLE_INCOME_CATEGORIES + " WHERE _id = " + id;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            IncomeCategory incomeCategory = new IncomeCategory();
            incomeCategory.setId(cursor.getLong(0));
            incomeCategory.setName(cursor.getString(1));
            incomeCategory.setParent_id(cursor.getLong(2));
            cursor.close();
            return incomeCategory;
        } else {
            cursor.close();
            return null;
        }
    }

    @Override
    public Cursor getAll() {
        String selectQuery = "SELECT * FROM " + DBHelper.TABLE_INCOME_CATEGORIES;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        return db.rawQuery(selectQuery, null);
    }

    @Override
    public int update(IncomeCategory incomeCategory) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBHelper.INCOME_CATEGORY_KEY_NAME, incomeCategory.getName());
        values.put(DBHelper.INCOME_CATEGORY_KEY_PARENT_ID, incomeCategory.getParent_id());
        int count;
        try {
            db.beginTransaction();
            count = db.update(DBHelper.TABLE_INCOME_CATEGORIES, values, DBHelper.INCOME_CATEGORY_KEY_ID + "=?", new String[]{String.valueOf(incomeCategory.getId())});
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        return count;
    }

    @Override
    public int delete(long id) {
        int result = 0;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int count = 0;
        try {
            db.beginTransaction();
            if (get(id).getParent_id() == -1) {
                count += deleteAllByParentId(id);
            }
            count += db.delete(DBHelper.TABLE_INCOME_CATEGORIES, DBHelper.INCOME_CATEGORY_KEY_ID + "=?", new String[]{String.valueOf(id)});
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        return count;
    }

    public int deleteAllByParentId(long id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int count;
        try {
            db.beginTransaction();
            count = db.delete(DBHelper.TABLE_INCOME_CATEGORIES, DBHelper.INCOME_CATEGORY_KEY_PARENT_ID + "=?", new String[]{String.valueOf(id)});
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        return count;
    }


    @Override
    public int deleteAll() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int count;
        try {
            db.beginTransaction();
            count = db.delete(DBHelper.TABLE_INCOME_CATEGORIES, null, null);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        return count;
    }

    public List<IncomeCategory> getAllToList() {
        List<IncomeCategory> list = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + DBHelper.TABLE_INCOME_CATEGORIES;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                IncomeCategory incomeCategory = new IncomeCategory();
                incomeCategory.setId(cursor.getLong(0));
                incomeCategory.setName(cursor.getString(1));
                incomeCategory.setParent_id(cursor.getLong(2));
                list.add(incomeCategory);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public Cursor getAllByParentId(long id) {
        String selectQuery = "SELECT * FROM " + DBHelper.TABLE_INCOME_CATEGORIES + " WHERE " + DBHelper.INCOME_CATEGORY_KEY_PARENT_ID + " = " + id;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        return db.rawQuery(selectQuery, null);
    }

    public List<IncomeCategory> getAllToListByParentId(long id) {
        List<IncomeCategory> list = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + DBHelper.TABLE_INCOME_CATEGORIES + " WHERE " + DBHelper.INCOME_CATEGORY_KEY_PARENT_ID + " = " + id;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                IncomeCategory incomeCategory = new IncomeCategory();
                incomeCategory.setId(cursor.getLong(0));
                incomeCategory.setName(cursor.getString(1));
                incomeCategory.setParent_id(cursor.getLong(2));
                list.add(incomeCategory);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

}

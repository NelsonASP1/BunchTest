package com.example.bunchtest

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteDatabase.CursorFactory
import android.database.sqlite.SQLiteOpenHelper
import androidx.annotation.Nullable

class DBHelper(context: Context): SQLiteOpenHelper(context, "UserData", null, 1) {

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("create table Userdata (empID INTEGER PRIMARY KEY AUTOINCREMENT, empName TEXT, empContact TEXT)")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("drop table if exists Userdata")
    }

    fun saveuserdata(name: String, email: String): Boolean {
        val p0 = this.writableDatabase
        val cv = ContentValues()

        cv.put("empName", name)
        cv.put("empContact", email)
        val result = p0.insert("Userdata", null, cv)
        if (result == -1 .toLong()){
            return false
        }
        return true
    }

    fun getData(): Cursor? {
        val p0 = this.writableDatabase
        val cursor = p0.rawQuery("select * from Userdata", null)
        return cursor
    }
}
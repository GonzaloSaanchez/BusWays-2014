package com.example.androidmaps;

import com.google.android.gms.internal.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLite extends SQLiteOpenHelper {

	
	
	String sql;
	
	public SQLite(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		  sql= "CREATE TABLE parada (id INTEGER PRIMERY KEY AUNTOINCREMENT, x TEXT, y TEXT)";
		  db.execSQL(sql);
		  sql = "INSERT INTO parada (x, y) VALUES ('-34.550630', '-58.452787')";
		  db.execSQL(sql);
		  sql = "INSERT INTO parada (x, y) VALUES ('-34.547480', '-58.456273')";
		  db.execSQL(sql);  
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub

	}

}

package com.example.myapplication.Class;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class JMDictHelper {
    private Context context;
    private SQLiteDatabase db;

    public JMDictHelper(Context context) {
        this.context = context;
        this.db = context.openOrCreateDatabase("jmdict", MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS jmdict (word TEXT, definition TEXT);");
    }

    public void parseJMDictFile(InputStream input) {

        BufferedReader r = new BufferedReader(new InputStreamReader(input));
        StringBuilder total = new StringBuilder();
        String line;
        try {
            while ((line = r.readLine()) != null) {
                total.append(line).append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Document doc = Jsoup.parse(total.toString());
        Elements entries = doc.select("entry");
        for (Element entry : entries) {
            String word = entry.select("keb").text();
            String definition = entry.select("gloss").text();
            db.execSQL("INSERT INTO jmdict (word, definition) VALUES (?, ?)", new String[] {word, definition});
        }
    }

    public String getDefinition(String word) {
        String definition = "";
        Cursor cursor = db.rawQuery("SELECT definition FROM jmdict WHERE word = ?", new String[] {word});
        if (cursor.moveToFirst()) {
            definition = cursor.getString(0);
        }
        cursor.close();
        return definition;
    }
}
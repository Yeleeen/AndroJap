package com.example.myapplication.Class;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.myapplication.R;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;


public class JMDictSAXParser {

    private JMDictSQLiteHelper helper;
    private SQLiteDatabase db;

    public JMDictSAXParser(Context context) {
        helper = new JMDictSQLiteHelper(context);
    }

    public void parseXML(Context context) {
        try {
            InputStream is = context.getResources().openRawResource(R.raw.jmdict);
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            DefaultHandler handler = new DefaultHandler() {
                boolean kanji = false;
                boolean sense = false;
                String kanjiValue = "";
                String definitionValue = "";
                SQLiteDatabase db = helper.getWritableDatabase();

                public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
                    if (qName.equalsIgnoreCase("k_ele")) {
                        kanji = true;
                    }
                    if (qName.equalsIgnoreCase("sense")) {
                        sense = true;
                    }
                }

                public void endElement(String uri, String localName, String qName) throws SAXException {
                    if (qName.equalsIgnoreCase("entry")) {
                        Cursor cursor = db.query("jmdict", new String[]{"kanji"}, "kanji = ?", new String[]{kanjiValue}, null, null, null);
                        if (!cursor.moveToFirst()) {
                            ContentValues values = new ContentValues();
                            values.put("kanji", kanjiValue);
                            values.put("definition", definitionValue);
                            db.insert("jmdict", null, values);
                        }
                        cursor.close();
                        kanjiValue = "";
                        definitionValue = "";
                    }
                }

                public void characters(char ch[], int start, int length) throws SAXException {
                    if (kanji) {
                        kanjiValue = new String(ch, start, length);
                        kanji = false;
                    }
                    if (sense) {
                        definitionValue = new String(ch, start, length);
                        sense = false;
                    }
                }
            };
            saxParser.parse(is, handler);
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
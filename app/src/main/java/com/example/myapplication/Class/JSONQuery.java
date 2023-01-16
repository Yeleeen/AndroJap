package com.example.myapplication.Class;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

import android.content.Context;

import com.example.myapplication.R;

import java.io.InputStream;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class JSONQuery {
    public static void queryJSON(Context context) {
        String jsonString = loadJSONFromRaw(context);
        try {
            JSONObject json = new JSONObject(jsonString);
            JSONObject jmdict = json.getJSONObject("JMdict");
            JSONArray entries = jmdict.getJSONArray("entry");
            String gloss;
            for (int i = 0; i < entries.length(); i++) {
                JSONObject entry = entries.getJSONObject(i);
                int ent_seq = entry.getInt("ent_seq");
                try {
                    JSONObject sense = entry.getJSONObject("sense");
                    gloss = sense.getString("gloss");
                } catch (JSONException e){
                    JSONArray sense = entry.getJSONArray("sense");
                     gloss = sense.getString(0);

                }



                System.out.println("ent_seq: " + ent_seq + ", gloss: " + gloss);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public static String searchWord(Context context, String searchWord) {
        String jsonString = loadJSONFromRaw(context);
        try {
            JSONObject json = new JSONObject(jsonString);
            JSONObject jmdict = json.getJSONObject("JMdict");
            JSONArray entries = jmdict.getJSONArray("entry");
            String keb = null;
            for (int i = 0; i < entries.length(); i++) {
                JSONObject entry = entries.getJSONObject(i);

                try{
                    JSONObject k_ele = entry.getJSONObject("k_ele");
                     keb = k_ele.getString("keb");
                }catch(JSONException e){
                    try{
                        JSONArray k_ele = entry.getJSONArray("k_ele");
                        keb = k_ele.getString(0);
                    }catch(JSONException f){
                        keb = "";
                    }
                    
                }


                if (keb.equals(searchWord)) {

                    String gloss;
                    try {
                        JSONObject sense = entry.getJSONObject("sense");
                        gloss = sense.getString("gloss");
                    } catch (JSONException e){
                        JSONArray sense = entry.getJSONArray("sense");
                        gloss = sense.getString(0);

                    }

                    System.out.println("gloss: AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA" + gloss);
                    return gloss;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }

    private static String loadJSONFromRaw(Context context) {
        String json = null;
        try {
            InputStream is = context.getResources().openRawResource(R.raw.jmdictjson);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            json = sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return json;
    }
}
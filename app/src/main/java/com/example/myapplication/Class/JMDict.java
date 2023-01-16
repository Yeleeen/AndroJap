package com.example.myapplication.Class;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class JMDict {

    @SerializedName("entries")
    private List<Entry> entries;

    public List<Entry> getEntries() {
        return entries;
    }

    public void setEntries(List<Entry> entries) {
        this.entries = entries;
    }



    public JMDict(List<Entry> entries) {
        this.entries = entries;
    }

    public class Entry {
        public List<Kanji> getKanji() {
            return kanji;
        }



        @SerializedName("kanji")
        private List<Kanji> kanji;

        @SerializedName("reading")
        private List<Reading> reading;

        @SerializedName("sense")
        private List<Sense> sense;

        //getters and setters
        public void setKanji(List<Kanji> kanji) {
            this.kanji = kanji;
        }

        public List<Reading> getReading() {
            return reading;
        }

        public void setReading(List<Reading> reading) {
            this.reading = reading;
        }

        public List<Sense> getSense() {
            return sense;
        }

        public void setSense(List<Sense> sense) {
            this.sense = sense;
        }

    }

    public class Kanji {
        public String getLiteral() {
            return literal;
        }

        public void setLiteral(String literal) {
            this.literal = literal;
        }

        @SerializedName("literal")
        private String literal;

        //getters and setters
    }

    public class Reading {
        public String getReading() {
            return reading;
        }

        public void setReading(String reading) {
            this.reading = reading;
        }

        @SerializedName("reading")
        private String reading;

        //getters and setters
    }

    public class Sense {
        public List<String> getGloss() {
            return gloss;
        }

        public void setGloss(List<String> gloss) {
            this.gloss = gloss;
        }

        @SerializedName("gloss")
        private List<String> gloss;

        //getters and setters
    }

    //getters and setters for entries
}
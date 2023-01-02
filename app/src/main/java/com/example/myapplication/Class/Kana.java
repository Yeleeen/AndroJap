package com.example.myapplication.Class;

public class Kana {


    static final int HIRAGANA_START = 0x3041;
    static final int HIRAGANA_END = 0x3096;
    static final int KATAKANA_START = 0x30A1;
    static final int KATAKANA_END = 0x30FA;

    String hiragana = "あいうえおぁぃぅぇぉゔかきくけこさしすせそたちつてとっなにぬねのんはひふへほ・ーまみむめもゝやゆよゞゃゅョ、らりるれろ。わゐゑをがぎぐげござじずぜぞだぢづでどばびぶべぼぱぴぷぺぽ";
    String katakana = "アイウエオァィゥェォヴカキクケコサシスセソタチツテトッナニヌネノンハヒフヘホ・ーマミムメモヽヤユヨヾャュョ、ラリルレロ。ワヰヱヲガギグゲゴザジズゼゾダヂヅデドバビブベボパピプペポ";

    public String kataToHira(String katakanaString){
        String hiraganaString = "";
        if (katakanaString == "") {
            return "";
        }

        for(int i = 0; i<katakanaString.length(); i++){
            if (isCharKatakana(katakanaString.charAt(i))) {
                hiraganaString = hiraganaString + hiragana.charAt(katakana.indexOf(katakanaString.charAt(i)));
            }
            else{
                hiraganaString = hiraganaString + katakanaString.charAt(i);
            }

        }
        return hiraganaString;

    }
    public boolean isCharKatakana(char kana){
        for (int j=0;j<katakana.length();j++) {
            if (kana == katakana.charAt(j)) {
                return true;
            }
        }
        return false;

    }
    public boolean isCharHiragana(char kana){
        for (int j=0;j<hiragana.length();j++) {
            if (kana == hiragana.charAt(j)) {
                return true;
            }
        }
        return false;

    }
    public boolean isCharKana(char kana){
        for (int j=0;j<hiragana.length();j++) {
            if (kana == hiragana.charAt(j) || kana == katakana.charAt(j)) {
                return true;
            }
    }
        return false;
    }
    public boolean isKana(String string){
        for(int i=0;i<string.length();i++){
            if (!isCharHiragana(string.charAt(i))){
                return true;
            }
        }


        return false;
    }
}

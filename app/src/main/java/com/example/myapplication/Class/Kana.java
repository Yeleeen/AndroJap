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

            hiraganaString = hiraganaString + hiragana.charAt(katakana.indexOf(katakanaString.charAt(i)));
        }
        return hiraganaString;

    }
}

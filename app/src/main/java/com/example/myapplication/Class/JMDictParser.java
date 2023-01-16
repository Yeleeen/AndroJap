package com.example.myapplication.Class;

import android.content.Context;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class JMDictParser {
    private HashMap<String, String> dictionary = new HashMap<>();

    public void parseXML(Context context, InputStream is) {
        try {

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(is);
            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName("entry");
            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    String kanji = eElement.getElementsByTagName("k_ele").item(0).getTextContent();
                    String definition = eElement.getElementsByTagName("sense").item(0).getTextContent();
                    dictionary.put(kanji, definition);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getDefinition(String kanji) {
        return dictionary.get(kanji);
    }
}
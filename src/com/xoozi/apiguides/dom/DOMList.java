package com.xoozi.apiguides.dom;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class DOMList{

    private List<DOMItem>   _list = new ArrayList<DOMItem>();
    private String          _name;


    DOMList(InputStream is, String path) throws IOException, ParserConfigurationException,
            SAXException {
                

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        Document doc = builder.parse(is);

        Element root = doc.getDocumentElement();

        _name = root.getAttribute(DOMItem.ATTR_NAME);

        NodeList itemList = root.getElementsByTagName(DOMItem.NODE_ITEM);

        for(int i = 0; i < itemList.getLength(); i++){

            Element ie = (Element) itemList.item(i);
            DOMItem item = new  DOMItem(ie, path);
            _list.add(item);
        }
    }


    public List<DOMItem> getList(){
        return _list;
    }


    public String getName(){
        return _name;
    }
}

package com.neo.controller;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.junit.Test;

import java.util.Iterator;
import java.util.List;

public class XmlTest {

    @Test
    public void parseString2xml() {

        StringBuffer str = new StringBuffer();
        str.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<users id=\"1\">\n" +
                "        <username>冰与火之歌</username>\n" +
                "        <password>乔治马丁</password>\n" +
                "</users>");

        try {
            Document doc = (Document) DocumentHelper.parseText(str.toString());
            Element roots = doc.getRootElement();
            System.out.println("根节点 = [" + roots.getName() + "]");
            System.out.println("内容：" + roots.getText());
            ;
            //只有根节点……
            Iterator elements=roots.elementIterator();
            while (elements.hasNext()){
                Element child= (Element) elements.next();
                System.out.println("节点名称 = [" + child.getName() + "]"+"节点内容："+child.getText());
                List subElemets=child.elements();
               for(int i=0;i<subElemets.size();i++){
                   Element subChild= (Element) subElemets.get(i);
                   System.out.println("子节点名称："+subChild.getName()+"子节点内容："+subChild.getText());
               }

            }

        } catch (DocumentException e) {
            e.printStackTrace();
        }


    }






}

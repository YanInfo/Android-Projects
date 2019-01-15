package com.neo.mytools;


import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;

/**
 * <p>Description: </p>
 *
 * @author zhangyan
 * @date 2019/1/10 10:07
 */

public class PublicTools {

    public void xmlParse(String str, String username, String password) {
        try {
            Document doc = (Document) DocumentHelper.parseText(str);
            Element roots = doc.getRootElement();
            System.out.println("根节点 = [" + roots.getName() + "]");
            System.out.println("内容：" + roots.getText());
            //只有根节点……
            Iterator elements = roots.elementIterator();
            while (elements.hasNext()) {
                Element child = (Element) elements.next();
                System.out.println("节点名称 = [" + child.getName() + "]" + "节点内容：" + child.getText());
                if (child.getName().equals("name")) {
                    username = child.getText();
                }
                if (child.getName().equals("password")) {
                    password = child.getText();
                }
                System.out.println("###########" + "用户名：" + username + "密码：" + password + "###########");
            }

        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    public void returnXml(int type, HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();
        //返回状态码
        StringBuffer stringBuffer = new StringBuffer();
        //返回xml格式
        stringBuffer.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<user>\n" +
                "\t<state>" + type + "</state>\n" +
                "</user>");
        out.print(stringBuffer);
        out.flush();
        out.close();

    }


}

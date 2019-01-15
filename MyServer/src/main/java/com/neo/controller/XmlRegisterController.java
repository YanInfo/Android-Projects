package com.neo.controller;

import com.neo.service.Service;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Iterator;

/**
 * <p>Description: </p>
 *
 * @author zhangyan
 * @date 2019/1/6 20:02
 */
@RestController
@RequestMapping("/register")
public class XmlRegisterController {

    private static final long serialVersionUID = 369840050351775312L;
    private int type;

    @RequestMapping("/xml/data")
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = "";
        String password = "";

        //流变成字符串
        StringBuffer sb = new StringBuffer();

        InputStream inputStream = request.getInputStream();
        InputStreamReader isr = new InputStreamReader(inputStream);
        BufferedReader reader = new BufferedReader(isr);
        try {
            String line;
            line = reader.readLine();
            while (line != null) {
                sb.append(line);
                line = reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != isr) {
                    isr.close();
                    isr.close();
                }
                if (null != reader) {
                    reader.close();
                    reader = null;
                }
                if (null != isr) {
                    isr.close();
                    isr = null;
                }
            } catch (IOException e) {
            }
            System.out.println(sb.toString());
        }


        try {
            Document doc = (Document) DocumentHelper.parseText(sb.toString());
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


        // 新建服务对象
        Service serv = new Service();

        //判断username是否存在
        if (!serv.execute(username)) {
            // 验证处理
            boolean loged = serv.register(username, password);
            if (loged) {
                System.out.print("Register Successful !!!");
                type = 0;
            } else {
                System.out.println("插入失败");
                type = 1;
            }
        } else {
            System.out.println("用户已存在，Failed");
            type = 1;
        }

        // 返回信息到客户端
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html");

        //返回状态码，封装成xml格式，最后关闭流
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



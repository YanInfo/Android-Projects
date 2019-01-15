package com.neo.controller;

import com.neo.service.Service;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

/**
 * <p>Description: </p>
 *
 * @author zhangyan
 * @date 2019/1/6 20:02
 */
@RestController
@RequestMapping("/register")
public class JsonRegisterController {

    private static final long serialVersionUID = 369840050351775312L;
    private int type;

    @RequestMapping("/json/data")
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = "", password = "";

        try {
            // 获取输入流
            BufferedReader streamReader = new BufferedReader(new InputStreamReader(request.getInputStream(), "UTF-8"));

            // 写入数据到Stringbuilder
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = streamReader.readLine()) != null) {
                sb.append(line);
            }

            System.out.println("getJsonString" + sb.toString());
            JSONObject jsonObject = new JSONObject(sb.toString());
            username = jsonObject.getString("username");
            password = jsonObject.getString("password");

        } catch (JSONException e) {
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
        PrintWriter out = response.getWriter();

        //返回状态码
        out.print(serv.toJson(type));
        out.flush();
        out.close();

    }

}

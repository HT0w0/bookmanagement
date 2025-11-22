package com.epoint.bookmanagement.action.bookinfo;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.epoint.bookmanagement.bizlogic.bookinfo.BookInfoService;
import com.epoint.bookmanagement.bizlogic.bookinfo.domain.BookInfo;
import com.epoint.bookmanagement.utils.StringUtil;

/**
 * Servlet implementation class BookInfoAddAction
 */
@WebServlet("/bookinfoaddaction")
public class BookInfoAddAction extends HttpServlet
{
    // 创建Service层对象
    private BookInfoService bookInfoService = new BookInfoService();

    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public BookInfoAddAction() {
        super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String method = request.getParameter("method");
        if (StringUtil.isNotBlank(method)) {
            if ("addbookinfo".equals(method)) {
                addBookInfo(request, response);
            }
        }
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        doGet(request, response);
    }

    /**
     * 新增图书信息
     */
    public void addBookInfo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String data = request.getParameter("data");
        // 将前台获取到的请求参数转换为实体类
        BookInfo bookInfo = JSON.parseObject(data, BookInfo.class);
        bookInfo.setBookId(UUID.randomUUID().toString());

        String str = bookInfoService.addBookInfo(bookInfo);
        response.getWriter().write(str);
    }
}

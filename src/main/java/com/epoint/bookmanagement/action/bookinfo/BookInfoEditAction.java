
package com.epoint.bookmanagement.action.bookinfo;

import java.io.IOException;

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
 * Servlet implementation class BookInfoEditAction
 */
@WebServlet("/bookinfoeditaction")
public class BookInfoEditAction extends HttpServlet
{
    // 创建Service层对象
    private BookInfoService bookInfoService = new BookInfoService();

    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public BookInfoEditAction() {
        super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String method = request.getParameter("method");
        if (StringUtil.isNotBlank(method)) {
            if ("querybookinfobyid".equals(method)) {
                queryBookInfoById(request, response);
            }
            if ("updatebookinfo".equals(method)) {
                updateBookInfo(request, response);
            }
        }
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    /**
     * 与addBookInfo 类似：能够更新数据
     */
    public void updateBookInfo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String data = request.getParameter("data");
        BookInfo bookInfo = JSON.parseObject(data, BookInfo.class);
        String str = bookInfoService.updateBookInfo(bookInfo);
        response.getWriter().write(str);
    }

    /**
     * 根据id查找书本信息
     * 
     * @throws IOException
     */
    private void queryBookInfoById(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String bookId = request.getParameter("bookid");
        if (StringUtil.isNotBlank(bookId)) {
            BookInfo bookInfo = bookInfoService.queryBookInfoByID(bookId);
            response.getWriter().write(JSON.toJSONString(bookInfo));
        }
        else {
            // 参数为空时返回提示
            response.getWriter().write("图书ID不能为空");
        }
    }
}

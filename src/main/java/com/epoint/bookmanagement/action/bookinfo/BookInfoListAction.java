package com.epoint.bookmanagement.action.bookinfo;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.epoint.bookmanagement.bizlogic.bookinfo.BookInfoService;
import com.epoint.bookmanagement.bizlogic.bookinfo.domain.BookInfo;
import com.epoint.bookmanagement.utils.StringUtil;

/**
 * Servlet implementation class BookInfoListAction
 */
@WebServlet("/bookinfolistaction")
public class BookInfoListAction extends HttpServlet
{
    // 创建Service层对象
    private BookInfoService bookInfoService = new BookInfoService();

    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public BookInfoListAction() {
        super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String method = request.getParameter("method");
        if (StringUtil.isNotBlank(method)) {
            if ("querybookinfobykeywords".equals(method)) {
                queryBookInfoByKeywords(request, response);
            }
            if ("deletebookinfo".equals(method)) {
                deleteBookInfo(request, response);
            }
            if ("queryremainbybookid".equals(method)) {
                queryRemainByBookId(request, response);
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
     * 能够根据id值调用service相关方法删除数据
     */
    public void deleteBookInfo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String bookIds = request.getParameter("bookids");
        String str = bookInfoService.deleteBookInfo(bookIds);
        response.getWriter().write(str);
    }

    /**
     * 根据关键字查找图书信息
     * 
     * @param request
     * @param response
     * @throws IOException
     */
    private void queryBookInfoByKeywords(HttpServletRequest request, HttpServletResponse response) throws IOException {

        int pageIndex = Integer.parseInt(request.getParameter("pageIndex"));
        int pageSize = Integer.parseInt(request.getParameter("pageSize"));
        String bookName = request.getParameter("bookName");
        String bookType = request.getParameter("bookType");

        Integer bookTypeValue = null;
        if (StringUtil.isNotBlank(bookType)) {
            bookTypeValue = Integer.parseInt(bookType);
        }

        // 加入搜索条件之后的列表数据
        List<BookInfo> bookInfoList = bookInfoService.queryBookinfoByKeyWords(pageIndex, pageSize, bookName, bookTypeValue);

        int total = bookInfoService.findLength();
        // 加入搜索条件之后的列表数据条数
        int listSize = bookInfoService.queryBookinfoByKeyWords(0, total, bookName, bookTypeValue).size();

        Map<String, Object> result = new HashMap<>();
        result.put("data", bookInfoList);
        result.put("total", listSize);

        response.getWriter().write(JSONObject.toJSONString(result));
    }

    /**
     * 根据图书id查找剩余数量
     * 
     * @param request
     * @param response
     * @throws IOException
     */
    private void queryRemainByBookId(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String bookId = request.getParameter("bookId");
        // 验证bookId参数是否存在
        if (StringUtil.isNotBlank(bookId)) {
            BookInfo bookInfo = bookInfoService.queryBookInfoByID(bookId);
            // 判断bookInfo是否为空
            if (bookInfo != null) {
                int remain = bookInfo.getRemain();
                response.getWriter().write(String.valueOf(remain));
            }
            else {
                response.getWriter().write("0");
            }
        }
        else {
            response.getWriter().write("0");
        }
    }
}

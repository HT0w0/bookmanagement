package com.epoint.bookmanagement.action.borrowinfo;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.epoint.bookmanagement.bizlogic.bookinfo.BookInfoService;
import com.epoint.bookmanagement.bizlogic.bookinfo.domain.BookInfo;
import com.epoint.bookmanagement.bizlogic.borrowinfo.BorrowInfoService;
import com.epoint.bookmanagement.bizlogic.borrowinfo.domain.BorrowInfo;
import com.epoint.bookmanagement.utils.StringUtil;

/**
 * Servlet implementation class BorrowInfoAddAction
 */
@WebServlet("/borrowinfoaddaction")
public class BorrowInfoAddAction extends HttpServlet
{
    // 创建service对象
    private BorrowInfoService borrowInfoService = new BorrowInfoService();

    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public BorrowInfoAddAction() {
        super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String method = request.getParameter("method");
        if (StringUtil.isNotBlank(method)) {
            if ("addborrowinfo".equals(method)) {
                addBorrowInfo(request, response);
            }
            if ("querybookinfo".equals(method)) {
                queryBookInfo(request, response);
            }
            if ("getnextborrowid".equals(method)) {
                getNextBorrowId(request, response);
            }
            if ("getbooknamebybookid".equals(method)) {
                getBookNameByBookId(request, response);
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
     * 新增借阅信息
     * 
     * @param request
     * @param response
     * @throws IOException
     */
    private void addBorrowInfo(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String data = request.getParameter("data");
        // 将前台获取到的请求参数转换为实体类
        BorrowInfo borrowInfo = JSON.parseObject(data, BorrowInfo.class);

        String str = borrowInfoService.addBorrowInfo(borrowInfo);
        response.getWriter().write(str);
    }

    /**
     * 在借阅信息新增页“所借图书”下拉列表中查询图书信息列表
     * 
     * @throws IOException
     */
    public void queryBookInfo(HttpServletRequest request, HttpServletResponse response) throws IOException {
        BookInfoService bookInfoService = new BookInfoService();
        // 获取表1列表数据
        List<BookInfo> bookInfoList = bookInfoService.queryBookinfoByKeyWords(0, bookInfoService.findLength(), null, null);
        // 对数据遍历满足个性化需求
        // List<BookInfo> bookInfoList2 = new ArrayList<>();
        for (BookInfo bookInfo : bookInfoList) {
            // //个性化处理 -数据筛选 列表只需要显示超过10本的图书信息
            // if(bookInfo.getRemain() >= 10) {
            // bookInfoList2.add(bookInfo);
            // }
            // 个性化处理 -字符串拼接
            bookInfo.setBookName(bookInfo.getBookName() + "(" + bookInfo.getRemain() + ")");
        }
        // response.getWriter().write(JSONObject.toJSONString(bookInfoList2));
        response.getWriter().write(JSONObject.toJSONString(bookInfoList));
    }

    /**
     * borrowid的递增
     * 
     * @throws IOException
     */
    private void getNextBorrowId(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String borrowId = borrowInfoService.getNextBorrowId();
        response.getWriter().write(borrowId);
    }

    /**
     * 根据bookid查询bookname
     * 
     * @param request
     * @param response
     * @throws IOException
     */
    private void getBookNameByBookId(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String bookId = request.getParameter("bookid");
        BookInfoService bookInfoService = new BookInfoService();
        BookInfo bookInfo = bookInfoService.queryBookInfoByID(bookId);
        response.getWriter().write(bookInfo.getBookName());
    }
}


package com.epoint.bookmanagement.action.borrowinfo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
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
import com.epoint.bookmanagement.bizlogic.borrowinfo.BorrowInfoService;
import com.epoint.bookmanagement.bizlogic.borrowinfo.domain.BorrowInfo;
import com.epoint.bookmanagement.utils.StringUtil;

/**
 * Servlet implementation class BorrowInfoListAction
 */
@WebServlet("/borrowinfolistaction")
public class BorrowInfoListAction extends HttpServlet
{
    // 创建service层对象
    private BorrowInfoService borrowInfoService = new BorrowInfoService();
    private BookInfoService bookInfoService = new BookInfoService();

    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public BorrowInfoListAction() {
        super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String method = request.getParameter("method");
        if (StringUtil.isNotBlank(method)) {
            if ("queryborrowinfo".equals(method)) {
                queryBorrowInfo(request, response);
            }
            if ("deleteborrowinfo".equals(method)) {
                deleteBorrowInfo(request, response);
            }
            if ("returnbook".equals(method)) {
                returnBook(request, response);
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
     * 查找借阅信息
     * 
     * @throws IOException
     */
    private void queryBorrowInfo(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int pageIndex = Integer.parseInt(request.getParameter("pageIndex"));
        int pageSize = Integer.parseInt(request.getParameter("pageSize"));
        String sortField = request.getParameter("sortField");
        String sortOrder = request.getParameter("sortOrder");

        List<BorrowInfo> borrowInfoList = new ArrayList<>();
        borrowInfoList = borrowInfoService.queryBorrowInfo(pageIndex, pageSize, sortField, sortOrder);

        // 用map集合添加bookName信息存入List中
        List<Map<String, Object>> borrowInfoMapList = new ArrayList<>();
        // 遍历借阅信息list
        for (BorrowInfo borrowInfo : borrowInfoList) {
            Map<String, Object> map = new HashMap<>();
            map.put("borrowId", borrowInfo.getBorrowId());
            map.put("bookId", borrowInfo.getBookId());
            map.put("borrower", borrowInfo.getBorrower());
            map.put("phone", borrowInfo.getPhone());
            map.put("borrowTime", borrowInfo.getBorrowTime());
            map.put("returnTime", borrowInfo.getReturnTime());
            map.put("bookName", getBookName(borrowInfo.getBookId()));
            borrowInfoMapList.add(map);
        }

        // 获得list的总条数
        int listLength = borrowInfoService.findLength();

        // 将map集合以json形式返回前端
        Map<String, Object> json = new HashMap<String, Object>();
        json.put("data", borrowInfoMapList);
        json.put("total", listLength);

        response.getWriter().write(JSONObject.toJSONStringWithDateFormat(json, "yyyy-MM-dd HH:mm:ss"));
    }

    /**
     * 根据bookId查询bookName
     * 
     * @param bookId
     * @return
     */
    private String getBookName(String bookId) {
        BookInfo bookInfo = bookInfoService.queryBookInfoByID(bookId);
        // bookInfo 为null时
        return bookInfo == null ? "" : bookInfo.getBookName();
    }

    /**
     * 根据borrowid还书
     * 
     * @param request
     * @param response
     * @throws IOException
     */
    private void returnBook(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String borrowId = request.getParameter("borrowid");
        BorrowInfo borrowInfo = borrowInfoService.queryBorrowInfoByBorrowId(borrowId);
        borrowInfo.setReturnTime(new Date());
        String str = bookInfoService.returnBook(borrowInfo);
        response.getWriter().write(str);
    }

    /**
     * @throws IOException
     *             删除书本信息（同时删除所对应的借阅信息）
     * @param request
     * @param response
     */
    private void deleteBorrowInfo(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String borrowId = request.getParameter("borrowid");
        String str = borrowInfoService.deleteBorrowInfoByBorrowId(borrowId);
        response.getWriter().write(str);
    }
}

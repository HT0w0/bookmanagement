package com.epoint.bookmanagement.bizlogic.bookinfo;

import java.util.ArrayList;
import java.util.List;

import com.epoint.bookmanagement.bizlogic.bookinfo.domain.BookInfo;
import com.epoint.bookmanagement.bizlogic.borrowinfo.domain.BorrowInfo;
import com.epoint.bookmanagement.dao.BookInfoDao;
import com.epoint.bookmanagement.dao.BorrowInfoDao;
import com.epoint.bookmanagement.utils.StringUtil;

public class BookInfoService
{
    // 创建DAO层对象
    private BookInfoDao bookInfoDao = new BookInfoDao();

    /**
     * 添加图书信息
     */
    public String addBookInfo(BookInfo bookInfo) {
        int i = 0;
        // 重名验证
        BookInfo bookInfoExist = bookInfoDao.checkExist("", bookInfo.getBookName(), bookInfo.getAuthor());
        if (bookInfoExist != null) {
            return "该作者下已有此图书";
        }
        else {
            i = bookInfoDao.addBookInfo(bookInfo);
            if (i > 0) {
                return "数据添加成功";
            }
            else {
                return "数据添加失败";
            }
        }
    }

    /**
     * 更新图书信息
     */
    public String updateBookInfo(BookInfo bookInfo) {
        int i = 0;
        // 查询原来的图书信息
        BookInfo before = bookInfoDao.queryBookInfoByID(bookInfo.getBookId());
        String bookNameBefore = before.getBookName();
        String bookNameNow = bookInfo.getBookName();

        BookInfo bookInfoExist = bookInfoDao.checkExist(bookNameBefore, bookNameNow, bookInfo.getAuthor());
        if (bookInfoExist != null) {
            return "该作者下已有此图书";
        }
        else {
            i = bookInfoDao.updateBookInfo(bookInfo);
            if (i > 0) {
                return "书本信息修改成功";
            }
            else {
                return "修改失败！";
            }
        }
    }

    /**
     * 删除图书信息（传入单个图书id删除单条数据）
     */
    public String deleteBookInfoByBookId(String bookId) {
        int i = bookInfoDao.deleteBookinfo(bookId);
        if (i > 0) {
            return "已成功删除" + i + "条数据";
        }
        else {
            return "数据删除出现异常！";
        }
    }

    /**
     * 根据图书id查询图书信息
     */
    public BookInfo queryBookInfoByID(String bookId) {
        BookInfo bookInfo = bookInfoDao.queryBookInfoByID(bookId);
        return bookInfo;
    }

    /**
     * 查询图书信息列表（返回查询的实体集合）
     */
    public List<BookInfo> queryBookinfoByKeyWords(int pageIndex, int pageSize, String bookName, Integer bookType) {
        List<BookInfo> bookInfos = bookInfoDao.queryBookinfoByKeyWords(pageIndex, pageSize, bookName, bookType);
        return bookInfos;
    }

    /**
     * 查询图书信息表中所有数据的条数
     */
    public int findLength() {
        int i = bookInfoDao.findLength();
        return i;
    }

    /**
     * 归还图书信息
     * 
     * @param borrowInfo
     * @return
     */
    public String returnBook(BorrowInfo borrowInfo) {
        int i = bookInfoDao.returnBook(borrowInfo);
        if (i > 0) {
            return "图书归还成功";
        }
        else {
            return "图书归还失败";
        }
    }

    /**
     * 删除书本信息（同时删除对应的借阅信息）
     */
    public String deleteBookInfo(String bookIds) {
        // 两张表之间的删除
        // BookInfo bookInfo = bookInfoDao.queryBookInfoByID(bookIds);
        // BorrowInfoDao borrowInfoDao = new BorrowInfoDao();
        // //根据传来的bookIds，判断有几本书没还
        // int totalCount = borrowInfoDao.queryBorrowInfoCountByBookId(bookIds);
        // if(totalCount > 0) {
        // return bookInfo.getBookName() + "存在借阅信息，删除失败";
        // }
        // else {
        // return bookInfo.getBookName() + "删除成功！";
        // }

        // 两张表之间的删除
        List<String> deleteSuccess = new ArrayList<>();
        List<String> deleteFail = new ArrayList<>();
        List<String> deleteError = new ArrayList<>();

        // 拆分所有的bookIds
        String[] str = bookIds.split(",");
        for (String bookId : str) {
            // 根据bookId找到BookInfo实体，便于获取当前数据中的图书名
            BookInfo bookInfo = bookInfoDao.queryBookInfoByID(bookId);
            BorrowInfoDao borrowInfoDao = new BorrowInfoDao();
            // 当前书本下是否存在归还记录
            int totalCount = borrowInfoDao.queryBorrowInfoCountByBookId(bookId);
            if (totalCount > 0) {
                deleteFail.add(bookInfo.getBookName());
            }
            else {
                // 数据成功删除返回的执行条数就为1
                int m = bookInfoDao.deleteBookInfoByBookId(bookId);
                if (m > 0) {
                    deleteSuccess.add(bookInfo.getBookName());
                }
                else {
                    deleteError.add(bookInfo.getBookName());
                }
            }
        }

        StringBuffer resultString = new StringBuffer();
        if (!deleteSuccess.isEmpty()) {
            resultString.append(StringUtil.joinListToString(deleteSuccess, "、")).append("删除成功！</br>");
        }
        if (!deleteFail.isEmpty()) {
            resultString.append(StringUtil.joinListToString(deleteFail, "、")).append("存在借阅信息，删除失败！</br>");
        }
        if (!deleteError.isEmpty()) {
            resultString.append(StringUtil.joinListToString(deleteError, "、")).append("删除发生错误！</br>");
        }
        return resultString.toString();
    }
}

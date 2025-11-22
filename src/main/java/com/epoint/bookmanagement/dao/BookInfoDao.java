package com.epoint.bookmanagement.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.epoint.bookmanagement.bizlogic.bookinfo.domain.BookInfo;
import com.epoint.bookmanagement.bizlogic.borrowinfo.domain.BorrowInfo;
import com.epoint.bookmanagement.utils.JDBCUtil;
import com.epoint.bookmanagement.utils.StringUtil;

public class BookInfoDao
{

    /**
     * 添加图书信息（返回新增成功的数据条数）
     * 
     * @param bookInfo
     * @return
     */
    public int addBookInfo(BookInfo bookInfo) {
        Connection con = JDBCUtil.getConnection();
        String sql = "insert into bookinfo values (?,?,?,?,?,?)";
        PreparedStatement ps = null;
        int i = 0;
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, bookInfo.getBookId());
            ps.setString(2, bookInfo.getBookName());
            ps.setString(3, bookInfo.getPublisher());
            ps.setString(4, bookInfo.getAuthor());
            ps.setInt(5, bookInfo.getBookType());
            ps.setInt(6, bookInfo.getRemain());
            i = ps.executeUpdate();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            JDBCUtil.closeConnection(null, ps, con);
        }
        return i;
    }

    /**
     * 更新图书信息（返回更新成功的数据条数）
     * 
     * @param bookinfo
     * @return
     */
    public int updateBookInfo(BookInfo bookinfo) {
        Connection con = JDBCUtil.getConnection();
        String sql = "update bookinfo set bookname = ?,publisher = ?,author = ?,booktype = ?,remain = ? where bookId = ?";
        PreparedStatement ps = null;
        int i = 0;
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, bookinfo.getBookName());
            ps.setString(2, bookinfo.getPublisher());
            ps.setString(3, bookinfo.getAuthor());
            ps.setInt(4, bookinfo.getBookType());
            ps.setInt(5, bookinfo.getRemain());
            ps.setString(6, bookinfo.getBookId());
            i = ps.executeUpdate();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            JDBCUtil.closeConnection(null, ps, con);
        }
        return i;
    }

    /**
     * 根据图书id删除图书信息（用,隔开的多个id，实现单表的多选删除）
     */
    public int deleteBookinfo(String bookIds) {
        Connection con = JDBCUtil.getConnection();
        PreparedStatement ps = null;
        String sql = "delete from bookinfo where find_in_set (bookid,?)";
        int i = 0;
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, bookIds);
            i = ps.executeUpdate();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            JDBCUtil.closeConnection(null, ps, con);
        }
        return i;
    }

    /**
     * 根据图书id查询图书信息 （返回BookInfo 实体）
     */
    public BookInfo queryBookInfoByID(String bookId) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        BookInfo bookInfo = null;
        try {
            con = JDBCUtil.getConnection();
            String sql = "select * from bookinfo where bookid = ?";
            ps = con.prepareStatement(sql);
            ps.setString(1, bookId);
            rs = ps.executeQuery();
            if (rs.next()) {
                bookInfo = new BookInfo();
                bookInfo.setBookId(rs.getString("bookid"));
                bookInfo.setBookName(rs.getString("bookname"));
                bookInfo.setPublisher(rs.getString("publisher"));
                bookInfo.setAuthor(rs.getString("author"));
                bookInfo.setBookType(rs.getInt("booktype"));
                bookInfo.setRemain(rs.getInt("remain"));
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            JDBCUtil.closeConnection(rs, ps, con);
        }
        return bookInfo;
    }

    /**
     * 根据关键词对图书信息列表进行分页查询（假设关键词为String bookname、Integer booktype）
     */
    public List<BookInfo> queryBookinfoByKeyWords(int pageIndex, int pageSize, String bookname, Integer booktype) {
        Connection con = JDBCUtil.getConnection();
        int indexnum = pageIndex * pageSize;
        PreparedStatement ps = null;
        ResultSet rs = null;

        List<BookInfo> bookInfoList = new ArrayList<BookInfo>();
        BookInfo bookInfo = null;

        StringBuffer sb = new StringBuffer();
        sb.append(" select * from bookinfo where 1=1 ");

        if (StringUtil.isNotBlank(bookname)) {
            sb.append(" and bookname like concat('%', ? ,'%') ");
        }
        if (booktype != null) {
            sb.append(" and booktype = ? ");
        }
        sb.append(" limit ?,?");

        String sql = sb.toString();
        int i = 1;
        try {
            ps = con.prepareStatement(sql);
            if (StringUtil.isNotBlank(bookname)) {
                ps.setString(i++, bookname);
            }
            if (booktype != null) {
                ps.setInt(i++, booktype);
            }
            ps.setInt(i++, indexnum);
            ps.setInt(i++, pageSize);
            rs = ps.executeQuery();
            while (rs.next()) {
                bookInfo = new BookInfo();
                bookInfo.setBookId(rs.getString("bookid"));
                bookInfo.setBookName(rs.getString("bookname"));
                bookInfo.setAuthor(rs.getString("author"));
                bookInfo.setPublisher(rs.getString("publisher"));
                bookInfo.setBookType(rs.getInt("booktype"));
                bookInfo.setRemain(rs.getInt("remain"));
                bookInfoList.add(bookInfo);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            JDBCUtil.closeConnection(rs, ps, con);
        }
        return bookInfoList;
    }

    /**
     * 查询图书信息表中所有数据的条数
     */
    public int findLength() {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int i = 0;
        try {
            con = JDBCUtil.getConnection();
            String sql = "select count(*) from bookinfo";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                i = rs.getInt(1);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            JDBCUtil.closeConnection(rs, ps, con);
        }
        return i;
    }

    /**
     * 修改页和新增页面公用的重名验证方法
     * 
     * @param bookName
     * @param author
     * @return
     */
    public BookInfo checkExist(String bookNameBefore, String bookNameNow, String author) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String sql = "select * from bookinfo where bookname != ? and bookname = ? and author = ?";
        try {
            con = JDBCUtil.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, bookNameBefore);
            ps.setString(2, bookNameNow);
            ps.setString(3, author);
            rs = ps.executeQuery();

            while (rs.next()) {
                BookInfo bookInfo = new BookInfo();
                bookInfo.setBookId(rs.getString("bookid"));
                bookInfo.setBookName(rs.getString("bookname"));
                bookInfo.setAuthor(rs.getString("author"));
                bookInfo.setPublisher(rs.getString("publisher"));
                bookInfo.setBookType(rs.getInt("booktype"));
                bookInfo.setRemain(rs.getInt("remain"));
                return bookInfo;
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            JDBCUtil.closeConnection(rs, ps, con);
        }
        return null;
    }

    /**
     * 归还图书
     * 
     * @param borrowInfo
     * @return
     */
    public int returnBook(BorrowInfo borrowInfo) {
        Connection con = JDBCUtil.getConnection();
        String sql = "update borrowinfo set returntime = ? where borrowid = ?";
        String sql2 = "update bookinfo set remain = remain + 1 where bookid = ?";
        PreparedStatement ps = null;
        int i = 0;
        int j = 0;
        int result = 0;
        try {
            con.setAutoCommit(false);
            ps = con.prepareStatement(sql);
            ps.setObject(1, borrowInfo.getReturnTime());
            ps.setString(2, borrowInfo.getBorrowId());
            i = ps.executeUpdate();
            if (i > 0) {
                ps.close();
                ps = con.prepareStatement(sql2);
                ps.setString(1, borrowInfo.getBookId());
                j = ps.executeUpdate();
            }
            if (i > 0 && j > 0) {
                result = 1;
                con.commit();
            }
            else {
                con.rollback();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            try {
                con.rollback();
            }
            catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
        finally {
            JDBCUtil.closeConnection(null, ps, con);
        }
        return result;
    }

    /**
     * 根据图书id删除图书信息（传入单个id）
     * 
     * @param bookId
     * @return
     */
    public int deleteBookInfoByBookId(String bookId) {
        Connection con = JDBCUtil.getConnection();
        String sql = "delete from bookinfo where bookid = ?";
        String sql2 = "delete from borrowinfo where bookid = ?";
        PreparedStatement ps = null;
        int i = 0;
        int j = 0;
        int result = 0;
        try {
            con.setAutoCommit(false);
            ps = con.prepareStatement(sql);
            ps.setString(1, bookId);
            i = ps.executeUpdate();
            if (i > 0) {
                ps.close();
                ps = con.prepareStatement(sql2);
                ps.setString(1, bookId);
                j = ps.executeUpdate();
            }
            if (i > 0 && j >= 0) {
                // j >= 0 表示图书还没有借阅记录
                result = 1;
                con.commit();
            }
            else {
                con.rollback();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            try {
                con.rollback();
            }
            catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
        finally {
            JDBCUtil.closeConnection(null, ps, con);
        }
        return result;
    }

}

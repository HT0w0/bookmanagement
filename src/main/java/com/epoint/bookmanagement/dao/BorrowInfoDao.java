package com.epoint.bookmanagement.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.epoint.bookmanagement.bizlogic.borrowinfo.domain.BorrowInfo;
import com.epoint.bookmanagement.utils.JDBCUtil;
import com.epoint.bookmanagement.utils.StringUtil;

public class BorrowInfoDao
{

    /**
     * 新增借阅信息
     */
    public int addBorrowInfo(BorrowInfo borrowInfo) {
        Connection con = JDBCUtil.getConnection();
        PreparedStatement ps = null;
        String sql = "insert into borrowinfo (borrowid, bookid, borrower, phone, borrowtime, returntime) values (?, ?, ?, ?, ?, ?)";
        String sql2 = "update bookinfo set remain = remain - 1 where bookid = ?";
        int i = 0;
        int j = 0;
        int result = 0;
        try {
            con.setAutoCommit(false);
            ps = con.prepareStatement(sql);
            ps.setString(1, borrowInfo.getBorrowId());
            ps.setString(2, borrowInfo.getBookId());
            ps.setString(3, borrowInfo.getBorrower());
            ps.setString(4, borrowInfo.getPhone());
            // 时间字段用setObject，保留时分秒
            ps.setObject(5, borrowInfo.getBorrowTime());
            ps.setObject(6, borrowInfo.getReturnTime());
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
     * 修改借阅信息
     */
    public int updateBorrowInfo(BorrowInfo borrowInfo) {
        Connection con = null;
        PreparedStatement ps = null;
        int result = 0;
        try {
            con = JDBCUtil.getConnection();
            String sql = "update borrowinfo set bookid=?, borrower=?, phone=?, borrowtime=?, returntime=? where borrowid=?";
            ps = con.prepareStatement(sql);
            ps.setString(1, borrowInfo.getBookId());
            ps.setString(2, borrowInfo.getBorrower());
            ps.setString(3, borrowInfo.getPhone());
            // 时间字段用setObject
            ps.setObject(4, borrowInfo.getBorrowTime());
            ps.setObject(5, borrowInfo.getReturnTime());
            ps.setString(6, borrowInfo.getBorrowId());
            result = ps.executeUpdate();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            JDBCUtil.closeConnection(null, ps, con);
        }
        return result;
    }

    /**
     * 删除单条借阅信息
     */
    public int deleteBorrowInfoByBorrowId(String borrowId) {
        Connection con = null;
        PreparedStatement ps = null;
        int result = 0;
        try {
            con = JDBCUtil.getConnection();
            String sql = "delete from borrowinfo where borrowid=?";
            ps = con.prepareStatement(sql);
            ps.setString(1, borrowId);
            result = ps.executeUpdate();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            JDBCUtil.closeConnection(null, ps, con);
        }
        return result;
    }

    /**
     * 根据借阅编号查询借阅实体
     */
    public BorrowInfo queryBorrowInfoByBorrowId(String borrowId) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        BorrowInfo borrowInfo = null;
        try {
            con = JDBCUtil.getConnection();
            String sql = "select * from borrowinfo where borrowId=?";
            ps = con.prepareStatement(sql);
            ps.setString(1, borrowId);
            rs = ps.executeQuery();
            if (rs.next()) {
                borrowInfo = new BorrowInfo();
                borrowInfo.setBorrowId(rs.getString("borrowId"));
                borrowInfo.setBookId(rs.getString("bookId"));
                borrowInfo.setBorrower(rs.getString("borrower"));
                borrowInfo.setPhone(rs.getString("phone"));
                // 时间字段用getTimestamp，保留时分秒
                borrowInfo.setBorrowTime(rs.getTimestamp("borrowTime"));
                borrowInfo.setReturnTime(rs.getTimestamp("returnTime"));
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            JDBCUtil.closeConnection(rs, ps, con);
        }
        return borrowInfo;
    }

    /**
     * 查询借阅信息表中的数据条数
     */
    public int findLength() {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int i = 0;
        try {
            con = JDBCUtil.getConnection();
            String sql = "select count(*) from borrowinfo";
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
     * 查询列表数据，带有排序字段和排序方向
     */
    public List<BorrowInfo> queryBorrowInfo(int pageIndex, int pageSize, String sortField, String sortOrder) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<BorrowInfo> borrowList = new ArrayList<>();
        // 处理sql语句
        String sql = " select * from borrowinfo where 1=1 ";
        StringBuffer sb = new StringBuffer();
        sb.append(sql);

        if (StringUtil.isNotBlank(sortField)) {
            sb.append(" order by ").append(sortField);
            if (StringUtil.isNotBlank(sortOrder)) {
                sb.append(" ").append(sortOrder);
            }
        }
        sb.append(" limit ?,? ");
        sql = sb.toString();
        try {
            con = JDBCUtil.getConnection();
            ps = con.prepareStatement(sql);
            // 计算起始索引
            int indexNum = pageIndex * pageSize;
            ps.setInt(1, indexNum);
            ps.setInt(2, pageSize);
            rs = ps.executeQuery();
            while (rs.next()) {
                BorrowInfo borrowInfo = new BorrowInfo();
                borrowInfo.setBorrowId(rs.getString("borrowId"));
                borrowInfo.setBookId(rs.getString("bookId"));
                borrowInfo.setBorrower(rs.getString("borrower"));
                borrowInfo.setPhone(rs.getString("phone"));
                borrowInfo.setBorrowTime(rs.getTimestamp("borrowTime"));
                borrowInfo.setReturnTime(rs.getTimestamp("returnTime"));
                borrowList.add(borrowInfo);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            JDBCUtil.closeConnection(rs, ps, con);
        }
        return borrowList;
    }

    /**
     * borrowid的递增
     */
    public String getNextBorrowId() {
        Connection con = JDBCUtil.getConnection();
        String sql = "select concat('Borrow',year(now()), lpad (ifnull(max(right(borrowid, 4)),0) + 1, 4, 0) ) as maxnum from borrowinfo where mid(borrowid,7,4) = year(now())";
        // 添加where条件 where mid(borrowid,7,4) = extract(year from sysdate())
        PreparedStatement ps = null;
        ResultSet rs = null;

        String maxnum = null;
        try {
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                maxnum = rs.getString("maxnum");
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            JDBCUtil.closeConnection(rs, ps, con);
        }
        return maxnum;
        // if(maxnum == null) {
        // return "Borrow" + new SimpleDateFormat("yyyy").format(new Date()) + "0001";
        // }
        // else {
        // return maxnum;
        // }
    }

    /**
     * 根据bookId查询当前图书还有多少没有归还
     * 
     * @param bookIds
     * @return
     */
    public int queryBorrowInfoCountByBookId(String bookId) {
        Connection con = JDBCUtil.getConnection();
        String sql = "select count(*) as totalcount from borrowinfo where bookid = ? and returntime is null";
        PreparedStatement ps = null;
        ResultSet rs = null;
        int totalCount = 0;
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, bookId);
            rs = ps.executeQuery();
            while (rs.next()) {
                totalCount = rs.getInt("totalcount");
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            JDBCUtil.closeConnection(rs, ps, con);
        }
        return totalCount;
    }
}

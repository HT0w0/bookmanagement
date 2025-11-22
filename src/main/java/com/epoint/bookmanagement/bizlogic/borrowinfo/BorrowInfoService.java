package com.epoint.bookmanagement.bizlogic.borrowinfo;

import java.util.List;

import com.epoint.bookmanagement.bizlogic.borrowinfo.domain.BorrowInfo;
import com.epoint.bookmanagement.dao.BorrowInfoDao;

public class BorrowInfoService
{
    // 创建Dao层对象
    private BorrowInfoDao borrowInfoDao = new BorrowInfoDao();

    /**
     * 新增借阅信息
     */
    public String addBorrowInfo(BorrowInfo borrowInfo) {
        int i = borrowInfoDao.addBorrowInfo(borrowInfo);
        if (i > 0) {
            return "借阅信息添加成功！";
        }
        else {
            return "图书剩余数量不足，借阅失败！";
        }
    }

    /**
     * 修改借阅信息
     */
    public String updateBorrowInfo(BorrowInfo borrowinfo) {
        int i = borrowInfoDao.updateBorrowInfo(borrowinfo);
        if (i > 0) {
            return "数据修改成功";
        }
        else {
            return "数据修改失败";
        }
    }

    /**
     * 删除单条借阅信息
     */
    public String deleteBorrowInfoByBorrowId(String borrowId) {
        int i = borrowInfoDao.deleteBorrowInfoByBorrowId(borrowId);
        if (i > 0) {
            return "借阅信息删除成功";
        }
        else {
            return "借阅信息删除失败";
        }
    }

    /**
     * 根据借阅编号查询借阅实体
     */
    public BorrowInfo queryBorrowInfoByBorrowId(String borrowId) {
        BorrowInfo borrowInfo = borrowInfoDao.queryBorrowInfoByBorrowId(borrowId);
        return borrowInfo;
    }

    /**
     * 查询借阅信息表中的数据条数
     */
    public int findLength() {
        int i = borrowInfoDao.findLength();
        return i;
    }

    /**
     * 查询列表数据，带有排序字段和排序方向
     */
    public List<BorrowInfo> queryBorrowInfo(int pageIndex, int pageSize, String sortField, String sortOrder) {
        List<BorrowInfo> borrowInfos = borrowInfoDao.queryBorrowInfo(pageIndex, pageSize, sortField, sortOrder);
        return borrowInfos;
    }

    /**
     * borrowid的递增
     */
    public String getNextBorrowId() {
        String borrowId = borrowInfoDao.getNextBorrowId();
        return borrowId;
    }
}

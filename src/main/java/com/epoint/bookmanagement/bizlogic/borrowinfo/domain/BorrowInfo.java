package com.epoint.bookmanagement.bizlogic.borrowinfo.domain;

import java.util.Date;

public class BorrowInfo
{
    private String borrowId;
    private String bookId;
    private String borrower;
    private String phone;
    private Date borrowTime;
    private Date returnTime;

    public BorrowInfo(String borrowId, String bookId, String borrower, String phone, Date borrowTime, Date returnTime) {
        super();
        this.borrowId = borrowId;
        this.bookId = bookId;
        this.borrower = borrower;
        this.phone = phone;
        this.borrowTime = borrowTime;
        this.returnTime = returnTime;
    }

    public BorrowInfo() {
        super();
        // TODO Auto-generated constructor stub
    }

    public String getBorrowId() {
        return borrowId;
    }

    public void setBorrowId(String borrowId) {
        this.borrowId = borrowId;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getBorrower() {
        return borrower;
    }

    public void setBorrower(String borrower) {
        this.borrower = borrower;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Date getBorrowTime() {
        return borrowTime;
    }

    public void setBorrowTime(Date date) {
        this.borrowTime = date;
    }

    public Date getReturnTime() {
        return returnTime;
    }

    public void setReturnTime(Date date) {
        this.returnTime = date;
    }

    @Override
    public String toString() {
        return "BorrowInfo [borrowId=" + borrowId + ", bookId=" + bookId + ", borrower=" + borrower + ", phone=" + phone + ", borrowTime=" + borrowTime + ", returnTime=" + returnTime + "]";
    }

}

package com.epoint.bookmanagement.bizlogic.bookinfo.domain;

public class BookInfo
{
    //
    private String bookId;
    //
    private String bookName;
    //
    private String publisher;
    //
    private String author;
    //
    private Integer bookType;
    //
    private Integer remain;

    public BookInfo(String bookId, String bookName, String publisher, String author, Integer bookType, Integer remain) {
        super();
        this.bookId = bookId;
        this.bookName = bookName;
        this.publisher = publisher;
        this.author = author;
        this.bookType = bookType;
        this.remain = remain;
    }

    public BookInfo() {
        super();
        // TODO Auto-generated constructor stub
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Integer getBookType() {
        return bookType;
    }

    public void setBookType(Integer bookType) {
        this.bookType = bookType;
    }

    public Integer getRemain() {
        return remain;
    }

    public void setRemain(Integer remain) {
        this.remain = remain;
    }

    @Override
    public String toString() {
        return "BookInfo [bookId=" + bookId + ", bookName=" + bookName + ", publisher=" + publisher + ", author=" + author + ", bookType=" + bookType + ", remain=" + remain + "]";
    }

}

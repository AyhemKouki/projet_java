package model;

public class Borrow {

    private int id;
    private int userId;
    private int bookId;
    private String borrowDate;
    private String returnDate;

    public Borrow(int id, int userId, int bookId, String borrowDate, String returnDate) {
        this.id = id;
        this.userId = userId;
        this.bookId = bookId;
        this.borrowDate = borrowDate;
        this.returnDate = returnDate;
    }

    public int getId() { return id; }
    public int getUserId() { return userId; }
    public int getBookId() { return bookId; }
    public String getBorrowDate() { return borrowDate; }
    public String getReturnDate() { return returnDate; }

    public void setId(int id) { this.id = id; }
    public void setUserId(int userId) { this.userId = userId; }
    public void setBookId(int bookId) { this.bookId = bookId; }
    public void setBorrowDate(String borrowDate) { this.borrowDate = borrowDate; }
    public void setReturnDate(String returnDate) { this.returnDate = returnDate; }
}
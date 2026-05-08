package model;

public class Magazine extends LibraryItem{
    private int issueNumber;

    public Magazine(int id, String title, int issueNumber, boolean available, String imagePath) {

        super(id, title, available, imagePath);
        this.issueNumber = issueNumber;
    }
    public int getIssueNumber() {
        return issueNumber;
    }
    public void setIssueNumber(int issueNumber) {}
}

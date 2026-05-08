package model;

public class Book extends LibraryItem {
    private String author;
    private String category;

    public Book(int id , String title , String author , String category , boolean available ,  String imagePath) {
        super(id, title, available, imagePath);
        this.author = author;
        this.category = category;
    }

    public String getAuthor(){
        return author;
    }
    public String getCategory(){
        return category;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
    public void setCategory(String category) {
        this.category = category;
    }
}

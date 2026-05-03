package model;

public class Book {
    private int id;
    private String title;
    private String author;
    private String category;
    private boolean available;
    private String imagePath;

    public Book(int id , String title , String author , String category , boolean available ,  String imagePath) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.category = category;
        this.available = available;
        this.imagePath = imagePath;
    }

    public int getId(){
        return id;
    }
    public String getTitle(){
        return title;
    }
    public String getAuthor(){
        return author;
    }
    public String getCategory(){
        return category;
    }
    public boolean getAvailable(){
        return available;
    }
    public String getImagePath() {return imagePath;}

    public void setId(int id){
        this.id = id;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setAuthor(String author) {
        this.author = author;
    }
    public void setCategory(String category) {
        this.category = category;
    }
    public void setAvailable(boolean available) {
        this.available = available;
    }
    public void setImagePath(String imagePath) {this.imagePath = imagePath;}
}

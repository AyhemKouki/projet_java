package model;

public class LibraryItem {

    protected int id;
    protected String title;
    protected boolean available;
    protected String imagePath;

    public LibraryItem(int id, String title, boolean available, String imagePath) {
        this.id = id;
        this.title = title;
        this.available = available;
        this.imagePath = imagePath;
    }

    public int getId(){
        return id;
    }
    public String getTitle(){
        return title;
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
    public void setAvailable(boolean available) {
        this.available = available;
    }
    public void setImagePath(String imagePath) {this.imagePath = imagePath;}
}
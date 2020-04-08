package de.ur.mi.android.base;

public class Note {
    private int id;
    private String title;
    private String text;
    private String date;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Note(String title, String text, String date) {
        this.title = title;
        this.text = text;
        this.date = date;
    }

    /**
     * Creates an empty note
     */
    public Note() {

    }

    @Override
    public String toString() {
        return title + "\n" + date;
    }
}

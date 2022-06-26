package example.win10.kozbookapp.model;

import androidx.annotation.NonNull;

public class Author {
    private Integer author_id;
    private String name;

    public Author(){

    }

    public Author(String name) {
        this.name = name;
    }

    public Author(Integer author_id, String name) {
        this.author_id = author_id;
        this.name = name;
    }

    public Integer getAuthor_id() {
        return author_id;
    }

    public void setAuthor_id(Integer author_id) {
        this.author_id = author_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NonNull
    @Override
    public String toString() {
        return this.getName();
    }
}

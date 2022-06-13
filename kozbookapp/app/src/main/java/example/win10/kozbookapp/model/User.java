package example.win10.kozbookapp.model;

import java.util.ArrayList;
import java.util.List;

public class User {
    private int user_id;
    private String username;
    private String password;
    private List<Library> libraries;

    public User() {
    }

    public User(int user_id, String username, String password) {
        this.user_id = user_id;
        this.username = username;
        this.password = password;
        this.libraries = new ArrayList<>();
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Library> getLibraries() {
        return libraries;
    }

    public void setLibraries(List<Library> libraries) {
        this.libraries = libraries;
    }

    public void addLibrary(Library library){
        this.libraries.add(library);
    }

}

package example.win10.kozbookapp.model;

import android.text.format.DateFormat;

import java.util.Calendar;
import java.util.Date;

public class AccessBook {
    private Book book;
    private User user;
    private Date startDate;
    private Date endDate;

    public AccessBook() {
    }

    public AccessBook(Book book, User user, int start_year, int start_month, int start_day) {
        this.book = book;
        this.user = user;
        Calendar cal = new Calendar.Builder().setCalendarType("gregorian")
                .setDate(start_year, start_month, start_day).build();
        this.startDate = cal.getTime();
    }

    public AccessBook(Book book, User user, Date startDate, Date endDate) {
        this.book = book;
        this.user = user;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getStartDate() {
        return startDate;
    }

    public CharSequence getStartDateString() {
        return DateFormat.format("dd-mm-yyyy", this.startDate);
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public CharSequence getEndDateString() {
        return DateFormat.format("dd-mm-yyyy", this.endDate);
    }
}

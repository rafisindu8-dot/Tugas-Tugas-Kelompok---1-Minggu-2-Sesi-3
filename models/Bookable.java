package models;

public interface Bookable {
    boolean book(int quantity);
    boolean cancel();
    boolean isAvailable();
}
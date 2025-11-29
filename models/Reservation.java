package models;

import java.time.LocalDate;

public sealed abstract class Reservation 
    permits FlightReservation, HotelReservation {
    
    protected String confirmationNumber;
    protected LocalDate bookingDate;
    protected double totalPrice;
    
    public Reservation(String confirmationNumber, LocalDate bookingDate, double totalPrice) {
        this.confirmationNumber = confirmationNumber;
        this.bookingDate = bookingDate;
        this.totalPrice = totalPrice;
    }
    
    public abstract void displayDetails();
    public abstract String getServiceType();
    
    public String getConfirmationNumber() {
        return confirmationNumber;
    }
    
    public void setConfirmationNumber(String confirmationNumber) {
        this.confirmationNumber = confirmationNumber;
    }
    
    public LocalDate getBookingDate() {
        return bookingDate;
    }
    
    public void setBookingDate(LocalDate bookingDate) {
        this.bookingDate = bookingDate;
    }
    
    public double getTotalPrice() {
        return totalPrice;
    }
    
    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
}
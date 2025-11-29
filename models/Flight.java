package models;

import java.time.LocalDateTime;

public class Flight implements Bookable {
    private String flightNumber;
    private String origin;
    private String destination;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    private double price;
    private int availableSeats;
    private int totalSeats;
    
    public Flight(String flightNumber, String origin, String destination, 
                 LocalDateTime departureTime, LocalDateTime arrivalTime, 
                 double price, int totalSeats) {
        this.flightNumber = flightNumber;
        this.origin = origin;
        this.destination = destination;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.price = price;
        this.totalSeats = totalSeats;
        this.availableSeats = totalSeats;
    }
    
    @Override
    public boolean book(int quantity) {
        if (quantity <= availableSeats) {
            availableSeats -= quantity;
            return true;
        }
        return false;
    }
    
    @Override
    public boolean cancel() {
        if (availableSeats < totalSeats) {
            availableSeats++;
            return true;
        }
        return false;
    }
    
    @Override
    public boolean isAvailable() {
        return availableSeats > 0;
    }
    
    public String getFlightNumber() { return flightNumber; }
    public String getOrigin() { return origin; }
    public String getDestination() { return destination; }
    public LocalDateTime getDepartureTime() { return departureTime; }
    public LocalDateTime getArrivalTime() { return arrivalTime; }
    public double getPrice() { return price; }
    public int getAvailableSeats() { return availableSeats; }
    public int getTotalSeats() { return totalSeats; }
    
    @Override
    public String toString() {
        return String.format("Penerbangan %s: %s -> %s | %s | Kursi: %d/%d | Harga: Rp %,.2f",
                flightNumber, origin, destination, 
                departureTime.toLocalDate(), availableSeats, totalSeats, price);
    }
}
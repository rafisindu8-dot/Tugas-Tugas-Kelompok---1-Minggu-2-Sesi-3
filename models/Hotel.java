package models;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Hotel implements Bookable {
    private String hotelId;
    private String name;
    private String location;
    private double pricePerNight;
    private int availableRooms;
    private int totalRooms;
    
    public Hotel(String hotelId, String name, String location, 
                 double pricePerNight, int totalRooms) {
        this.hotelId = hotelId;
        this.name = name;
        this.location = location;
        this.pricePerNight = pricePerNight;
        this.totalRooms = totalRooms;
        this.availableRooms = totalRooms;
    }
    
    @Override
    public boolean book(int quantity) {
        if (quantity <= availableRooms) {
            availableRooms -= quantity;
            return true;
        }
        return false;
    }
    
    @Override
    public boolean cancel() {
        if (availableRooms < totalRooms) {
            availableRooms++;
            return true;
        }
        return false;
    }
    
    @Override
    public boolean isAvailable() {
        return availableRooms > 0;
    }
    
    public double calculateTotalPrice(LocalDate checkIn, LocalDate checkOut) {
        long nights = ChronoUnit.DAYS.between(checkIn, checkOut);
        return nights * pricePerNight;
    }
    
    public String getHotelId() { return hotelId; }
    public String getName() { return name; }
    public String getLocation() { return location; }
    public double getPricePerNight() { return pricePerNight; }
    public int getAvailableRooms() { return availableRooms; }
    public int getTotalRooms() { return totalRooms; }
    
    @Override
    public String toString() {
        return String.format("Hotel %s: %s | Lokasi: %s | Kamar: %d/%d | Harga/malam: Rp %,.2f",
                hotelId, name, location, availableRooms, totalRooms, pricePerNight);
    }
}
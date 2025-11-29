package models;

import java.time.LocalDate;

public final class HotelReservation extends Reservation {
    private Hotel hotel;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private String guestName;
    private int numberOfGuests;
    
    public HotelReservation(String confirmationNumber, Hotel hotel, 
                          LocalDate checkInDate, LocalDate checkOutDate,
                          String guestName, int numberOfGuests) {
        super(confirmationNumber, LocalDate.now(), 
              hotel.calculateTotalPrice(checkInDate, checkOutDate));
        this.hotel = hotel;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.guestName = guestName;
        this.numberOfGuests = numberOfGuests;
    }
    
    @Override
    public void displayDetails() {
        long nights = java.time.temporal.ChronoUnit.DAYS.between(checkInDate, checkOutDate);
        System.out.println("=== Detail Reservasi Hotel ===");
        System.out.println("Nomor Konfirmasi: " + confirmationNumber);
        System.out.println("Hotel: " + hotel.getName());
        System.out.println("Lokasi: " + hotel.getLocation());
        System.out.println("Check-in: " + checkInDate + " | Check-out: " + checkOutDate);
        System.out.println("Durasi: " + nights + " malam");
        System.out.println("Tamu: " + guestName);
        System.out.println("Jumlah Tamu: " + numberOfGuests);
        System.out.println("Total Harga: Rp " + String.format("%,.2f", totalPrice));
        System.out.println("Tanggal Pemesanan: " + bookingDate);
    }
    
    @Override
    public String getServiceType() {
        return "HOTEL";
    }
    
    public Hotel getHotel() { return hotel; }
    public LocalDate getCheckInDate() { return checkInDate; }
    public LocalDate getCheckOutDate() { return checkOutDate; }
    public String getGuestName() { return guestName; }
    public int getNumberOfGuests() { return numberOfGuests; }
}
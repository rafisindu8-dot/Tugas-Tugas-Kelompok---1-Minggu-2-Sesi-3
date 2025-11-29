package models;

import java.time.LocalDate;
import java.util.List;

public final class FlightReservation extends Reservation {
    private Flight flight;
    private List<String> passengerNames;
    private String contactInfo;
    private int numberOfPassengers;
    
    public FlightReservation(String confirmationNumber, Flight flight, 
                           List<String> passengerNames, String contactInfo) {
        super(confirmationNumber, LocalDate.now(), flight.getPrice() * passengerNames.size());
        this.flight = flight;
        this.passengerNames = passengerNames;
        this.contactInfo = contactInfo;
        this.numberOfPassengers = passengerNames.size();
    }
    
    @Override
    public void displayDetails() {
        System.out.println("=== Detail Reservasi Penerbangan ===");
        System.out.println("Nomor Konfirmasi: " + confirmationNumber);
        System.out.println("Penerbangan: " + flight.getFlightNumber());
        System.out.println("Rute: " + flight.getOrigin() + " -> " + flight.getDestination());
        System.out.println("Tanggal: " + flight.getDepartureTime().toLocalDate());
        System.out.println("Penumpang: " + String.join(", ", passengerNames));
        System.out.println("Kontak: " + contactInfo);
        System.out.println("Total Harga: Rp " + String.format("%,.2f", totalPrice));
        System.out.println("Tanggal Pemesanan: " + bookingDate);
    }
    
    @Override
    public String getServiceType() {
        return "PENERBANGAN";
    }
    
    public Flight getFlight() { return flight; }
    public List<String> getPassengerNames() { return passengerNames; }
    public String getContactInfo() { return contactInfo; }
    public int getNumberOfPassengers() { return numberOfPassengers; }
}
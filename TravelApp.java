import models.*;
import exceptions.*;
import utils.ReservationUtility;

import java.util.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class TravelApp {
    private List<Flight> availableFlights;
    private List<Hotel> availableHotels;
    private List<Reservation> reservations;
    private Scanner scanner;
    
    public TravelApp() {
        this.availableFlights = new ArrayList<>();
        this.availableHotels = new ArrayList<>();
        this.reservations = new ArrayList<>();
        this.scanner = new Scanner(System.in);
        initializeSampleData();
    }
    
    private void initializeSampleData() {
        // Sample flights
        availableFlights.add(new Flight("GA101", "Jakarta", "Bali", 
            LocalDateTime.of(2024, 1, 15, 8, 0), 
            LocalDateTime.of(2024, 1, 15, 10, 30), 
            1500000, 150));
        
        availableFlights.add(new Flight("GA102", "Jakarta", "Surabaya", 
            LocalDateTime.of(2024, 1, 15, 14, 0), 
            LocalDateTime.of(2024, 1, 15, 15, 30), 
            800000, 120));
        
        availableFlights.add(new Flight("SJ201", "Surabaya", "Bali", 
            LocalDateTime.of(2024, 1, 16, 10, 0), 
            LocalDateTime.of(2024, 1, 16, 11, 0), 
            700000, 100));
        
        // Sample hotels
        availableHotels.add(new Hotel("H001", "Grand Bali Hotel", "Bali", 
            500000, 50));
        
        availableHotels.add(new Hotel("H002", "Surabaya Plaza", "Surabaya", 
            350000, 30));
        
        availableHotels.add(new Hotel("H003", "Jakarta Central", "Jakarta", 
            400000, 40));
    }
    
    public void run() {
        System.out.println("=== APLIKASI PEMESANAN PERJALANAN ===");
        
        while (true) {
            displayMainMenu();
            int choice = getIntegerInput("Pilih menu: ");
            
            try {
                switch (choice) {
                    case 1:
                        searchFlights();
                        break;
                    case 2:
                        searchHotels();
                        break;
                    case 3:
                        bookFlight();
                        break;
                    case 4:
                        bookHotel();
                        break;
                    case 5:
                        cancelReservation();
                        break;
                    case 6:
                        viewAllReservations();
                        break;
                    case 0:
                        System.out.println("Terima kasih telah menggunakan aplikasi!");
                        return;
                    default:
                        System.out.println("Pilihan tidak valid!");
                }
            } catch (ReservationException e) {
                System.out.println("Error: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Terjadi kesalahan: " + e.getMessage());
            }
            
            System.out.println("\nTekan Enter untuk melanjutkan...");
            scanner.nextLine();
        }
    }
    
    private void displayMainMenu() {
        System.out.println("\n=== MENU UTAMA ===");
        System.out.println("1. Cari Penerbangan");
        System.out.println("2. Cari Hotel");
        System.out.println("3. Pesan Penerbangan");
        System.out.println("4. Pesan Hotel");
        System.out.println("5. Batalkan Reservasi");
        System.out.println("6. Lihat Semua Pemesanan");
        System.out.println("0. Keluar");
    }
    
    private void searchFlights() {
        System.out.println("\n=== PENCARIAN PENERBANGAN ===");
        
        String origin = getStringInput("Kota asal: ");
        String destination = getStringInput("Kota tujuan: ");
        LocalDate travelDate = getDateInput("Tanggal perjalanan (yyyy-mm-dd): ");
        int passengers = getIntegerInput("Jumlah penumpang: ");
        
        if (!ReservationUtility.isPositiveNumber(passengers)) {
            System.out.println("Jumlah penumpang harus positif!");
            return;
        }
        
        List<Flight> foundFlights = availableFlights.stream()
            .filter(flight -> flight.getOrigin().equalsIgnoreCase(origin))
            .filter(flight -> flight.getDestination().equalsIgnoreCase(destination))
            .filter(flight -> flight.getDepartureTime().toLocalDate().equals(travelDate))
            .filter(flight -> flight.getAvailableSeats() >= passengers)
            .filter(Bookable::isAvailable)
            .toList();
        
        if (foundFlights.isEmpty()) {
            System.out.println("Tidak ada penerbangan tersedia untuk kriteria tersebut.");
        } else {
            System.out.println("\n=== HASIL PENCARIAN ===");
            foundFlights.forEach(System.out::println);
        }
    }
    
    private void searchHotels() {
        System.out.println("\n=== PENCARIAN HOTEL ===");
        
        String location = getStringInput("Lokasi: ");
        LocalDate checkIn = getDateInput("Tanggal check-in (yyyy-mm-dd): ");
        LocalDate checkOut = getDateInput("Tanggal check-out (yyyy-mm-dd): ");
        int guests = getIntegerInput("Jumlah tamu: ");
        
        if (!ReservationUtility.isValidDateRange(checkIn, checkOut)) {
            System.out.println("Tanggal tidak valid! Check-in harus sebelum check-out.");
            return;
        }
        
        if (!ReservationUtility.isPositiveNumber(guests)) {
            System.out.println("Jumlah tamu harus positif!");
            return;
        }
        
        List<Hotel> foundHotels = availableHotels.stream()
            .filter(hotel -> hotel.getLocation().equalsIgnoreCase(location))
            .filter(hotel -> hotel.getAvailableRooms() > 0)
            .filter(Bookable::isAvailable)
            .toList();
        
        if (foundHotels.isEmpty()) {
            System.out.println("Tidak ada hotel tersedia untuk kriteria tersebut.");
        } else {
            System.out.println("\n=== HASIL PENCARIAN ===");
            foundHotels.forEach(hotel -> {
                System.out.println(hotel);
                double totalPrice = hotel.calculateTotalPrice(checkIn, checkOut);
                System.out.println("  Total untuk periode tersebut: Rp " + 
                    String.format("%,.2f", totalPrice));
            });
        }
    }
    
    private void bookFlight() throws ReservationException {
        System.out.println("\n=== PEMESANAN PENERBANGAN ===");
        
        String flightNumber = getStringInput("Masukkan nomor penerbangan: ");
        Flight selectedFlight = availableFlights.stream()
            .filter(f -> f.getFlightNumber().equalsIgnoreCase(flightNumber))
            .findFirst()
            .orElseThrow(() -> new ReservationException("Penerbangan tidak ditemukan!"));
        
        if (!selectedFlight.isAvailable()) {
            throw new ReservationException("Penerbangan tidak tersedia!");
        }
        
        int passengers = getIntegerInput("Jumlah penumpang: ");
        if (passengers > selectedFlight.getAvailableSeats()) {
            throw new ReservationException("Kursi tidak cukup!");
        }
        
        List<String> passengerNames = new ArrayList<>();
        for (int i = 0; i < passengers; i++) {
            String name = getStringInput("Nama penumpang " + (i + 1) + ": ");
            passengerNames.add(name);
        }
        
        String contactInfo = getStringInput("Email kontak: ");
        if (!ReservationUtility.isValidEmail(contactInfo)) {
            throw new InvalidInputException("Format email tidak valid!");
        }
        
        if (selectedFlight.book(passengers)) {
            String confirmationNumber = ReservationUtility.generateConfirmationNumber();
            FlightReservation reservation = new FlightReservation(
                confirmationNumber, selectedFlight, passengerNames, contactInfo);
            
            reservations.add(reservation);
            
            System.out.println("\n=== PEMESANAN BERHASIL ===");
            reservation.displayDetails();
        } else {
            throw new ReservationException("Gagal memesan penerbangan!");
        }
    }
    
    private void bookHotel() throws ReservationException {
        System.out.println("\n=== PEMESANAN HOTEL ===");
        
        String hotelId = getStringInput("Masukkan ID hotel: ");
        Hotel selectedHotel = availableHotels.stream()
            .filter(h -> h.getHotelId().equalsIgnoreCase(hotelId))
            .findFirst()
            .orElseThrow(() -> new ReservationException("Hotel tidak ditemukan!"));
        
        if (!selectedHotel.isAvailable()) {
            throw new ReservationException("Hotel tidak tersedia!");
        }
        
        LocalDate checkIn = getDateInput("Tanggal check-in (yyyy-mm-dd): ");
        LocalDate checkOut = getDateInput("Tanggal check-out (yyyy-mm-dd): ");
        
        if (!ReservationUtility.isValidDateRange(checkIn, checkOut)) {
            throw new InvalidInputException("Tanggal tidak valid!");
        }
        
        int guests = getIntegerInput("Jumlah tamu: ");
        String guestName = getStringInput("Nama tamu: ");
        
        if (selectedHotel.book(1)) {
            String confirmationNumber = ReservationUtility.generateConfirmationNumber();
            HotelReservation reservation = new HotelReservation(
                confirmationNumber, selectedHotel, checkIn, checkOut, guestName, guests);
            
            reservations.add(reservation);
            
            System.out.println("\n=== PEMESANAN BERHASIL ===");
            reservation.displayDetails();
        } else {
            throw new ReservationException("Gagal memesan hotel!");
        }
    }
    
    private void cancelReservation() {
        System.out.println("\n=== PEMBATALAN RESERVASI ===");
        
        String confirmationNumber = getStringInput("Masukkan nomor konfirmasi: ");
        
        Reservation reservationToCancel = reservations.stream()
            .filter(r -> r.getConfirmationNumber().equals(confirmationNumber))
            .findFirst()
            .orElse(null);
        
        if (reservationToCancel == null) {
            System.out.println("Reservasi tidak ditemukan!");
            return;
        }
        
        if (reservationToCancel instanceof FlightReservation fr) {
            if (fr.getFlight().cancel()) {
                reservations.remove(reservationToCancel);
                System.out.println("Reservasi penerbangan berhasil dibatalkan!");
            }
        } else if (reservationToCancel instanceof HotelReservation hr) {
            if (hr.getHotel().cancel()) {
                reservations.remove(reservationToCancel);
                System.out.println("Reservasi hotel berhasil dibatalkan!");
            }
        }
    }
    
    private void viewAllReservations() {
        System.out.println("\n=== SEMUA PEMESANAN ===");
        
        if (reservations.isEmpty()) {
            System.out.println("Belum ada pemesanan.");
            return;
        }
        
        reservations.forEach(reservation -> {
            System.out.println("----------------------------------------");
            reservation.displayDetails();
        });
        
        long flightCount = reservations.stream()
            .filter(r -> r instanceof FlightReservation)
            .count();
        
        long hotelCount = reservations.stream()
            .filter(r -> r instanceof HotelReservation)
            .count();
        
        double totalRevenue = reservations.stream()
            .mapToDouble(Reservation::getTotalPrice)
            .sum();
        
        System.out.println("\n=== STATISTIK ===");
        System.out.printf("Total Reservasi: %d (Penerbangan: %d, Hotel: %d)%n", 
            reservations.size(), flightCount, hotelCount);
        System.out.printf("Total Pendapatan: Rp %,.2f%n", totalRevenue);
    }
    
    private String getStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }
    
    private int getIntegerInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                int value = scanner.nextInt();
                scanner.nextLine();
                return value;
            } catch (InputMismatchException e) {
                System.out.println("Input harus berupa angka! Silakan coba lagi.");
                scanner.nextLine();
            }
        }
    }
    
    private LocalDate getDateInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String dateStr = scanner.nextLine().trim();
                return LocalDate.parse(dateStr, DateTimeFormatter.ISO_LOCAL_DATE);
            } catch (DateTimeParseException e) {
                System.out.println("Format tanggal tidak valid! Gunakan format yyyy-mm-dd.");
            }
        }
    }
    
    public static void main(String[] args) {
        TravelApp app = new TravelApp();
        app.run();
    }
}
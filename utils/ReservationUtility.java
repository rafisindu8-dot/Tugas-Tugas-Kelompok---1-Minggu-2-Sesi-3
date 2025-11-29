package utils;

import java.util.Random;

public final class ReservationUtility {
    private static final Random random = new Random();
    
    private ReservationUtility() {}
    
    public static String generateConfirmationNumber() {
        return String.format("CNF%06d", random.nextInt(1000000));
    }
    
    public static boolean isValidDateRange(java.time.LocalDate start, java.time.LocalDate end) {
        return !start.isAfter(end) && !start.isBefore(java.time.LocalDate.now());
    }
    
    public static boolean isPositiveNumber(int number) {
        return number > 0;
    }
    
    public static boolean isValidEmail(String email) {
        return email != null && email.contains("@") && email.contains(".");
    }
}
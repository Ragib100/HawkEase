package me.hawkease;

import java.security.SecureRandom;

public class SecureOTPGenerator {
    public static String generateOTP() {
        SecureRandom secureRandom = new SecureRandom();
        int otp = 100000 + secureRandom.nextInt(900000); // Ensures 6-digit OTP
        return String.valueOf(otp);
    }
}
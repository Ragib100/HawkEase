package me.hawkease;

import java.security.SecureRandom;

public class secure_otp_generator {
    public static String generateOTP() {
        SecureRandom secureRandom = new SecureRandom();
        int otp = 100000 + secureRandom.nextInt(900000);
        return String.valueOf(otp);
    }
}
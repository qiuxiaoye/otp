package govtech.example.otp.service;

import govtech.example.otp.model.OTPEntity;
import govtech.example.otp.repository.OtpRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class OtpService {

    @Autowired
    OtpRepository otpRepository;

    public int getOtpByPhoneNumber(String phoneNumber) {
        try {
            int otp = generateOtp();

            if (!isSingaporePhoneNumber(phoneNumber)) {
                throw new RuntimeException("Not a valid singapore phone number.");
            }

            if (otpRepository.getByPhoneNumber(phoneNumber).size() != 0) {
                if (otpRepository.getByPhoneNumber(phoneNumber).get(0).getOtpRequestedTime().plusMinutes(5)
                        .isAfter(LocalDateTime.now())) {
                    return otpRepository.getByPhoneNumber(phoneNumber).get(0).getOtpCode();
                }
            }

            OTPEntity otpEntity = new OTPEntity();
            otpEntity.setPhoneNumber(phoneNumber);
            otpEntity.setOtpCode(otp);
            otpEntity.setOtpRequestedTime(LocalDateTime.now());
            otpRepository.save(otpEntity);

            return otp;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public void validateSmsOtp(OTPEntity otpEntity) {
        try {
            List<OTPEntity> savedRecords = otpRepository.getByPhoneNumber(otpEntity.getPhoneNumber());
            if (savedRecords.size() == 0) {
                throw new RuntimeException("No phone number record. Please request for your otp");
            }

            if (!isSingaporePhoneNumber(otpEntity.getPhoneNumber())) {
                throw new RuntimeException("Not a valid singapore phone number.");
            }

            if (savedRecords.get(0).getOtpRequestedTime().plusMinutes(5).isBefore(otpEntity.getOtpRequestedTime()) // saved + 5 < query
                            || savedRecords.get(0).getOtpRequestedTime().isAfter(otpEntity.getOtpRequestedTime())) { // saved > query
                throw new RuntimeException("OTP expired. Please request your otp 1 more time");
            }

            if (otpEntity.getOtpCode() != savedRecords.get(0).getOtpCode()) {
                throw new RuntimeException("Invalid OTP.");
            }

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public List<OTPEntity> findAll() {
        return otpRepository.findAll();
    }

    private static int generateOtp() {
        int randomNumber = 0;
        int base = 100000;
        while (base >= 1) {
            randomNumber = randomNumber + ThreadLocalRandom.current().nextInt(0, 9 + 1) * base;
            base = base / 10;
        }
        return randomNumber;
    }

    public static boolean isEmptyOrWhiteSpace(String string) {
        return string == null || string.trim().isEmpty();
    }

    private static boolean isSingaporePhoneNumber (String phoneNumber) {
        return phoneNumber.matches("\\+65[6|8|9]\\d{7}");
    }
}

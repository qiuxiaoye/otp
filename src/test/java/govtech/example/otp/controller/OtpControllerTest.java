package govtech.example.otp.controller;

import govtech.example.otp.model.OTPEntity;
import govtech.example.otp.repository.OtpRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@SpringBootTest
public class OtpControllerTest {

    @Autowired
    OtpController otpController;

    @Autowired
    OtpRepository otpRepository;

    @Test
    public void testGetOtpByPhoneNumber_Success() {
        String phoneNumber = "+6582003469";
        Assertions.assertEquals(otpController.getOtpByPhoneNumber(phoneNumber).getStatusCode(),
                HttpStatus.CREATED);
    }

    @Test
    public void testGetOtpByPhoneNumber_Fail() {
        String phoneNumber = "+1";
        Assertions.assertEquals(otpController.getOtpByPhoneNumber(phoneNumber).getBody().getMessage(),
                "Not a valid singapore phone number.");
    }

    @Test
    public void testValidateSmsOtp_Success() {
        int id = 1;
        String phoneNumber = "+6582003469";
        int otpCode = 123456;
        LocalDateTime testDateTime = LocalDateTime.of(2021, 1, 1, 1, 1);

        OTPEntity testEntity = new OTPEntity();
        testEntity.setId(1);
        testEntity.setOtpCode(123456);
        testEntity.setPhoneNumber(phoneNumber);
        testEntity.setOtpRequestedTime(testDateTime);

        otpRepository.save(testEntity);

        Assertions.assertEquals(otpController.validateSmsOtp(testEntity).getStatusCode(),
                HttpStatus.OK);
    }

    @Test
    public void testValidateSmsOtp_Fail() {
        int id = 1;
        String phoneNumber = "+6582003469";
        int otpCode = 123456;
        LocalDateTime testDateTime = LocalDateTime.of(2021, 1, 1, 1, 1);

        OTPEntity repoEntity = new OTPEntity();
        repoEntity.setId(id);
        repoEntity.setOtpCode(otpCode);
        repoEntity.setPhoneNumber(phoneNumber);
        repoEntity.setOtpRequestedTime(testDateTime);

        OTPEntity testEntity = new OTPEntity();
        testEntity.setId(id);
        testEntity.setOtpCode(000000);
        testEntity.setPhoneNumber(phoneNumber);
        testEntity.setOtpRequestedTime(testDateTime);

        OTPEntity testEntity2 = new OTPEntity();
        testEntity2.setId(id);
        testEntity2.setOtpCode(otpCode);
        testEntity2.setPhoneNumber("1");
        testEntity2.setOtpRequestedTime(testDateTime);

        OTPEntity testEntity3 = new OTPEntity();
        testEntity3.setId(id);
        testEntity3.setOtpCode(otpCode);
        testEntity3.setPhoneNumber(phoneNumber);
        testEntity3.setOtpRequestedTime(LocalDateTime.of(2021, 1, 1, 1, 7));

        otpRepository.save(repoEntity);

        Assertions.assertEquals(otpController.validateSmsOtp(testEntity).getBody().getMessage(),
                "Invalid OTP.");

        Assertions.assertEquals(otpController.validateSmsOtp(testEntity2).getBody().getMessage(),
                "No phone number record. Please request for your otp");

        Assertions.assertEquals(otpController.validateSmsOtp(testEntity3).getBody().getMessage(),
                "OTP expired. Please request your otp 1 more time");
    }

}

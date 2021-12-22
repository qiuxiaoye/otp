package govtech.example.otp.controller;

import govtech.example.otp.message.ResponseMessage;
import govtech.example.otp.model.OTPEntity;
import govtech.example.otp.service.OtpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class OtpController {

    @Autowired
    OtpService otpService;

    @GetMapping("/users/{phoneNumber}")
    public ResponseEntity<ResponseMessage> getOtpByPhoneNumber(@PathVariable("phoneNumber") String phoneNumber) {
        try {
            int otp = otpService.getOtpByPhoneNumber(phoneNumber);
            return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseMessage(
                    "Phone number: " + phoneNumber + ". OTP is: " + otp + ". OTP SEND!"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(e.getMessage()));
        }
    }

    @PostMapping("/users")
    public ResponseEntity<ResponseMessage> validateSmsOtp(@RequestBody OTPEntity otpEntity) {
        try {
            otpService.validateSmsOtp(otpEntity);
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(
                    "otp is valid"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(e.getMessage()));
        }
    }

}

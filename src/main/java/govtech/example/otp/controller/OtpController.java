package govtech.example.otp.controller;

import govtech.example.otp.message.ResponseMessage;
import govtech.example.otp.model.OTPEntity;
import govtech.example.otp.service.OtpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class OtpController {

    @Autowired
    OtpService otpService;

    @GetMapping("/otp/{phoneNumber}")
    public ResponseEntity<ResponseMessage> getOtpByPhoneNumber(@PathVariable("phoneNumber") String phoneNumber) {
        try {
            int otp = otpService.getOtpByPhoneNumber(phoneNumber);
            return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseMessage(
                    "Phone number: " + phoneNumber + ". OTP is: " + otp + ". OTP SEND!"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(e.getMessage()));
        }
    }

    @PostMapping("/otp")
    public ResponseEntity<ResponseMessage> validateSmsOtp(@RequestBody OTPEntity otpEntity) {
        try {
            otpService.validateSmsOtp(otpEntity);
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(
                    "otp is valid"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(e.getMessage()));
        }
    }

    @GetMapping("/otp")
    public ResponseEntity<List<OTPEntity>> getAllOtps() {
        try {
            return new ResponseEntity<>(otpService.findAll(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }


}

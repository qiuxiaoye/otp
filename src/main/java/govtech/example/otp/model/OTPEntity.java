package govtech.example.otp.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "one_time_password")
public class OTPEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "phone_number", unique = true)
    private String phoneNumber;

    @Column(name = "otp_code")
    private int otpCode;

    @Column(name = "otp_requested_time")
    private LocalDateTime otpRequestedTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getOtpCode() {
        return otpCode;
    }

    public void setOtpCode(int otpCode) {
        this.otpCode = otpCode;
    }

    public LocalDateTime getOtpRequestedTime() {
        return otpRequestedTime;
    }

    public void setOtpRequestedTime(LocalDateTime otpRequestedTime) {
        this.otpRequestedTime = otpRequestedTime;
    }

    @Override
    public String toString() {
        return "OTPEntity{" +
                "id=" + id +
                ", phoneNumber=" + phoneNumber +
                ", otpCode=" + otpCode +
                ", otpCreateTime=" + otpRequestedTime +
                '}';
    }
}

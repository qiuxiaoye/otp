package govtech.example.otp.repository;

import govtech.example.otp.model.OTPEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OtpRepository extends JpaRepository<OTPEntity, Long> {
    List<OTPEntity> getByPhoneNumber(String phoneNumber);
}

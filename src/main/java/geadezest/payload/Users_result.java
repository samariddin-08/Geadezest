package geadezest.payload;


import geadezest.entity.enums.Result_status;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class Users_result {

    private String fullName;

    private String category;

    private String phoneNumber;

    private String next_test_duration;

    @Enumerated(EnumType.STRING)
    private Result_status status;

    private boolean enabled;

}

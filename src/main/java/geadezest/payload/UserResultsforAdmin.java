package geadezest.payload;

import geadezest.entity.enums.UserResults;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResultsforAdmin {

    private Integer id;

    private String fullName;

    private String categoryName;

    private String phoneNumber;

    private LocalDate nextTestDuration;

    private UserResults userResults;
}

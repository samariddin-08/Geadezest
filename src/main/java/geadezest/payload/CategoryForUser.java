package geadezest.payload;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryForUser {

    private Integer categoryId;

    private String categoryName;

    private LocalTime testTime;

    private LocalDate nextTestTimeDuration;
}

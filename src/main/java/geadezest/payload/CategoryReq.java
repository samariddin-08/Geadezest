package geadezest.payload;


import jakarta.persistence.Entity;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryReq {

    private String name;

    private String description;

    private LocalTime durationTime;

    private LocalDate nextTestWorkTime;


}

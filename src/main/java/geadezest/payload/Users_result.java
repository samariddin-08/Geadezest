package geadezest.payload;


import geadezest.entity.enums.Result_status;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class Users_result {

    private String  categoryName;
    private Integer correctAnswers;

    private Integer allQuestions;

    private LocalTime startTime;

    private LocalTime endTime;

    private LocalDate testDate;


}

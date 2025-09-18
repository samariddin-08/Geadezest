package geadezest.payload;

import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResultDtoInEye {

    private String fullName;

    private String categoryName;

    private Integer totalQuestions;

    private Integer totalAnswers;

    private double rate;

    private LocalTime workTime;

    private LocalDate testDate;

}

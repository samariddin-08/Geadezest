package geadezest.payload;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TestEndResults {

    private String firstName;
    private String lastName;
    private Integer totalQuestions;
    private Integer correctAnswers;
    private double totalScore;
    private LocalTime startTime;
    private LocalTime endTime;
    private LocalDate endDate;
}

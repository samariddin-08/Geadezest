package geadezest.entity;

import jakarta.persistence.*;
import lombok.*;


import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class TestResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    private User user;

    private Integer categoryId;

    private Integer totalQuestion;

    private Integer correctAnswers;

    private double score;

    private LocalDateTime finishedAt;
}

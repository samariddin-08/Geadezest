package geadezest.entity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "user_answers")
public class UserAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @ManyToOne(fetch = FetchType.LAZY)
    private Integer userId;

    @ManyToOne(fetch = FetchType.LAZY)
    private Test test;

    @ManyToMany
    private List<Option> selectedOptions = new ArrayList<>();

    private boolean correct;

    private LocalDateTime answeredAt = LocalDateTime.now();

    private Double score;
}

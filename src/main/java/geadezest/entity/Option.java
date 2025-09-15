package geadezest.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Option {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String text;

    private boolean correct;

    @ManyToOne
    @JoinColumn(name = "question_id")
    private Test question;

    public Option(String answer, boolean correct, Test test) {
        this.text = answer;
        this.correct = correct;
        this.question = test;
    }

}

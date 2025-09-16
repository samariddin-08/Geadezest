package geadezest.entity;

import geadezest.entity.enums.Question_type;
import geadezest.entity.enums.Test_difficulty;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Test {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer photoId;

    private String question;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Option> options;

    @ManyToOne
    private Category category;

    @Enumerated(EnumType.STRING)
    private Question_type question_type;

    @Enumerated(EnumType.STRING)
    private Test_difficulty difficulty;

    @CreatedBy
    private String createdBy;

}

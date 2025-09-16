package geadezest.entity;


import geadezest.entity.enums.Status;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer photoId;

    private String name;

    private String description;

    private Integer questionsCount;

    private Integer moreQuestionsCount;

    private Integer durationTime;

    private Integer nextTestTimeDuration;

    @CreatedBy
    private String createdBy;

    @Enumerated(EnumType.STRING)
    private Status status;

    private String deletedBy;

    private boolean enabled;



}

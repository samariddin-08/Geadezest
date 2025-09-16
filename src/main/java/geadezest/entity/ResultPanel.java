package geadezest.entity;

import geadezest.entity.enums.UserResults;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class ResultPanel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    private User user;

    private String categoryName;

    private LocalDate nextTestDuration;

    @Enumerated(EnumType.STRING)
    private UserResults userResults;
}

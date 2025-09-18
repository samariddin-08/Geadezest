package geadezest.payload;

import geadezest.entity.enums.Status;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryRes {
    private Integer id;
    private Integer photoId;
    private String name;
    private String description;
    private Integer questionCount;
    private Integer moreQuestionCount;
    private LocalTime durationTime;
    private LocalDate nextTestWorkTime;
    private String createdBy;
    private Status status;
    private String deletedBy;

}

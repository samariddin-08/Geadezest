package geadezest.payload;

import geadezest.entity.enums.Question_type;
import geadezest.entity.enums.Test_difficulty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TestRes {

    private Integer id;

    private Integer photoId;

    private String question;

    private String categoryName;

    private Question_type question_type;

    private Test_difficulty test_difficulty;

    private String createdBy;

}

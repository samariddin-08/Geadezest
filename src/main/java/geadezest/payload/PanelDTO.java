package geadezest.payload;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PanelDTO {

    private long categoryCount;

    private long userCount;

    private long answerCount;

    private long testCount;

    private String weeklyNews;
}

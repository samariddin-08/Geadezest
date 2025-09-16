package geadezest.payload;

import geadezest.entity.Option;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TestWorking {

    private Integer testId;

    private String question;

    private List<OptionTest>option;

}

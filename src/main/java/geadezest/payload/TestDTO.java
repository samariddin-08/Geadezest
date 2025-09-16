package geadezest.payload;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TestDTO {

    @Schema(hidden = true)
    private Integer id;

    private String question;

    private String category;

    private List<OptionDTO> options;

}

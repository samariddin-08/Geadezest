package geadezest.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OptionDTO {

    @Schema(hidden = true)
    private Integer id;

    private String answer;

    private boolean correct;

    @Schema(hidden = true)
    private Integer test_id;

}

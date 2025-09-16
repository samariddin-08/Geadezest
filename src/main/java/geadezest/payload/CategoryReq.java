package geadezest.payload;


import jakarta.persistence.Entity;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryReq {

    private String name;

    private String description;

    private Integer durationTime;

    private Integer nextTestWorkTime;


}

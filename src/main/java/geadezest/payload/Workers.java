package geadezest.payload;

import geadezest.entity.enums.Position;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Workers {

    private Integer id;

    private String first_name;
    private String last_name;

    private String phone_number;

    @Enumerated(EnumType.STRING)
    private Position position;
}

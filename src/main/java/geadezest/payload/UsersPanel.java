package geadezest.payload;


import lombok.*;

import java.awt.event.PaintEvent;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UsersPanel {

    private Integer id;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private LocalDate loginDate;

}

package geadezest.payload;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Sertificat {

    private Integer photoId;

    private String categoryName;

    private String firstName;

    private String lastName;

    private LocalDate createdDate;

    private double amaliyscore;

    private double nazariyscore;

    private double overallScore;



}

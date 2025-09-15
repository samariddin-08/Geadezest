package geadezest.payload;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public  class User_panel_for_admin {

    private String fullName;

    private LocalDate dateOfBirth;

    private String phoneNumber;

    private String region;

    private String district;

    private String street;


}

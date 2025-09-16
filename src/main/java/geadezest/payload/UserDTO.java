package geadezest.payload;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {

    @Schema(hidden = true)
    private Integer id;

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    @Email
    private String email;

    private String phone;

    private String password;

    private String region;

    private String district;

    private String street;

    private LocalDate createdDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate birthDate;




}

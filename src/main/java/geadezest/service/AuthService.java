package geadezest.service;

import geadezest.entity.User;
import geadezest.entity.enums.Role;
import geadezest.payload.ApiResponse;
import geadezest.payload.auth.AuthLogin;
import geadezest.payload.auth.AuthRegister;
import geadezest.repository.UserRepository;
import geadezest.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final Map<String,Integer> codes = new HashMap<>();
    private final Map<String,User> users = new HashMap<>();
    private final MailService mailService;
    private final JwtProvider jwtProvider;

    public ApiResponse register(AuthRegister register){
        if (userRepository.existsByEmail(register.getEmail())) {
            return new ApiResponse("Ushbu email allaqachon mavjud",
                    HttpStatus.BAD_REQUEST, false, null);
        }
        User user = new User();
            user.setFirstName(register.getFirstName());
            user.setLastName(register.getLastName());
            user.setEmail(register.getEmail());
            user.setPhone(register.getPhone());
            user.setRole(Role.ROLE_USER);
            user.setLoginDate(LocalDate.now());
            if (!register.getPassword().equals(register.getConfirmPassword())) {
                return new ApiResponse("Parollar mos emas",
                        HttpStatus.BAD_REQUEST, false, null);
            }
                user.setPassword(passwordEncoder.encode(register.getPassword()));
                Random random = new Random();
                int code = Math.abs(random.nextInt() % 10000);
                codes.put(user.getEmail(), code);
                try {
                    mailService.sendMail(user.getEmail(), "Ruyxatdan utish tasdiqlash kodi -> ", code);

                } catch (Exception e) {
                  return   new ApiResponse("Kod yuborishda xatolikyuzaga keldi qaytadan urinib kuring",
                            HttpStatus.INTERNAL_SERVER_ERROR, false, null);
                }
                users.put(user.getEmail(), user);
                return new ApiResponse("Muvaffaqiyatli utildi",HttpStatus.OK, true, null);
             }

    public ApiResponse check_code(String email, Integer code){
        if(users.containsKey(email)){
            Integer haveCod= codes.get(email);
            if(haveCod.equals(code)){
                codes.remove(email);
                User user = users.get(email);
                user.setEnabled(true);
                userRepository.save(user);
                return new ApiResponse("success checked code", HttpStatus.OK,true,null);
            }
            return new ApiResponse("Code incorrect",HttpStatus.BAD_REQUEST,false,null);
        }
        return new ApiResponse("email not found", HttpStatus.BAD_REQUEST,false,null);
    }

    public ApiResponse login(AuthLogin authlogin){
        Optional<User> byEmail = userRepository.findByEmail(authlogin.getEmail());
        if(!byEmail.isPresent()){
           return new ApiResponse("Email not found", HttpStatus.NOT_FOUND,false,null);
        }
        User user = byEmail.get();
            if (passwordEncoder.matches(authlogin.getPassword(), user.getPassword())){
                String token = jwtProvider.generateToken(authlogin.getEmail());
                return new ApiResponse("success login", HttpStatus.OK,true,token);
            }
            return new ApiResponse("Password incorrect", HttpStatus.OK,true,null);
       }
}

package geadezest.service;
import geadezest.entity.*;
import geadezest.entity.enums.UserResults;
import geadezest.payload.*;
import geadezest.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.*;

@Service
@RequiredArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final PhotoRepository photoRepository;
    private final UserContactRepository contactRepository;
    private final RegionRepository regionRepository;
    private final DistrictRepository districtRepository;
    private final StreetRepository streetRepository;
    private final ResultPanelRepository resultPanelRepository;
    private final TestResultRepository testResultRepository;
    private final CategoryRepository categoryRepository;


    public ApiResponse setPhoto(User user, MultipartFile file) {
        try {
            if (file != null && !file.isEmpty()) {
                Photo photo = new Photo();
                photo.setName(file.getOriginalFilename());
                photo.setType(file.getContentType());
                photo.setData(ImageUtils.compressImage(file.getBytes()));
                photoRepository.save(photo);
                user.setPhotoId(photo.getId());
                userRepository.save(user);
                return new ApiResponse("Saqlandi",HttpStatus.OK,true,null);
            }
        } catch (Exception e) {
            return new ApiResponse("Rasm yuklashda muammo yuzaga keldi",
                    HttpStatus.BAD_REQUEST,false,null);
        }
        return null;
    }
    public ApiResponse editProfile( User user, UserDTO userDTO) {

        boolean b = userRepository.existsByEmailAndIdNot(userDTO.getEmail(), user.getId());
        if (b) {
            return new ApiResponse("Ushbu email oldindan tizimda mavjud",
                    HttpStatus.BAD_REQUEST,false,null);
        }
        UserContact contact = user.getContact();
        if (contact == null) {
            contact = new UserContact();
        }
        Region region = contact.getRegion();
        if (region == null) {
            region = new Region();
        }
        regionRepository.save(region);
        region.setName(userDTO.getRegion());
        District district = contact.getDistrict();
        if (district == null) {
            district = new District();
        }
        districtRepository.save(district);
        district.setName(userDTO.getDistrict());
        Street street = contact.getStreet();
        if (street == null) {
            street = new Street();
        }
        streetRepository.save(street);
        street.setName(userDTO.getStreet());
        contact.setRegion(region);
        contact.setDistrict(district);
        contact.setStreet(street);
        contactRepository.save(contact);

        user.setContact(contact);

    user.setContact(contact);


        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());
        if (userDTO.getPassword() != null && !userDTO.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        }
        user.setPhone(userDTO.getPhone());
        user.setBirthDate(userDTO.getBirthDate());


        userRepository.save(user);
        return new ApiResponse("Muvaffaqiyatli tahrirlandi", HttpStatus.OK, true, null);
    }

    public ApiResponse getProfile(User user) {
        UserDTO  userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setEmail(user.getEmail());
        userDTO.setPhone(user.getPhone());
        if (user.getPhone()==null) {
            userDTO.setPhone(null);
        }
        userDTO.setBirthDate(user.getBirthDate());
        if (user.getContact() != null) {
            userDTO.setRegion(user.getContact().getRegion().getName());
            userDTO.setDistrict(user.getContact().getDistrict().getName());
            userDTO.setStreet(user.getContact().getStreet().getName());
        }else {
            userDTO.setRegion(null);
            userDTO.setDistrict(null);
            userDTO.setStreet(null);
        }
        return new ApiResponse("Sizing malumotlaringiz", HttpStatus.OK, true, userDTO);
    }


    public ApiResponse getAllUsers(String name,int page, int size) {
            Pageable pageable = PageRequest.of(page, size);
            Page<User> userPage = userRepository.all(name,pageable);
            if (userPage.isEmpty()) {
                return new ApiResponse("Users not found", HttpStatus.NOT_FOUND, false, null);
            }

       List<UsersPanel> dtoList = new ArrayList<>();
       for (User user : userPage.getContent()) {
           UsersPanel userDTO = new UsersPanel();
                userDTO.setFirstName(user.getFirstName());
                userDTO.setLastName(user.getLastName());
                userDTO.setPhoneNumber(user.getPhone());
                userDTO.setLoginDate(user.getCreatedDate());
                dtoList.add(userDTO);
       }

            Map<String, Object> response = new HashMap<>();
            response.put("users", dtoList);
            response.put("currentPage", userPage.getNumber());
            response.put("totalItems", userPage.getTotalElements());
            response.put("totalPages", userPage.getTotalPages());

            return new ApiResponse("Users found", HttpStatus.OK, true, response);
    }

    public ApiResponse getUserById(Integer id) {
        Optional<User> optionalUser = userRepository.findById(id);

        if (optionalUser.isEmpty()) {
            return new ApiResponse("User not found", HttpStatus.NOT_FOUND, false, null);
        }
        User user = optionalUser.get();
        User_panel_for_admin userDTO = new User_panel_for_admin();
        userDTO.setFullName(user.getFirstName()+" "+user.getLastName());
        userDTO.setPhoneNumber(user.getPhone());
        userDTO.setRegion(user.getContact().getRegion().getName());
        userDTO.setDistrict(user.getContact().getDistrict().getName());
        userDTO.setStreet(user.getContact().getStreet().getName());
        return new ApiResponse("User details found", HttpStatus.OK, true, userDTO);
    }


    public ApiResponse viewResults(User user){

        List<TestResult> byUser = testResultRepository.findByUser(user);
        if (byUser.isEmpty()) {
            return new ApiResponse("Results not found", HttpStatus.NOT_FOUND, false, null);
        }

        List<Users_result> dtoList = new ArrayList<>();
        for (TestResult testResult : byUser) {
            Users_result usersResult = new Users_result();
            usersResult.setAllQuestions(testResult.getTotalQuestion());
            usersResult.setCorrectAnswers(testResult.getCorrectAnswers());
            usersResult.setTestDate(testResult.getFinishedAt());
            usersResult.setCategoryName(categoryRepository.findById(testResult.getCategoryId()).get().getName());
            usersResult.setStartTime(testResult.getStartTime());
            usersResult.setEndTime(testResult.getEndTime());
            dtoList.add(usersResult);
        }
        return new ApiResponse("Results ", HttpStatus.OK, true, dtoList);
    }
    public ApiResponse get(String district, String region, int page , int size){
        Pageable pageable = PageRequest.of(page, size);
        Page<User> search = userRepository.search(district, region, pageable);
        if (search.isEmpty()) {
            return new ApiResponse("Users not found", HttpStatus.NOT_FOUND, false, null);
        }

        List<UsersPanel> dtoList = new ArrayList<>();
        for (User user : search.getContent()) {
            UsersPanel userDTO = new UsersPanel();
            userDTO.setFirstName(user.getFirstName());
            userDTO.setLastName(user.getLastName());
            userDTO.setPhoneNumber(user.getPhone());
            userDTO.setLoginDate(user.getCreatedDate());
            dtoList.add(userDTO);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("users", dtoList);
        response.put("currentPage", search.getNumber());
        response.put("totalItems", search.getTotalElements());
        response.put("totalPages", search.getTotalPages());

        return new ApiResponse("Users found", HttpStatus.OK, true, response);


    }
}

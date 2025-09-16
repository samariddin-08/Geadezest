package geadezest.service;

import geadezest.entity.*;
import geadezest.payload.ApiResponse;
import geadezest.payload.UserDTO;
import geadezest.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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

    public ApiResponse editProfile(MultipartFile file, User user, UserDTO userDTO) {

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

        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());
        if (userDTO.getPassword() != null && !userDTO.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        }
        user.setPhone(userDTO.getPhone());
        user.setBirthDate(userDTO.getBirthDate());

        try {
            if (file != null && !file.isEmpty()) {
                Photo photo = new Photo();
                photo.setName(file.getOriginalFilename());
                photo.setType(file.getContentType());
                photo.setData(ImageUtils.compressImage(file.getBytes()));
                photoRepository.save(photo);
                user.setPhotoId(photo.getId());

            }
        } catch (Exception e) {
            return new ApiResponse("Rasm yuklashda muammo yuzaga keldi",
                    HttpStatus.BAD_REQUEST,false,null);
        }
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

}

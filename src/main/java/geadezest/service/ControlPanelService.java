package geadezest.service;

import geadezest.entity.ResultPanel;
import geadezest.entity.TestResult;
import geadezest.entity.User;
import geadezest.entity.enums.Result_status;
import geadezest.entity.enums.Role;
import geadezest.entity.enums.UserResults;
import geadezest.payload.ApiResponse;
import geadezest.payload.ResultDtoInEye;
import geadezest.payload.UserResultsforAdmin;
import geadezest.repository.ResultPanelRepository;
import geadezest.repository.TestResultRepository;
import geadezest.repository.UserAnswerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ControlPanelService {

    private final ResultPanelRepository resultPanelRepository;
    private final TestResultRepository testResultRepository;
    private final UserAnswerRepository userAnswerRepository;

    public ApiResponse getUserResults(String fullName, String categoryName, UserResults status, int page, int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<ResultPanel> search = resultPanelRepository.search(fullName, categoryName, status, pageable);
        if (search.isEmpty()) {
            return new ApiResponse("Users not found", HttpStatus.NOT_FOUND, false, null);
        }
        List<UserResultsforAdmin> dtoList = new ArrayList<>();
        for (ResultPanel result : search.getContent()) {
            UserResultsforAdmin userDTO = new UserResultsforAdmin();
            userDTO.setId(result.getId());
            userDTO.setFullName(result.getUser().getFirstName() + " " + result.getUser().getLastName());
            userDTO.setCategoryName(result.getCategoryName());
            userDTO.setPhoneNumber(result.getUser().getPhone());
            userDTO.setNextTestDuration(result.getNextTestDuration());
            userDTO.setUserResults(result.getUserResults());
            dtoList.add(userDTO);
        }
        return new ApiResponse("Users found", HttpStatus.OK, true, dtoList);

    }

    public ApiResponse setResult(User user, Integer resultId, UserResults userResults) {
        if (user.getRole().equals(Role.ROLE_USER)) {
            return new ApiResponse("Ruxsat berilmagan", HttpStatus.BAD_REQUEST, false, null);
        }
        Optional<ResultPanel> byId = resultPanelRepository.findById(resultId);
        if (byId.isEmpty()) {
            return new ApiResponse("Results not found", HttpStatus.NOT_FOUND, false, null);
        }
        if (userResults.equals(UserResults.BEKOR_QILINGAN)) {
            ResultPanel resultPanel = byId.get();
            resultPanel.setUserResults(UserResults.BEKOR_QILINGAN);
            resultPanelRepository.save(resultPanel);
            return new ApiResponse("Results saved", HttpStatus.OK, true, resultPanel);
        } else if (userResults.equals(UserResults.TASDIQLANGAN)) {
            ResultPanel resultPanel = byId.get();
            resultPanel.setUserResults(UserResults.TASDIQLANGAN);
            resultPanelRepository.save(resultPanel);
            return new ApiResponse("Results saved", HttpStatus.OK, true, resultPanel);
        } else {
            return new ApiResponse("ushbu statusga uzgartirib bulmaydi ", HttpStatus.NOT_FOUND, false, null);
        }
    }


    public ApiResponse viewResult(User user, Integer resultId) {
        if (user.getRole().equals(Role.ROLE_USER)) {
            return new ApiResponse("Ruxsat berilmagan", HttpStatus.BAD_REQUEST, false, null);
        }
        Optional<ResultPanel> byId = resultPanelRepository.findById(resultId);
        if (byId.isEmpty()) {
            return new ApiResponse("Results not found", HttpStatus.NOT_FOUND, false, null);
        }
        ResultPanel resultPanel = byId.get();
        Optional<TestResult> byUser = testResultRepository.findByUserr(resultPanel.getUser());
        TestResult testResult = byUser.get();
        ResultDtoInEye result = new ResultDtoInEye();
        result.setFullName(resultPanel.getUser().getFirstName() + " " + resultPanel.getUser().getLastName());
        result.setCategoryName(resultPanel.getCategoryName());
        result.setTotalQuestions(testResult.getTotalQuestion());
        result.setTotalAnswers(testResult.getCorrectAnswers());
        result.setTestDate(testResult.getFinishedAt());
        result.setWorkTime(LocalTime.of(0, 20));
        return new ApiResponse("Results", HttpStatus.OK, true, result);

    }


}

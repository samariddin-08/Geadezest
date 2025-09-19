package geadezest.service;

import com.lowagie.text.PageSize;
import geadezest.entity.ResultPanel;
import geadezest.entity.TestResult;
import geadezest.entity.User;
import geadezest.entity.enums.Role;
import geadezest.entity.enums.UserResults;
import geadezest.payload.*;
import geadezest.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.Document;

import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.time.LocalDate;
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
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final TestRepository testRepository;

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

    public ApiResponse setResult(User user, Integer resultId, UserResults userResults, double amaliyScore) {
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

        }else if (userResults.equals(UserResults.TASDIQLANGAN)) {
                ResultPanel resultPanel1 = byId.get();
                Optional<TestResult> byUser = testResultRepository.findByUser(resultPanel1.getUser());

                if (byUser.isEmpty()) {
                    return new ApiResponse("User test result not found", HttpStatus.NOT_FOUND, false, null);
                }

                TestResult testResult = byUser.get();
                Sertificat sertificat = new Sertificat();
                sertificat.setFirstName(resultPanel1.getUser().getFirstName());
                sertificat.setLastName(resultPanel1.getUser().getLastName());
                sertificat.setAmaliyscore(amaliyScore);
                sertificat.setNazariyscore(testResult.getScore());
                sertificat.setOverallScore((amaliyScore + testResult.getScore()) / 2); // O‘rtacha variant
                sertificat.setCategoryName(resultPanel1.getCategoryName());
                sertificat.setPhotoId(resultPanel1.getUser().getPhotoId());
                sertificat.setCreatedDate(LocalDate.now());

                generatePdf(sertificat, "user.sertificat.pdf");

                resultPanel1.setUserResults(UserResults.TASDIQLANGAN);
                resultPanelRepository.save(resultPanel1);

                return new ApiResponse("Results saved", HttpStatus.OK, true, resultPanel1);
            }
        return new ApiResponse("Results not found", HttpStatus.NOT_FOUND, false, null);

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
        Optional<TestResult> byUser = testResultRepository.findByUser(resultPanel.getUser());
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

    private void generatePdf(Sertificat dto, String filePath) {
        try {
            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();

            Font titleFont = new Font(Font.HELVETICA, 18, Font.BOLD);
            document.add(new Paragraph("Student Result Report", titleFont));
            document.add(new Paragraph(" "));

            Font font = new Font(Font.HELVETICA, 12, Font.NORMAL);
            document.add(new Paragraph("Photo ID: " + dto.getPhotoId(), font));
            document.add(new Paragraph("Category: " + dto.getCategoryName(), font));
            document.add(new Paragraph("First Name: " + dto.getFirstName(), font));
            document.add(new Paragraph("Last Name: " + dto.getLastName(), font));
            document.add(new Paragraph("Created Date: " + dto.getCreatedDate(), font));
            document.add(new Paragraph("Amaliy Score: " + dto.getAmaliyscore(), font));
            document.add(new Paragraph("Nazariy Score: " + dto.getNazariyscore(), font));
            document.add(new Paragraph("Overall Score: " + dto.getOverallScore(), font));

            document.close();
        } catch (Exception e) {
            throw new RuntimeException("PDF yaratishda xatolik!", e);
        }
    }


    public ApiResponse controlPanel(User user){
        if (user.getRole().equals(Role.ROLE_ADMIN)) {
            long category = categoryRepository.count();
            long test = testRepository.count();
            long users = userRepository.countByRole(Role.ROLE_USER);
            long answers = testResultRepository.count();
            PanelDTO panelDTO = PanelDTO.builder()
                    .categoryCount(category != 0 ? category : 0L)
                    .testCount(test != 0 ? test : 0L)
                    .answerCount(answers != 0 ? answers : 0L)
                    .userCount(users != 0 ? users : 0L)
                    .weeklyNews(null)
                    .build();
            return new ApiResponse("All ", HttpStatus.OK, true, panelDTO);
        }
        return new ApiResponse("Ruxsat berilmagan", HttpStatus.BAD_REQUEST, false, null);
    }
}

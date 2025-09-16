package geadezest.service;
import geadezest.entity.*;
import geadezest.entity.enums.Question_type;
import geadezest.entity.enums.UserResults;
import geadezest.payload.*;
import geadezest.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class WorkingTest {

    private final TestRepository testRepository;
    private final OptionRepository optionRepository;
    private final UserAnswerRepository userAnswerRepository;
    private final TestResultRepository testResultRepository;
    private final CategoryRepository  categoryRepository;
    private final ResultPanelRepository resultPanelRepository;

    public ApiResponse test(Integer categoryId) {
        List<Test> allByCategoryId = testRepository.findAllByCategory_Id(categoryId);
        if (allByCategoryId.isEmpty()) {
            return new ApiResponse(
                    "Ushbu categoryda savollar mavjud emas",
                    HttpStatus.NOT_FOUND,
                    false,
                    null
            );
        }

        List<TestWorking> testWorkings = new ArrayList<>();


        for (Test test : allByCategoryId) {
            if (test.getQuestion_type().equals(Question_type.HISOBLANGAN_NATIJA)){
                TestWorking testWorking = new TestWorking();
                testWorking.setTestId(test.getId());
                testWorking.setQuestion(test.getQuestion());
                testWorkings.add(testWorking);
            }else {
                TestWorking testWorking = new TestWorking();
                testWorking.setTestId(test.getId());
                testWorking.setQuestion(test.getQuestion());
                List<OptionTest> optionTests = new ArrayList<>();
                for (Option option : test.getOptions()) {
                    OptionTest optionTest = new OptionTest();
                    optionTest.setOptionId(option.getId());
                    optionTest.setAnswer(option.getText());
                    optionTests.add(optionTest);
                }
                testWorking.setOption(optionTests);
                testWorkings.add(testWorking);
            }
        }
        return new ApiResponse("Testlar", HttpStatus.OK, true,
                testWorkings);
    }

    public ApiResponse submitAnswer(User user, Integer testId, List<Integer> optionsId, String answer) {

        Test test = testRepository.findById(testId)
                .orElseThrow(() -> new RuntimeException("Test topilmadi"));

        boolean alreadyAnswered = userAnswerRepository.existsByUserIdAndTest(user.getId(), test);
        if (alreadyAnswered) {
            return new ApiResponse(
                    "Siz bu savolga allaqachon javob berdingiz",
                    HttpStatus.BAD_REQUEST,
                    false,
                    null
            );
        }

        Map<String, Object> responseData = new HashMap<>();
        responseData.put("testId", test.getId());

        boolean isCorrect;
        UserAnswer userAnswer = new UserAnswer();
        userAnswer.setUserId(user.getId());
        userAnswer.setTest(test);
        userAnswer.setAnsweredAt(LocalDateTime.now());

        if (test.getQuestion_type().equals(Question_type.HISOBLANGAN_NATIJA)) {
            if (answer == null || test.getOptions() == null || test.getOptions().isEmpty()) {
                return new ApiResponse(
                        "Hisoblangan natija uchun javob mavjud emas",
                        HttpStatus.BAD_REQUEST,
                        false,
                        null
                );
            }

            String correctAnswer = test.getOptions().get(0).getText();
            isCorrect = answer.equals(correctAnswer);

            userAnswer.setScore(isCorrect ? 1.0 : 0.0);
            userAnswer.setCorrect(isCorrect);
            userAnswerRepository.save(userAnswer);

            responseData.put("yourAnswer", answer);
            responseData.put("correctAnswer", correctAnswer);
            responseData.put("isCorrect", isCorrect);

        } else {
            if (optionsId == null || optionsId.isEmpty()) {
                return new ApiResponse(
                        "Hech qanday option tanlanmadi",
                        HttpStatus.BAD_REQUEST,
                        false,
                        null
                );
            }

            List<Option> selectedOptions = optionRepository.findAllById(optionsId);
            if (selectedOptions.isEmpty()) {
                return new ApiResponse(
                        "Tanlangan optionlar topilmadi",
                        HttpStatus.BAD_REQUEST,
                        false,
                        null
                );
            }

            List<Option> correctOptions = test.getOptions().stream()
                    .filter(Option::isCorrect)
                    .toList();

            isCorrect = selectedOptions.containsAll(correctOptions) && correctOptions.containsAll(selectedOptions);

            userAnswer.setSelectedOptions(selectedOptions);
            userAnswer.setCorrect(isCorrect);
            userAnswer.setScore(isCorrect ? 1.0 : 0.0);
            userAnswerRepository.save(userAnswer);
            responseData.put("yourAnswer", selectedOptions.stream().map(Option::getText).toList());
            responseData.put("correctAnswers", correctOptions.stream().map(Option::getText).toList());
            responseData.put("isCorrect", isCorrect);
        }



        return new ApiResponse(
                isCorrect ? "To‘g‘ri javob" : "Noto‘g‘ri javob",
                HttpStatus.OK,
                true,
                responseData
        );
    }



    public ApiResponse finishTest(Integer categoryId, User user) {

        int totalQuestionsInCategory = testRepository.countByCategory_Id(categoryId);

        List<UserAnswer> answers = userAnswerRepository.
                findAllByUserIdAndTest_CategoryId(user.getId(), categoryId);
        int answeredQuestions = (int) answers.stream()
                .map(userAnswer -> userAnswer.getTest().getId())
                .distinct()
                .count();

        if (answeredQuestions < totalQuestionsInCategory) {
            return new ApiResponse(
                    "Testni tugatib bulmaydi barcha savollarga javob berilmagan",
                    HttpStatus.BAD_REQUEST, false, null);
        }

        Optional<Category> byId = categoryRepository.findById(categoryId);
        Category category = byId.get();
        ResultPanel resultPanel = new ResultPanel();
        resultPanel.setUser(user);
        resultPanel.setNextTestDuration(category.getNextTestTimeDuration());
        resultPanel.setUserResults(UserResults.KUTILMOQDA);
        resultPanel.setCategoryName(category.getName());
        resultPanelRepository.save(resultPanel);

        int correctAnswers = (int) answers.stream()
                .filter(UserAnswer::isCorrect)
                .map(userAnswer -> userAnswer.getTest().getId())
                .distinct()
                .count();


        double score = (double) correctAnswers / totalQuestionsInCategory * 100;

        TestResult result = new TestResult();
        result.setUser(user);
        result.setCategoryId(categoryId);
        result.setTotalQuestion(totalQuestionsInCategory);
        result.setCorrectAnswers(correctAnswers);
        result.setScore(score);
        result.setFinishedAt(LocalDateTime.now());
         testResultRepository.save(result);
         return new ApiResponse("results",HttpStatus.OK,true,result);
    }

}



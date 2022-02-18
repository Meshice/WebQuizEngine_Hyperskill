package engine.service;

import engine.persistance.QuizRepository;
import engine.persistance.SolvedQuestionRepository;
import engine.persistance.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class QuizService {

    @Autowired
    QuizRepository quizRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    SolvedQuestionRepository solvedQuestionRepository;

    public Question addQuiz(Question question, UserDetails details) {
        question.setOwner(details.getUsername());
        quizRepository.save(question);
        return question;
    }

    public Question getQuiz(int id) {
        Optional<Question> question = quizRepository.findById(id);
        if (question.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Quiz with this ID not found");
        }
        return question.get();
    }

    public Page<Question> getAllQuiz(int page) {
        PageRequest pageable = PageRequest.of(page, 10);
        return quizRepository.findAll(pageable);
    }

    public Page<SolvedQuestion> getCompletedQuiz(int page, UserDetails userDetails) {
        String username = userDetails.getUsername();
        PageRequest pageable = PageRequest.of(page, 10, Sort.by("completedAt").descending());
        return solvedQuestionRepository.findAllByOwner(username, pageable);
    }



    public Feedback solveQuiz(int id, Answer answer, UserDetails userDetails) {
        Question question = getQuiz(id);
        List<Integer> answers = answer.getAnswer();
        boolean correct = Set.copyOf(question.getAnswer()).equals(Set.copyOf(answers));
        if (correct) {
            SolvedQuestion solvedQuestion = new SolvedQuestion(userDetails.getUsername(), id);
            solvedQuestionRepository.save(solvedQuestion);
        }
        return getFeedback(correct);
    }

    public void deleteQuiz(int id, UserDetails details) {
        Optional<Question> questionOptional = quizRepository.findById(id);
        Question question = questionOptional.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (!question.getOwner().equals(details.getUsername())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        quizRepository.deleteById(id);
    }

    private Feedback getFeedback(boolean correct) {
        Feedback feedback = new Feedback();
        if (correct) {
            feedback.setSuccess(true);
            feedback.setFeedback("Congratulations, you're right!");
        } else {
            feedback.setSuccess(false);
            feedback.setFeedback("Wrong answer! Please, try again.");
        }
        return feedback;
    }

}

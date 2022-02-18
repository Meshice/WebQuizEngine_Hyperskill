package engine.controller;

import engine.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
public class QuizController {

    @Autowired
    UserService userService;


    @Autowired
    QuizService quizService;


    @GetMapping("/quizzes/{id}")
    public Question getQuiz(@PathVariable(value = "id", required = false) Integer id) {
        return quizService.getQuiz(id);
    }

    @GetMapping("/quizzes")
    public Page<Question> getQuizzes(@RequestParam(value = "page",defaultValue = "0") int page) {
        return quizService.getAllQuiz(page);
    }

    @GetMapping("/quizzes/completed")
    public Page<SolvedQuestion> getCompletedQuiz(@AuthenticationPrincipal UserDetails userDetails, @RequestParam(value = "page",defaultValue = "0") int page) {
        return quizService.getCompletedQuiz(page,userDetails);
    }

    @PostMapping("/quizzes/{id}/solve")
    public Feedback solveQuiz(@RequestBody Answer answer, @PathVariable("id") int id, @AuthenticationPrincipal UserDetails userDetails) {
        return quizService.solveQuiz(id, answer, userDetails);
    }

    @PostMapping("/quizzes")
    public Question addQuiz(@RequestBody @Valid Question question, @AuthenticationPrincipal UserDetails userDetails) {
        return quizService.addQuiz(question, userDetails);
    }

    @DeleteMapping("/quizzes/{id}")
    public ResponseEntity deleteQuiz(@PathVariable("id") int id, @AuthenticationPrincipal UserDetails userDetails) {
        quizService.deleteQuiz(id, userDetails);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody @Valid UserSecurity userSecurity) {
        userService.addUser(userSecurity);
        return ResponseEntity.ok().build();
    }

}

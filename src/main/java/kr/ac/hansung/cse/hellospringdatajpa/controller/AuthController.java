package kr.ac.hansung.cse.hellospringdatajpa.controller;

import kr.ac.hansung.cse.hellospringdatajpa.entity.User;
import kr.ac.hansung.cse.hellospringdatajpa.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.validation.Valid;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    // 로그인 페이지
    @GetMapping("/login")
    public String loginPage(@RequestParam(value = "error", required = false) String error,
        @RequestParam(value = "logout", required = false) String logout,
        Model model) {

        if (error != null) {
            model.addAttribute("errorMessage", "이메일 또는 비밀번호가 올바르지 않습니다.");
        }

        if (logout != null) {
            model.addAttribute("logoutMessage", "성공적으로 로그아웃되었습니다.");
        }

        return "auth/login";
    }

    // 회원가입 페이지
    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("user", new User());
        return "auth/register";
    }

    // 회원가입 처리
    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("user") User user,
        BindingResult bindingResult,
        Model model) {

        // 유효성 검사 오류가 있는 경우
        if (bindingResult.hasErrors()) {
            return "auth/register";
        }

        // 이메일 중복 체크
        if (userService.emailExists(user.getEmail())) {
            model.addAttribute("errorMessage", "이미 존재하는 이메일입니다.");
            return "auth/register";
        }

        try {
            // 사용자 저장
            userService.save(user);
            model.addAttribute("successMessage", "회원가입이 완료되었습니다. 로그인해주세요.");
            return "auth/login";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "회원가입 중 오류가 발생했습니다.");
            return "auth/register";
        }
    }
}
package kr.ac.hansung.cse.hellospringdatajpa.controller;

import kr.ac.hansung.cse.hellospringdatajpa.entity.User;
import kr.ac.hansung.cse.hellospringdatajpa.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')") // 컨트롤러 전체에 관리자 권한 필요
public class AdminController {

    @Autowired
    private UserService userService;

    // 관리자 대시보드
    @GetMapping({"", "/"})
    public String adminDashboard(Model model) {
        List<User> users = userService.findAll();

        // 통계 정보
        long totalUsers = users.size();
        long adminUsers = users.stream()
            .filter(user -> user.hasRole("ROLE_ADMIN"))
            .count();
        long regularUsers = totalUsers - adminUsers;

        model.addAttribute("totalUsers", totalUsers);
        model.addAttribute("adminUsers", adminUsers);
        model.addAttribute("regularUsers", regularUsers);

        return "admin/dashboard";
    }

    // 사용자 목록 관리
    @GetMapping("/users")
    public String manageUsers(Model model) {
        List<User> users = userService.findAll();
        model.addAttribute("users", users);
        return "admin/users";
    }

    // 관리자 권한 부여
    @PostMapping("/users/{email}/grant-admin")
    public String grantAdminRole(@PathVariable String email, RedirectAttributes redirectAttributes) {
        try {
            User user = userService.findByEmail(email);
            if (user != null) {
                if (user.hasRole("ROLE_ADMIN")) {
                    redirectAttributes.addFlashAttribute("warningMessage",
                        "사용자 " + email + "은 이미 관리자 권한을 가지고 있습니다.");
                } else {
                    userService.grantAdminRole(email);
                    redirectAttributes.addFlashAttribute("successMessage",
                        "사용자 " + email + "에게 관리자 권한을 부여했습니다.");
                }
            } else {
                redirectAttributes.addFlashAttribute("errorMessage",
                    "사용자를 찾을 수 없습니다.");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage",
                "권한 부여 중 오류가 발생했습니다: " + e.getMessage());
        }

        return "redirect:/admin/users";
    }
}
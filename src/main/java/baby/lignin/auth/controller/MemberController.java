package baby.lignin.auth.controller;


import baby.lignin.auth.model.Token;
import baby.lignin.auth.repository.MemberRepository;
import baby.lignin.auth.service.MemberService;

import baby.lignin.auth.util.ApiResponse;
import baby.lignin.auth.util.ApiResponseGenerator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.util.*;

@Tag(name = "Login API", description = "로그인/인증")
@RequiredArgsConstructor
@Controller
@RequestMapping("/api/member")
public class MemberController {

    private final MemberService memberService;

    @Operation(summary = "kakao 로그인 요청", description = "로그인 요청")
    @Parameter(name = "request", description = "카카오 인증 서버로부터 발급된 인가 코드")
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ApiResponse<ApiResponse.SuccessBody<Token>> kakaoLogin(@RequestParam(value = "code", required = false) String code) {
        return ApiResponseGenerator.success(memberService.getToken(code), HttpStatus.OK);
    }



    @RequestMapping(value="/logout")
    public String logout(HttpSession session) throws IOException {

        System.out.println("TokenLogout" + (String)session.getAttribute("access_token"));
        memberService.logout((String)session.getAttribute("access_token"));
        session.invalidate();;
        return "redirect:/";
    }



    @RequestMapping(value="/unlink")
    public String access(HttpSession session) throws IOException {

        System.out.println("TokenUnlink" + (String)session.getAttribute("access_token"));
        memberService.unlink((String)session.getAttribute("access_token"));
        session.invalidate();
        return "redired:/";
    }



}

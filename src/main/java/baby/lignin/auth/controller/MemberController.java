package baby.lignin.auth.controller;


import baby.lignin.auth.model.Token;
import baby.lignin.auth.model.response.MemberResponse;
import baby.lignin.auth.service.MemberService;

import baby.lignin.auth.util.ApiResponse;
import baby.lignin.auth.util.ApiResponseGenerator;
import baby.lignin.auth.util.MessageCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
    private final HttpSession session;
    @Operation(summary = "kakao 로그인 요청", description = "로그인 요청")
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ApiResponse<ApiResponse.SuccessBody<Token>> kakaoLogin(@RequestParam(value = "code", required = false) String code) {
        Token tk = memberService.getToken(code);
        session.setAttribute("access_token",tk.getAccessToken());
        return ApiResponseGenerator.success(tk, HttpStatus.OK);
    }

    @Operation(summary = "사용자 유저 정보")
    @GetMapping(value = "/info")
    public ApiResponse<ApiResponse.SuccessBody<MemberResponse>> getUserInfo(@RequestHeader("Authorization") String token) throws Exception {
        return ApiResponseGenerator.success(memberService.getUserInfo(token),HttpStatus.OK, MessageCode.SUCCESS);
    }


    @Operation(summary = "로그아웃")
    @RequestMapping(value="/logout", method= RequestMethod.GET)
    public ApiResponse<ApiResponse.SuccessBody<Void>> logout(@RequestHeader("Authorization") String token) {
        memberService.logout(token);
        System.out.println("왜 실패지? " + MessageCode.SUCCESS);
        return ApiResponseGenerator.success(HttpStatus.OK, MessageCode.SUCCESS);
    }



    @Operation(summary = "사용자 탈퇴")
    @RequestMapping(value="/unlink", method= RequestMethod.GET)
    public ApiResponse<ApiResponse.SuccessBody<Void>> access(@RequestHeader("Authorization") String token) {
        memberService.unlink(token);
        return ApiResponseGenerator.success(HttpStatus.OK, MessageCode.RESOURCE_DELETED);
    }




}

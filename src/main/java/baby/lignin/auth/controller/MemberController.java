package baby.lignin.auth.controller;


import baby.lignin.auth.model.Token;
import baby.lignin.auth.service.MemberService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.util.*;

@RequiredArgsConstructor
@Controller
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    @RequestMapping(value = "/kakao", method = RequestMethod.GET)
    public String kakaoLogin(@RequestParam(value = "code", required = false) String code, HttpSession session) {
        // authorizedCode: 카카오 서버로부터 받은 인가 코드
        System.out.println("AccessCode "+code);
        Token tk= memberService.getAccessToken(code);
        String accessToken = tk.getAccessToken();
        String refreshToken = tk.getRefreshToken();

        System.out.println("AccessToken : "+ accessToken);
        System.out.println("refreshToken : "+ refreshToken);
       // userService.kakaoLogin(code);

        HashMap<String, Object> userInfo = memberService.getUserInfo(tk.getAccessToken());
        System.out.println("###nickname#### : " + userInfo.get("nickname"));
        System.out.println("###profile_image : " + userInfo.get("profile_image"));
        System.out.println("###email#### : " + userInfo.get("email"));
        session.setAttribute("access_token", accessToken);
        return "redirect:/";
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

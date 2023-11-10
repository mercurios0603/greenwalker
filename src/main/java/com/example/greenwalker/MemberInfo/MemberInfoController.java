package com.example.greenwalker.MemberInfo;

import com.example.greenwalker.DataNotFoundException;
import com.example.greenwalker.Member.Member;
import com.example.greenwalker.Member.MemberRepository;
import com.example.greenwalker.Member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.Optional;

@RequiredArgsConstructor
@Controller
@RequestMapping("/memberinfo")
public class MemberInfoController {

  private final MemberRepository memberRepository;
  private final MemberService memberService;

  @GetMapping("")
  public String memberinfo(Model model, Principal principal) {

    Member member = memberService.getMember(principal.getName());
    model.addAttribute("member", member);

    return "member_info";
  }
}

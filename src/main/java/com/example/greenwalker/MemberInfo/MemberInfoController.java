package com.example.greenwalker.MemberInfo;

import com.example.greenwalker.DataNotFoundException;
import com.example.greenwalker.Member.Member;
import com.example.greenwalker.Member.MemberRepository;
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

  @GetMapping("")
  public String memberinfo(Model model, Principal principal) {
    String membername = principal.getName();
    Optional<Member> memberOptional = memberRepository.findByusername(membername);

    if (memberOptional.isPresent()) {
      Member member = memberOptional.get();
      model.addAttribute("member", member);
    } else {
      throw new DataNotFoundException("siteuser not found");
    }
    return "member_info";
  }
}

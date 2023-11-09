package com.example.greenwalker.Target;

import com.example.greenwalker.Member.Member;
import com.example.greenwalker.Member.MemberService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Controller
@RequestMapping("/target")
public class TargetController {

  private final TargetService targetService;
  private final MemberService memberService;

  @GetMapping("/regist")
  public String targetRegist(TargetCreateForm targetCreateForm) {
    return "target_regist";
  }

  @PostMapping("/regist")
  public String targetRegist(@Valid TargetCreateForm targetCreateForm, BindingResult bindingResult, Principal principal) {
      if (bindingResult.hasErrors()) {
        return "target_regist";
      }
      Member member = this.memberService.getMember(principal.getName());
      this.targetService.createTarget(targetCreateForm.getLocationcategory(), targetCreateForm.getLocationname( )
              , targetCreateForm.getLocationaddress(), Double.parseDouble(targetCreateForm.getLocationlat()), Double.parseDouble(targetCreateForm.getLocationlng()), member);
    return "redirect:/target/regist";
  }



  @GetMapping("/search")
  public String targetSearch(Model model) {

    // 현재 활성화할 탭을 설정
    String activeTab = "pills-home"; // 검색 탭 기본 활성화
    model.addAttribute("activeTab", activeTab); //

    return "search";
  }

  @PostMapping("/search")
  public String targetSearch(Model model, @RequestParam Map<String, String> requestParams) {

    String keyword = requestParams.get("keyword");
    System.out.print(keyword);

    // 현재 활성화할 탭을 설정
    String activeTab = "pills-profile"; // 모험 탭 유지
    model.addAttribute("activeTab", activeTab); //

    String pname1 = requestParams.get("pname1");
    String paddress1 = requestParams.get("paddress1");
    Double latclick1 = Double.parseDouble(requestParams.get("latclick1"));
    Double lngclick1 = Double.parseDouble(requestParams.get("lngclick1"));

    // 입력값 유지용 (너무 조잡하다)
    model.addAttribute("keyword", keyword);

    model.addAttribute("pname1", pname1);
    model.addAttribute("paddress1", paddress1);
    model.addAttribute("latclick1", latclick1);
    model.addAttribute("lngclick1", lngclick1);

    List<Target> searchresult = this.targetService.searchTarget(latclick1, lngclick1);

    model.addAttribute("searchresult", searchresult);

    return "/search";
  }
}

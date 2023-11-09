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
  public String targetSearch(Model model, TargetSearchForm targetsearchForm) {

    // 현재 활성화할 탭을 설정
    String activeTab = "pills-home"; // 검색 탭 기본 활성화
    model.addAttribute("activeTab", activeTab); //

    return "search";
  }

  @PostMapping("/search")
  public String targetSearch(Model model, @RequestParam Map<String, String> requestParams,
                             @Valid TargetSearchForm targetsearchForm, BindingResult bindingResult) {

    String activeTab = "pills-profile"; // 모험 탭 유지

    if (bindingResult.hasErrors()) {
      model.addAttribute("activeTab", activeTab); // 현재 활성화 탭을 설정
      return "search";
    }

    model.addAttribute("activeTab", activeTab); // 현재 활성화 탭을 설정

    Double targetLat = Double.valueOf(targetsearchForm.getLatclick1());
    Double targetLng = Double.valueOf(targetsearchForm.getLngclick1());

    List<Target> searchresult = this.targetService.searchTarget(targetLat, targetLng);

    model.addAttribute("searchresult", searchresult);

    return "/search";
  }
}

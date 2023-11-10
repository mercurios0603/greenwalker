package com.example.greenwalker.Target;

import com.example.greenwalker.DataNotFoundException;
import com.example.greenwalker.Member.Member;
import com.example.greenwalker.Member.MemberRepository;
import com.example.greenwalker.Member.MemberService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import org.springframework.ui.Model;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Controller
@RequestMapping("/target")
public class TargetController {

  private final TargetService targetService;
  private final MemberService memberService;

  @GetMapping("/favorite")
  public String targetFavorite(Model model, TargetCreateForm targetCreateForm, Principal principal) {

    Member member = memberService.getMember(principal.getName());
    model.addAttribute("member", member);

    return "favorite";
  }

  @GetMapping("/regist")
  public String targetRegist(Model model, TargetCreateForm targetCreateForm, Principal principal) {

    Member member = memberService.getMember(principal.getName());
    model.addAttribute("member", member);

    return "location_regist";
  }

  @PostMapping("/regist")
  public String targetRegist(@Valid TargetCreateForm targetCreateForm, BindingResult bindingResult, Principal principal) {

    Member member = this.memberService.getMember(principal.getName());

    if (bindingResult.hasErrors()) {
      return "location_regist";
    }

    this.targetService.createTarget(targetCreateForm.getLocationcategory(), targetCreateForm.getLocationname( )
            , targetCreateForm.getLocationaddress(), Double.parseDouble(targetCreateForm.getLocationlat()), Double.parseDouble(targetCreateForm.getLocationlng()), member);

    return "redirect:/target/favorite";
  }

  @GetMapping("/modify/{id}")
  public String targetModify(TargetCreateForm targetCreateForm, @PathVariable("id") Long id) {

      Target target = this.targetService.getTarget(id);
      targetCreateForm.setLocationname(target.getLocationName());
      targetCreateForm.setLocationaddress(target.getLocationAddress());
      targetCreateForm.setLocationlat(String.valueOf(target.getLocationLat()));
      targetCreateForm.setLocationlng(String.valueOf(target.getLocationLng()));
      targetCreateForm.setLocationcategory(target.getLocationCategory());

      return "location_regist";
  };

  @PostMapping("/modify/{id}")
  public String targetModify(@Valid TargetCreateForm targetCreateForm, BindingResult bindingResult,
                             @PathVariable("id") Long id) {

    Target target = this.targetService.getTarget(id);

    if (bindingResult.hasErrors()) {
      return "location_regist";
    }

    this.targetService.modifyTarget(target, targetCreateForm.getLocationcategory(), targetCreateForm.getLocationname( )
            , targetCreateForm.getLocationaddress(), targetCreateForm.getLocationlat(), targetCreateForm.getLocationlng());

    return "redirect:/target/favorite";
  }


  @GetMapping("/delete/{id}")
  public String targetDelete(@PathVariable("id") Long id) {
    Target target = this.targetService.getTarget(id);;
    this.targetService.deleteTarget(target);
    return "redirect:/target/favorite";
  };

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

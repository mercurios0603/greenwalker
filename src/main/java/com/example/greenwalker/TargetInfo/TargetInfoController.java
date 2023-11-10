package com.example.greenwalker.TargetInfo;

import com.example.greenwalker.Member.Member;
import com.example.greenwalker.Member.MemberService;
import com.example.greenwalker.Target.TargetCreateForm;
import com.example.greenwalker.Target.TargetRepository;

import com.example.greenwalker.Target.TargetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.Optional;

@RequiredArgsConstructor
@Controller
@RequestMapping("/targetinfo")
public class TargetInfoController {


  final private MemberService memberService;
  final private TargetService targetService;

  @GetMapping("/{id}")
  //해당 URL에 있는 {id}가 @PathVariable로 가져와져서 Integer id에 들어가고,
  // 이 id가 다시 articleService의 getArticle 메서드의 매개변수로 들어간다.
  public String detail(Model model, @PathVariable("id") Integer id) {

    // 해당 id로 검색하여 Target 테이블의 정보도 가져오고 (OneToOne 연결)
    // 마찬가지로 해당 id로 검색하여 TargetInfo 자체의 테이블 정보도 가져와서 내보내야 함.
    // 나중에 타임리프에서 표현할때는 이런식으로 구현되어야함.
    // <td><span th:if="${question.author != null}" th:text="${question.author.username}"></span></td>
    // 여기의 경우에는... targetinfo.target.locationCategory 이런식이겠네. 요걸 구현하기 위해 콘트롤러와 서비스에서 해야되는일 확인
    // {id} 의 경로가 없을 경우에는 돌려보내면 안된다. (아마 콘트롤러 단에서 권한 처리 Authorize 하면 해결될걸?)
    // -> 지금 다 되고 있음, get&Post 단에서 처리? 또는 타임리프 문법으로 해당 자료가 없어야 한다던가.)

    return "target_info";
  }

  @GetMapping("/regist")
  public String targetRegist(Model model, TargetCreateForm targetCreateForm, Principal principal) {

    Member member = memberService.getMember(principal.getName());
    model.addAttribute("member", member);

    return "target_regist";
  }

  @PostMapping("/regist")
  public String targetRegist(@Valid TargetCreateForm targetCreateForm, BindingResult bindingResult, Principal principal) {

    Member member = this.memberService.getMember(principal.getName());

    if (bindingResult.hasErrors()) {
      return "target_regist";
    }

    this.targetService.createTarget(targetCreateForm.getLocationcategory(), targetCreateForm.getLocationname( )
            , targetCreateForm.getLocationaddress(), Double.parseDouble(targetCreateForm.getLocationlat()), Double.parseDouble(targetCreateForm.getLocationlng()), member);

    return "redirect:/targetinfo/regist";
  }

}

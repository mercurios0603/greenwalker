package com.example.greenwalker;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MainController {
  @GetMapping("/main")
  public String main() {
    return "main";
  }

  @GetMapping("/guide")
  public String guiden() {
    return "guide";
  }

  // redirect와 forward 두 종류가 있다.
  @GetMapping("/")
  public String root() {
    return "redirect:/main";
  }

  @GetMapping("/search")
  public String search() {
    return "search";
  }
}

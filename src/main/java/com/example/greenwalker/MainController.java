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

  // redirect와 forward 두 종류가 있다.
  @GetMapping("/")
  public String root() {
    return "redirect:/main";
  }
}

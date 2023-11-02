package com.example.greenwalker.Search;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SearchController {

  @GetMapping("/main/search")
  public String destination() {

    return "redirect:/main";
  }
}

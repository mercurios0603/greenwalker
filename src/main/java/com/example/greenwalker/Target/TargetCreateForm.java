package com.example.greenwalker.Target;

import jakarta.validation.constraints.NotEmpty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TargetCreateForm {

  @NotEmpty(message = "장소 카테고리를 선택해주세요")
  private String locationcategory;

  @NotEmpty(message = "장소 선택은 필수사항입니다")
  private String locationname;

  private String locationaddress;

  private String locationlat;

  private String locationlng;
}

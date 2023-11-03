package com.example.greenwalker.Target;

import jakarta.validation.constraints.NotEmpty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TargetCreateForm {

  @NotEmpty(message = "장소 카테고리를 선택해주세요")
  private String locationcategory;

  @NotEmpty(message = "장소이름은 필수사항입니다")
  private String locationname;

  @NotEmpty(message = "주소는 필수사항입니다")
  private String locationaddress;

  @NotEmpty(message = "경도는 필수사항입니다")
  private String locationlat;

  @NotEmpty(message = "위도는 필수사항입니다")
  private String locationlng;
}

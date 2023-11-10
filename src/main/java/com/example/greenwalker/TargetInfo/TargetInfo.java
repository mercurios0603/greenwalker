package com.example.greenwalker.TargetInfo;

import com.example.greenwalker.Target.Target;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class TargetInfo {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String targetTitle;

  private String targetContent;

  private String missionCategory;

  @OneToOne
  private Target target;
}

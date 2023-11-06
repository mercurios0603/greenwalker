package com.example.greenwalker.TargetStatus;

import com.example.greenwalker.Target.Target;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
@Entity
public class TargetStatus {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private Integer recoveryHealth;

  private Integer recoveryMana;

  private Integer toStr;

  private Integer toAgi;

  private Integer toInt;

  @OneToOne
  private Target target;
}

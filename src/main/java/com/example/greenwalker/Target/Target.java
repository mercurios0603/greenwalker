package com.example.greenwalker.Target;

import com.example.greenwalker.Member.Member;

import com.example.greenwalker.TargetInfo.TargetInfo;
import com.example.greenwalker.TargetStatus.TargetStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Target {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String locationCategory;

  private String locationName;

  private String locationAddress;

  private Double locationLat;

  private Double locationLng;

  @ManyToOne
  private Member member;

  @OneToOne(mappedBy = "target", cascade = CascadeType.REMOVE)
  private TargetInfo targetinfo;

  @OneToOne(mappedBy = "target", cascade = CascadeType.REMOVE)
  private TargetStatus targetstatus;
}

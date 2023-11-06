package com.example.greenwalker.MemberInfo;

import com.example.greenwalker.Member.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class MemberInfo {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String introduce;

  @OneToOne
  private Member member;
}

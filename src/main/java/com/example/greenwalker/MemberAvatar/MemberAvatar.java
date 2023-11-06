package com.example.greenwalker.MemberAvatar;

import com.example.greenwalker.Member.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class MemberAvatar {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique= true)
  private String avatarname;

  private Integer avatarhealth;

  private Integer avatarmana;

  private Integer avatarstr;

  private Integer avataragi;

  private Integer avatarint;

  private Integer avatarluc;

  @OneToOne
  private Member member;
}

package com.example.greenwalker.Member;

import java.util.List;

import com.example.greenwalker.MemberAvatar.MemberAvatar;
import com.example.greenwalker.MemberInfo.MemberInfo;
import com.example.greenwalker.Target.Target;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Member {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true)
  private String username;

  private String password;

  @Column(unique = true)
  private String email;

  @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE)
  private List<Target> targetlist;

  @OneToOne(mappedBy = "member", cascade = CascadeType.REMOVE)
  private MemberInfo memberinfo;

  @OneToOne(mappedBy = "member", cascade = CascadeType.REMOVE)
  private MemberAvatar memberavatar;
}

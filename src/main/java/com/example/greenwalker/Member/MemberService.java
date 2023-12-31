package com.example.greenwalker.Member;

import com.example.greenwalker.DataNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;


@RequiredArgsConstructor
@Service
public class MemberService {

  private final MemberRepository memberRepository;
  private final PasswordEncoder passwordEncoder;

  public Member create(String username, String email, String password) {
    Member member = new Member();
    member.setUsername(username);
    member.setEmail(email);
    // 패스워드 암호화 실행
    // BCryptPasswordEncoder는 BCrypt 해싱 함수(BCrypt hashing function)를 사용해서 비밀번호를 암호화한다.
    member.setPassword(passwordEncoder.encode(password));
    this.memberRepository.save(member);
    return member;
  }
  public Member getMember(String username) {
    Optional<Member> member = this.memberRepository.findByusername(username);
    if (member.isPresent()) {
      return member.get();
    } else {
      throw new DataNotFoundException("siteuser not found");
    }
  }
}

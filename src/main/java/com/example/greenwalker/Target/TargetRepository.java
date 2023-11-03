package com.example.greenwalker.Target;

import com.example.greenwalker.Member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TargetRepository extends JpaRepository<Target, Long> {
  List<Target> findAll();

}

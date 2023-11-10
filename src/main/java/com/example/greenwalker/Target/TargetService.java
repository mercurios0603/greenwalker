package com.example.greenwalker.Target;

import com.example.greenwalker.DataNotFoundException;
import com.example.greenwalker.Member.Member;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class TargetService {

  private final TargetRepository targetRepository;

  public Target getTarget(Long id) {
    // JpaRepository의 기능을 이용하여 기사번호 id에 맞는 기사를 찾아 가져오는 것임
    Optional<Target> target = this.targetRepository.findById(id);
    if (target.isPresent()) {
      return target.get();
    } else {
      throw new DataNotFoundException("article not found");
    }
  }

  public Target createTarget(String locationcategory, String locationname, String locationaddress, Double locationlat, Double locationlng, Member member) {

    Target target = new Target();

    target.setLocationCategory(locationcategory);
    target.setLocationName(locationname);
    target.setLocationAddress(locationaddress);
    target.setLocationLat(locationlat);
    target.setLocationLng(locationlng);
    target.setMember(member);

    this.targetRepository.save(target);
    return target;

  }

  public void modifyTarget(Target target, String locationcategory, String locationname, String locationaddress,
                           String locationlat, String locationlng) {

    target.setLocationCategory(locationcategory);
    target.setLocationName(locationname);
    target.setLocationAddress(locationaddress);
    target.setLocationLat(Double.valueOf(locationlat));
    target.setLocationLng(Double.valueOf(locationlng));

    this.targetRepository.save(target);
  }

  public void deleteTarget(Target target) {
    this.targetRepository.delete(target);
  }

  public List<Target> searchTarget(Double latclick1, Double lngclick1) {
    List<Target> allTarget = targetRepository.findAll();
    List<Target> nearbyPlaces = new ArrayList<>();

    // 하버사인 공식에 의해 출발지를 기준으로 반경 5km 이내의 장소만 출력 (5km 성인기준 1시간 15분 정도 소요)
    Integer radius = 5;

    for (Target target : allTarget) {
      double distance = calculateDistance(latclick1, lngclick1, target.getLocationLat(), target.getLocationLng());
      if (distance < radius) {
        nearbyPlaces.add(target);
      }
    }
    return nearbyPlaces;
  }

  private double calculateDistance(double aLat, double aLng, double bLat, double bLng) {

    double earthRadius = 6371; // 지구 반지름 (단위: km)
    double dLat = Math.toRadians(bLat - aLat);
    double dLng = Math.toRadians(bLng - aLng);
    double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
            + Math.cos(Math.toRadians(aLat)) * Math.cos(Math.toRadians(bLat))
            * Math.sin(dLng / 2) * Math.sin(dLng / 2);
    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    return earthRadius * c;
  }
}

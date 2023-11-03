package com.example.greenwalker.Target;

import com.example.greenwalker.Member.Member;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class TargetService {

  private final TargetRepository targetRepository;

  public Target createTarget(String locationcategory, String locationname, String locationaddress, Double locationlat, Double locationlng) {

    Target target = new Target();

    target.setLocationCategory(locationcategory);
    target.setLocationName(locationname);
    target.setLocationAddress(locationaddress);
    target.setLocationLat(locationlat);
    target.setLocationLng(locationlng);

    this.targetRepository.save(target);
    return target;

  }

  public List<Target> searchTarget(Double latclick1, Double lngclick1, Integer radius) {
    List<Target> allTarget = targetRepository.findAll();
    List<Target> nearbyPlaces = new ArrayList<>();

    for (Target target : allTarget) {
      double distance = calculateDistance(latclick1, lngclick1, target.getLocationLat(), target.getLocationLng());
      System.out.println(distance); // 하버사인 공식에 의한 직선거리 산출 콘솔 확인용
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

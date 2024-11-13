package com.example.interfaces;

public interface Upgratable {
    String getAcademicLevel();  // returnează nivelul academic bazat pe punctaj
    boolean isEligibleForUpgrade();  // verifică dacă este eligibil pentru upgrade (peste 700)
    String getUpgradeStatus();  // returnează un mesaj despre statusul actual și posibilitatea de upgrade
} 
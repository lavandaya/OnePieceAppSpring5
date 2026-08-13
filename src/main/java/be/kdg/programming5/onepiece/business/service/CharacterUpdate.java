package be.kdg.programming5.onepiece.business.service;

import be.kdg.programming5.onepiece.business.domain.Powertype;

public record CharacterUpdate(String name, Integer age, String appearance,
                              Powertype powertype, Double power,
                              String crewName, String swordName) {
}
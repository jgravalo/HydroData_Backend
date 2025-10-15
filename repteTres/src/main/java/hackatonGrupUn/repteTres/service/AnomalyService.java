package hackatonGrupUn.repteTres.service;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AnomalyService {

    private static final Map<String, Double> districtAverages = new HashMap<>();

    static {
        districtAverages.put("Ciutat Vella", 85.498111);
        districtAverages.put("Eixample", 99.982298);
        districtAverages.put("Gràcia", 100.864000);
        districtAverages.put("Horta-Guinardó", 93.266222);
        districtAverages.put("Les Corts", 109.846500);
        districtAverages.put("Nou Barris", 92.932889);
        districtAverages.put("Sant Andreu", 92.425333);
        districtAverages.put("Sant Martí", 97.436211);
        districtAverages.put("Sants-Montjuïc", 94.757312);
        districtAverages.put("Sarrià-Sant Gervasi", 104.992833);
    }

    public Map<String, Double> getDistrictAverages() {
        return districtAverages;
    }
}
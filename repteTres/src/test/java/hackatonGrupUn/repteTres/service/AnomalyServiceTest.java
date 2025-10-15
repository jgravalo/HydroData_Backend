package hackatonGrupUn.repteTres.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

@DisplayName("AnomalyService Tests")
class AnomalyServiceTest {

    private AnomalyService anomalyService;

    @BeforeEach
    void setUp() {
        anomalyService = new AnomalyService();
    }

    // ========== BASIC FUNCTIONALITY TESTS ==========

    @Test
    @DisplayName("GetDistrictAverages: Should return map with all 10 Barcelona districts")
    void getDistrictAverages_ShouldReturnMapWithAllDistricts() {
        Map<String, Double> result = anomalyService.getDistrictAverages();

        assertThat(result).isNotNull();
        assertThat(result).hasSize(10);
        assertThat(result).containsKeys(
                "Ciutat Vella",
                "Eixample",
                "Gràcia",
                "Horta-Guinardó",
                "Les Corts",
                "Nou Barris",
                "Sant Andreu",
                "Sant Martí",
                "Sants-Montjuïc",
                "Sarrià-Sant Gervasi"
        );
    }

    @Test
    @DisplayName("GetDistrictAverages: Should return non-empty map")
    void getDistrictAverages_ShouldReturnNonEmptyMap() {
        Map<String, Double> result = anomalyService.getDistrictAverages();

        assertThat(result).isNotEmpty();
    }

    @Test
    @DisplayName("GetDistrictAverages: Should return same map instance on multiple calls")
    void getDistrictAverages_ShouldReturnSameMapInstance() {
        Map<String, Double> firstCall = anomalyService.getDistrictAverages();
        Map<String, Double> secondCall = anomalyService.getDistrictAverages();

        assertThat(firstCall).isSameAs(secondCall);
    }

    // ========== SPECIFIC DISTRICT VALUES TESTS ==========

    @Test
    @DisplayName("GetDistrictAverages: Should return correct value for Ciutat Vella")
    void getDistrictAverages_ShouldReturnCorrectValueForCiutatVella() {
        Map<String, Double> result = anomalyService.getDistrictAverages();

        assertThat(result.get("Ciutat Vella")).isCloseTo(85.498111, within(0.000001));
    }

    @Test
    @DisplayName("GetDistrictAverages: Should return correct value for Eixample")
    void getDistrictAverages_ShouldReturnCorrectValueForEixample() {
        Map<String, Double> result = anomalyService.getDistrictAverages();

        assertThat(result.get("Eixample")).isCloseTo(99.982298, within(0.000001));
    }

    @Test
    @DisplayName("GetDistrictAverages: Should return correct value for Gràcia")
    void getDistrictAverages_ShouldReturnCorrectValueForGracia() {
        Map<String, Double> result = anomalyService.getDistrictAverages();

        assertThat(result.get("Gràcia")).isCloseTo(100.864000, within(0.000001));
    }

    @Test
    @DisplayName("GetDistrictAverages: Should return correct value for Horta-Guinardó")
    void getDistrictAverages_ShouldReturnCorrectValueForHortaGuinardo() {
        Map<String, Double> result = anomalyService.getDistrictAverages();

        assertThat(result.get("Horta-Guinardó")).isCloseTo(93.266222, within(0.000001));
    }

    @Test
    @DisplayName("GetDistrictAverages: Should return correct value for Les Corts")
    void getDistrictAverages_ShouldReturnCorrectValueForLesCorts() {
        Map<String, Double> result = anomalyService.getDistrictAverages();

        assertThat(result.get("Les Corts")).isCloseTo(109.846500, within(0.000001));
    }

    @Test
    @DisplayName("GetDistrictAverages: Should return correct value for Nou Barris")
    void getDistrictAverages_ShouldReturnCorrectValueForNouBarris() {
        Map<String, Double> result = anomalyService.getDistrictAverages();

        assertThat(result.get("Nou Barris")).isCloseTo(92.932889, within(0.000001));
    }

    @Test
    @DisplayName("GetDistrictAverages: Should return correct value for Sant Andreu")
    void getDistrictAverages_ShouldReturnCorrectValueForSantAndreu() {
        Map<String, Double> result = anomalyService.getDistrictAverages();

        assertThat(result.get("Sant Andreu")).isCloseTo(92.425333, within(0.000001));
    }

    @Test
    @DisplayName("GetDistrictAverages: Should return correct value for Sant Martí")
    void getDistrictAverages_ShouldReturnCorrectValueForSantMarti() {
        Map<String, Double> result = anomalyService.getDistrictAverages();

        assertThat(result.get("Sant Martí")).isCloseTo(97.436211, within(0.000001));
    }

    @Test
    @DisplayName("GetDistrictAverages: Should return correct value for Sants-Montjuïc")
    void getDistrictAverages_ShouldReturnCorrectValueForSantsMontjuic() {
        Map<String, Double> result = anomalyService.getDistrictAverages();

        assertThat(result.get("Sants-Montjuïc")).isCloseTo(94.757312, within(0.000001));
    }

    @Test
    @DisplayName("GetDistrictAverages: Should return correct value for Sarrià-Sant Gervasi")
    void getDistrictAverages_ShouldReturnCorrectValueForSarriaSantGervasi() {
        Map<String, Double> result = anomalyService.getDistrictAverages();

        assertThat(result.get("Sarrià-Sant Gervasi")).isCloseTo(104.992833, within(0.000001));
    }

    // ========== PARAMETERIZED TESTS ==========

    @ParameterizedTest
    @CsvSource({
            "Ciutat Vella, 85.498111",
            "Eixample, 99.982298",
            "Gràcia, 100.864000",
            "Horta-Guinardó, 93.266222",
            "Les Corts, 109.846500",
            "Nou Barris, 92.932889",
            "Sant Andreu, 92.425333",
            "Sant Martí, 97.436211",
            "Sants-Montjuïc, 94.757312",
            "Sarrià-Sant Gervasi, 104.992833"
    })
    @DisplayName("GetDistrictAverages: Should verify all district values are correct")
    void getDistrictAverages_ShouldVerifyAllDistrictValues(String district, Double expectedValue) {
        Map<String, Double> result = anomalyService.getDistrictAverages();

        assertThat(result.get(district)).isCloseTo(expectedValue, within(0.000001));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "Ciutat Vella",
            "Eixample",
            "Gràcia",
            "Horta-Guinardó",
            "Les Corts",
            "Nou Barris",
            "Sant Andreu",
            "Sant Martí",
            "Sants-Montjuïc",
            "Sarrià-Sant Gervasi"
    })
    @DisplayName("GetDistrictAverages: Should contain all Barcelona districts")
    void getDistrictAverages_ShouldContainAllBarcelonaDistricts(String district) {
        Map<String, Double> result = anomalyService.getDistrictAverages();

        assertThat(result).containsKey(district);
        assertThat(result.get(district)).isNotNull();
        assertThat(result.get(district)).isPositive();
    }

    // ========== VALUE VALIDATION TESTS ==========

    @Test
    @DisplayName("GetDistrictAverages: Should have all values as positive numbers")
    void getDistrictAverages_ShouldHaveAllPositiveValues() {
        Map<String, Double> result = anomalyService.getDistrictAverages();

        assertThat(result.values()).allMatch(value -> value > 0);
    }

    @Test
    @DisplayName("GetDistrictAverages: Should have all values within reasonable range")
    void getDistrictAverages_ShouldHaveValuesInReasonableRange() {
        Map<String, Double> result = anomalyService.getDistrictAverages();

        assertThat(result.values()).allMatch(value -> value >= 80.0 && value <= 120.0);
    }

    @Test
    @DisplayName("GetDistrictAverages: Should have no null values")
    void getDistrictAverages_ShouldHaveNoNullValues() {
        Map<String, Double> result = anomalyService.getDistrictAverages();

        assertThat(result.values()).doesNotContainNull();
    }

    @Test
    @DisplayName("GetDistrictAverages: Should have no null keys")
    void getDistrictAverages_ShouldHaveNoNullKeys() {
        Map<String, Double> result = anomalyService.getDistrictAverages();

        assertThat(result.keySet()).doesNotContainNull();
    }

    @Test
    @DisplayName("GetDistrictAverages: Should have all keys as non-empty strings")
    void getDistrictAverages_ShouldHaveNonEmptyKeys() {
        Map<String, Double> result = anomalyService.getDistrictAverages();

        assertThat(result.keySet()).allMatch(key -> !key.isEmpty());
    }

    // ========== DISTRICT ORDERING AND STATISTICS TESTS ==========

    @Test
    @DisplayName("GetDistrictAverages: Should identify Les Corts as highest average")
    void getDistrictAverages_ShouldIdentifyLesCorsAsHighest() {
        Map<String, Double> result = anomalyService.getDistrictAverages();

        Double maxValue = result.values().stream().max(Double::compare).orElse(0.0);
        assertThat(result.get("Les Corts")).isEqualTo(maxValue);
        assertThat(maxValue).isCloseTo(109.846500, within(0.000001));
    }

    @Test
    @DisplayName("GetDistrictAverages: Should identify Ciutat Vella as lowest average")
    void getDistrictAverages_ShouldIdentifyCiutatVellaAsLowest() {
        Map<String, Double> result = anomalyService.getDistrictAverages();

        Double minValue = result.values().stream().min(Double::compare).orElse(0.0);
        assertThat(result.get("Ciutat Vella")).isEqualTo(minValue);
        assertThat(minValue).isCloseTo(85.498111, within(0.000001));
    }

    @Test
    @DisplayName("GetDistrictAverages: Should calculate correct average across all districts")
    void getDistrictAverages_ShouldCalculateCorrectOverallAverage() {
        Map<String, Double> result = anomalyService.getDistrictAverages();

        Double average = result.values().stream()
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0.0);

        assertThat(average).isCloseTo(97.150077, within(0.1));
    }

    // ========== DISTRICT NAME FORMATTING TESTS ==========

    @Test
    @DisplayName("GetDistrictAverages: Should use correct Catalan characters in district names")
    void getDistrictAverages_ShouldUseCatalanCharacters() {
        Map<String, Double> result = anomalyService.getDistrictAverages();

        assertThat(result).containsKeys("Gràcia", "Horta-Guinardó", "Sants-Montjuïc", "Sarrià-Sant Gervasi");
    }

    @Test
    @DisplayName("GetDistrictAverages: Should use hyphens in compound district names")
    void getDistrictAverages_ShouldUseHyphensInCompoundNames() {
        Map<String, Double> result = anomalyService.getDistrictAverages();

        assertThat(result).containsKeys(
                "Horta-Guinardó",
                "Sants-Montjuïc",
                "Sarrià-Sant Gervasi"
        );
    }

    @Test
    @DisplayName("GetDistrictAverages: Should have exact district name spelling")
    void getDistrictAverages_ShouldHaveExactSpelling() {
        Map<String, Double> result = anomalyService.getDistrictAverages();
        Set<String> districtNames = result.keySet();

        assertThat(districtNames).contains("Sant Martí");
        assertThat(districtNames).contains("Sant Andreu");
        assertThat(districtNames).doesNotContain("San Martí"); // Not Spanish
        assertThat(districtNames).doesNotContain("San Andreu"); // Not Spanish
    }

    // ========== STATIC MAP BEHAVIOR TESTS ==========

    @Test
    @DisplayName("GetDistrictAverages: Should return same data across different service instances")
    void getDistrictAverages_ShouldReturnSameDataAcrossInstances() {
        AnomalyService firstService = new AnomalyService();
        AnomalyService secondService = new AnomalyService();

        Map<String, Double> firstResult = firstService.getDistrictAverages();
        Map<String, Double> secondResult = secondService.getDistrictAverages();

        assertThat(firstResult).isEqualTo(secondResult);
        assertThat(firstResult).isSameAs(secondResult); // Static map
    }

    @Test
    @DisplayName("GetDistrictAverages: Should maintain data consistency across multiple calls")
    void getDistrictAverages_ShouldMaintainDataConsistency() {
        Map<String, Double> firstCall = anomalyService.getDistrictAverages();
        Map<String, Double> secondCall = anomalyService.getDistrictAverages();
        Map<String, Double> thirdCall = anomalyService.getDistrictAverages();

        assertThat(firstCall).isEqualTo(secondCall).isEqualTo(thirdCall);
        assertThat(firstCall.get("Eixample")).isEqualTo(secondCall.get("Eixample"));
        assertThat(secondCall.get("Eixample")).isEqualTo(thirdCall.get("Eixample"));
    }

    // ========== EDGE CASE TESTS ==========

    @Test
    @DisplayName("GetDistrictAverages: Should return null for non-existent district")
    void getDistrictAverages_ShouldReturnNullForNonExistentDistrict() {
        Map<String, Double> result = anomalyService.getDistrictAverages();

        assertThat(result.get("Non-Existent District")).isNull();
        assertThat(result.get("Madrid")).isNull();
        assertThat(result.get("")).isNull();
    }

    @Test
    @DisplayName("GetDistrictAverages: Should be case-sensitive for district names")
    void getDistrictAverages_ShouldBeCaseSensitiveForDistrictNames() {
        Map<String, Double> result = anomalyService.getDistrictAverages();

        assertThat(result.get("Eixample")).isNotNull();
        assertThat(result.get("eixample")).isNull();
        assertThat(result.get("EIXAMPLE")).isNull();
    }

    @Test
    @DisplayName("GetDistrictAverages: Should handle special characters in district lookups")
    void getDistrictAverages_ShouldHandleSpecialCharacters() {
        Map<String, Double> result = anomalyService.getDistrictAverages();

        // Then - These should work with exact special characters
        assertThat(result.get("Gràcia")).isNotNull();
        assertThat(result.get("Gracia")).isNull(); // Without accent
        assertThat(result.get("Horta-Guinardó")).isNotNull();
        assertThat(result.get("Sants-Montjuïc")).isNotNull();
    }

    // ========== MAP IMMUTABILITY TESTS ==========

    @Test
    @DisplayName("GetDistrictAverages: Should return modifiable map")
    void getDistrictAverages_ShouldReturnModifiableMap() {
        Map<String, Double> result = anomalyService.getDistrictAverages();

        assertThat(result).isInstanceOf(HashMap.class);
    }

    @Test
    @DisplayName("GetDistrictAverages: Should verify map type is HashMap")
    void getDistrictAverages_ShouldReturnHashMap() {
        Map<String, Double> result = anomalyService.getDistrictAverages();

        assertThat(result).isInstanceOf(HashMap.class);
    }

    // ========== PRECISION TESTS ==========

    @Test
    @DisplayName("GetDistrictAverages: Should maintain decimal precision for all values")
    void getDistrictAverages_ShouldMaintainDecimalPrecision() {
        Map<String, Double> result = anomalyService.getDistrictAverages();

        assertThat(result.get("Ciutat Vella")).isEqualTo(85.498111);
        assertThat(result.get("Eixample")).isEqualTo(99.982298);
        assertThat(result.get("Nou Barris")).isEqualTo(92.932889);
    }

    @Test
    @DisplayName("GetDistrictAverages: Should have consistent decimal places across values")
    void getDistrictAverages_ShouldHaveConsistentDecimalPlaces() {
        Map<String, Double> result = anomalyService.getDistrictAverages();

        assertThat(result.values()).allMatch(value -> value != Math.floor(value));
    }
}
package hackatonGrupUn.repteTres.controller;

import hackatonGrupUn.repteTres.service.AnomalyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AnomalyController Tests")
class AnomalyControllerTest {

    @Mock
    private AnomalyService anomalyService;

    @InjectMocks
    private AnomalyController anomalyController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(anomalyController).build();
    }

    @Test
    @DisplayName("Should return district averages with HTTP 200 when service returns data")
    void getDistrictAverages_ShouldReturnOkWithData_WhenServiceReturnsAverages() {
        Map<String, Double> expectedAverages = new HashMap<>();
        expectedAverages.put("District1", 45.5);
        expectedAverages.put("District2", 67.8);
        expectedAverages.put("District3", 23.4);

        when(anomalyService.getDistrictAverages()).thenReturn(expectedAverages);

        ResponseEntity<Map<String, Double>> response = anomalyController.getDistrictAverages();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).hasSize(3);
        assertThat(response.getBody()).containsEntry("District1", 45.5);
        assertThat(response.getBody()).containsEntry("District2", 67.8);
        assertThat(response.getBody()).containsEntry("District3", 23.4);

        verify(anomalyService, times(1)).getDistrictAverages();
    }

    @Test
    @DisplayName("Should return empty map with HTTP 200 when no districts exist")
    void getDistrictAverages_ShouldReturnEmptyMap_WhenNoDistrictsExist() {
        Map<String, Double> emptyMap = new HashMap<>();
        when(anomalyService.getDistrictAverages()).thenReturn(emptyMap);

        ResponseEntity<Map<String, Double>> response = anomalyController.getDistrictAverages();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).isEmpty();

        verify(anomalyService, times(1)).getDistrictAverages();
    }

    @Test
    @DisplayName("Should handle service returning single district")
    void getDistrictAverages_ShouldReturnSingleDistrict_WhenOnlyOneDistrictExists() {
        Map<String, Double> singleDistrictMap = new HashMap<>();
        singleDistrictMap.put("OnlyDistrict", 100.0);

        when(anomalyService.getDistrictAverages()).thenReturn(singleDistrictMap);

        ResponseEntity<Map<String, Double>> response = anomalyController.getDistrictAverages();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(1);
        assertThat(response.getBody()).containsEntry("OnlyDistrict", 100.0);

        verify(anomalyService, times(1)).getDistrictAverages();
    }

    @Test
    @DisplayName("Should handle districts with zero averages")
    void getDistrictAverages_ShouldHandleZeroAverages_WhenDistrictsHaveNoActivity() {
        Map<String, Double> zeroAverages = new HashMap<>();
        zeroAverages.put("InactiveDistrict1", 0.0);
        zeroAverages.put("InactiveDistrict2", 0.0);

        when(anomalyService.getDistrictAverages()).thenReturn(zeroAverages);

        ResponseEntity<Map<String, Double>> response = anomalyController.getDistrictAverages();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).containsEntry("InactiveDistrict1", 0.0);
        assertThat(response.getBody()).containsEntry("InactiveDistrict2", 0.0);

        verify(anomalyService, times(1)).getDistrictAverages();
    }

    @Test
    @DisplayName("Should handle districts with negative averages")
    void getDistrictAverages_ShouldHandleNegativeValues_WhenAveragesAreNegative() {
        Map<String, Double> negativeAverages = new HashMap<>();
        negativeAverages.put("District1", -15.5);
        negativeAverages.put("District2", -30.2);

        when(anomalyService.getDistrictAverages()).thenReturn(negativeAverages);

        ResponseEntity<Map<String, Double>> response = anomalyController.getDistrictAverages();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).containsEntry("District1", -15.5);
        assertThat(response.getBody()).containsEntry("District2", -30.2);

        verify(anomalyService, times(1)).getDistrictAverages();
    }

    @Test
    @DisplayName("MockMvc: Should return 200 and JSON with district averages")
    void getDistrictAverages_WithMockMvc_ShouldReturnJsonResponse() throws Exception {
        Map<String, Double> expectedAverages = new HashMap<>();
        expectedAverages.put("District1", 45.5);
        expectedAverages.put("District2", 67.8);

        when(anomalyService.getDistrictAverages()).thenReturn(expectedAverages);

        mockMvc.perform(get("/hydraulic/api/v1/averages"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.District1").value(45.5))
                .andExpect(jsonPath("$.District2").value(67.8));

        verify(anomalyService, times(1)).getDistrictAverages();
    }

    @Test
    @DisplayName("MockMvc: Should return empty JSON object when no districts")
    void getDistrictAverages_WithMockMvc_ShouldReturnEmptyJsonObject() throws Exception {
        when(anomalyService.getDistrictAverages()).thenReturn(new HashMap<>());

        mockMvc.perform(get("/hydraulic/api/v1/averages"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().json("{}"));

        verify(anomalyService, times(1)).getDistrictAverages();
    }

    @Test
    @DisplayName("MockMvc: Should verify correct endpoint mapping")
    void getDistrictAverages_ShouldMapToCorrectEndpoint() throws Exception {
        when(anomalyService.getDistrictAverages()).thenReturn(new HashMap<>());

        mockMvc.perform(get("/hydraulic/api/v1/averages"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/hydraulic/api/v1/average"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should verify service is called exactly once per request")
    void getDistrictAverages_ShouldCallServiceOnce() {
        Map<String, Double> averages = new HashMap<>();
        averages.put("District1", 50.0);
        when(anomalyService.getDistrictAverages()).thenReturn(averages);

        anomalyController.getDistrictAverages();

        verify(anomalyService, times(1)).getDistrictAverages();
        verifyNoMoreInteractions(anomalyService);
    }

    @Test
    @DisplayName("Should handle large number of districts efficiently")
    void getDistrictAverages_ShouldHandleLargeDataset() {
        Map<String, Double> largeDataset = new HashMap<>();
        for (int i = 0; i < 1000; i++) {
            largeDataset.put("District" + i, Math.random() * 100);
        }

        when(anomalyService.getDistrictAverages()).thenReturn(largeDataset);

        ResponseEntity<Map<String, Double>> response = anomalyController.getDistrictAverages();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(1000);

        verify(anomalyService, times(1)).getDistrictAverages();
    }

    @Test
    @DisplayName("Should handle districts with extreme values")
    void getDistrictAverages_ShouldHandleExtremeValues() {
        Map<String, Double> extremeValues = new HashMap<>();
        extremeValues.put("MaxDistrict", Double.MAX_VALUE);
        extremeValues.put("MinDistrict", Double.MIN_VALUE);
        extremeValues.put("InfinityDistrict", Double.POSITIVE_INFINITY);

        when(anomalyService.getDistrictAverages()).thenReturn(extremeValues);

        ResponseEntity<Map<String, Double>> response = anomalyController.getDistrictAverages();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).containsEntry("MaxDistrict", Double.MAX_VALUE);
        assertThat(response.getBody()).containsEntry("MinDistrict", Double.MIN_VALUE);
        assertThat(response.getBody()).containsEntry("InfinityDistrict", Double.POSITIVE_INFINITY);
    }

    @Test
    @DisplayName("Should handle districts with special characters in names")
    void getDistrictAverages_ShouldHandleSpecialCharactersInDistrictNames() {
        Map<String, Double> specialNames = new HashMap<>();
        specialNames.put("District-1", 45.5);
        specialNames.put("District_2", 67.8);
        specialNames.put("District 3", 23.4);
        specialNames.put("Distrito-ñ", 89.1);

        when(anomalyService.getDistrictAverages()).thenReturn(specialNames);

        ResponseEntity<Map<String, Double>> response = anomalyController.getDistrictAverages();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(4);
        assertThat(response.getBody()).containsKeys("District-1", "District_2", "District 3", "Distrito-ñ");
    }
}
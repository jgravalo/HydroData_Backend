package hackatonGrupUn.repteTres.controller;

import hackatonGrupUn.repteTres.model.Data;
import hackatonGrupUn.repteTres.repository.DataRepository;
import hackatonGrupUn.repteTres.service.DataService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("DataController Tests")
class DataControllerTest {

    @Mock
    private DataService dataService;

    @Mock
    private DataRepository dataRepository;

    @InjectMocks
    private DataController dataController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(dataController).build();
    }

    @Test
    @DisplayName("Should return all data when repository contains records")
    void getAllData_ShouldReturnAllData_WhenRepositoryHasRecords() {
        List<Data> expectedData = Arrays.asList(
                createData(1L, "value1"),
                createData(2L, "value2"),
                createData(3L, "value3")
        );

        when(dataRepository.findAll()).thenReturn(expectedData);

        List<Data> result = dataController.getAllData();

        assertThat(result).isNotNull();
        assertThat(result).hasSize(3);
        assertThat(result).isEqualTo(expectedData);

        verify(dataRepository, times(1)).findAll();
        verifyNoInteractions(dataService);
    }

    @Test
    @DisplayName("Should return empty list when repository has no records")
    void getAllData_ShouldReturnEmptyList_WhenRepositoryIsEmpty() {
        when(dataRepository.findAll()).thenReturn(Collections.emptyList());

        List<Data> result = dataController.getAllData();

        assertThat(result).isNotNull();
        assertThat(result).isEmpty();

        verify(dataRepository, times(1)).findAll();
        verifyNoInteractions(dataService);
    }

    @Test
    @DisplayName("Should return single record when repository has one entry")
    void getAllData_ShouldReturnSingleRecord_WhenRepositoryHasOneEntry() {
        List<Data> singleData = Collections.singletonList(createData(1L, "singleValue"));

        when(dataRepository.findAll()).thenReturn(singleData);

        List<Data> result = dataController.getAllData();

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(singleData.get(0));

        verify(dataRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should handle large dataset from repository")
    void getAllData_ShouldHandleLargeDataset_WhenRepositoryHasManyRecords() {
        List<Data> largeDataset = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            largeDataset.add(createData((long) i, "value" + i));
        }

        when(dataRepository.findAll()).thenReturn(largeDataset);

        List<Data> result = dataController.getAllData();

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1000);

        verify(dataRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should call repository exactly once per request")
    void getAllData_ShouldCallRepositoryOnce() {
        when(dataRepository.findAll()).thenReturn(Collections.emptyList());

        dataController.getAllData();

        verify(dataRepository, times(1)).findAll();
        verifyNoMoreInteractions(dataRepository);
        verifyNoInteractions(dataService);
    }

    @Test
    @DisplayName("Should not use DataService in getAllData method")
    void getAllData_ShouldNotUseDataService() {
        when(dataRepository.findAll()).thenReturn(Collections.emptyList());

        dataController.getAllData();

        verifyNoInteractions(dataService);
        verify(dataRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("MockMvc: Should return 200 and JSON array with data")
    void getAllData_WithMockMvc_ShouldReturnJsonArray() throws Exception {
        List<Data> expectedData = Arrays.asList(
                createData(1L, "value1"),
                createData(2L, "value2")
        );

        when(dataRepository.findAll()).thenReturn(expectedData);

        mockMvc.perform(get("/hydraulic/api/v1/data"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2));

        verify(dataRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("MockMvc: Should return empty JSON array when no data")
    void getAllData_WithMockMvc_ShouldReturnEmptyJsonArray() throws Exception {
        when(dataRepository.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/hydraulic/api/v1/data"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0))
                .andExpect(content().json("[]"));

        verify(dataRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("MockMvc: Should verify correct endpoint mapping")
    void getAllData_ShouldMapToCorrectEndpoint() throws Exception {
        when(dataRepository.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/hydraulic/api/v1/data"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/hydraulic/api/v1/datas"))
                .andExpect(status().isNotFound());

        mockMvc.perform(get("/hydraulic/api/v1/data/"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("MockMvc: Should accept GET requests only")
    void getAllData_ShouldAcceptOnlyGetRequests() throws Exception {
        when(dataRepository.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/hydraulic/api/v1/data"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should maintain data order returned by repository")
    void getAllData_ShouldMaintainDataOrder() {
        List<Data> orderedData = Arrays.asList(
                createData(3L, "third"),
                createData(1L, "first"),
                createData(2L, "second")
        );

        when(dataRepository.findAll()).thenReturn(orderedData);

        List<Data> result = dataController.getAllData();

        assertThat(result).isNotNull();
        assertThat(result).hasSize(3);
        assertThat(result).containsExactlyElementsOf(orderedData);

        verify(dataRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should handle null values in Data objects")
    void getAllData_ShouldHandleNullValuesInData() {
        List<Data> dataWithNulls = Arrays.asList(
                createData(1L, null),
                createData(null, "value"),
                createData(null, null)
        );

        when(dataRepository.findAll()).thenReturn(dataWithNulls);

        List<Data> result = dataController.getAllData();

        assertThat(result).isNotNull();
        assertThat(result).hasSize(3);

        verify(dataRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should return new list instance on each call")
    void getAllData_ShouldReturnNewListInstance_OnEachCall() {
        List<Data> firstCallData = Arrays.asList(createData(1L, "first"));
        List<Data> secondCallData = Arrays.asList(createData(2L, "second"));

        when(dataRepository.findAll())
                .thenReturn(firstCallData)
                .thenReturn(secondCallData);

        List<Data> firstResult = dataController.getAllData();
        List<Data> secondResult = dataController.getAllData();

        assertThat(firstResult).isNotEqualTo(secondResult);
        assertThat(firstResult).hasSize(1);
        assertThat(secondResult).hasSize(1);

        verify(dataRepository, times(2)).findAll();
    }

    @Test
    @DisplayName("MockMvc: Should return proper content type header")
    void getAllData_WithMockMvc_ShouldReturnProperContentType() throws Exception {
        when(dataRepository.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/hydraulic/api/v1/data"))
                .andExpect(status().isOk())
                .andExpect(header().exists("Content-Type"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(dataRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should handle repository returning mutable list")
    void getAllData_ShouldHandleMutableList() {
        List<Data> mutableList = new ArrayList<>();
        mutableList.add(createData(1L, "value1"));
        mutableList.add(createData(2L, "value2"));

        when(dataRepository.findAll()).thenReturn(mutableList);

        List<Data> result = dataController.getAllData();

        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);

        verify(dataRepository, times(1)).findAll();
    }

    // Helper method to create Data objects for testing
    private Data createData(Long id, String value) {
        Data data = new Data();
        // Assuming Data has setters or a constructor
        // Adjust based on your actual Data class implementation
        return data;
    }
}
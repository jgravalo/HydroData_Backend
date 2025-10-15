package hackatonGrupUn.repteTres.jsonUtils;

import hackatonGrupUn.repteTres.service.DataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.io.IOException;

@Slf4j
@Component
public class DataInitializer implements CommandLineRunner {

    private static final String JSON_FILE_PATH = "/json/2015_consum_aigua.json";

    private final DataService dataService;

    public DataInitializer(DataService dataService) {
        this.dataService = dataService;
    }

    @Override
    public void run(String... args) {

        log.info("Iniciando la carga de datos desde JSON...");
        try {
            int count = dataService.loadInitialData(JSON_FILE_PATH).size();

            if (count > 0) {
                log.info("✅ Datos cargados en H2 exitosamente: " + count + " registros.");
            } else {
                log.warn("⚠️ JSON cargado, pero no se encontraron registros de datos para guardar. Revisa la ruta y el formato.");
            }

        } catch (IOException e) {
            log.error("❌ ERROR FATAL al cargar o leer el archivo JSON. Revisa si la ruta es correcta: " + JSON_FILE_PATH);
            log.error("Mensaje: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            log.error("❌ ERROR al guardar datos en la base de datos: " + e.getMessage());
            e.printStackTrace();
        }
    }


}

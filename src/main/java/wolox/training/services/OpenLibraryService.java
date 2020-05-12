package wolox.training.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import wolox.training.exceptions.BookNotFoundException;
import wolox.training.services.dtos.OpenLibraryBookDTO;

@Service("openLibraryService")
public class OpenLibraryService {

    @Autowired
    @Qualifier("environment")
    private Environment environment;

    private static final String OPEN_LIBRARY_PATH = "/api/books";

    private static final String OPEN_LIBRARY_PARAMS = "?bibkeys=ISBN:%s&format=json&jscmd=data";

    public wolox.training.pojos.dtos.BookDTO bookInformation(String isbn)
        throws InterruptedException, IOException, URISyntaxException, BookNotFoundException {
        HttpResponse<String> response = this.makeRequest(isbn);
        JsonNode json = (new ObjectMapper()).readTree(response.body());

        if (json.isEmpty()) {
            throw new BookNotFoundException();
        } else {
            return new OpenLibraryBookDTO(isbn, json).convertToDTO();
        }
    }

    private HttpResponse<String> makeRequest(String isbn)
        throws URISyntaxException, IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder()
                                      .uri(new URI(String.format(this.buildUrl(), isbn)))
                                      .build();

        return httpClient.send(httpRequest, BodyHandlers.ofString());
    }

    private String buildUrl() {
        return this.environment.getProperty("app.open-library-url") + OPEN_LIBRARY_PATH
                   + OPEN_LIBRARY_PARAMS;
    }

}

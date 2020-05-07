package wolox.training.services;

import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import wolox.training.exceptions.BookNotFoundException;
import wolox.training.helpers.Json;
import wolox.training.services.dtos.BookDTO;

public class OpenLibraryService {

    private HttpClient httpClient;

    private HttpRequest httpRequest;

    private static final String OPEN_LIBRARY_URI = "https://openlibrary.org/api/books";

    private static final String OPEN_LIBRARY_PARAMS = "?bibkeys=ISBN:%s&format=json&jscmd=data";

    private String url;

    public OpenLibraryService() {
        this.httpClient = HttpClient.newHttpClient();
        this.url = OPEN_LIBRARY_URI + OPEN_LIBRARY_PARAMS;
    }

    public wolox.training.pojos.dtos.BookDTO bookInformation(String isbn)
        throws InterruptedException, IOException, URISyntaxException, BookNotFoundException {
        HttpResponse<String> response = this.makeRequest(isbn);
        JsonNode json = Json.stringToJson(response.body());

        if (json.isEmpty()) {
            throw new BookNotFoundException();
        } else {
            return new BookDTO(isbn, json).convertToDTO();
        }
    }

    private HttpResponse<String> makeRequest(String isbn)
        throws URISyntaxException, IOException, InterruptedException {
        this.httpRequest = HttpRequest.newBuilder()
                               .uri(new URI(String.format(this.url, isbn)))
                               .build();

        return this.httpClient.send(this.httpRequest, BodyHandlers.ofString());
    }

}

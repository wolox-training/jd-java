package wolox.training.services.dtos;

import com.fasterxml.jackson.databind.JsonNode;
import wolox.training.pojos.dtos.BookDTO;

public class OpenLibraryBookDTO {

    private JsonNode json;

    private String isbn;

    public OpenLibraryBookDTO(String isbn, JsonNode json) {
        this.isbn = isbn;
        this.json = json.get("ISBN:" + isbn);
    }

    public BookDTO convertToDTO() {
        return new BookDTO(
            0,
            this.getGenre(),
            this.getAuthor(),
            this.getImage(),
            this.getTitle(),
            this.getSubtitle(),
            this.getPublisher(),
            this.getYear(),
            this.getPages(),
            this.isbn
        );
    }

    private String getGenre() {
        String genre = this.getStringFromArray(this.json.get("subjects"));
        return genre.length() > 200 ? genre.substring(0, 200) : genre;
    }

    private String getAuthor() {
        return this.getStringFromArray(this.json.get("authors"));
    }

    private String getImage() {
        return this.json.get("cover").get("large").asText();
    }

    private String getTitle() {
        return this.json.get("title").asText();
    }

    private String getSubtitle() {
        return this.json.has("subtitle") ? this.json.get("subtitle").asText() : "No specified";
    }

    private String getPublisher() {
        return this.getStringFromArray(this.json.get("publishers"));
    }

    private String getYear() {
        return this.json.get("publish_date").asText();
    }

    private int getPages() {
        return this.json.get("number_of_pages").asInt();
    }

    private String getStringFromArray(JsonNode elements) {
        String elementsJoined = "";
        for (JsonNode element : elements) {
            elementsJoined += elementsJoined.isEmpty() ? "" : ",";
            elementsJoined += element.get("name").asText();
        }
        return elementsJoined;
    }

}

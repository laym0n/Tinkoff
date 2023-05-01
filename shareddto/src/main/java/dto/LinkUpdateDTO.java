package dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.net.URI;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LinkUpdateDTO {
    private int id;
    private URI uri;
    private String description;
    private int[] tgChatIds;
}

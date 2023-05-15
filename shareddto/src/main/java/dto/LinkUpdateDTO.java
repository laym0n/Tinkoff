package dto;

import java.net.URI;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LinkUpdateDTO {
    private int id;
    private URI uri;
    private String description;
    private int[] tgChatIds;
}

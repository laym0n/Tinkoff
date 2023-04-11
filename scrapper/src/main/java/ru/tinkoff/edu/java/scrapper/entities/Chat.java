package ru.tinkoff.edu.java.scrapper.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
public class Chat {
    private int id;
    private List<TrackedLink> links;

    public Chat(int id) {
        this.id = id;
        this.links = new ArrayList<>();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Chat chat = (Chat) o;
        return getId() == chat.getId() && getLinks().equals(chat.getLinks());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}

package ru.tinkoff.edu.java.scrapper.usecases;

import ru.tinkoff.edu.java.scrapper.dto.request.LinkUpdateRequest;

import java.util.List;

public interface CheckUpdateLinksUseCase {
    List<LinkUpdateRequest> checkUpdateLinks();
}

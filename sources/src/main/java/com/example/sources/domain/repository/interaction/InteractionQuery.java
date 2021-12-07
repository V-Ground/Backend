package com.example.sources.domain.repository.interaction;

import com.example.sources.domain.dto.response.InteractionResponseData;

import java.util.List;

public interface InteractionQuery {
    List<InteractionResponseData> findAllByCourseId(Long courseId);
}

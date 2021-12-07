package com.example.sources.domain.repository.interactionsubmit;

import com.example.sources.domain.dto.response.InteractionSubmitResponseData;

import java.util.List;

public interface InteractionSubmitQuery {
    List<InteractionSubmitResponseData> findAllByInteractionId(Long interactionId);
}

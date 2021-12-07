package com.example.sources.domain.repository.interactionsubmit;

import com.example.sources.domain.entity.InteractionSubmit;
import com.example.sources.domain.repository.interaction.InteractionQuery;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InteractionSubmitRepository extends JpaRepository<InteractionSubmit, Long>, InteractionQuery {
}

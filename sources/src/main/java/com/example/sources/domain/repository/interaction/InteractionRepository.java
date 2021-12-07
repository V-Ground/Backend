package com.example.sources.domain.repository.interaction;

import com.example.sources.domain.entity.Interaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InteractionRepository extends JpaRepository<Interaction, Long>, InteractionQuery {

}

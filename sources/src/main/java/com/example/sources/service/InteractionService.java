package com.example.sources.service;

import com.example.sources.domain.dto.request.CreateInteractionRequestData;
import com.example.sources.domain.dto.response.CreateInteractionResponseData;
import com.example.sources.domain.entity.Course;
import com.example.sources.domain.entity.Interaction;
import com.example.sources.domain.repository.course.CourseRepository;
import com.example.sources.domain.repository.courseuser.CourseUserRepository;
import com.example.sources.domain.repository.interaction.InteractionRepository;
import com.example.sources.domain.repository.interactionsubmit.InteractionSubmitRepository;
import com.example.sources.domain.repository.user.UserRepository;
import com.example.sources.exception.AuthenticationFailedException;
import com.example.sources.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
public class InteractionService {
    private final InteractionRepository interactionRepository;
    private final InteractionSubmitRepository interactionSubmitRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final CourseUserRepository courseUserRepository;
    private final ModelMapper modelMapper;

    public CreateInteractionResponseData addInteraction(Long courseId,
                                                        CreateInteractionRequestData request,
                                                        Long tokenUserId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new NotFoundException("클래스 번호 " + courseId));

        boolean owner = course.isOwner(tokenUserId);
        if(!owner) {
            throw new AuthenticationFailedException();
        }

        Interaction map = modelMapper.map(request, Interaction.class);
        map.publish(course);

        Interaction savedInteraction = interactionRepository.save(map);

        return modelMapper.map(savedInteraction, CreateInteractionResponseData.class);
    }
}

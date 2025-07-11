package com.lloll.myro.domain.schedule.api;

import com.lloll.myro.domain.schedule.dao.TagRepository;
import com.lloll.myro.domain.schedule.domain.Tag;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/tags")
@RequiredArgsConstructor
public class TagController {

    private final TagRepository tagRepository;

    @PostMapping()
    public void createTag(@RequestBody String request) {
        Tag tag = new Tag(request);
        tagRepository.save(tag);
    }

    @GetMapping()
    public List<Tag> getTags() {
        return tagRepository.findAll();
    }

    @GetMapping("/{id}")
    public Tag getTagById(@PathVariable Long id) {
        return tagRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Tag not found"));
    }

    @Transactional
    @PutMapping("/{id}")
    public Tag updateTag(@PathVariable Long id, @RequestBody String request) {
        Tag existingTag = tagRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Tag not found"));
        existingTag.updateName(request);
        return existingTag;
    }

    @DeleteMapping("/{id}")
    public void deleteTag(@PathVariable Long id) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Tag not found"));
        tagRepository.delete(tag);
    }
}

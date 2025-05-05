package com.lloll.myro.domain.schedule.dao;

import com.lloll.myro.domain.schedule.domain.Tag;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Long> {
    Optional<Tag> findByName(String name);
}

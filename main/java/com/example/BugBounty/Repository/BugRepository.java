package com.example.BugBounty.Repository;

import com.example.BugBounty.model.Bug;
import com.example.BugBounty.model.BugStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

public interface BugRepository extends JpaRepository<Bug, Integer> {

    // ✅ Custom query to filter bugs by difficulty, tech stack, and status
    @Query("SELECT b FROM Bug b WHERE "
            + "(:difficulty IS NULL OR b.difficulty = :difficulty) "
            + "AND (:techStack IS NULL OR b.techStack LIKE %:techStack%) "
            + "AND (:status IS NULL OR b.status = :status)")
    List<Bug> findByFilters(@Param("difficulty") String difficulty,
                            @Param("techStack") String techStack,
                            @Param("status") BugStatus status);

    // ✅ Custom query to count developers working on each bug (with alias for clarity)
    @Query("SELECT b.id AS bugId, COUNT(d.id) AS developerCount " +
            "FROM Bug b LEFT JOIN b.developers d " +
            "GROUP BY b.id")
    List<Map<String, Object>> getBugsWithDeveloperCount();
}

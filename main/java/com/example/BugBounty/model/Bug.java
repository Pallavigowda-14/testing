package com.example.BugBounty.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "bugs")
public class Bug {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private String difficulty;  // EASY, MEDIUM, HARD

    @Column(nullable = false)
    private String techStack;  // Java, React, etc.

    @Column(nullable = false)
    private BigDecimal reward;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BugStatus status;  // Uses Enum instead of String

    @ManyToOne
    @JoinColumn(name = "posted_by", nullable = false)
    private User postedBy;  // Company who posted the bug

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    // ✅ New field to track developers working on the bug
    @OneToMany(mappedBy = "bug", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Developer> developers;

    // ✅ Method to get the number of developers working on this bug
    public int getDevelopersWorking() {
        return developers != null ? developers.size() : 0;
    }

    // Constructors
    public Bug() {}

    public Bug(String title, String description, String difficulty, String techStack, BigDecimal reward, BugStatus status, User postedBy) {
        this.title = title;
        this.description = description;
        this.difficulty = difficulty;
        this.techStack = techStack;
        this.reward = reward;
        this.status = status;
        this.postedBy = postedBy;
        this.createdAt = LocalDateTime.now();
    }

    // Getters & Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getDifficulty() { return difficulty; }
    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }

    public String getTechStack() { return techStack; }
    public void setTechStack(String techStack) { this.techStack = techStack; }

    public BigDecimal getReward() { return reward; }
    public void setReward(BigDecimal reward) { this.reward = reward; }

    public BugStatus getStatus() { return status; }
    public void setStatus(BugStatus status) { this.status = status; }

    public User getPostedBy() { return postedBy; }
    public void setPostedBy(User postedBy) { this.postedBy = postedBy; }

    public LocalDateTime getCreatedAt() { return createdAt; }

    public List<Developer> getDevelopers() { return developers; }
    public void setDevelopers(List<Developer> developers) { this.developers = developers; }
}

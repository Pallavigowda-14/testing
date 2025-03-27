package com.example.BugBounty.Controller;

import com.example.BugBounty.Services.BugService;
import com.example.BugBounty.model.BugStatus;
import com.example.BugBounty.Repository.BugRepository;
import com.example.BugBounty.model.Bug;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/bugs")
@CrossOrigin(origins = "*", maxAge = 3600)
public class BugController {

    private final BugRepository bugRepository;
    private final BugService bugService; // ✅ Inject BugService

    // ✅ Constructor Injection
    public BugController(BugRepository bugRepository, BugService bugService) {
        this.bugRepository = bugRepository;
        this.bugService = bugService; // ✅ Initialize BugService
    }

    // ✅ Get all bugs with optional filtering
    @GetMapping
    public List<Bug> getFilteredBugs(
            @RequestParam(required = false) String difficulty,
            @RequestParam(required = false) String techStack,
            @RequestParam(required = false) BugStatus status) {
        return bugRepository.findByFilters(difficulty, techStack, status);
    }

    // ✅ Get a bug by ID
    @GetMapping("/{id}")
    public Bug getBugById(@PathVariable int id) {
        return bugRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bug not found"));
    }

    // ✅ Create a new bug
    @PostMapping
    public Bug createBug(@RequestBody Bug bug) {
        return bugRepository.save(bug);
    }

    // ✅ Update an existing bug
    @PutMapping("/{id}")
    public Bug updateBug(@PathVariable int id, @RequestBody Bug bugDetails) {
        Bug bug = bugRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bug not found"));

        bug.setTitle(bugDetails.getTitle());
        bug.setDescription(bugDetails.getDescription());
        bug.setDifficulty(bugDetails.getDifficulty());
        bug.setTechStack(bugDetails.getTechStack());
        bug.setReward(bugDetails.getReward());
        bug.setStatus(bugDetails.getStatus());

        return bugRepository.save(bug);
    }

    // ✅ Delete a bug
    @DeleteMapping("/{id}")
    public String deleteBug(@PathVariable int id) {
        bugRepository.deleteById(id);
        return "Bug with ID " + id + " deleted successfully.";
    }

    // ✅ Fetch Developer Count
    @GetMapping("/developers-count")
    public List<Map<String, Object>> getBugsWithDeveloperCount() {
        return bugService.getBugsWithDeveloperCount();
    }
}

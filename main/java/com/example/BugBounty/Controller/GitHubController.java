package com.example.BugBounty.Controller;

import com.example.BugBounty.Services.FileService;
import com.example.BugBounty.Services.GitHubService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/github")
public class GitHubController {

    private final GitHubService gitHubService;
    private final FileService fileService;

    public GitHubController(GitHubService gitHubService, FileService fileService) {
        this.gitHubService = gitHubService;
        this.fileService = fileService;
    }


    @PostMapping("/clone-and-fetch-prs")
    public ResponseEntity<?> cloneRepoAndFetchPRs(@RequestBody Map<String, String> request) {
        String pat = request.get("pat");
        String repoUrl = request.get("repoUrl");

        if (pat == null || repoUrl == null) {
            return ResponseEntity.badRequest().body("Missing PAT or repo URL");
        }

        try {
            // Generate a unique folder name for each repo
            String repoName = repoUrl.substring(repoUrl.lastIndexOf("/") + 1).replace(".git", "");
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String localPath = "C:\\temp\\github_repos\\" + repoName + "_" + timestamp; // Unique folder

            // Step 1: Clone Repository
            gitHubService.cloneRepository(repoUrl, pat, localPath);

            // Step 2: Fetch Pull Requests
            List<Map<String, Object>> pullRequests = gitHubService.fetchPullRequests(repoUrl, pat);
            return ResponseEntity.ok(Map.of("localPath", localPath, "pullRequests", pullRequests));

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/repo-files")
    public ResponseEntity<?> getRepoFiles(@RequestParam(value = "repoPath", required = false) String repoPath) {
        if (repoPath == null || repoPath.isEmpty()) {
            repoPath = "C:\\temp\\github_repos"; // Default path
        }
        File repoDir = new File(repoPath);
        if (!repoDir.exists() || !repoDir.isDirectory()) {
            return ResponseEntity.badRequest().body("Invalid repository path: " + repoPath);
        }

        List<Map<String, Object>> folderStructure = fileService.getFolderStructure(repoPath);
        return ResponseEntity.ok(folderStructure);
    }
}

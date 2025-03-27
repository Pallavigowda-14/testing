package com.example.BugBounty.Services;

import com.example.BugBounty.Repository.BugRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

@Service
public class BugService {

    private final BugRepository bugRepository;

    public BugService(BugRepository bugRepository) {
        this.bugRepository = bugRepository;
    }

    public List<Map<String, Object>> getBugsWithDeveloperCount() {
        return bugRepository.getBugsWithDeveloperCount();
    }
}

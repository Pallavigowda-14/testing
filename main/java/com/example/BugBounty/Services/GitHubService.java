

package com.example.BugBounty.Services;


import org.eclipse.jgit.api.Git;

import org.springframework.http.ResponseEntity;

import org.springframework.http.HttpHeaders;

import org.springframework.http.HttpEntity;

import org.springframework.http.HttpMethod;

import org.springframework.stereotype.Service;

import org.springframework.web.client.RestTemplate;

import org.eclipse.jgit.api.errors.GitAPIException;

import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import org.eclipse.jgit.transport.SshTransport;

import java.io.File;

import java.util.List;

import java.util.Map;

@Service

public class GitHubService {

    private static final String GITHUB_API = "https://api.github.com";

    public void cloneRepository(String repoUrl, String pat, String localPath) {

        File localDir = new File(localPath);

        // Ensure the directory is clean before cloning

        if (localDir.exists()) {

            deleteDirectory(localDir);

        }

        System.out.println("Cloning repo: " + repoUrl);

        try {

            Git.cloneRepository()

                    .setURI(repoUrl)

                    .setDirectory(localDir)

                    .setCredentialsProvider(new UsernamePasswordCredentialsProvider(pat, "")) // Secure Authentication

                    .call();

            System.out.println("Clone completed successfully.");

        } catch (GitAPIException e) {

            System.err.println("Error cloning repository: " + e.getMessage());

            e.printStackTrace();

        }

    }

    public List<Map<String, Object>> fetchPullRequests(String repoUrl, String pat) {

        String repoPath = repoUrl.replace("https://github.com/", ""); // Extract "owner/repo"

        String apiUrl = GITHUB_API + "/repos/" + repoPath + "/pulls";

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();

        headers.set("Authorization", "Bearer " + pat);

        headers.set("Accept", "application/vnd.github+json");

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<List> response = restTemplate.exchange(apiUrl, HttpMethod.GET, entity, List.class);

        return response.getBody();

    }

    private void deleteDirectory(File dir) {

        File[] files = dir.listFiles();

        if (files != null) {

            for (File file : files) {

                if (file.isDirectory()) {

                    deleteDirectory(file);

                } else {

                    file.delete();

                }

            }

        }

        dir.delete();

    }

}
 
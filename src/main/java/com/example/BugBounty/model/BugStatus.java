
package com.example.BugBounty.model;


public enum BugStatus {
    OPEN,       // Bug is newly posted and open for fixing
    CLAIMED,    // Someone has taken up the bug to solve
    IN_PROGRESS, // Bug is actively being worked on (NEW)
    REVIEWED,   // The solution is being reviewed
    RESOLVED,   // The bug has been successfully fixed
    REJECTED    // The solution was rejected
}

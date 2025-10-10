/**
 * Invexus DMS - <ClassName>
 * Author: LaTroy Richardson (CEN-3024C)
 * Purpose: This give the status an option of four different states and keep its logic in IO separate from service
 * Notes: Phase 1 - CLI, in-memory repository, file import (CSV/TXT).
 */

package app.domain;

public enum AssetStatus {
    // created a nested enum so status can utilize it for each asset
        IN_STOCK,
        ASSIGNED,
        REPAIR,
        RETIRED

}


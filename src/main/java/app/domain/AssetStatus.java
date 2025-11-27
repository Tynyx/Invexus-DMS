/**
 * Invexus DMS - <ClassName>
 * Author: LaTroy Richardson (CEN-3024C)
 * Purpose: This give the status an option of four different states and keep its logic in IO separate from service
 * Notes: Phase 1 - CLI, in-memory repository, file import (CSV/TXT).
 */

package app.domain;

/**
 * Represents the lifecycle/status of an asset within the inventory system.
 */
public enum AssetStatus {

    /** Asset is available in storage and not currently assigned. */
    IN_STOCK,

    /** Asset is currently assigned to a user, workstation, or location. */
    ASSIGNED,

    /** Asset is under repair or maintenance and not available for normal use. */
    REPAIR,

    /** Asset has been permanently retired from use. */
    RETIRED
}



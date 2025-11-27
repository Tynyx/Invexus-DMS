// LaTroy Richardson CEN-3024C - 13950
// Software Development 1
// Asset Repository Interface
//
// This interface lays out the blueprint for how assets get stored, found, listed, or deleted.
// It doesn’t actually do the work itself — it just defines the rules that any class using it must follow.
// The goal is to keep data handling clean and consistent, no matter where it’s stored later
// (like in memory, a CSV file, or a real database).


package app.repository;

import app.domain.Asset;
import java.util.List;
import java.util.Optional;

// Method Overview:
// - save(Asset asset): Handles saving or updating an asset and returns the saved object.
// - findByTag(String assetTag): Looks up an asset by its tag and returns it wrapped in Optional
//   so we avoid null pointer issues if it’s not found.
// - findAll(): Grabs and returns all assets currently in storage.
// - delete(String assetTag): Removes an asset by tag and returns true or false depending on success.
// - searchByName(String keyword): Searches asset names and returns a list of anything that matches.

/**
 * Persistence boundary for assets.
 */
public interface AssetRepository {

    /**
     * Inserts a new asset. Duplicates tags are rejected
     * @param a asset to save
     * @return {@code null} if one row was inserted
     */
    boolean save(Asset a);


    /**
     * Fetches a specific asset by tag
     * @param assetTag is used to find the specific asset by primary key
     * @return optional asset
     */
    Optional<Asset> findByTag(String assetTag);

    /**
     * Fetches all assets ordered by tag.
     * @return list of rows (never {@code null})
     */
    List<Asset> findAll();

    /**
     * Edit a specific asset located by its tag
     * @param a asset with fields to update
     * @return {@code true} if one row was updated.
     */
    boolean update(Asset a);

    /**
     * Deletes an asset by tag
     * @param assetTag the asset primary key
     * @return {@code true} if one row was deleted
     */
    boolean delete(String assetTag);




}

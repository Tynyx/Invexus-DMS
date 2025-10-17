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


public interface AssetRepository {

    Asset save(Asset asset);

    Optional<Asset> findByTag(String assetTag);

    List<Asset> findAll();

    boolean delete(String assetTag);




}

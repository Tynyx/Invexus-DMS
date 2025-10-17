// LaTroy Richardson CEN-3024C - 13950
// Software Development 1
// InMemoryAssetRepository Class
//
// This class provides a working version of the AssetRepository interface using an in-memory data structure.
// It uses a LinkedHashMap to store Asset objects with their assetTag as the unique key.
// The purpose of this class is to manage data without needing an external file or database yet.
//
// Key operations:
// - save(Asset asset): Adds or updates an asset in the map.
// - findByTag(String assetTag): Searches by unique tag and returns an Optional with the result.
// - findAll(): Returns a list of all stored assets.
// - delete(String assetTag): Removes an asset from memory.
// - searchByName(String keyword): Looks for assets that match part of a name (case-insensitive).
//
// Purpose:
// Acts as a temporary data layer for Invexus DMS, allowing AssetManager and App to test logic flow
// before connecting to permanent storage later.


package app.repository;

import app.domain.Asset;


import java.util.*;

public class InMemoryAssetRepository implements AssetRepository {

    private final Map<String, Asset> store = new LinkedHashMap<>();

    // returns a list of all assets currently in the map
    @Override
    public List<Asset> findAll() {
        return new ArrayList<>(store.values());
    }

    // Adds or update an asset in the map using its tags as the key, then returns that same asset.
    @Override
    public Asset save(Asset asset) {

        // if the store does not contain this keyiD already then go ahead and save it.
            if (asset == null || asset.getStatus() == null) throw new IllegalArgumentException("Asset or tag is required");
            store.put(asset.getAssetTag(), asset);

            return asset;
    }

    //Looks for an asset by tag and if its found it will populate if not it will not populate
    @Override
    public Optional<Asset> findByTag(String assetTag) {
        if (assetTag == null || assetTag.isBlank()) {
            return Optional.empty();
        }
        return Optional.ofNullable(store.get(assetTag));
    }

    // Removes the asset with that tag, returns true if its exited, false otherwise
    @Override
    public boolean delete(String assetTag) {
        return store.remove(assetTag) != null;
    }








}

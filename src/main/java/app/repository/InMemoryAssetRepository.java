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

/**
 * Simple in-memory implementation of {@link AssetRepository} backed by a {@link java.util.Map}.
 * <p>
 * This repository is primarily intended for testing and non-persistent use.
 */
public class InMemoryAssetRepository implements AssetRepository {

    private final Map<String, Asset> store = new LinkedHashMap<>();

    /**
     * Creates a new in-memory asset repository with an empty store.
     */
    public InMemoryAssetRepository() {
        // store is already initialized at field declaration
    }

    /**
     * return all assets currently stored in memory.
     * @return a new list containing every asset in the repository
     */
    @Override
    public List<Asset> findAll() {
        return new ArrayList<>(store.values());
    }


    /**
     * Updates an existing asset in the repository
     * @param a asset with fields to update
     * @return True if the asset was found and updated, else False
     */
    @Override
    public boolean update(Asset a) {
        return false;
    }

    /**
     * take a new asset and save it
     * @param a asset to save
     * @return {@code true} if asset tag is unique, {@code false} if already stored
     */
    @Override
    public boolean save(Asset a) {

        // if the store does not contain this keyiD already then go ahead and save it.
            if (a == null || a.getStatus() == null) throw new IllegalArgumentException("Asset or tag is required");
            store.put(a.getAssetTag(), a);
        return true;
    }

    /**
     * search for an specific asset by it tag
     * @param assetTag is used to find the specific asset by primary key
     * @return {@code optional} if tag is found, {@code optional} if not found or null
     */
    @Override
    public Optional<Asset> findByTag(String assetTag) {
        if (assetTag == null || assetTag.isBlank()) {
            return Optional.empty();
        }
        return Optional.ofNullable(store.get(assetTag));
    }

    /**
     * Deletes a assetTag by searching for its priamery key
     * @param assetTag the asset primary key
     * @return {@code true} if assettag is found and deleted, {@code false} if not found or deleted
     */
    @Override
    public boolean delete(String assetTag) {
        return store.remove(assetTag) != null;
    }








}

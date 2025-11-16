/**
 * Invexus DMS - <ClassName>
 * Author: LaTroy Richardson (CEN-3024C)
 * Purpose: this is basically the class that handles all the data from Asset with functions to call upon to give easy flow
 * Notes: Phase 1 - CLI, in-memory repository, file import (CSV/TXT).
 */





package app.service;


import app.domain.Asset;
import app.domain.AssetStatus;

import app.repository.AssetRepository;


import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

/**
 * High-level service that coordinates assets operations between the UI and the underlying repo
 */
public class AssetManager  {


    // calls on the Repository we created to use only the data through it and not get it anywhere else
    private final AssetRepository repo;




    //Constructors

    /**
     * Create a new asset manager using the given repository
     * @param repo the repository used to store and retrieve assets
     */
    public AssetManager( AssetRepository repo) {
                this.repo = repo;

    }

    //methods
    /**
     * Returns all assets in repository, order by tag.
     * @return immutable snapshot of all assets
     */
    public List<Asset> findAll() {
        return repo.findAll();
    }

    /**
     * Attempts to add a new asset to the repository.
     * Asset is only saved if no other asset with the same tag already exists.
     * @param a the asset to add; must have a non-null, unique asset tag
     * @return {@code true} if the asset was added, or {@code false} if an asset with tag exist.
     */
    public boolean add(Asset a) {
        Optional<Asset> existing = repo.findByTag(a.getAssetTag());

        if (existing.isPresent()) {
            return false;
        } else {
            repo.save(a);
            return true;
        }
    }

    /**
     * Updates an existing asset in the repository
     * The update only occurs if an asset with the same tag is present.
     * @param a the asset containing the updated data
     * @return {@code true} if the asset was found and updated, {@code false} if no a tag found
     */
    public boolean update(Asset a) {
        Optional<Asset> existing = repo.findByTag(a.getAssetTag());

        if (existing.isEmpty()) {
            return false;
        } else {
            repo.save(a);
            return true;
        }
    }

    /**
     * Deletes an asset by its tag.
     * @param tag the asset to be deleted; must not be null or blank
     * @return True, if asset with tag was deleted, False if asset was not found or deleted
     */
    public boolean delete(String tag) {
        if (tag == null || tag.isBlank()) {
            return false;
        } else {

            return repo.delete(tag);
        }
    }

    /**
     * Find an asset but the primary Key Tag
     * @param tag the asset tag to be searched for
     * @return an {@link Optional} containing the matching asset, or {@code Optional.empty()} if not found
     */
    public Optional<Asset> findByTag(String tag) {

        if (repo.findByTag(tag).isEmpty()) {
            return Optional.empty();
        }

        return repo.findByTag(tag);

    }


    /**
     * List all the assets currently stored in the repo
     * @return a list of assets
     */
    public List<Asset> listAll() {
        return repo.findAll();

    }


    /**
     * Imports a batch of assets into the repository.
     * @param parsed parsed the list of assets to import; may be null
     * @return the number of assets successfully
     */
    public int importAssets(List<Asset> parsed) {
        int added = 0;
        if (parsed == null) return 0;
        for (Asset a : parsed) {
            if (a == null) continue;
            if (add(a)) added++;
        }
        return added;
    }

    /**
     * Calculates the total inventory value of assets
     * @return the value of all the assets sumed together and formatted in USD
     */
    public BigDecimal totalInventoryValue() {
        return repo.findAll().stream()
                .map(Asset::getUnitCost)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * for handling the status enum when adding or editing assetStatus
     * @param s is the string to parse into the four options "in_stock, Assigned, repair, Retired"
     * @return the parsed string into the correct
     */
    public static AssetStatus parseStatus (String s) {
        if ( s == null) throw new IllegalArgumentException("Status is required.");
        String key = s.trim().toUpperCase(Locale.ROOT).replace(' ','_');
        return AssetStatus.valueOf(key);
    }
}

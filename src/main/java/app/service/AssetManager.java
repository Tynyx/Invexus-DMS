/**
 * Invexus DMS - <ClassName>
 * Author: LaTroy Richardson (CEN-3024C)
 * Purpose: this is basically the class that handles all the data from Asset with functions to call upon to give easy flow
 * Notes: Phase 1 - CLI, in-memory repository, file import (CSV/TXT).
 */





package app.service;


import app.domain.Asset;
import app.repository.AssetRepository;


import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
public class AssetManager  {


    // calls on the Repository we created to use only the data through it and not get it anywhere else
    private final AssetRepository repo;

    //Constructors
    public AssetManager( AssetRepository repository) {
                this.repo = repository;

    }


    // methods
    public boolean add(Asset a) {
        Optional<Asset> existing = repo.findByTag(a.getAssetTag());

        if (existing.isPresent()) {
            return false;
        } else {
            repo.save(a);
            return true;
        }
    }

    public boolean update(Asset a) {
        Optional<Asset> existing = repo.findByTag(a.getAssetTag());

        if (existing.isEmpty()) {
            return false;
        } else {
            repo.save(a);
            return true;
        }
    }

    public boolean delete(String tag) {
        if (tag == null || tag.isBlank()) {
            return false;
        } else {

            return repo.delete(tag);
        }
    }

    public Optional<Asset> findByTag(String tag) {

        if (repo.findByTag(tag).isEmpty()) {
            return Optional.empty();
        }

        return repo.findByTag(tag);

    }



    public List<Asset> listAll() {
        return repo.findAll();

    }


    public int importAssets(List<Asset> parsed) {
        int added = 0;
        if (parsed == null) return 0;
        for (Asset a : parsed) {
            if (a == null) continue;
            if (add(a)) added++;
        }
        return added;
    }

    public BigDecimal totalInventoryValue() {
        List<Asset> all = repo.findAll();
        return all.stream()
                .map(Asset::getUnitCost)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}

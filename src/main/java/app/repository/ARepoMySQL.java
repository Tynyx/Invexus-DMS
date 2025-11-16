package app.repository;

import app.domain.Asset;
import app.domain.AssetStatus;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;

/**
 * MySQL repo for AssetRepository
 * connects the Database to the Repo
 * which we call upon to save all asset
 * info into the DB not the InMemory Repo
 *
 */
public class ARepoMySQL implements AssetRepository {

    private final DataSource ds;

    /**
     * Creates a new MySQL-backed asset repository using the given data source.
     *
     * @param ds the {@link javax.sql.DataSource} used to obtain database connections
     */
    public ARepoMySQL(DataSource ds) {
        this.ds = ds;
    }

    @Override
    public boolean save(Asset a) {
        if (a == null || a.getAssetTag() == null || a.getAssetTag().isBlank()) return false;

        final String sql =
                "INSERT INTO assets (tag, name, location, acquired_on, cost, quantity, status, Assigned) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?) " +
                        "ON DUPLICATE KEY UPDATE " +
                        "  name = VALUES(name), " +
                        "  location = VALUES(location), " +
                        "  acquired_on = VALUES(acquired_on), " +
                        "  cost = VALUES(cost), " +
                        "  quantity = VALUES(quantity), " +
                        "  status = VALUES(status), " +
                        "  Assigned = VALUES(Assigned)";

        try (Connection c = ds.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, a.getAssetTag());
            ps.setString(2, a.getName());
            ps.setString(3, a.getLocation());
            ps.setDate  (4, a.getPurchaseDate()==null ? null : java.sql.Date.valueOf(a.getPurchaseDate()));
            ps.setBigDecimal(5, a.getUnitCost());
            ps.setInt   (6, a.getQuantity());
            ps.setString(7, a.getStatus()==null ? null : a.getStatus().name());
            ps.setBoolean(8, a.isAssigned());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }



    @Override
    public List<Asset> findAll() {
        final String sql =
                "SELECT tag, name, location, acquired_on, cost, quantity, status, Assigned " +
                        "FROM assets ORDER BY tag";

        List<Asset> out = new ArrayList<>();
        try (Connection c = ds.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) out.add(map(rs));
        } catch (SQLException ignored) { }
        return out;
    }



    @Override
    public Optional<Asset> findByTag(String tag) {
        final String sql = "SELECT tag, name, location, purchase_date, unit_cost, qty, status, assigned " +
                "FROM assets WHERE tag = ?";
        try (Connection c = ds.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, tag);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(map(rs));
            }
        } catch (SQLException ignored) { }
        return Optional.empty();
    }

    @Override
    public boolean update(Asset a) {
        final String sql =
                "UPDATE assets SET name=?, location=?, acquired_on=?, cost=?, quantity=?, status=?, Assigned=? " +
                        "WHERE tag=?";
        try (Connection c = ds.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, a.getName());
            ps.setString(2, a.getLocation());
            ps.setDate  (3, a.getPurchaseDate()==null ? null : java.sql.Date.valueOf(a.getPurchaseDate()));
            ps.setBigDecimal(4, a.getUnitCost());
            ps.setInt   (5, a.getQuantity());
            ps.setString(6, a.getStatus()==null ? null : a.getStatus().name());
            ps.setBoolean(7, a.isAssigned());
            ps.setString(8, a.getAssetTag());

            return ps.executeUpdate() == 1;
        } catch (SQLException e) { return false; }
    }


    @Override
    public boolean delete(String tag) {
        final String sql = "DELETE FROM assets WHERE tag = ?";
        try (Connection c = ds.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, tag);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) { return false; }
    }

    private Asset map(ResultSet rs) throws SQLException {
        // Guard status possibly null
        String st = rs.getString("status");
        AssetStatus status = (st == null || st.isBlank()) ? null : AssetStatus.valueOf(st);

        return new Asset(
                rs.getString("tag"),
                rs.getString("name"),
                rs.getString("location"),
                rs.getDate("acquired_on") == null ? null : rs.getDate("acquired_on").toLocalDate(),
                rs.getBigDecimal("cost"),
                rs.getInt("quantity"),
                status,
                rs.getBoolean("Assigned")    // 8th arg
        );
    }


}

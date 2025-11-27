//LaTroy Richardson CEN-3024C - 13950
// Software Development 1
// Asset Class
//This class is the main object of this app, it's the class that holds the information of the new and existing assets
// and will be the class that's manipulated to produce the information need for display

package app.domain;



import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

/**
 * The class that give the layout of the attributes and methods evolving
 * around the asset that needs to be implemented or edited
 *
 */
public class Asset {


    // These are the attributes for this class
    // assetTag will be the primaryKey

    final String assetTag;
    private String name;
    private String location;
    private LocalDate purchaseDate;
    private BigDecimal unitCost;
    private final AssetStatus status;
    private boolean assigned;

    private int quantity;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Asset asset = (Asset) o;
        return Objects.equals(assetTag, asset.assetTag);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(assetTag);
    }

    //Constructors
    /**
     * Creates a new asset with the given properties.
     *
     * @param assetTag     unique identifier for this asset
     * @param name         descriptive name of the asset
     * @param location     physical or logical location of the asset
     * @param purchaseDate date the asset was purchased
     * @param unitCost     cost of a single unit of this asset
     * @param quantity     number of units owned
     * @param status       current lifecycle status of the asset
     * @param assigned     whether the asset is currently assigned
     */
    public Asset(String assetTag, String name, String location, LocalDate purchaseDate, BigDecimal unitCost, int quantity, AssetStatus status, boolean assigned) {
        this.assetTag = assetTag;
        this.name = name;
        this.location = location;
        this.purchaseDate = purchaseDate;
        this.unitCost = unitCost;
        this.quantity = quantity;
        this.status = status;
        this.assigned = assigned;

    }

    // Getters and Setters which allows the coder to pull and set the value of each attribute

    /**
     * Immutable key for this asset (acts as primary key in the DB).
     * @return tag, never {@code null} or blank
     */
    public String getAssetTag() {
        return assetTag;
    }

    /**
     * Retrieve the status of an asset
     * @return the status of the asset
     */
    public AssetStatus getStatus() {
        return status;
    }


    /**
     * Returns the name fo this asset
     * @return the asset name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of this asset
     * @param name the new asset name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Return the location of this asset
     * @return the asset location
     */
    public String getLocation() {
        return location;
    }

    /**
     * Set the location of this asset
     * @param location the new location for this asset
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * Return the date of purchase for the asset.
     * @return the purchase date
     */
    public LocalDate getPurchaseDate() {
        return purchaseDate;
    }


    /**
     * Returns the cost of a unit for an asset
     * @return the cost of an asset unit in the big decimal format
     */
    public BigDecimal getUnitCost() {
        return unitCost;
    }


    /**
     * Return the amount of units an asset have
     * @return the amount units in the asset
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * return if the asset is assigned or not assigned
     * @return {@code true} if the asset is assigned, {@code false} if the asset is not found or assigned
     */
    public boolean getAssigned() {
        return assigned;
    }

    /**
     * Sets whether this asset is currently assigned.
     *
     * @param assigned {@code true} if the asset is assigned, otherwise {@code false}
     */
    public void setAssigned(boolean assigned) {
        this.assigned = assigned;
    }



    // Override to ensure the code can be used to make string even though they not strings
    @Override
    public String toString() {
        return "Asset{" +
                "assetTag='" + assetTag + '\'' +
                ", name='" + name + '\'' +
                ", category='" + location + '\'' +
                ", purchaseDate=" + purchaseDate +
                ", unitCost=" + unitCost +
                ", quantity=" + quantity +
                ", status='" + status + '\'' +
                ", assigned=" + assigned +

                '}';
    }


    /**
     * Returns whether this asset is currently assigned.
     * <p>
     * This method follows the JavaBean {@code isXxx} convention.
     *
     * @return {@code true} if the asset is assigned, otherwise {@code false}
     */
    public boolean isAssigned() {
        return assigned;
    }


    public void setQuantity(int newQuantity) {
        this.quantity = newQuantity;
    }
}

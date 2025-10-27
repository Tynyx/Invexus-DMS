//LaTroy Richardson CEN-3024C - 13950
// Software Development 1
// Asset Class
//This class is the main object of this app, it's the class that holds the information of the new and existing assets
// and will be the class that's manipulated to produce the information need for display

package app.domain;



import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

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
    private LocalDate warrantyEnd;
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
    public String getAssetTag() {
        return assetTag;
    }

    public AssetStatus getStatus() {
        return status;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public LocalDate getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(LocalDate purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public BigDecimal getUnitCost() {
        return unitCost;
    }

    public void setUnitCost(BigDecimal unitCost) {
        this.unitCost = unitCost;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }


    public boolean getAssigned() {
        return assigned;
    }

    public void setAssigned(boolean assigned) {
        this.assigned = assigned;
    }

    public LocalDate getWarrantyEnd() {
        return warrantyEnd;
    }

    public void setWarrantyEnd(LocalDate warrantyEnd) {
        this.warrantyEnd = warrantyEnd;
    }



    public Asset copy() {
        return new Asset(
                this.assetTag,
                this.name,
                this.location,
                this.purchaseDate,
                this.unitCost,
                this.quantity,
                this.status,
                this.assigned
        );
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
                ", status='" + status + '\'' +
                ", assigned=" + assigned +
                ", warrantyEnd=" + warrantyEnd +
                '}';
    }


    public boolean isAssigned() {
        return assigned;
    }


}

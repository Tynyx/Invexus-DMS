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
    private String category;
    private LocalDate purchaseDate;
    private BigDecimal unitCost;
    private final AssetStatus status  ;
    private boolean assigned;
    private LocalDate warrantyEnd;

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
    public Asset(String assetTag, String name, String category, LocalDate purchaseDate, BigDecimal unitCost, AssetStatus status, boolean assigned, LocalDate warrantyEnd) {
        this.assetTag = assetTag;
        this.name = name;
        this.category = category;
        this.purchaseDate = purchaseDate;
        this.unitCost = unitCost;
        this.status = status;
        this.assigned = assigned;
        this.warrantyEnd = warrantyEnd;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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

    // Override to ensure the code can be used to make string even though they not strings
    @Override
    public String toString() {
        return "Asset{" +
                "assetTag='" + assetTag + '\'' +
                ", name='" + name + '\'' +
                ", category='" + category + '\'' +
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

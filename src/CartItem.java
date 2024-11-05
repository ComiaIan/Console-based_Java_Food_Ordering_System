// CartItem.java
public class CartItem {
    private FoodItem item;
    private int quantity;

    public CartItem(FoodItem item, int quantity) {
        this.item = item;
        this.quantity = quantity;
    }

    public FoodItem getItem() {
        return item;
    }

    public int getQuantity() {
        return quantity;
    }
}

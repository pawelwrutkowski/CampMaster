package entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Offer {
    private int offerId;
    private int groupId;
    private int price;
    private int active;

    private Group group;

}

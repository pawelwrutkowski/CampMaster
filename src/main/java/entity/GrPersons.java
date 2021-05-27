package entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GrPersons {
    private int groupId;
    private int personId;
    private Integer offerId;
    private LocalDate pairDate;
    private Person person;
    private Group group;
    private Offer offer;
}

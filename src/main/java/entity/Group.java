package entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Group {
    private int groupId;
    private String name;
    private int locationId;
    private int ageRange;
    private int categoriesId;
    private LocalDate startDate;
    private LocalDate endDate;
    private int size;

    private GrCategory grCategory;
    private Location location;
}
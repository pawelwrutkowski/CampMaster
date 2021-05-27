package entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersBadge {
    private int badgeId;
    private int personId;
    private LocalDate acquired;
    private Badge badge;
    private Person person;
}

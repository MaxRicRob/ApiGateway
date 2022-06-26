package domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class ProductComponent {

    private int id;
    private String name;
    private long price;
    private int weight;
    private String color;
    private String origin;
    private int awesomeness;
    private String farmer;
    private boolean organic;
    private int calories;

}

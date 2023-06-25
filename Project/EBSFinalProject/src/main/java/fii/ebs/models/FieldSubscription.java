package fii.ebs.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Builder
@AllArgsConstructor
public class FieldSubscription {
    private String field;
    private String operator;
    private Object value;
}

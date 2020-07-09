package com.mfs.sms.pojo.to;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SaleTo {
    private String[] productId;
    private Integer[] count;
    private String numberId;
    private Boolean sale;
}

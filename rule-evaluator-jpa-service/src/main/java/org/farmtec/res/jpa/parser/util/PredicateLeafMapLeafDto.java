package org.farmtec.res.jpa.parser.util;

import org.farmtec.res.jpa.model.PredicateLeaf;
import org.farmtec.res.service.rule.loader.dto.LeafDto;

/**
 * Created by dp on 07/02/2021
 */
public class PredicateLeafMapLeafDto {
    public static LeafDto PredicateLeafToLeafDto(PredicateLeaf predicateLeaf) {
        LeafDto leafDto = new LeafDto();
        if(predicateLeaf!=null) {
            leafDto.setType(predicateLeaf.getType());
            leafDto.setOperation(predicateLeaf.getOperation());
            leafDto.setTag(predicateLeaf.getTag());
            leafDto.setValue(predicateLeaf.getValue());
        }
        return  leafDto;
    }
}

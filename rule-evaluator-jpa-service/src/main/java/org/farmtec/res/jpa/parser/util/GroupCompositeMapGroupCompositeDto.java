package org.farmtec.res.jpa.parser.util;

import org.farmtec.res.jpa.model.GroupComposite;
import org.farmtec.res.service.rule.loader.dto.GroupCompositeDto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.farmtec.res.jpa.parser.RulesParserJpaImpl.GROUP_PREFIX;
import static org.farmtec.res.jpa.parser.RulesParserJpaImpl.PREDICATE_PREFIX;

/**
 * Created by dp on 07/02/2021
 */
public class GroupCompositeMapGroupCompositeDto {

    public static GroupCompositeDto groupCompositeToGroupCompositeDto(GroupComposite groupComposite) {
        GroupCompositeDto groupCompositeDto = new GroupCompositeDto();
        if(groupComposite!=null) {
            groupCompositeDto.setOperation(groupComposite.getLogicalOperation());

            List<String> groupCompositeNames;
            if(null != groupComposite.getGroupComposites()) {
                groupCompositeNames = groupComposite.getGroupComposites().size()>0?
                        groupComposite.getGroupComposites().stream()
                                .map(gp ->  GROUP_PREFIX+gp.getId())
                                .collect(Collectors.toList())
                        :new ArrayList<>();
            } else {
                groupCompositeNames = new ArrayList<>();
            }


            List<String> predicatesName;
            if(null != groupComposite.getPredicateLeaves()) {
                predicatesName = groupComposite.getPredicateLeaves().size()>0?
                        groupComposite.getPredicateLeaves().stream()
                                .map(p ->  PREDICATE_PREFIX+p.getId())
                                .collect(Collectors.toList())
                        :new ArrayList<>();
            } else {
                predicatesName = new ArrayList<>();
            }

            groupCompositeNames.addAll(predicatesName);

            groupCompositeDto.setPredicateNames(groupCompositeNames);
        }
        return groupCompositeDto;
    }
}

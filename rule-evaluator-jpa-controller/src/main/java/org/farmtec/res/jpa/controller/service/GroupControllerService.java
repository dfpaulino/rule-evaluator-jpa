package org.farmtec.res.jpa.controller.service;

import org.farmtec.res.jpa.controller.exception.ResourceNotFound;
import org.farmtec.res.jpa.model.GroupComposite;
import org.farmtec.res.jpa.model.PredicateLeaf;
import org.farmtec.res.jpa.repositories.GroupCompositeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Created by dp on 14/03/2021
 */
@Service
public class GroupControllerService {

    private final GroupCompositeRepository groupCompositeRepository;

    public GroupControllerService(GroupCompositeRepository groupCompositeRepository) {
        this.groupCompositeRepository = groupCompositeRepository;
    }

    @Transactional
    public GroupComposite getGroupById(long id ){
        return groupCompositeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFound("GroupId Not Found"));
    }

    @Transactional
    public GroupComposite deleteGroupPredicate(long groupId,long predicateId) {

        GroupComposite gc = groupCompositeRepository.findById(groupId)
                .orElseThrow(() -> new ResourceNotFound("GroupId Not Found"));
        gc.getPredicateLeaves().removeIf(p -> p.getId() == predicateId);
        return gc;
    }

    @Transactional
    public GroupComposite deleteChildGroup(long groupParentId,long groupChildId) {

        GroupComposite gc = groupCompositeRepository.findById(groupParentId)
                .orElseThrow(() -> new ResourceNotFound("GroupId Not Found"));
        gc.getGroupComposites().removeIf(p -> p.getId() == groupChildId);
        return gc;
    }

    @Transactional
    public void deleteGroupId(long id) {
        groupCompositeRepository.deleteById(id);
    }

    @Transactional
    public GroupComposite addGroup(long parentId, GroupComposite groupComposite) {
        //todo add validation for predicates
        Optional<GroupComposite> gcOpt = groupCompositeRepository.findById(parentId);
        GroupComposite toReturn;
        if(gcOpt.isPresent()) {
            //this is a manage entity...should save at end of scope
            GroupComposite fetchedGroup = gcOpt.get();
            fetchedGroup.getGroupComposites().add(groupComposite);
            //groupCompositeRepository.save(fetchedGroup);
            toReturn = fetchedGroup;
        }else {
            //not sure how useful a group is with no link to a rule
            //keep it for now. to remove is we are 100% its useless
            toReturn = groupCompositeRepository.save(groupComposite);
        }
        return toReturn;
    }
    @Transactional
    public GroupComposite addPredicateToGroup(long groupId, PredicateLeaf predicateLeaf) {
        //todo add validation to predicate
        GroupComposite groupComposite = groupCompositeRepository.findById(groupId)
                .orElseThrow(() -> new ResourceNotFound("GroupId Not Found"));
        groupComposite.getPredicateLeaves().add(predicateLeaf);
        return groupComposite;
    }
}

package org.farmtec.res.jpa.controller;

import org.farmtec.res.jpa.controller.representationModel.GroupCompositeRepresentationModel;
import org.farmtec.res.jpa.controller.representationModel.RepresentationModelConverterUtil;
import org.farmtec.res.jpa.model.GroupComposite;
import org.farmtec.res.jpa.model.PredicateLeaf;
import org.farmtec.res.jpa.repositories.GroupCompositeRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * Created by dp on 23/02/2021
 */
@RestController
@RequestMapping("/groups")

public class GroupCompositeController {

    private final GroupCompositeRepository groupCompositeRepository;

    public GroupCompositeController(GroupCompositeRepository groupCompositeRepository) {
        this.groupCompositeRepository = groupCompositeRepository;
    }

    @GetMapping("/{id}")
    @Transactional
    public GroupCompositeRepresentationModel getGroupById(@PathVariable("id") long id) {
        GroupComposite groupComposite = groupCompositeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("group not found"));
        return RepresentationModelConverterUtil.fromGroupComposite(groupComposite, id);
    }

    @DeleteMapping("/{id}/predicate/{predicateId}")
    @Transactional
    public GroupCompositeRepresentationModel deletePredicate(@PathVariable("id") long groupId,
                                                             @PathVariable("predicateId") long predicateId) {

        GroupComposite gc = groupCompositeRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group Not Found"));
        gc.getPredicateLeaves().removeIf(p -> p.getId() == predicateId);
        groupCompositeRepository.save(gc);
        return RepresentationModelConverterUtil.fromGroupComposite(gc, groupId);
    }

    @DeleteMapping("/{id}/group/{childId}")
    @Transactional
    public GroupCompositeRepresentationModel deleteChildGroup(@PathVariable("id") long groupId,
                                                              @PathVariable("childId") long childId) {

        GroupComposite gc = groupCompositeRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group Not Found"));
        gc.getGroupComposites().removeIf(p -> p.getId() == childId);
        return RepresentationModelConverterUtil.fromGroupComposite(gc, groupId);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public void deleteGroup(@PathVariable("id") long groupId) {
        groupCompositeRepository.deleteById(groupId);
    }

    @PostMapping("/{id}")
    @Transactional
    public GroupCompositeRepresentationModel addGroup(@PathVariable("id") long parentId,
                                                      @RequestBody GroupComposite groupComposite) {
        //todo  validate GroupLeaf
        Optional<GroupComposite> gcOpt = groupCompositeRepository.findById(parentId);
        GroupCompositeRepresentationModel toReturn;
        if(gcOpt.isPresent()) {
            //this is a manage entity...should save at end of scope
            gcOpt.get().getGroupComposites().add(groupComposite);
            toReturn = RepresentationModelConverterUtil.fromGroupComposite(gcOpt.get(), parentId);
        }else {
            GroupComposite saved = groupCompositeRepository.save(groupComposite);
            toReturn = RepresentationModelConverterUtil.fromGroupComposite(saved, 0);
        }
        return toReturn;
    }

    @PostMapping("/{id}/predicate")
    @Transactional
    public GroupCompositeRepresentationModel addPredicateToGroup(@PathVariable("id") long groupId,
                                                                 @RequestBody PredicateLeaf predicateLeaf) {

        return null;
    }
}

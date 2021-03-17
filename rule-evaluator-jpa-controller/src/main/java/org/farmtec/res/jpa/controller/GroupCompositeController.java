package org.farmtec.res.jpa.controller;

import org.farmtec.res.jpa.controller.representationModel.GroupCompositeRepresentationModel;
import org.farmtec.res.jpa.controller.representationModel.RepresentationModelConverterUtil;
import org.farmtec.res.jpa.controller.service.GroupControllerService;
import org.farmtec.res.jpa.model.GroupComposite;
import org.farmtec.res.jpa.model.PredicateLeaf;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;


/**
 * Created by dp on 23/02/2021
 */
@RestController
@RequestMapping("/groups")
public class GroupCompositeController {

    private final GroupControllerService groupControllerService;

    public GroupCompositeController(GroupControllerService groupControllerService) {
        this.groupControllerService = groupControllerService;
    }

    @GetMapping("/{id}")
    @Transactional
    public GroupCompositeRepresentationModel getGroupById(@PathVariable("id") long id) {
        GroupComposite groupComposite = groupControllerService.getGroupById(id);
        return RepresentationModelConverterUtil.fromGroupComposite(groupComposite, id);
    }

    @DeleteMapping("/{id}/predicate/{predicateId}")
    @Transactional
    public GroupCompositeRepresentationModel deletePredicate(@PathVariable("id") long groupId,
                                                             @PathVariable("predicateId") long predicateId) {

        GroupComposite gc = groupControllerService.deleteGroupPredicate(groupId, predicateId);
        return RepresentationModelConverterUtil.fromGroupComposite(gc, groupId);
    }

    @DeleteMapping("/{id}/group/{childId}")
    @Transactional
    public GroupCompositeRepresentationModel deleteChildGroup(@PathVariable("id") long groupId,
                                                              @PathVariable("childId") long childId) {

        GroupComposite gc = groupControllerService.deleteChildGroup(groupId, childId);
        return RepresentationModelConverterUtil.fromGroupComposite(gc, groupId);
    }

    @DeleteMapping("/{id}")
    public void deleteGroup(@PathVariable("id") long groupId) {
        groupControllerService.getGroupById(groupId);
    }

    @PostMapping("/{id}")
    @Transactional
    public GroupCompositeRepresentationModel addGroup(@PathVariable("id") long parentId,
                                                      @RequestBody GroupComposite groupComposite) {
        return RepresentationModelConverterUtil.fromGroupComposite(groupControllerService
                .addGroup(parentId, groupComposite), parentId);
    }

    @PostMapping("/{id}/predicate")
    @Transactional
    public GroupCompositeRepresentationModel addPredicateToGroup(@PathVariable("id") long groupId,
                                                                 @RequestBody PredicateLeaf predicateLeaf) {
        GroupComposite gc = groupControllerService.addPredicateToGroup(groupId,predicateLeaf);
        return RepresentationModelConverterUtil.fromGroupComposite(gc, 0);
    }
}

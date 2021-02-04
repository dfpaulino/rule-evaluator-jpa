package org.farmtec.res.jpa.repositories;

import org.farmtec.res.jpa.model.PredicateLeaf;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by dp on 04/02/2021
 */
@Repository
public interface PredicateLeafRepository extends JpaRepository<PredicateLeaf,Long> {
}

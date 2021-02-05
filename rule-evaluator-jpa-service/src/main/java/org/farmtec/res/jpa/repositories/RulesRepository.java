package org.farmtec.res.jpa.repositories;

import org.farmtec.res.jpa.model.Rule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by dp on 31/01/2021
 */
@Repository
public interface RulesRepository extends JpaRepository<Rule,Long> {
}

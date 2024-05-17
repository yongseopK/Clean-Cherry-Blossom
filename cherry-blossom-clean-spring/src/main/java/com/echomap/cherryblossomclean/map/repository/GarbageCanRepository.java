package com.echomap.cherryblossomclean.map.repository;

import com.echomap.cherryblossomclean.map.entity.GarbageCan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GarbageCanRepository extends JpaRepository<GarbageCan, Long> {
    List<GarbageCan> findByDistrict(String district);

}

package bughunter.bughunterserver.dao;

import bughunter.bughunterserver.model.entity.Edge;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

/**
 * @author sean
 * @date 2019-01-23.
 */
@Transactional
public interface EdgeDao extends CrudRepository<Edge, Long> {

    @Query("SELECT e FROM Edge e WHERE e.appKey = :appKey " +
            "AND e.isCovered = :isCovered " +
            "ORDER BY e.number DESC")
    List<Edge> findByAppKeyAndIsCovered(@Param("appKey") String appKey,
                                        @Param("isCovered") Integer isCovered);


    @Query("SELECT e FROM Edge e WHERE e.sourceNode = :sourceWindow " +
            "AND e.targetNode = :targetWindow " +
            "ORDER BY e.number DESC")
    List<Edge> findBySourceNodeAndTargetNodeOrderByNumber(@Param("sourceWindow") String sourceWindow,
                                                          @Param("targetWindow") String targetWindow);

    //    @Query("SELECT e FROM edge e WHERE e.sourceNode = :sourceNode and e.targetNode = :targetNode " +
//            "and e.callbacks = :callbacks and e.appKey = :appKey")
    Edge findBySourceNodeAndTargetNodeAndEventHandlersAndAppKey(String sourceNode, String targetNode, String eventHandlers, String appKey);

    @Query("select e from Edge e where e.sourceNode = :sourceWindow " +
            "and e.targetNode = :targetWindow" +
            " and e.isCovered = :i " +
            "and e.number < 5 order by number DESC")
    List<Edge> findBySourceNodeAndTargetNodeAndIsCoveredOrderByNumber(
            @Param("sourceWindow") String sourceWindow,
            @Param("targetWindow") String targetWindow,
            @Param("i") int i);

    List<Edge> findByAppKey(String appKey);

    List<Edge> findBySourceNode(String window);
}

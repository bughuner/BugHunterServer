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

    List<Edge> findByAppKeyAndIsCovered(String appKey, Integer isCovered);

    List<Edge> findByAppKeyAndSourceNode(String appKey, String sourceNode);

    List<Edge> findByAppKeyAndTargetNode(String appKey, String window);

    List<Edge> findBySourceNodeAndTargetNode(String sourceWindow, String targetWindow);

    //    @Query("SELECT e FROM edge e WHERE e.sourceNode = :sourceNode and e.targetNode = :targetNode " +
//            "and e.callbacks = :callbacks and e.appKey = :appKey")
    Edge findBySourceNodeAndTargetNodeAndEventHandlersAndAppKey(String sourceNode, String targetNode, String eventHandlers, String appKey);

    @Query("select e from edge e where e.sourceNode = :source and e.targetNode = :target and e.isCovered = :i " +
            "and e.number < 5 order by number DESC")
    List<Edge> findBySourceNodeAndTargetNodeAndIsCovered(@Param("source") String activityBeforeAction, @Param("target") String activityAfterAction, @Param("i") int i);

    List<Edge> findByAppKey(String appKey);

    List<Edge> findBySourceNode(String window);
}

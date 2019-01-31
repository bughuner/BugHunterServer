package bughunter.bughunterserver.service;

import bughunter.bughunterserver.DTO.EdgeDTO;
import bughunter.bughunterserver.model.entity.Edge;

import java.util.List;

/**
 * @author sean
 * @date 2019-01-23.
 */

public interface EdgeService {

    Edge save(Edge edge);

    List<List<Edge>> getRecommendedBugs(String appKey, String currentWindow);

    Edge getEdgeBySourceNodeAndTargetNodeAndCallbacksAndAppkey(String sourceNode, String targetNode, String callbacks, String callbacks1);

    Edge getNextBugHint(String currentWindow, List<EdgeDTO> edgeDTOs);
}
